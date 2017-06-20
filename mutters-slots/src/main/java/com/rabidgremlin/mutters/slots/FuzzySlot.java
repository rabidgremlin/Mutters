package com.rabidgremlin.mutters.slots;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.Slot;
import com.rabidgremlin.mutters.core.SlotMatch;

/**
 * This slot matches against a list of expected values.
 * 
 * It uses a Jaro Winkler Distance function to perform fuzzy matching.
 * 
 * @author rabidgremlin
 *
 */
public class FuzzySlot
    extends Slot
{
  private String name;

  private double tolerance;

  private List<String> options = new ArrayList<String>();

  /**
   * Constructor.
   * 
   * @param name The name of the slot.
   * @param options The list of options to match on.
   * @param tolerance The fuzzy matching tolerance (0.1 to 1.0).
   */
  public FuzzySlot(String name, String[] options, double tolerance)
  {
    this.name = name;
    this.options = Arrays.asList(options);
    this.tolerance = tolerance;
  }

  /**
   * Constructor. Defaults to a fuzzy matching tolerance of 0.98.
   * 
   * @param name The name of the slot.
   * @param options The list of options to match on.
   */
  public FuzzySlot(String name, String[] options)
  {
    this(name, options, 0.98);
  }

  @Override
  public SlotMatch match(String token, Context context)
  {
    String lowerToken = token.toLowerCase();
    double bestMatchScore = 0;
    String bestMatch = null;

    for (String option : options)
    {
      double score = StringUtils.getJaroWinklerDistance(lowerToken, option.toLowerCase());
      if (score > bestMatchScore)
      {
        bestMatchScore = score;
        bestMatch = option;
      }
    }

    if (bestMatchScore > tolerance && bestMatch != null)
    {
      return new SlotMatch(this, token, bestMatch);
    }
    else
    {
      return null;
    }
  }

  public String getName()
  {
    return name;
  }

  @Override
  public String toString()
  {
    return "FuzzySlot [name=" + name + ", options=" + options + "]";
  }

}
