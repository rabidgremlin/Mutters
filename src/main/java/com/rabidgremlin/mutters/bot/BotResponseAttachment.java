package com.rabidgremlin.mutters.bot;

import java.util.HashMap;
import java.util.Map;

import edu.emory.mathcs.backport.java.util.Collections;

public class BotResponseAttachment
{
  // the type of the attachment
  private String type = null;

  // the parameters for the attachment
  private Map<String, Object> parameters;

  public BotResponseAttachment(String type)
  {
    super();
    this.type = type;
  }

  @SuppressWarnings("unchecked")
  public Map<String, Object> getParameters()
  {
    if (parameters == null)
    {
      return null;
    }

    return Collections.unmodifiableMap(parameters);
  }

  public void addParameters(String name, Object value)
  {
    if (parameters == null)
    {
      parameters = new HashMap<String, Object>();
    }

    parameters.put(name, value);
  }

  public String getType()
  {
    return type;
  }
}
