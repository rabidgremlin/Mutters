package com.rabidgremlin.mutters.bot.ink;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

import com.bladecoder.ink.runtime.Story;

public final class StoryUtils
{
	protected StoryUtils()
	{
		// utility class, hide constructor
	}

	public static Number getNumber(Story story, String varName)
	{
		// TODO figure out why numbers are sometimes strings....
		try
		{
			return (int) story.getVariablesState().get(varName);
		}
		catch (java.lang.ClassCastException e)
		{

			String numStr = (String) story.getVariablesState().get(varName);
			if (StringUtils.isEmpty(numStr))
			{
				return null;
			}

			// try parse as long first
			try
			{
				return Long.parseLong(numStr);
			}
			catch (NumberFormatException nfe1)
			{
				// try parse as decimal
				try
				{
					return Double.parseDouble(numStr);
				}
				catch (NumberFormatException nfe2)
				{
					return null;
				}
			}
		}
	}

	public static LocalDate getLocalDate(Story story, String varName)
	{
		String dateStr = (String) story.getVariablesState().get(varName);
		if (StringUtils.isEmpty(dateStr))
		{
			return null;
		}

		try
		{
			return LocalDate.parse(dateStr);
		}
		catch (IllegalArgumentException e)
		{
			return null;
		}
	}

	public static Boolean getBoolean(Story story, String varName)
	{
		Integer boolVal = (Integer) story.getVariablesState().get(varName);
		if (boolVal == null)
		{
			return null;
		}

		try
		{
			if (boolVal.equals(1))
			{
				return true;
			}
			return false;
		}
		catch (IllegalArgumentException e)
		{
			return null;
		}
	}

	public static String getString(Story story, String varName)
	{
		return (String) story.getVariablesState().get(varName);
	}
}
