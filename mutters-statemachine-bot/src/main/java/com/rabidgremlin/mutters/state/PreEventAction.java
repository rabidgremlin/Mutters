package com.rabidgremlin.mutters.state;

import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.session.Session;

public interface PreEventAction
{
  void execute(final IntentMatch intentMatch, final Session session);
}
