package com.rabidgremlin.mutters.bot.statemachine;

import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.session.Session;
import com.rabidgremlin.mutters.state.IntentResponse;
import com.rabidgremlin.mutters.state.State;

public class CancelTaxiState
    extends State
{

  public CancelTaxiState()
  {
    super("CancelTaxiState");
  }

  @Override
  public IntentResponse execute(IntentMatch intentMatch, Session session)
  {
    return IntentResponse.newTellResponse("Your taxi has been cancelled");
  }

}
