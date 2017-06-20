package com.rabidgremlin.mutters.bot.statemachine;

import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.session.Session;
import com.rabidgremlin.mutters.state.IntentResponse;
import com.rabidgremlin.mutters.state.State;

public class TaxiStatusState
    extends State
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
