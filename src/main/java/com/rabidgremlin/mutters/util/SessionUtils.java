package com.rabidgremlin.mutters.util;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.session.Session;

public class SessionUtils
{

	public static final String SLOT_PREFIX = "SLOT_JLA1974_";

	protected SessionUtils()
	{
		// utility class
	}

	public static void setReprompt(Session session, String reprompt)
	{
		session.setAttribute(SLOT_PREFIX + "0987654321REPROMPT1234567890", reprompt);
	}

	public static String getReprompt(Session session)
	{
		return (String)session.getAttribute(SLOT_PREFIX + "0987654321REPROMPT1234567890");
	}
	
	public static void setRepromptHint(Session session, String repromptHint)
	{
		session.setAttribute(SLOT_PREFIX + "0987654321REPROMPTHINT1234567890", repromptHint);
	}

	public static String getRepromptHint(Session session)
	{
		return (String)session.getAttribute(SLOT_PREFIX + "0987654321REPROMPTHINT1234567890");
	}

	public static void setNumberSlotIntoSession(Session session, String slotName, Number value)
	{
		session.setAttribute(SLOT_PREFIX + slotName, value);
	}

	public static void setStringSlotIntoSession(Session session, String slotName, String value)
	{
		session.setAttribute(SLOT_PREFIX + slotName, value);
	}

	public static void setLocalDateSlotIntoSession(Session session, String slotName, LocalDate value)
	{
		session.setAttribute(SLOT_PREFIX + slotName, value);
	}

	public static void setLocalTimeSlotIntoSession(Session session, String slotName, LocalTime value)
	{
		session.setAttribute(SLOT_PREFIX + slotName, value);
	}

	public static String getStringSlot(IntentMatch match, String slotName, String defaultValue)
	{
		if (match.getSlotMatch(slotName) != null && match.getSlotMatch(slotName).getValue() != null)
		{
			// TODO better cast handling
			return (String) match.getSlotMatch(slotName).getValue();
		}
		else
		{
			return defaultValue;
		}
	}

	public static Number getNumberSlot(IntentMatch match, String slotName, Number defaultValue)
	{
		if (match.getSlotMatch(slotName) != null && match.getSlotMatch(slotName).getValue() != null)
		{
			// TODO better cast handling
			return (Number) match.getSlotMatch(slotName).getValue();
		}
		else
		{
			return defaultValue;
		}
	}

	public static LocalDate getLocalDateSlot(IntentMatch match, String slotName, LocalDate defaultValue)
	{
		if (match.getSlotMatch(slotName) != null && match.getSlotMatch(slotName).getValue() != null)
		{
			// TODO better cast handling
			return (LocalDate) match.getSlotMatch(slotName).getValue();
		}
		else
		{
			return defaultValue;
		}
	}

	public static LocalTime getLocalTimeSlot(IntentMatch match, String slotName, LocalTime defaultValue)
	{
		if (match.getSlotMatch(slotName) != null && match.getSlotMatch(slotName).getValue() != null)
		{
			// TODO better cast handling
			return (LocalTime) match.getSlotMatch(slotName).getValue();
		}
		else
		{
			return defaultValue;
		}
	}

	public static void saveSlotsToSession(IntentMatch match, Session session)
	{
		for (SlotMatch slotMatch : match.getSlotMatches().values())
		{
			session.setAttribute(SLOT_PREFIX + slotMatch.getSlot().getName(), slotMatch.getValue());
		}
	}

	public static String getStringFromSlotOrSession(IntentMatch match, Session session, String slotName, String defaultValue)
	{
		String sessionValue = (String) session.getAttribute(SLOT_PREFIX + slotName);
		if (sessionValue != null)
		{
			return sessionValue;
		}

		return getStringSlot(match, slotName, defaultValue);
	}

	public static Number getNumberFromSlotOrSession(IntentMatch match, Session session, String slotName, Number defaultValue)
	{
		Number sessionValue = (Number) session.getAttribute(SLOT_PREFIX + slotName);
		if (sessionValue != null)
		{
			return sessionValue;
		}

		return getNumberSlot(match, slotName, defaultValue);
	}

	public static LocalDate getLocalDateFromSlotOrSession(IntentMatch match, Session session, String slotName, LocalDate defaultValue)
	{
		LocalDate sessionValue = (LocalDate) session.getAttribute(SLOT_PREFIX + slotName);
		if (sessionValue != null)
		{
			return sessionValue;
		}

		return getLocalDateSlot(match, slotName, defaultValue);
	}

	public static LocalTime getLocalTimeFromSlotOrSession(IntentMatch match, Session session, String slotName, LocalTime defaultValue)
	{
		LocalTime sessionValue = (LocalTime) session.getAttribute(SLOT_PREFIX + slotName);
		if (sessionValue != null)
		{
			return sessionValue;
		}

		return getLocalTimeSlot(match, slotName, defaultValue);
	}

}
