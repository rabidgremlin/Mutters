package com.rabidgremlin.mutters.bot.statemachine;

import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.session.Session;
import com.rabidgremlin.mutters.state.IntentResponse;
import com.rabidgremlin.mutters.state.State;
import com.rabidgremlin.mutters.util.SessionUtils;

public class TaxiStatusState extends State
{

	public TaxiStatusState()
	{
		super("TaxiStatusState");
	}

	@Override
	public IntentResponse execute(IntentMatch intentMatch, Session session)
	{				
		return IntentResponse.newTellResponse("Your taxi is about 7 minutes away"); 
	}

}
