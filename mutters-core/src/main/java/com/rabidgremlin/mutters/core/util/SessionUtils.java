package com.rabidgremlin.mutters.core.util;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.session.Session;

import java.util.List;

/**
 * This utility class provides methods working with a Session object.
 * 
 * @author rabidgremlin
 *
 */
public class SessionUtils
{
  /** Logger.*/
  private static final Logger LOG = LoggerFactory.getLogger(SessionUtils.class);
  
  /** Prefix for slot values stored in session to avoid any name collisions. */
  public static final String SLOT_PREFIX = "SLOT_JLA1974_";

  protected SessionUtils()
  {
    // utility class
  }

  /**
   * Removes a stored slot value from a session.
   * 
   * @param session The session.
   * @param slotName The name of the slot.
   */
  public static void removeSlotfromSession(Session session, String slotName)
  {
    session.removeAttribute(SLOT_PREFIX + slotName);
  }

  /**
   * Stores a reprompt string in a session.
   * 
   * @param session The session.
   * @param reprompt The reprompt string.
   */
  public static void setReprompt(Session session, String reprompt)
  {
    session.setAttribute(SLOT_PREFIX + "0987654321REPROMPT1234567890", reprompt);
  }

  /**
   * Gets the current reprompt string from the session.
   * 
   * @param session The session.
   * @return The reprompt string or null if there is no reprompt string.
   */
  public static String getReprompt(Session session)
  {
    return (String) session.getAttribute(SLOT_PREFIX + "0987654321REPROMPT1234567890");
  }

  /**
   * Stores the reprompt quick replies in the session.
   *
   * @param session The session.
   * @param repromptQuickReplies The reprompt quick replies.
   */
  public static void setRepromptQuickReplies(Session session, List<String> repromptQuickReplies) {
    session.setAttribute(SLOT_PREFIX + "0987654321REPROMPTQUICKREPLIES1234567890", repromptQuickReplies);
  }

  /**
   * Gets the current reprompt quick replies from the session.
   *
   * @param session The session.
   * @return The reprompt quick replies from the session.
   */
  public static List<String> getRepromptQuickReplies(Session session)
  {
    return (List<String>) session.getAttribute(SLOT_PREFIX + "0987654321REPROMPTQUICKREPLIES1234567890");
  }

  /**
   * Stores a reprompt hint string in a session.
   * 
   * @param session The session.
   * @param repromptHint The reprompt hint.
   */
  public static void setRepromptHint(Session session, String repromptHint)
  {
    session.setAttribute(SLOT_PREFIX + "0987654321REPROMPTHINT1234567890", repromptHint);
  }

  /**
   * Gets the current reprompt hint from the session.
   * 
   * @param session The session.
   * @return The current reprompt hint or null if there is no reprompt hint.
   */
  public static String getRepromptHint(Session session)
  {
    return (String) session.getAttribute(SLOT_PREFIX + "0987654321REPROMPTHINT1234567890");
  }

  /**
   * Stores a Number slot value in the session.
   * 
   * @param session The session.
   * @param slotName The name of the slot.
   * @param value The value to store.
   */
  public static void setNumberSlotIntoSession(Session session, String slotName, Number value)
  {
    session.setAttribute(SLOT_PREFIX + slotName, value);
  }

  /**
   * Stores a String slot value in the session.
   * 
   * @param session The session.
   * @param slotName The name of the slot.
   * @param value The value to store.
   */
  public static void setStringSlotIntoSession(Session session, String slotName, String value)
  {
    session.setAttribute(SLOT_PREFIX + slotName, value);
  }

  /**
   * Stores a jodaTime LocalDate slot value in the session.
   * 
   * @param session The session.
   * @param slotName The name of the slot.
   * @param value The value to store.
   */
  public static void setLocalDateSlotIntoSession(Session session, String slotName,
    LocalDate value)
  {
    session.setAttribute(SLOT_PREFIX + slotName, value);
  }

  /**
   * Stores a jodaTime LocalTime slot value in the session.
   * 
   * @param session The session.
   * @param slotName The name of the slot.
   * @param value The value to store.
   */
  public static void setLocalTimeSlotIntoSession(Session session, String slotName,
    LocalTime value)
  {
    session.setAttribute(SLOT_PREFIX + slotName, value);
  }

  /**
   * Gets a String based slot value from an intent match.
   * 
   * @param match The intent match to get the slot value from.
   * @param slotName The name of the slot.
   * @param defaultValue The default value to use if no slot found.
   * @return The string value.
   */
  public static String getStringSlot(IntentMatch match, String slotName, String defaultValue)
  {
    if (match.getSlotMatch(slotName) != null && match.getSlotMatch(slotName).getValue() != null)
    {
      try
      {
        return (String) match.getSlotMatch(slotName).getValue();
      }
      catch(ClassCastException e)
      {
    	  // failed to cast so assume invalid string and return default
    	  LOG.warn("Non String value: {} found in slot {}", match.getSlotMatch(slotName).getValue(), slotName);
    	  return defaultValue;
      }
    }
    else
    {
      return defaultValue;
    }
  }

