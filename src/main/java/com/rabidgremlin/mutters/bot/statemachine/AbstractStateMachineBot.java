package com.rabidgremlin.mutters.bot.statemachine;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabidgremlin.mutters.bot.Bot;
import com.rabidgremlin.mutters.bot.BotException;
import com.rabidgremlin.mutters.bot.BotResponse;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.session.Session;
import com.rabidgremlin.mutters.state.IntentResponse;
import com.rabidgremlin.mutters.state.StateMachine;
import com.rabidgremlin.mutters.util.SessionUtils;

/**
 * This is the base bot class for bots that use a state machine to manage conversation state.
 * 
 * See the <a href=
 * "https://github.com/rabidgremlin/Mutters/blob/master/src/test/java/com/rabidgremlin/mutters/bot/statemachine/TaxiStateMachineBot.java"
 * target="_blank">TaxiStateMachineBot</a> for an example of how this type of bot works.
 * 
 * @author rabidgremlin
 *
 */
public abstract class AbstractStateMachineBot
    implements Bot
{
  /** Logger for the bot. */
  private Logger log = LoggerFactory.getLogger(AbstractStateMachineBot.class);

  /** The intent matcher for the bot. */
  protected IntentMatcher matcher;

  /** The state machine for the bot. */
  protected StateMachine stateMachine;

  /** Default response for when the bot cannot figure out what was said to it. */
  protected String defaultResponse = "Pardon?";

  /**
   * Constructs the bot.
   * 
   */
  public AbstractStateMachineBot()
  {
    // get the matcher set up
    matcher = setUpIntents();

    // get the state machine set up
    stateMachine = setUpStateMachine();
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
    log.debug("session: {} context: {} messageText: {}",
        new Object[]{ session, context, messageText });

    // set up default response in case bot has issue processing input
    String responseText = SessionUtils.getReprompt(session);
    if (responseText == null)
    {
      responseText = defaultResponse;
    }

    // default to reprompt hint if we have one
    String hint = SessionUtils.getRepromptHint(session);

    try
    {
      String reprompt = null;
      String reponseAction = null;
      Map<String, Object> responseActionParams = null;
      boolean askResponse = true;

      // TODO: Implement intent filtering via expected intents
      IntentMatch intentMatch = matcher.match(messageText, context, null);

      if (intentMatch != null)
      {
        IntentResponse response = stateMachine.trigger(intentMatch, session);
        responseText = response.getResponse();
        hint = response.getHint();
        reprompt = response.getReprompt();
        reponseAction = response.getAction();
        responseActionParams = response.getActionParams();

        if (response.isSessionEnded())
        {
          session.reset();
          askResponse = false;
        }
        else
        {
          if (reprompt != null)
          {
            SessionUtils.setReprompt(session, reprompt);
            SessionUtils.setRepromptHint(session, hint);
          }
          else
          {
            SessionUtils.setReprompt(session, defaultResponse + " " + responseText);
            SessionUtils.setRepromptHint(session, null);
          }
        }
      }

      return new BotResponse(responseText, hint, askResponse, reponseAction, responseActionParams);
    }
    catch (IllegalStateException e)
    {
      throw new BotException("Hit illegal state", e);
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
   * This method should create and populate an IntentMatcher. This IntentMatcher will be used by the bot to determine
   * what a user said to the bot.
   * 
   * @return The IntentMatcher for the bot to use.
   */
  public abstract IntentMatcher setUpIntents();

  /**
   * This method should create the StateMachine used by the bot to track the state of the user's conversation.
   * 
   * @return The StateMachine for the bot to use.
   */
  public abstract StateMachine setUpStateMachine();
}
