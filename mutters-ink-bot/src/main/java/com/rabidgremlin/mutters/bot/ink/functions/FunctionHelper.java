/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.bot.ink.functions;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Utility class to parse data for InkFunction calls in an Ink script.
 * 
 * @author rabidgremlin
 *
 */
public final class FunctionHelper
{
  private FunctionHelper()
  {
    // private constructor for utility class
  }

  public static FunctionDetails parseFunctionString(String paramString) throws IllegalArgumentException
  {
    // trim any whitespace
    String trimmedLine = paramString.trim();

    // trimmed line is the data for the functions
    FunctionDetails functionDetails = new FunctionDetails(trimmedLine);

    try
    {
      // do we have any :: ? If so try parse out params
      if (trimmedLine.contains("::"))
      {
        // spit by ::
        ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(trimmedLine.split("::")));

        // while we have tokens
        while (!tokens.isEmpty())
        {
          // grab name and value
          String name = tokens.get(0);
          String value = tokens.get(1);

          // pop from list
          tokens.remove(0);
          tokens.remove(0);

          // doe svalue have word on end ? and we are not at end of tokens ?
          int lastSpace = value.lastIndexOf(" ");
          if (lastSpace != -1 && tokens.size() > 0)
          {
            // yes, last word of value is actually next name, so extract
            String nextName = value.substring(lastSpace);
            value = value.substring(0, lastSpace);

            // push next name back onto list
            tokens.add(0, nextName);
          }

          // store extract name value pair
          functionDetails.addFunctionParams(name.trim(), value.trim());
        }
      }
    }
    catch (Exception e)
    {
      throw new IllegalArgumentException("Unable to parse function parameters.", e);
    }

    return functionDetails;
  }

}
