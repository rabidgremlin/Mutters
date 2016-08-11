package com.rabidgremlin.mutters.state;

import java.util.Collections;
import java.util.Map;

public class IntentResponse
{

	private boolean sessionEnded;
	private String response;
	private String action;
	private Map<String, Object> actionParams;

	public static IntentResponse newAskResponse(String response)
	{
		return new IntentResponse(false, response, null, null);
	}

	public static IntentResponse newAskResponse(String response, String action, Map<String, Object> actionParams)
	{
		return new IntentResponse(false, response, action, actionParams);
	}

	public static IntentResponse newTellResponse(String response)
	{
		return new IntentResponse(true, response, null, null);
	}

	public static IntentResponse newTellResponse(String response, String action, Map<String, Object> actionParams)
	{
		return new IntentResponse(true, response, action, actionParams);
	}

	public IntentResponse(boolean sessionEnded, String response, String action, Map<String, Object> actionParams)
	{

		this.sessionEnded = sessionEnded;
		this.response = response;
		this.action = action;
		this.actionParams = actionParams;
	}

	public boolean isSessionEnded()
	{
		return sessionEnded;
	}

	public String getResponse()
	{
		return response;
	}

	public String getAction()
	{
		return action;
	}

	public Map<String, Object> getActionParams()
	{
		if (actionParams == null)
		{
			return null;
		}

		return Collections.unmodifiableMap(actionParams);
	}

}
