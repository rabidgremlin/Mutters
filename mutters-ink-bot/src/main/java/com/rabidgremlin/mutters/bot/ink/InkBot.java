package com.rabidgremlin.mutters.bot.ink;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bladecoder.ink.runtime.Choice;
import com.bladecoder.ink.runtime.Story;
import com.bladecoder.ink.runtime.StoryException;
import com.rabidgremlin.mutters.bot.ink.InkBotConfiguration.ConfusedKnot;
import com.rabidgremlin.mutters.bot.ink.InkBotConfiguration.GlobalIntent;
import com.rabidgremlin.mutters.bot.ink.functions.AddAttachmentFunction;
import com.rabidgremlin.mutters.bot.ink.functions.AddQuickReplyFunction;
import com.rabidgremlin.mutters.bot.ink.functions.GetLongTermAttributeFunction;
import com.rabidgremlin.mutters.bot.ink.functions.RemoveLongTermAttributeFunction;
import com.rabidgremlin.mutters.bot.ink.functions.SetHintFunction;
import com.rabidgremlin.mutters.bot.ink.functions.SetLongTermAttributeFunction;
import com.rabidgremlin.mutters.bot.ink.functions.SetRepromptFunction;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.bot.Bot;
import com.rabidgremlin.mutters.core.bot.BotException;
import com.rabidgremlin.mutters.core.bot.BotResponse;
import com.rabidgremlin.mutters.core.session.Session;

/**
 * This is the base bot class for bots using the Ink narrative scripting language from Inkle. The bot requires a
 * compiled ink file in .json format. The choices in the ink file should match the names of intents returned by the
 * IntentMatcher.
 * 
 * See http://www.inklestudios.com/ink/ for more info on Ink
 * 
 * This class also adds the ADD_ATTACHMENT, ADD_QUICK_REPLY, SET_HINT, SET_REPROMPT functions to the bot.
 * 
 * @see com.rabidgremlin.mutters.bot.ink.functions.AddAttachmentFunction
 * @see com.rabidgremlin.mutters.bot.ink.functions.AddQuickReplyFunction
 * @see com.rabidgremlin.mutters.bot.ink.functions.SetHintFunction
 * @see com.rabidgremlin.mutters.bot.ink.functions.SetRepromptFunction
 * 
 * @author rabidgremlin
 *
 */
