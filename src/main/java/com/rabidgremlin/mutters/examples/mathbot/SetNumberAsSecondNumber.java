package com.rabidgremlin.mutters.examples.mathbot;

import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.session.Session;
import com.rabidgremlin.mutters.state.PreEventAction;
import com.rabidgremlin.mutters.util.SessionUtils;

public class SetNumberAsSecondNumber
    implements PreEventAction
{

  @Override
  public void execute(IntentMatch intentMatch, Session session)
  {

    Number number = SessionUtils.getNumberSlot(intentMatch, "number", null);

    if (number != null)
    {
      SessionUtils.setNumberSlotIntoSession(session, "number2", number);
    }
  }

}
