/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.state;

import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.session.Session;

public interface Guard
{
  boolean passes(final IntentMatch request, final Session session);

  String getDescription();
}
