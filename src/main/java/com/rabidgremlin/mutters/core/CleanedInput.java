package com.rabidgremlin.mutters.core;

import java.util.Collections;
import java.util.List;

/**
 * This class holds a cleaned version of a utterance as a series of tokens.
 * 
 * @see com.rabidgremlin.mutters.core.InputCleaner
 * 
 * @author rabidgremlin
 *
 */
public class CleanedInput
{
  /** The list of orginal tokens. */
  private List<String> originalTokens;

  /** The cleaned tokens. */
  private List<String> cleanedTokens;

  /**
   * Constructor.
   * 
   * @param originalTokens The original tokens.
   * @param cleanedTokens The cleaned tokens.
   */
  public CleanedInput(List<String> originalTokens, List<String> cleanedTokens)
  {
    this.originalTokens = originalTokens;
    this.cleanedTokens = cleanedTokens;
  }

  /**
   * Returns the original tokens.
   * 
   * @return The original tokens.
   */
  public List<String> getOriginalTokens()
  {
    return Collections.unmodifiableList(originalTokens);
  }

  /**
   * Returns the cleaned tokens.
   * 
   * @return The cleaned tokens.
   */
  public List<String> getCleanedTokens()
  {
    return Collections.unmodifiableList(cleanedTokens);
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
    return "CleanedInput [originalTokens=" + originalTokens + ", cleanedTokens=" + cleanedTokens
        + "]";
  }

}
