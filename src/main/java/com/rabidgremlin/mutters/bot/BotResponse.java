package com.rabidgremlin.mutters.bot;

import java.util.Collections;
import java.util.Map;

public class BotResponse
{

	private String response;	
	private String hint;
	private boolean askResponse;
	private String action;
	private Map<String, Object> actionParams;

	public BotResponse(String response, String hint, boolean askResponse, String action, Map<String, Object> actionParams)
	{
		this.response = response;		
		this.hint = hint;
		this.askResponse = askResponse;
		this.action = action;
		this.actionParams = actionParams;
	}

	public String getResponse()
	{
		return response;
	}

	public boolean isAskResponse()
	{
		return askResponse;
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
	
	public String getHint()
	{
		return hint;
	}

}
