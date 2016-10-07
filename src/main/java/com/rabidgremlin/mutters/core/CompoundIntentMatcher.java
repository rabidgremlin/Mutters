package com.rabidgremlin.mutters.core;

/**
 * An IntentMatcher that is a combination of two other intent matachers. Useful for instance for combining a
 * TemplatedIntentMatcher and a MLIntentMatcher. Class will stop on the first match returned by a matcher.
 * 
 * @author rabidgremlin
 *
 */
public class CompoundIntentMatcher
    implements IntentMatcher
{
  /** The first matcher. */
  private IntentMatcher firstMatcher;

  /** The second matcher. */
  private IntentMatcher secondMatcher;

  /**
   * Constructor for the class.
   * 
   * @param firstMatcher The first intent matcher.
   * @param secondMatcher The second intent matcher.
   */
  public CompoundIntentMatcher(IntentMatcher firstMatcher, IntentMatcher secondMatcher)
  {
    this.firstMatcher = firstMatcher;
    this.secondMatcher = secondMatcher;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.rabidgremlin.mutters.core.IntentMatcher#match(String utterance, Context context)
   * 
   */
  @Override
  public IntentMatch match(String utterance, Context context)
  {
    // see if we can find match in first matcher
    IntentMatch match = firstMatcher.match(utterance, context);

    // no ? try second one
    if (match == null)
    {
      match = secondMatcher.match(utterance, context);
    }

    return match;
  }
}
