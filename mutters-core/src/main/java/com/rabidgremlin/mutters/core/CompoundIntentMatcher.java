/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core;

import java.util.Objects;
import java.util.Set;

/**
 * An IntentMatcher that is a combination of two other intent matchers. Useful
 * for instance for combining a TemplatedIntentMatcher and an implementation of
 * an AbstractMachineLearningIntentMatcher. Class will stop on the first match
 * returned by a matcher.
 * 
 * @author rabidgremlin
 *
 */
public class CompoundIntentMatcher implements IntentMatcher
{
  /** The first matcher. */
  private IntentMatcher firstMatcher;

  /** The second matcher. */
  private IntentMatcher secondMatcher;

  /**
   * Constructor for the class.
   * 
   * @param firstMatcher  The first intent matcher.
   * @param secondMatcher The second intent matcher.
   */
  public CompoundIntentMatcher(IntentMatcher firstMatcher, IntentMatcher secondMatcher)
  {
    this.firstMatcher = Objects.requireNonNull(firstMatcher);
    this.secondMatcher = Objects.requireNonNull(secondMatcher);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.rabidgremlin.mutters.core.IntentMatcher#match(String utterance,
   * Context context, Set<String> expectedIntents)
   * 
   */
  @Override
  public IntentMatch match(String utterance, Context context, Set<String> expectedIntents)
  {
    // see if we can find match in first matcher
    IntentMatch match = firstMatcher.match(utterance, context, expectedIntents);

    // no ? try second one
    if (!match.matched())
    {
      match = secondMatcher.match(utterance, context, expectedIntents);
    }

    return match;
  }
}
