package com.rabidgremlin.mutters.core;

import java.util.Collections;
import java.util.List;

public class CleanedInput
{
  private List<String> originalTokens;

  private List<String> cleanedTokens;

  public CleanedInput(List<String> originalTokens, List<String> cleanedTokens)
  {
    this.originalTokens = originalTokens;
    this.cleanedTokens = cleanedTokens;
  }

  public List<String> getOriginalTokens()
  {
    return Collections.unmodifiableList(originalTokens);
  }

  public List<String> getCleanedTokens()
  {
    return Collections.unmodifiableList(cleanedTokens);
  }

  @Override
  public String toString()
  {
    return "CleanedInput [originalTokens=" + originalTokens + ", cleanedTokens=" + cleanedTokens + "]";
  }

}
