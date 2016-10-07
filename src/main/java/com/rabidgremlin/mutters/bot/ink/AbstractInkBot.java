package com.rabidgremlin.mutters.bot.ink;

import java.io.InputStream;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bladecoder.ink.runtime.Choice;
import com.bladecoder.ink.runtime.Story;
import com.rabidgremlin.mutters.bot.Bot;
import com.rabidgremlin.mutters.bot.BotException;
import com.rabidgremlin.mutters.bot.BotResponse;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.session.Session;
import com.rabidgremlin.mutters.util.SessionUtils;

/**
 * This is the base bot class for bots using the Ink narrative scripting language from Inkle. The bot requires a
 * compiled ink file in .json format. The choices in the ink file should match the names of intents returned by the
 * IntentMatcher created by the setUpIntents() method.
 * 
 * See http://www.inklestudios.com/ink/ for more info on Ink
 * 
 * This class also adds the SET_ACTION, SET_HINT, SET_REPROMPT functions to the bot.
 * 
 * See the <a href=
 * "https://github.com/rabidgremlin/Mutters/blob/master/src/test/java/com/rabidgremlin/mutters/bot/ink/TaxiInkBot.java"
 * target="_blank">TaxiInkBot</a> for an example of how this type of bot works.
 * 
 * @see com.rabidgremlin.mutters.bot.ink.SetActionFunction
 * @see com.rabidgremlin.mutters.bot.ink.SetHintFunction
 * @see com.rabidgremlin.mutters.bot.ink.SetRepromptFunction
 * 
 * @author rabidgremlin
 *
 */
