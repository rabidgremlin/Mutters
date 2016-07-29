package com.rabidgremlin.mutters.examples.mathbot;

import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.session.Session;
import com.rabidgremlin.mutters.state.PreEventAction;
import com.rabidgremlin.mutters.util.SessionUtils;

public class SetNumberAsFirstNumber implements PreEventAction
{

	@Override
	public void execute(IntentMatch intentMatch, Session session)
	{

		Integer number = SessionUtils.getIntegerSlot(intentMatch, "number", null);

		if (number != null)
		{
			SessionUtils.setIntegerSlotIntoSession(session, "number1", number);
		}
	}

}