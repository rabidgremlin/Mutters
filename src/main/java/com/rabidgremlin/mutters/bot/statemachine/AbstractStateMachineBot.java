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

public abstract class AbstractStateMachineBot
    implements Bot
{
  private Logger log = LoggerFactory.getLogger(AbstractStateMachineBot.class);

  protected IntentMatcher matcher;

  protected StateMachine stateMachine;

  protected String defaultResponse = "Pardon?";

  public AbstractStateMachineBot()
  {
    matcher = setUpIntents();
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

      IntentMatch intentMatch = matcher.match(messageText, context);

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

  public String getDefaultResponse()
  {
    return defaultResponse;
  }

  public void setDefaultResponse(String defaultResponse)
  {
    this.defaultResponse = defaultResponse;
  }

  public abstract IntentMatcher setUpIntents();

  public abstract StateMachine setUpStateMachine();
}