public abstract class InkBot<T extends InkBotConfiguration>
    implements Bot
{
  /** Logger for the bot. */
  private Logger log = LoggerFactory.getLogger(InkBot.class);

  /** The intent matcher for the bot. */
  protected IntentMatcher matcher;

  /** The ink JSON for the bot. */
  protected String inkStoryJson;

  /** Default responses for when the bot cannot figure out what was said to it. */
  protected String[] defaultResponses = { "Pardon?" };

  /** Map of InkBotFunctions the bot knows. */
  protected HashMap<String, InkBotFunction> inkBotFunctions = new HashMap<String, InkBotFunction>();

  /** Map of global intents for the bot. */
  protected HashMap<String, String> globalIntents = new HashMap<>();

  /** Random for default reponses. */
  private Random rand = new Random();

  /** Debug value key for matched intent. */
  public final static String DK_MATCHED_INTENT = "matchedIntent";

  /** Debug value key for intent matching scores. */
  public final static String DK_INTENT_MATCHING_SCORES = "intentMatchingScores";

  /** The number of failed to understand attempts before bot is confused. */
  private int maxAttemptsBeforeConfused = -1;

  /** The name of the ink knot to jump too when the bot is confused. */
  private String confusedKnotName = null;

  /** A thread local map that holds the story instance for each unique story JSON */
  private static ThreadLocal<Map<String, Story>> threadLocalStoryMap = ThreadLocal.withInitial(HashMap::new);

  /**
   * Constructs the bot.
   * 
   * @param configuration The InkBotConfiguration object for the bot.
   * 
   */
  public InkBot(T configuration)
  {
    // get the matcher set up
    matcher = configuration.getIntentMatcher();

    // get the story json
    inkStoryJson = configuration.getStoryJson();

    // Add default functions
    addFunction(new SetHintFunction());
    addFunction(new SetRepromptFunction());
    addFunction(new AddAttachmentFunction());
    addFunction(new AddQuickReplyFunction());
    addFunction(new SetLongTermAttributeFunction());
    addFunction(new GetLongTermAttributeFunction());
    addFunction(new RemoveLongTermAttributeFunction());

    // add any other functions for the bot
    List<InkBotFunction> functions = configuration.getInkFunctions();
    if (functions != null)
    {
      for (InkBotFunction function : functions)
      {
        addFunction(function);
      }
    }

    // add any any global intents
    List<GlobalIntent> globalIntents = configuration.getGlobalIntents();
    if (globalIntents != null)
    {
      for (GlobalIntent globalIntent : globalIntents)
      {
        addGlobalIntent(globalIntent.getIntentName(), globalIntent.getKnotName());
      }
    }

    // set up confused knot if supplied
    ConfusedKnot confusedKnot = configuration.getConfusedKnot();
    if (confusedKnot != null)
    {
      setConfusedKnot(confusedKnot.getMaxAttemptsBeforeConfused(), confusedKnot.getConfusedKnotName());
    }

    // set up default phrases if supplied
    List<String> defaultResponses = configuration.getDefaultResponses();
    if (defaultResponses != null)
    {
      setDefaultResponses(defaultResponses.toArray(new String[0]));
    }
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

    // choose a default response
    String defaultResponse = defaultResponses[rand.nextInt(defaultResponses.length)];

    // set up default response in case bot has issue processing input
    currentResponse.setResponseText(SessionUtils.getReprompt(session));
    if (currentResponse.getResponseText() == null)
    {
      currentResponse.setResponseText(defaultResponse);
    }
   
    // preserve hint if we had reprompt hint
    currentResponse.setHint(SessionUtils.getRepromptHint(session));

    // preserve quick replies if we had them
    currentResponse.setResponseQuickReplies(SessionUtils.getRepromptQuickReplies(session));

    // keep hold of matched intent for logging and debug
    String matchedIntent = null;

    int failedToUnderstandCount = SessionUtils.getFailedToUnderstandCount(session);
    log.debug("current failed count is {}", failedToUnderstandCount);

    try
    {
      Story story = threadLocalStoryMap.get().get(inkStoryJson);

      if (story == null)
      {
        synchronized (this)
        {
          // wrap create in synchronized block because something in JSON parsing is not threadsafe
          story = new StoryDecorator(inkStoryJson);
          threadLocalStoryMap.get().put(inkStoryJson, story);
        }
      }
      else
      {
        story.resetState();
      }

      // call hook so externs and other things can be applied
      afterStoryCreated(story);
      
      // restore the story state
      SessionUtils.loadInkStoryState(session, story, inkStoryJson);

      // call hook so additional things can be applied to story after state has been restored 
      afterStoryStateLoaded(story);

      // get to right place in story, capture any pretext
      String preText = processStory(session, currentResponse, story, null).toString();

      // build expected intents set
      HashSet<String> expectedIntents = new HashSet<String>();
      // add all the names of the global intents
      expectedIntents.addAll(globalIntents.keySet());
      // add all the choices
      for (Choice choice : story.getCurrentChoices())
      {
        expectedIntents.add(choice.getText());
      }

      // create debug values map
      HashMap<String, Object> debugValues = new HashMap<String, Object>();

      // match the intents
      IntentMatch intentMatch = matcher.match(messageText, context, expectedIntents, debugValues);

      if (intentMatch != null)
      {
        // record name of intent we matched on
        matchedIntent = intentMatch.getIntent().getName();

        // call after match hook, allows fixups to be applied
        afterIntentMatch(intentMatch, session, story);

        // copy any slot values into ink vars
        setSlotValuesInInk(intentMatch.getSlotMatches().values(), story);

        // did we match something flag. Used so we can set reprompt correctly
        boolean foundMatch = false;

        // check if this is a global intent
        String knotName = globalIntents.get(intentMatch.getIntent().getName());

        // if global intent then jump to knot, otherwise pick choice
        if (knotName != null)
        {
          story.choosePathString(knotName);
          getResponseText(session, currentResponse, story, intentMatch, preText);
          foundMatch = true;
        }
        else
        {
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

                getResponseText(session, currentResponse, story, intentMatch, preText);

                foundMatch = true;
                break;
              }
              choiceIndex++;
            }
          }
        }

        // did we match to global or choice ?
        if (foundMatch)
        {
          // reset failed count
          failedToUnderstandCount = 0;

          setRepromptInSession(currentResponse, session, defaultResponse);
        }
        else
        {
          // found intent but did not match global or choice so increment fail count
          failedToUnderstandCount += 1;
        }
      }
      else
      {
        // did not find intent so increment fail account
        failedToUnderstandCount += 1;
      }

      log.debug("failed count is now {}", failedToUnderstandCount);

      // do we have confused knot and failed attempt > max failed attempts ?
      if (confusedKnotName != null && failedToUnderstandCount >= maxAttemptsBeforeConfused)
      {
        log.debug("Bot is confused. failedToUnderstandCount({}) >= maxAttemptsBeforeConfused ({})", failedToUnderstandCount, maxAttemptsBeforeConfused);
        log.debug("jumping to {} ", confusedKnotName);
        // jump to confused knot
        story.choosePathString(confusedKnotName);
        // continue story
        getResponseText(session, currentResponse, story, intentMatch, preText);
        // reset failed count
        failedToUnderstandCount = 0;
        
        setRepromptInSession(currentResponse, session, defaultResponse);
      }

      // save failed count
      SessionUtils.setFailedToUnderstandCount(session, failedToUnderstandCount);

      // save current story state
      SessionUtils.saveInkStoryState(session, story, inkStoryJson);

      // does story have any more choices ?
      if (story.getCurrentChoices().size() == 0)
      {
        // no, conversation is done, wipe session and we are not returning an ask response
        session.reset();
        currentResponse.setAskResponse(false);
      }

      // populate debug values map with matched intent
      if (matchedIntent != null)
      {
        debugValues.put(DK_MATCHED_INTENT, matchedIntent);
      }

      // build and return response
      return new BotResponse(currentResponse.getResponseText(), currentResponse.getHint(), currentResponse.isAskResponse(),
          currentResponse.getResponseAttachments(),
          currentResponse.getResponseQuickReplies(), debugValues);
    }
    catch (Exception e)
    {
      throw new BotException("Unexpected error", e);
    }
  }
  
  private void setRepromptInSession(CurrentResponse currentResponse, Session session, String defaultResponse)
  {
    if (currentResponse.getReprompt() != null)
    {
      SessionUtils.setReprompt(session, currentResponse.getReprompt());
    }
    else
    {
      SessionUtils.setReprompt(session, defaultResponse + " " + currentResponse.getResponseText());
    }
    SessionUtils.setRepromptHint(session, currentResponse.getHint());
    SessionUtils.setRepromptQuickReplies(session, currentResponse.getResponseQuickReplies());
  }

   private void setSlotValuesInInk(Collection<SlotMatch> slotMatches, Story story) throws Exception
  {
    for (SlotMatch slotMatch : slotMatches)
    {
      if (slotMatch.getValue() instanceof Number)
      {
        story.getVariablesState().set(slotMatch.getSlot().getName().toLowerCase(), slotMatch.getValue());
      }
      else
      {
        story.getVariablesState().set(slotMatch.getSlot().getName().toLowerCase(), slotMatch.getValue().toString());
      }
    }
  }

  private void getResponseText(Session session, CurrentResponse currentResponse, Story story, IntentMatch intentMatch, String preText)
    throws StoryException, Exception
  {
    // reset reprompt, hint and quick replies
    currentResponse.setReprompt(null);
    currentResponse.setHint(null);
    currentResponse.setResponseQuickReplies(null);

    // get the story output and build the reponse
    StringBuffer response = processStory(session, currentResponse, story, intentMatch);    
    
    // add any pretext if we have it
    preText = StringUtils.chomp(preText).trim(); // remove any trailing \n and trim to ensure we actually have some content
    if (StringUtils.isNotBlank(preText))
    {
      response.insert(0,"\n");
      response.insert(0,preText);      
    }   

    currentResponse.setResponseText(response.toString());
  }

  /** Processes the story until the next set of choices, triggering any InkFunctions along the way.
   * 
   * @param session The current session.
   * @param currentResponse The current response.
   * @param story The current story.
   * @param intentMatch The current intent match.
   * @return String buffer containing output.
   * @throws StoryException Thrown if there is an error. 
   * @throws Exception Thrown if there is an error.
   */
  private StringBuffer processStory(Session session, CurrentResponse currentResponse, Story story, IntentMatch intentMatch) throws StoryException, Exception 
  {
    StringBuffer response = new StringBuffer();   
       
    
    while (story.canContinue())
    {
      String line = story.Continue();
      
      // log any warnings
      // in theory we should be getting a warning if state wasn't restored correctly
      // so we can handle the issue. this is currently not happening. 
      // See TestSessionRestore for current hack
      if (story.hasWarning())
      {
    	 log.warn("Ink story has warnings: {}",  story.getCurrentWarnings());
      }
      
      // log any errors
      if (story.hasError())
      {
    	 log.error("Ink story has errors: {}",  story.getCurrentErrors());
      }
      
      processStoryLine(line,response,currentResponse, session, intentMatch, story);
    }
    
    // strip any leading \n deals with some ink inconsistencies such as in switch statements
    if (response.length() > 0 && response.charAt(0) == '\n')
    {
    	response.deleteCharAt(0);
    }

    // chop off last \n
    if (response.length() > 0 && response.charAt(response.length() - 1) == '\n')
    {
      response.setLength(response.length() - 1);
    }
    
	return response;
  }
  
  /** Processes a story line triggering any InkFunctions that are found.
   * 
   * @param line The story line to process.
   * @param response The response to populate.
   * @param currentResponse The current response.
   * @param session The current session.
   * @param intentMatch The current intent match.
   * @param story The current story.
   */
  private void processStoryLine(String line, StringBuffer response, CurrentResponse currentResponse, Session session, IntentMatch intentMatch, Story story)
  {
    log.debug("Line {}", line);
	
	String trimmedLine = line.trim();
	
	if (trimmedLine.startsWith("::"))
	{
	  String functionName = trimmedLine.split(" ")[0].substring(2).trim();
	  String param = trimmedLine.substring(functionName.length() + 2).trim();
	
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

  /**
   * Sets the default response for the bot. This is the bot's response if it doesn't understand what was said.
   * 
   * @param defaultResponses The new default bot responses.
   */
  private void setDefaultResponses(String[] defaultResponses)
  {
    this.defaultResponses = defaultResponses;
  }

  /**
   * Adds a InkBotFunction to the bot.
   * 
   * @param function The function to add.
   */
  private void addFunction(InkBotFunction function)
  {
    inkBotFunctions.put(function.getFunctionName().toLowerCase(), function);
  }

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
   * This method can be overridden to manipulate the Story object used by the bot just after the story state has been
   * loaded from the session. This method is useful for setting story variables based on external data.
   * 
   * @param story The story whose state has just been loaded.
   */
  protected void afterStoryStateLoaded(Story story)
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

  /**
   * Adds a global intent to the list of global intents for the bot.
   * 
   * @param intentName The name of the intent.
   * @param knotName The name of the knot to jump to when intent is triggered.
   */
  private void addGlobalIntent(String intentName, String knotName)
  {
    globalIntents.put(intentName, knotName);
  }

  /**
   * Sets the confused knot for the bot.
   * 
   * @param maxAttemptsBeforeConfused The number of failed attempts before the but is confused.
   * @param confusedKnotName The name of the knot to jump too when the bot is confused.
   */
  private void setConfusedKnot(int maxAttemptsBeforeConfused, String confusedKnotName)
  {
    this.maxAttemptsBeforeConfused = maxAttemptsBeforeConfused;
    this.confusedKnotName = confusedKnotName;
  }

}