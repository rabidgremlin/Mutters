package com.rabidgremlin.mutters.examples.mathbot;

import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.session.Session;
import com.rabidgremlin.mutters.state.IntentResponse;
import com.rabidgremlin.mutters.state.State;
import com.rabidgremlin.mutters.util.SessionUtils;

public class SecondNumberState
    extends State
{

  public SecondNumberState()
  {
    super("SecondNumberState");
  }

  @Override
  public IntentResponse execute(IntentMatch intentMatch, Session session)
  {

    // save or slots into session
    SessionUtils.saveSlotsToSession(intentMatch, session);

    // get the numbers
    Number number1 = SessionUtils.getNumberFromSlotOrSession(intentMatch, session, "number1", null);

    return IntentResponse.newAskResponse(String.format("Add %s to which number?", number1.toString()));

  }

}
