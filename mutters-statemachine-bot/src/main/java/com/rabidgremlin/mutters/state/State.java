package com.rabidgremlin.mutters.state;

import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.session.Session;

public abstract class State
{

  protected String name;

  public State(String name)
  {
    this.name = name;
  }

  public abstract IntentResponse execute(final IntentMatch intentMatch, final Session session);

  public String getName()
  {
    return name;
  }

  @Override
  public String toString()
  {
    return "State [name=" + name + "]";
  }

}
