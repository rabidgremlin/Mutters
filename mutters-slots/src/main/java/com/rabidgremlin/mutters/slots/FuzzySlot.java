/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.slots;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.rabidgremlin.mutters.core.AbstractSlot;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.SlotMatch;

/**
 * This slot matches against a list of expected values.
 * 
 * It uses a Jaro Winkler Distance function to perform fuzzy matching.
 * 
 * @author rabidgremlin
 *
 */
public class FuzzySlot extends AbstractSlot<String>
{
  private static final double DEFAULT_TOLERANCE = 0.95d;

  private final double tolerance;

  private final List<String> options;

  /**
   * Constructor.
   *
   * @param name      The name of the slot.
   * @param options   The list of options to match on.
   * @param tolerance The fuzzy matching tolerance (0.1 to 1.0).
   */
  public FuzzySlot(String name, List<String> options, double tolerance)
  {
    super(name);
    if (tolerance < 0.1 || tolerance > 1)
    {
      throw new IllegalArgumentException("Invalid tolerance: " + tolerance);
    }
    this.options = Objects.requireNonNull(options);
    this.tolerance = tolerance;
  }

  /**
   * Constructor.
   * 
   * @param name      The name of the slot.
   * @param options   The list of options to match on.
   * @param tolerance The fuzzy matching tolerance (0.1 to 1.0).
   */
  public FuzzySlot(String name, String[] options, double tolerance)
  {
    this(name, Arrays.asList(options), tolerance);
  }

  /**
   * Constructor. Defaults to a fuzzy matching tolerance of 0.95.
   *
   * @param name    The name of the slot.
   * @param options The list of options to match on.
   */
  public FuzzySlot(String name, List<String> options)
  {
    this(name, options, DEFAULT_TOLERANCE);
  }

  /**
   * Constructor. Defaults to a fuzzy matching tolerance of 0.95.
   * 
   * @param name    The name of the slot.
   * @param options The list of options to match on.
   */
  public FuzzySlot(String name, String... options)
  {
    this(name, options, DEFAULT_TOLERANCE);
  }

  @Override
  public Optional<SlotMatch<String>> match(String token, Context context)
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
      return Optional.of(new SlotMatch<>(this, token, bestMatch));
    }
    else
    {
      return Optional.empty();
    }
  }

  @Override
  public String toString()
  {
    return "FuzzySlot [name=" + getName() + ", options=" + options + ", tolerance=" + tolerance + "]";
  }
}
