package com.rabidgremlin.mutters.bot.statemachine;

import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.session.Session;
import com.rabidgremlin.mutters.state.IntentResponse;
import com.rabidgremlin.mutters.state.State;
import com.rabidgremlin.mutters.util.SessionUtils;

public class GetAddressState extends State
{

	public GetAddressState()
	{
		super("GetAddressState");
	}

	@Override
	public IntentResponse execute(IntentMatch intentMatch, Session session)
	{				
		return IntentResponse.newAskResponse("What is the pick up address ?"); 
	}

}
