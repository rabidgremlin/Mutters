package com.rabidgremlin.mutters.bot.statemachine;

import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.session.Session;
import com.rabidgremlin.mutters.state.IntentResponse;
import com.rabidgremlin.mutters.state.State;

public class GetAddressState
    extends State
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
