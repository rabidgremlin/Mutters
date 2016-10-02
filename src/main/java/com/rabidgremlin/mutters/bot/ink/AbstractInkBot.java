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
import com.rabidgremlin.mutters.bot.BotResponse;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.session.Session;
import com.rabidgremlin.mutters.util.SessionUtils;

public abstract class AbstractInkBot
    implements Bot
{
  private Logger log = LoggerFactory.getLogger(AbstractInkBot.class);

  protected IntentMatcher matcher;

  protected String inkStoryJson;

  protected String defaultResponse = "Pardon?";

  protected HashMap<String, InkBotFunction> inkBotFunctions = new HashMap<String, InkBotFunction>();

  public AbstractInkBot()
  {
    matcher = setUpIntents();
    inkStoryJson = getStoryJson();

    addFunction("SET_HINT", new InkBotFunction()
    {
      @Override
      public void execute(CurrentResponse currentResponse, Session session, IntentMatch intentMatch, Story story, String param)
      {
        currentResponse.hint = param;
      }
    });

    addFunction("SET_REPROMPT", new InkBotFunction()
    {
      @Override
      public void execute(CurrentResponse currentResponse, Session session, IntentMatch intentMatch, Story story, String param)
      {
        currentResponse.reprompt = param;
      }
    });

    addFunction("SET_ACTION", new InkBotFunction()
    {
      @Override
      public void execute(CurrentResponse currentResponse, Session session, IntentMatch intentMatch, Story story, String param)
      {
        // HACK HACK doesn't handle spaces in name value pairs
        // OPEN_URL url:http:\/\/trackcab.example.com/t/{taxiNo}
        String trimmedLine = param.trim();
        String actionName = trimmedLine.split(" ")[0].substring(0).trim();
        String[] nameValues = trimmedLine.substring(actionName.length() + 1).trim().split(" ");

        currentResponse.reponseAction = actionName;
        currentResponse.responseActionParams = new HashMap<String, Object>();

        for (String nameValue : nameValues)
        {
          String name = nameValue.split(":")[0].substring(0).trim();
          String value = nameValue.substring(name.length() + 1).trim();

          currentResponse.responseActionParams.put(name, value);
        }
      }
    });

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
  {
    log.info("============================================================== \n session: {} context: {} messageText: {}",
        new Object[]{ session, context, messageText });

    CurrentResponse currentResponse = new CurrentResponse();

    // set up default response in case bot has issue processing input
    currentResponse.responseText = SessionUtils.getReprompt(session);
    if (currentResponse.responseText == null)
    {
      currentResponse.responseText = defaultResponse;
    }

    // preserve hint if we had reprompt hint
    currentResponse.hint = SessionUtils.getRepromptHint(session);

    try
    {
      // TODO is this efficent or do we need ThreadLocals ?
      Story story = new Story(inkStoryJson);

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
          story.getVariablesState().set(slotMatch.getSlot().getName().toLowerCase(), slotMatch.getValue().toString());
        }

        // get to right place in story
        story.continueMaximally();

        // loop through choices find the one that matches intent
        if (story.getCurrentChoices().size() > 0)
        {
          int choiceIndex = 0;
          for (Choice c : story.getCurrentChoices())
          {
            log.info("Checking choice:" + c.getText());
            if (StringUtils.equalsIgnoreCase(intentMatch.getIntent().getName(), c.getText()))
            {
              log.info("Choosing:" + c.getText());
              story.chooseChoiceIndex(choiceIndex);

              // reset reprompt and hint
              currentResponse.reprompt = null;
              currentResponse.hint = null;

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

                log.info("Line {}", line);

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

              currentResponse.responseText = response.toString();

              break;
            }
            choiceIndex++;
          }

          SessionUtils.saveInkStoryState(session, story.getState());

          if (story.getCurrentChoices().size() == 0)
          {
            session.reset();
            currentResponse.askResponse = false;
          }
        }
        else
        {
          session.reset();
          currentResponse.askResponse = false;
        }

        if (currentResponse.reprompt != null)
        {
          SessionUtils.setReprompt(session, currentResponse.reprompt);
          SessionUtils.setRepromptHint(session, currentResponse.hint);
        }
        else
        {
          SessionUtils.setReprompt(session, defaultResponse + " " + currentResponse.responseText);
          SessionUtils.setRepromptHint(session, currentResponse.hint);
        }

      }

      return new BotResponse(currentResponse.responseText, currentResponse.hint, currentResponse.askResponse, currentResponse.reponseAction,
          currentResponse.responseActionParams);
    }
    catch (Exception e)
    {
      log.warn("Unexpected error.", e);
      return new BotResponse(currentResponse.responseText, currentResponse.hint, true, null, null);
    }
  }

  public String getDefaultResponse()
  {
    return defaultResponse;
  }

  public void setDefaultResponse(String defaultResponse)
  {
    this.defaultResponse = defaultResponse;
  }

  protected String loadStoryJsonFromClassPath(String inkJsonFileName)
  {
    try
    {
      InputStream inkJsonStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(inkJsonFileName);

      return IOUtils.toString(inkJsonStream, "UTF-8").replace('\uFEFF', ' ');
    }
    catch (Exception e)
    {
      throw new IllegalStateException("Failed to load ink json.", e);
    }
  }

  protected void addFunction(String functionName, InkBotFunction function)
  {
    inkBotFunctions.put(functionName.toLowerCase(), function);
  }

  public abstract IntentMatcher setUpIntents();

  public abstract String getStoryJson();

  public abstract void setUpFunctions();

  protected void afterStoryCreated(Story story)
  {
    // do nothing
  }

  protected void afterIntentMatch(IntentMatch intentMatch, Session session, Story story)
  {
    // do nothing
  }

}
