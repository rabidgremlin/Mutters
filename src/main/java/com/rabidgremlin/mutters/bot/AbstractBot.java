package com.rabidgremlin.mutters.bot;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.session.Session;
import com.rabidgremlin.mutters.state.IntentResponse;
import com.rabidgremlin.mutters.state.StateMachine;
import com.rabidgremlin.mutters.util.SessionUtils;

public abstract class AbstractBot
{
	private Logger log = LoggerFactory.getLogger(AbstractBot.class);
	protected IntentMatcher matcher;
	protected StateMachine stateMachine;
	protected String defaultResponse = "Pardon?";

	public AbstractBot()
	{
		matcher = setUpIntents();
		stateMachine = setUpStateMachine();
	}

	public BotResponse respond(Session session, Context context, String messageText)
	{
		log.debug("session: {} context: {} messageText: {}", new Object[] { session, context, messageText });
		try
		{
			String responseText = defaultResponse;
			String hint = null;
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
					}
					else
					{
						SessionUtils.setReprompt(session, defaultResponse + " " + responseText);
					}
				}
			}

			return new BotResponse(responseText, hint, askResponse, reponseAction, responseActionParams);
		}
		catch (IllegalStateException e)
		{
			log.warn("Hit illegal state", e);

			String repromptText = SessionUtils.getReprompt(session);
			if (repromptText == null)
			{
				repromptText = defaultResponse;
			}
			return new BotResponse(repromptText, null, true, null, null);
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
