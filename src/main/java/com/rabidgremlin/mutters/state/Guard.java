package com.rabidgremlin.mutters.state;

import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.session.Session;

public interface Guard
{
  public boolean passes(final IntentMatch request, final Session session);

  public String getDescription();
}
