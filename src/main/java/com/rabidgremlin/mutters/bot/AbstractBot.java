package com.rabidgremlin.mutters.bot;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.session.Session;
import com.rabidgremlin.mutters.state.IntentResponse;
import com.rabidgremlin.mutters.state.StateMachine;

public abstract class AbstractBot
{
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
		String responseText = defaultResponse;

		IntentMatch intentMatch = matcher.match(messageText, context);

		if (intentMatch != null)
		{
			IntentResponse response = stateMachine.trigger(intentMatch, session);
			responseText = response.getResponse();

			if (response.isSessionEnded())
			{
				session.reset();
			}
		}

		return new BotResponse(responseText);
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
