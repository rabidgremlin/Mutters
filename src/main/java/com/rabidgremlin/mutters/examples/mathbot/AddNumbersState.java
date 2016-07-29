package com.rabidgremlin.mutters.examples.mathbot;

import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.session.Session;
import com.rabidgremlin.mutters.state.IntentResponse;
import com.rabidgremlin.mutters.state.State;
import com.rabidgremlin.mutters.util.SessionUtils;

public class AddNumbersState extends State
{

	public AddNumbersState()
	{
		super("AddNumbersState");
	}

	@Override
	public IntentResponse execute(IntentMatch intentMatch, Session session)
	{

		// save or slots into session
		SessionUtils.saveSlotsToSession(intentMatch, session);

		// get the numbers
		Integer number1 = SessionUtils.getIntegerFromSlotOrSession(intentMatch, session, "number1", null);
		Integer number2 = SessionUtils.getIntegerFromSlotOrSession(intentMatch, session, "number2", null);

		return IntentResponse.newTellResponse(String.format("%d + %d is %d", number1, number2, number1 + number2));
	}

}
