package com.rabidgremlin.mutters.core.session;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This class represents a user's session with the bot. It maintains the any state needed by the bot.
 * 
 * @author rabidgremlin
 *
 */
public class Session
    implements Serializable
{
  /** Map of attributes for the session. */
  private HashMap<String, Object> attributes = new HashMap<String, Object>();

  /**
   * Get the specified attribute.
   * 
   * @param attributeName The name of the attribute.
   * @return The attribute or null if it isn't in the map.
   */
  public Object getAttribute(String attributeName)
  {
    return attributes.get(attributeName.toLowerCase());
  }

  /**
   * Sets the value of the specified attribute.
   * 
   * @param attributeName The name of the attribute.
   * @param value The value of the attribute.
   */
  public void setAttribute(String attributeName, Object value)
  {
    attributes.put(attributeName.toLowerCase(), value);
  }

  /**
   * Removes the specified attribute from the session.
   * 
   * @param attributeName The name of the attribute.
   */
  public void removeAttribute(String attributeName)
  {
    attributes.remove(attributeName.toLowerCase());
  }

  /**
   * Resets the session. Removing all attributes.
   * 
   */
  public void reset()
  {
    attributes = new HashMap<String, Object>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.String#toString()
   * 
   */
  @Override
  public String toString()
  {
    return "Session [attributes=" + attributes + "]";
  }

}
