/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core.bot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class holds the details of an attachment to a response. Attachments can
 * be used to return rich media info or other meta data for each bot response.
 * 
 * Each attachment must have a type and optionally can have one or more
 * parameters.
 * 
 * @author rabidgremlin
 *
 */
public class BotResponseAttachment
{
  // the type of the attachment
  private String type = null;

  // the parameters for the attachment
  private Map<String, Object> parameters;

  /**
   * Constructor.
   * 
   * @param type The type of the attachment.
   */
  public BotResponseAttachment(String type)
  {
    this.type = Objects.requireNonNull(type);
  }

  /**
   * Gets the parameters for the attachment.
   * 
   * @return The map of parameters.
   */
  public Map<String, Object> getParameters()
  {
    if (parameters == null)
    {
      return null;
    }

    return Collections.unmodifiableMap(parameters);
  }

  /**
   * Adds a parameter to the attachment.
   * 
   * @param name  The name of the parameter.
   * @param value The value of the parameter.
   */
  public void addParameters(String name, Object value)
  {
    if (parameters == null)
    {
      parameters = new HashMap<String, Object>();
    }

    parameters.put(name, value);
  }

  /**
   * Returns the type of the attachment.
   * 
   * @return The type of the attachment.
   */
  public String getType()
  {
    return type;
  }
}
