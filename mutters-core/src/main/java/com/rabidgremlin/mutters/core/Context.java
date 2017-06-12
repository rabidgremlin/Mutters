package com.rabidgremlin.mutters.core;

import java.util.Locale;
import java.util.TimeZone;

/**
 * This class provides context of user's locale and timezone for slots to use when matching.
 * 
 * If locale and tiemzone are not specified then the JVM defaults are used.
 * 
 * @author rabidgremlin
 *
 */
public class Context
{
  /** The user's locale. Defaults to JVM default. */
  private Locale locale = Locale.getDefault();

  /** The user's locale. Defaults to JVM default. */
  private TimeZone timeZone = TimeZone.getDefault();

  /**
   * Gets the user's locale.
   * 
   * @return The user's locale.
   */
  public Locale getLocale()
  {
    return locale;
  }

  /**
   * Sets the user's locale.
   * 
   * @param locale The user's locale.
   */
  public void setLocale(Locale locale)
  {
    this.locale = locale;
  }

  /**
   * Gets the user's time zone.
   * 
   * @return The user's time zone.
   */
  public TimeZone getTimeZone()
  {
    return timeZone;
  }

  /**
   * Sets the user's time zone.
   * 
   * @param timeZone The user's time zone.
   */
  public void setTimeZone(TimeZone timeZone)
  {
    this.timeZone = timeZone;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   * 
   */
  @Override
  public String toString()
  {
    return "Context [locale=" + locale + ", timeZone=" + timeZone + "]";
  }

}
