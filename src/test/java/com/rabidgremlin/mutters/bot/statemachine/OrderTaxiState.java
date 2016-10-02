package com.rabidgremlin.mutters.bot.statemachine;

import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.session.Session;
import com.rabidgremlin.mutters.state.IntentResponse;
import com.rabidgremlin.mutters.state.State;
import com.rabidgremlin.mutters.util.SessionUtils;

public class OrderTaxiState
    extends State
{

  public OrderTaxiState()
  {
    super("OrderTaxiState");
  }

  @Override
  public IntentResponse execute(IntentMatch intentMatch, Session session)
  {

    // save or slots into session
    SessionUtils.saveSlotsToSession(intentMatch, session);

    // get the numbers
    String address = SessionUtils.getStringFromSlotOrSession(intentMatch, session, "address", null);

    return IntentResponse.newTellResponse(String.format("Taxi %s is on its way", Integer.toHexString(address.hashCode()).substring(0, 4)));
  }

}
