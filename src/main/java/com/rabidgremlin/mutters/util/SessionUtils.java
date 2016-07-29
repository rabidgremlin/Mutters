package com.rabidgremlin.mutters.util;

import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.session.Session;

public class SessionUtils
{

	private static final String SLOT_PREFIX = "SLOT_JLA1974_";

	protected SessionUtils()
	{
		// utility class
	}

	public static void setNumberSlotIntoSession(Session session, String slotName, Number value)
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

}
