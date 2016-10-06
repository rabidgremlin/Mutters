package com.rabidgremlin.mutters.bot.ink;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

import com.bladecoder.ink.runtime.Story;

/**
 * Utility class for manipulating ink variables in a story. Note that ink variable names are case sensitive.
 * 
 * @author rabidgremlin
 *
 */
public final class StoryUtils
{
  protected StoryUtils()
  {
    // utility class, hide constructor
  }

  /**
   * Returns the specified variable as a Number.
   * 
   * @param story The story to get the variable from.
   * @param varName The name of the variable (case-sensitive).
   * @return The Number or null if the variable does not exist or is not a number.
   */
  public static Number getNumber(Story story, String varName)
  {
    // sometimes numbers are strings
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

  /**
   * Returns the specified variable as a LocalDate. Note LocalDates should be stored as an ink string in the format
   * yyyy-MM-dd.
   * 
   * @param story The story to get the variable from.
   * @param varName The name of the variable (case-sensitive).
   * @return The LocalDate or null if the variable does not exist or is not a LocalDate.
   */
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

  /**
   * This method returns the boolean value of the specified story variable. Note Ink stores booleans values as integers,
   * 1 is true. This method returns false if variable does not exist.
   * 
   * @param story The story to extract the variable from.
   * @param varName The name of variable. Case sensitive.
   * @return The value of variable, or false if the variable does not exist.
   */
  public static boolean getBoolean(Story story, String varName)
  {
    Integer boolVal = (Integer) story.getVariablesState().get(varName);
    if (boolVal == null)
    {
      return false;
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
      return false;
    }
  }

  /**
   * Returns the specified variable as a String.
   * 
   * @param story The story to get the variable from.
   * @param varName The name of the variable (case-sensitive).
   * @return The String or null if the variable does not exist or it is not a string.
   */
  public static String getString(Story story, String varName)
  {
    try
    {
      return (String) story.getVariablesState().get(varName);
    }
    catch (java.lang.ClassCastException cce)
    {
      return null;
    }
  }
}
