package com.rabidgremlin.mutters.bot;

public class BotResponse
{

	private String response;
	private boolean askResponse;

	public BotResponse(String response,boolean askResponse)
	{
		this.response = response;
		this.askResponse = askResponse;
	}

	public String getResponse()
	{
		return response;
	}

	public boolean isAskResponse()
	{
		return askResponse;
	}
	
	

}
