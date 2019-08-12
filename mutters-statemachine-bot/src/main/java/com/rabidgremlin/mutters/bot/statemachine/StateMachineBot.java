package com.rabidgremlin.mutters.bot.statemachine;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.core.bot.Bot;
import com.rabidgremlin.mutters.core.bot.BotException;
import com.rabidgremlin.mutters.core.bot.BotResponse;
import com.rabidgremlin.mutters.core.bot.BotResponseAttachment;
import com.rabidgremlin.mutters.core.session.Session;
import com.rabidgremlin.mutters.core.util.SessionUtils;
import com.rabidgremlin.mutters.state.IntentResponse;
import com.rabidgremlin.mutters.state.StateMachine;

/**
 * This is the base bot class for bots that use a state machine to manage conversation state.
 * 
 * 
 * @author rabidgremlin
 *
 */
public abstract class StateMachineBot<T extends StateMachineBotConfiguration>
    implements Bot
{
  /** Logger for the bot. */
  private Logger log = LoggerFactory.getLogger(StateMachineBot.class);

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
  public StateMachineBot(T configuration)
  {
    // get the matcher set up
    matcher = configuration.getIntentMatcher();

    // get the state machine set up
    stateMachine = configuration.getStateMachine();

    // set default response if supplied
    String defaultResponse = configuration.getDefaultResponse();
    if (defaultResponse != null)
    {
      setDefaultResponse(defaultResponse);
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
      List<BotResponseAttachment> responseAttachments = null;
      List<String> responseQuickReplies = null;
      boolean askResponse = true;

      // TODO: Implement intent filtering via expected intents
      // TODO: Implement debug values
      IntentMatch intentMatch = matcher.match(messageText, context, null, null);

      if (intentMatch != null)
      {
        IntentResponse response = stateMachine.trigger(intentMatch, session);
        responseText = response.getResponse();
        hint = response.getHint();
        reprompt = response.getReprompt();
        responseAttachments = response.getAttachments();
        responseQuickReplies = response.getQuickReplies();

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

      return new BotResponse(responseText, hint, askResponse, responseAttachments, responseQuickReplies, null);
    }
    catch (IllegalStateException e)
    {
      throw new BotException("Hit illegal state", e);
    }
  }

  /**
   * Sets the default response for the bot. This is the bot's response if it doesn't understand what was said.
   * 
   * @param defaultResponse The new default bot response.
   */
  private void setDefaultResponse(String defaultResponse)
  {
    this.defaultResponse = defaultResponse;
  }

}
