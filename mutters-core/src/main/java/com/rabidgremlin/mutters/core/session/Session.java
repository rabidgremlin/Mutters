/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core.session;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This class represents a user's session with the bot. It maintains the any
 * state needed by the bot. The session holds two types of attributes. Normal
 * attributes are typically stored whilst a conversation takes place. They are
 * removed from the session when the reset() method is called which normally
 * takes place when a conversation ends.
 * 
 * Long term attributes are designed to hang around as long as the session. They
 * are typically used to store data across conversations in the same session.
 * 
 * @author rabidgremlin
 *
 */
public class Session implements Serializable
{
  /** Map of attributes for the session. */
  private HashMap<String, Object> attributes = new HashMap<>();

  private HashMap<String, Object> longTermAttributes = new HashMap<>();

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
   * @param value         The value of the attribute.
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
   * Resets the session. Removing all attributes. Note: Long term attributes are
   * not removed from the session.
   * 
   */
  public void reset()
  {
    attributes = new HashMap<>();
  }

  /**
   * Get the specified long term attribute.
   * 
   * @param attributeName The name of the attribute.
   * @return The attribute or null if it isn't in the map.
   */
  public Object getLongTermAttribute(String attributeName)
  {
    return longTermAttributes.get(attributeName.toLowerCase());
  }

  /**
   * Sets the value of the specified long term attribute.
   * 
   * @param attributeName The name of the attribute.
   * @param value         The value of the attribute.
   */
  public void setLongTermAttribute(String attributeName, Object value)
  {
    longTermAttributes.put(attributeName.toLowerCase(), value);
  }

  /**
   * Removes the specified long term attribute from the session.
   * 
   * @param attributeName The name of the attribute.
   */
  public void removeLongTermAttribute(String attributeName)
  {
    longTermAttributes.remove(attributeName.toLowerCase());
  }

  /**
   * Resets the session. Removing all attributes (both normal and long term).
   * 
   */
  public void resetAll()
  {
    attributes = new HashMap<>();
    longTermAttributes = new HashMap<>();
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
    return "Session [attributes=" + attributes + ", longTermAttributes=" + longTermAttributes + "]";
  }

}
