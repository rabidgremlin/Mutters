package com.rabidgremlin.mutters.examples.mathbot;

import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.session.Session;
import com.rabidgremlin.mutters.state.Guard;
import com.rabidgremlin.mutters.util.SessionUtils;

public class HaveTwoNumbersGuard implements Guard {

	@Override
	public boolean passes(final IntentMatch match, final Session session) {

		// save or slots into session
		SessionUtils.saveSlotsToSession(match, session);

		// get the numbers
		Integer number1 = SessionUtils.getIntegerFromSlotOrSession(match, session, "number1", null);
		Integer number2 = SessionUtils.getIntegerFromSlotOrSession(match, session, "number2", null);

		// do we have both numbers
		return number1 != null && number2 != null;
	}

	@Override
	public String getDescription() {
		return "Has two numbers";
	}

}