public abstract class AbstractInkBot
    implements Bot
{
  /** Logger for the bot. */
  private Logger log = LoggerFactory.getLogger(AbstractInkBot.class);

  /** The intent matcher for the bot. */
  protected IntentMatcher matcher;

  /** The ink JSON for the bot. */
  protected String inkStoryJson;

  /** Default response for when the bot cannot figure out what was said to it. */
  protected String defaultResponse = "Pardon?";

  /** Map of InkBotFunctions the bot knows. */
  protected HashMap<String, InkBotFunction> inkBotFunctions = new HashMap<String, InkBotFunction>();

  /**
   * Constructs the bot.
   * 
   */
  public AbstractInkBot()
  {
    // get the matcher set up
    matcher = setUpIntents();

    // get the story json
    inkStoryJson = getStoryJson();

    // Add default functions
    addFunction(new SetHintFunction());
    addFunction(new SetRepromptFunction());
    addFunction(new SetActionFunction());

    // add any other functions for the bot
    setUpFunctions();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.rabidgremlin.mutters.bot.Bot#respond(com.rabidgremlin.mutters.session.Session,
   * com.rabidgremlin.mutters.core.Context, java.lang.String)
   */
  @Override
  public BotResponse respond(Session session, Context context, String messageText)
    throws BotException
  {
    log.debug("===> \n session: {} context: {} messageText: {}",
        new Object[]{ session, context, messageText });

    CurrentResponse currentResponse = new CurrentResponse();

    // set up default response in case bot has issue processing input
    currentResponse.setResponseText(SessionUtils.getReprompt(session));
    if (currentResponse.getResponseText() == null)
    {
      currentResponse.setResponseText(defaultResponse);
    }

    // preserve hint if we had reprompt hint
    currentResponse.setHint(SessionUtils.getRepromptHint(session));

    try
    {
      Story story = null;

      // wrap create in synchronized block because something in JSON parsing is not threadsafe
      synchronized (this)
      {
        story = new Story(inkStoryJson);
      }

      // call hook so externs and other things can be applied
      afterStoryCreated(story);

      SessionUtils.loadInkStoryState(session, story.getState());

      IntentMatch intentMatch = matcher.match(messageText, context);

      if (intentMatch != null)
      {
        // call after match hook, allows fixups to be applied
        afterIntentMatch(intentMatch, session, story);

        // copy any slot values into ink vars
        for (SlotMatch slotMatch : intentMatch.getSlotMatches().values())
        {
          story.getVariablesState().set(slotMatch.getSlot().getName().toLowerCase(),
              slotMatch.getValue().toString());
        }

        // get to right place in story
        story.continueMaximally();

        // loop through choices find the one that matches intent
        if (story.getCurrentChoices().size() > 0)
        {
          int choiceIndex = 0;
          for (Choice c : story.getCurrentChoices())
          {
            log.debug("Checking choice: {}", c.getText());
            if (StringUtils.equalsIgnoreCase(intentMatch.getIntent().getName(), c.getText()))
            {
              log.debug("Choosing: {}", c.getText());
              story.chooseChoiceIndex(choiceIndex);

              // reset reprompt and hint
              currentResponse.setReprompt(null);
              currentResponse.setHint(null);

              StringBuffer response = new StringBuffer();
              boolean first = true;
              while (story.canContinue())
              {
                String line = story.Continue();

                // skip first line as ink replays choice first
                if (first)
                {
                  first = false;
                  continue;
                }

                line = line.replaceAll("\n", "");

                log.debug("Line {}", line);

                String trimmedLine = line.trim();

                if (trimmedLine.startsWith(":"))
                {
                  String functionName = trimmedLine.split(" ")[0].substring(1).trim();
                  String param = trimmedLine.substring(functionName.length() + 1).trim();

                  InkBotFunction function = inkBotFunctions.get(functionName.toLowerCase());
                  if (function != null)
                  {
                    function.execute(currentResponse, session, intentMatch, story, param);
                  }
                  else
                  {
                    log.warn("Did not find function named {}", functionName);
                  }
                }
                else
                {
                  response.append(line);
                }
              }

              currentResponse.setResponseText(response.toString());

              break;
            }
            choiceIndex++;
          }

          SessionUtils.saveInkStoryState(session, story.getState());

          if (story.getCurrentChoices().size() == 0)
          {
            session.reset();
            currentResponse.setAskResponse(false);
          }
        }
        else
        {
          session.reset();
          currentResponse.setAskResponse(false);
        }

        if (currentResponse.getReprompt() != null)
        {
          SessionUtils.setReprompt(session, currentResponse.getReprompt());
          SessionUtils.setRepromptHint(session, currentResponse.getHint());
        }
        else
        {
          SessionUtils.setReprompt(session, defaultResponse + " " + currentResponse.getResponseText());
          SessionUtils.setRepromptHint(session, currentResponse.getHint());
        }

      }

      return new BotResponse(currentResponse.getResponseText(), currentResponse.getHint(),
          currentResponse.isAskResponse(), currentResponse.getReponseAction(),
          currentResponse.getResponseActionParams());
    }
    catch (Exception e)
    {
      throw new BotException("Unexpected error", e);
    }
  }

  /**
   * Returns the default response of the bot.
   * 
   * @return The default response.
   */
  public String getDefaultResponse()
  {
    return defaultResponse;
  }

  /**
   * Sets the default response for the bot. This is the bot's response if it doesn't understand what was said.
   * 
   * @param defaultResponse The new default bot response.
   */
  public void setDefaultResponse(String defaultResponse)
  {
    this.defaultResponse = defaultResponse;
  }

  /**
   * Helper method to load a compiled Ink .json file from the classpath.
   * 
   * @param inkJsonFileName The name of the JSON file.
   * @return The ink story as a JSON string.
   */
  protected String loadStoryJsonFromClassPath(String inkJsonFileName)
  {
    try
    {
      InputStream inkJsonStream = Thread.currentThread().getContextClassLoader()
          .getResourceAsStream(inkJsonFileName);

      // replace seems to be a weird hack. as in example from blade-ink library
      return IOUtils.toString(inkJsonStream, "UTF-8").replace('\uFEFF', ' ');
    }
    catch (Exception e)
    {
      throw new IllegalStateException("Failed to load ink json.", e);
    }
  }

  /**
   * Adds a InkBotFunction to the bot.
   * 
   * @param function The function to add.
   */
  protected void addFunction(InkBotFunction function)
  {
    inkBotFunctions.put(function.getFunctionName().toLowerCase(), function);
  }

  /**
   * This method should create and populate an IntentMatcher. This IntentMatcher will be used by the bot to determine
   * what a user said to the bot. The names of the Intents returned by the IntentMatcher should match choices in the Ink
   * story for the bot to work.
   * 
   * @return The IntentMatcher for the bot to use.
   */
  public abstract IntentMatcher setUpIntents();

  /**
   * This method should load and return the compiled Ink story in JSON format. This is the Ink story that the bot will
   * use to figure out how to respond to a user's message.
   * 
   * @return The compiled story as a JSON string.
   */
  public abstract String getStoryJson();

  /**
   * This method should set up any InkBotFunctions for the bot. This method should call the addFunction() method.
   * 
   * @see #addFunction(InkBotFunction function)
   */
  public abstract void setUpFunctions();

  /**
   * This method can be overridden to manipulate the Story object used by the bot just after it is created. Note the bot
   * may create the story multiple times. This method is useful for registering external functions with the Ink runtime.
   * 
   * @param story The just created story.
   */
  protected void afterStoryCreated(Story story)
  {
    // do nothing
  }

  /**
   * This method can be overridden to manipulate the results of an intent match. It allows the match to be manipulated
   * before the class uses it to progress the ink story.
   * 
   * @param intentMatch The intent match.
   * @param session The current user's session.
   * @param story The current story.
   */
  protected void afterIntentMatch(IntentMatch intentMatch, Session session, Story story)
  {
    // do nothing
  }

}