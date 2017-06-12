package com.rabidgremlin.mutters.input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This utility class cleans a user's input for processing.
 * 
 * @author rabidgremlin
 *
 */
public final class InputCleaner
{
  /* Logger. */
  private static Logger log = LoggerFactory.getLogger(InputCleaner.class);

  /**
   * Private constrcutor for utility class.
   * 
   */
  private InputCleaner()
  {
    // do nothing
  }

  /**
   * This method takes an utterance string and returns cleaned input.
   * 
   * @param inputString The input string.
   * @return The cleaned input.
   */
  public static CleanedInput cleanInput(String inputString)
  {    
    List<String> originalTokens = Arrays.asList((inputString.trim().replaceAll("[\\?|!|,]*", "").split("\\s+")));
    List<String> cleanedTokens = new ArrayList<String>();

    for (String token : originalTokens)
    {
      cleanedTokens.add(token);
    }

    CleanedInput cleanedInput = new CleanedInput(originalTokens, cleanedTokens);

    log.debug("Cleaned Input: {} -> {}", inputString, cleanedInput);

    return cleanedInput;
  }
}