  /**
   * Gets a Number based slot value from an intent match.
   * 
   * @param match The intent match to get the slot value from.
   * @param slotName The name of the slot.
   * @param defaultValue The default value to use if no slot found.
   * @return The string value.
   */
  public static Number getNumberSlot(IntentMatch match, String slotName, Number defaultValue)
  {
    if (match.getSlotMatch(slotName) != null && match.getSlotMatch(slotName).getValue() != null)
    {
      try
      {
        return (Number) match.getSlotMatch(slotName).getValue();
      }
      catch(ClassCastException e)
      {
    	  // failed to cast so assume invalid number and return default
    	  LOG.warn("Non Number value: {} found in slot {}", match.getSlotMatch(slotName).getValue(), slotName);
    	  return defaultValue;
      }
    }
    else
    {
      return defaultValue;
    }
  }

  /**
   * Gets a jodaTime LocalDate based slot value from an intent match.
   * 
   * @param match The intent match to get the slot value from.
   * @param slotName The name of the slot.
   * @param defaultValue The default value to use if no slot found.
   * @return The local date value.
   */
  public static LocalDate getLocalDateSlot(IntentMatch match, String slotName,
    LocalDate defaultValue)
  {
    if (match.getSlotMatch(slotName) != null && match.getSlotMatch(slotName).getValue() != null)
    {
      try
      {
        return (LocalDate) match.getSlotMatch(slotName).getValue();
      }
      catch(ClassCastException e)
      {
    	  // failed to cast so assume invalid localdate and return default
    	  LOG.warn("Non LocalDate value: {} found in slot {}", match.getSlotMatch(slotName).getValue(), slotName);
    	  return defaultValue;
      }
    }
    else
    {
      return defaultValue;
    }
  }

  /**
   * Gets a jodaTime LocalTime based slot value from an intent match.
   * 
   * @param match The intent match to get the slot value from.
   * @param slotName The name of the slot.
   * @param defaultValue The default value to use if no slot found.
   * @return The local time value.
   */
  public static LocalTime getLocalTimeSlot(IntentMatch match, String slotName,
    LocalTime defaultValue)
  {
    if (match.getSlotMatch(slotName) != null && match.getSlotMatch(slotName).getValue() != null)
    {
      try
      {
        return (LocalTime) match.getSlotMatch(slotName).getValue();
      }
      catch(ClassCastException e)
      {
    	  // failed to cast so assume invalid localtime and return default
    	  LOG.warn("Non LocalTime value: {} found in slot {}", match.getSlotMatch(slotName).getValue(), slotName);
    	  return defaultValue;
      }
    }
    else
    {
      return defaultValue;
    }
  }

  /**
   * Saves all the matched slots for an IntentMatch into the session.
   * 
   * @param match The intent match.
   * @param session The session.
   */
  public static void saveSlotsToSession(IntentMatch match, Session session)
  {
    for (SlotMatch slotMatch : match.getSlotMatches().values())
    {
      session.setAttribute(SLOT_PREFIX + slotMatch.getSlot().getName(), slotMatch.getValue());
    }
  }

  /**
   * Gets a String value from the session (if it exists) or the slot (if a match exists).
   * 
   * @param match The intent match.
   * @param session The session.
   * @param slotName The name of the slot.
   * @param defaultValue The default value if not value found in the session or slot.
   * @return The string value.
   */
  public static String getStringFromSlotOrSession(IntentMatch match, Session session,
    String slotName, String defaultValue)
  {
    String sessionValue = (String) session.getAttribute(SLOT_PREFIX + slotName);
    if (sessionValue != null)
    {
      return sessionValue;
    }

    return getStringSlot(match, slotName, defaultValue);
  }

  /**
   * Gets a Number value from the session (if it exists) or the slot (if a match exists).
   * 
   * @param match The intent match.
   * @param session The session.
   * @param slotName The name of the slot.
   * @param defaultValue The default value if not value found in the session or slot.
   * @return The number value.
   */
  public static Number getNumberFromSlotOrSession(IntentMatch match, Session session,
    String slotName, Number defaultValue)
  {
    Number sessionValue = (Number) session.getAttribute(SLOT_PREFIX + slotName);
    if (sessionValue != null)
    {
      return sessionValue;
    }

    return getNumberSlot(match, slotName, defaultValue);
  }

  /**
   * Gets a jodaTime LocalDate value from the session (if it exists) or the slot (if a match exists).
   * 
   * @param match The intent match.
   * @param session The session.
   * @param slotName The name of the slot.
   * @param defaultValue The default value if not value found in the session or slot.
   * @return The local date value.
   */
  public static LocalDate getLocalDateFromSlotOrSession(IntentMatch match, Session session,
    String slotName, LocalDate defaultValue)
  {
    LocalDate sessionValue = (LocalDate) session.getAttribute(SLOT_PREFIX + slotName);
    if (sessionValue != null)
    {
      return sessionValue;
    }

    return getLocalDateSlot(match, slotName, defaultValue);
  }

  /**
   * Gets a jodaTime LocalTime value from the session (if it exists) or the slot (if a match exists).
   * 
   * @param match The intent match.
   * @param session The session.
   * @param slotName The name of the slot.
   * @param defaultValue The default value if not value found in the session or slot.
   * @return The local time value.
   */
  public static LocalTime getLocalTimeFromSlotOrSession(IntentMatch match, Session session,
    String slotName, LocalTime defaultValue)
  {
    LocalTime sessionValue = (LocalTime) session.getAttribute(SLOT_PREFIX + slotName);
    if (sessionValue != null)
    {
      return sessionValue;
    }

    return getLocalTimeSlot(match, slotName, defaultValue);
  }

}