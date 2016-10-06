package com.rabidgremlin.mutters.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class InputCleaner
{
  private static Logger log = LoggerFactory.getLogger(InputCleaner.class);

  private InputCleaner()
  {
    // do nothing
  }

  public static CleanedInput cleanInput(String inputString)
  {
    // Soundex soundexr = new Soundex();

    // TODO more punctuation and white space removal..
    List<String> originalTokens = Arrays.asList((inputString.replaceAll("\\?", "").split("\\s+")));
    List<String> cleanedTokens = new ArrayList<String>();

    for (String token : originalTokens)
    {
      if (token.startsWith("{") && token.endsWith("}"))
      {
        cleanedTokens.add(token);
      }
      else
      {
        // String soundex = soundexr.soundex(token);
        // if (soundex.equals(""))
        // {
        cleanedTokens.add(token);
        // }
        // else
        // {
        // cleanedTokens.add(soundex);
        // }
      }
    }

    CleanedInput cleanedInput = new CleanedInput(originalTokens, cleanedTokens);

    log.debug("Cleaned Input: {} -> {}", inputString, cleanedInput);

    return cleanedInput;
  }
}
