package com.rabidgremlin.mutters.core;

import java.util.Locale;
import java.util.TimeZone;

// provides context of user's locale and timezone for slots to use when matching
public class Context
{

	private Locale locale = Locale.getDefault();
	private TimeZone timeZone = TimeZone.getDefault();

	public Locale getLocale()
	{
		return locale;
	}

	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}

	public TimeZone getTimeZone()
	{
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone)
	{
		this.timeZone = timeZone;
	}

}
