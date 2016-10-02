package com.rabidgremlin.mutters.examples.mathbot;

import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.session.Session;
import com.rabidgremlin.mutters.state.IntentResponse;
import com.rabidgremlin.mutters.state.State;
import com.rabidgremlin.mutters.util.SessionUtils;

public class AddNumbersState
    extends State
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
    Number number1 = SessionUtils.getNumberFromSlotOrSession(intentMatch, session, "number1", null);
    Number number2 = SessionUtils.getNumberFromSlotOrSession(intentMatch, session, "number2", null);

    return IntentResponse
        .newTellResponse(String.format("%s + %s is %s", number1.toString(), number2.toString(), (number1.doubleValue() + number2.doubleValue())).toString());
  }

}
