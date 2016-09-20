package com.rabidgremlin.mutters.bot.ink;

import java.util.Map;

/**
 * Simple class to hold data for current response.
 * 
 * @author rabidgremlin
 *
 */
public class CurrentResponse
{
	String responseText;
	String hint;
	String reprompt = null;
	String reponseAction = null;
	Map<String, Object> responseActionParams = null;
	boolean askResponse = true;

	@Override
	public String toString()
	{
		return "CurrentResponse [responseText=" + responseText + ", hint=" + hint + ", reprompt=" + reprompt + ", reponseAction=" + reponseAction + ", responseActionParams=" + responseActionParams
		        + ", askResponse=" + askResponse + "]";
	}

}
