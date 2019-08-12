package com.rabidgremlin.mutters.bot.statemachine;

import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.session.Session;
import com.rabidgremlin.mutters.core.util.SessionUtils;
import com.rabidgremlin.mutters.state.Guard;

public class HaveAddressGuard
    implements Guard
{

  @Override
  public boolean passes(final IntentMatch match, final Session session)
  {

    // save or slots into session
    SessionUtils.saveSlotsToSession(match, session);

    // get the address
    String address = SessionUtils.getStringFromSlotOrSession(match, session, "address", null);

    // do we have the address
    return address != null;
  }

  @Override
  public String getDescription()
  {
    return "Have address";
  }

}
