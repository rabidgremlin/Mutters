/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BinaryOperator;

/**
 * An IntentMatcher that is a combination of two other intent matchers. Useful
 * for instance for combining a TemplatedIntentMatcher and an implementation of
 * an AbstractMachineLearningIntentMatcher. Class will stop on the first match
 * returned by a matcher.
 *
 * @author rabidgremlin
 * @author wilmol
 * @see #compose(IntentMatcher...)
 * @see #compose(BinaryOperator, IntentMatcher...)
 */
public class CompoundIntentMatcher implements IntentMatcher
{
  /**
   * The first intent matcher.
   */
  private final IntentMatcher firstMatcher;

  /**
   * The second intent matcher.
   */
  private final IntentMatcher secondMatcher;

  /**
   * Strategy for merging {@link MatcherScores} if both matchers fail to match.
   */
  private final BinaryOperator<MatcherScores> noMatchScoreMerger;

  /**
   * Constructor for the class.
   * <p>
   * Uses default no match score merging strategy which returns no scores.
   *
   * @param firstMatcher  The first intent matcher.
   * @param secondMatcher The second intent matcher.
   */
  public CompoundIntentMatcher(IntentMatcher firstMatcher, IntentMatcher secondMatcher)
  {
    this(firstMatcher, secondMatcher, (a, b) -> new MatcherScores());
  }

  /**
   * Constructor for the class.
   *
   * @param firstMatcher       The first intent matcher.
   * @param secondMatcher      The second intent matcher.
   * @param noMatchScoreMerger Strategy for merging {@link MatcherScores} if both
   *                           matchers fail to match.
   */
  public CompoundIntentMatcher(IntentMatcher firstMatcher, IntentMatcher secondMatcher,
      BinaryOperator<MatcherScores> noMatchScoreMerger)
  {
    this.firstMatcher = Objects.requireNonNull(firstMatcher);
    this.secondMatcher = Objects.requireNonNull(secondMatcher);
    this.noMatchScoreMerger = Objects.requireNonNull(noMatchScoreMerger);
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
    IntentMatch firstMatch = firstMatcher.match(utterance, context, expectedIntents);

    if (firstMatch.matched())
    {
      return firstMatch;
    }
    else
    {
      // no ? try second one
      IntentMatch secondMatch = secondMatcher.match(utterance, context, expectedIntents);
      if (secondMatch.matched())
      {
        return secondMatch;
      }
      else
      {
        // neither matcher matched, combine their scores
        MatcherScores combinedScores = noMatchScoreMerger.apply(firstMatch.getMatcherScores(),
            secondMatch.getMatcherScores());
        return new NoIntentMatch(utterance, combinedScores);
      }
    }
  }

  /**
   * Static factory for composing {@link IntentMatcher}s using
   * {@link CompoundIntentMatcher}.
   * <p>
   * Uses default no match score merging strategy which returns no scores.
   *
   * @param intentMatchers intent matchers to compose
   * @return composed intent matcher
   */
  public static IntentMatcher compose(List<IntentMatcher> intentMatchers)
  {
    return compose((a, b) -> new MatcherScores(), intentMatchers);
  }

  /**
   * Static factory for composing {@link IntentMatcher}s using
   * {@link CompoundIntentMatcher}.
   * <p>
   * Uses default no match score merging strategy which returns no scores.
   *
   * @param intentMatchers intent matchers to compose
   * @return composed intent matcher
   */
  public static IntentMatcher compose(IntentMatcher... intentMatchers)
  {
    return compose(Arrays.asList(intentMatchers));
  }

  /**
   * Static factory for composing {@link IntentMatcher}s using
   * {@link CompoundIntentMatcher}.
   *
   * @param noMatchScoreMerger Strategy for merging {@link MatcherScores} if no
   *                           matchers match.
   * @param intentMatchers     intent matchers to compose
   * @return composed intent matcher
   */
  public static IntentMatcher compose(BinaryOperator<MatcherScores> noMatchScoreMerger,
      List<IntentMatcher> intentMatchers)
  {
    return intentMatchers.stream()
        .reduce(
            (firstMatcher, secondMatcher) -> new CompoundIntentMatcher(firstMatcher, secondMatcher, noMatchScoreMerger))
        .orElseThrow(() -> new IllegalArgumentException("No intent matchers provided."));
  }

  /**
   * Static factory for composing {@link IntentMatcher}s using
   * {@link CompoundIntentMatcher}.
   *
   * @param noMatchScoreMerger Strategy for merging {@link MatcherScores} if no
   *                           matchers match.
   * @param intentMatchers     intent matchers to compose
   * @return composed intent matcher
   */
  public static IntentMatcher compose(BinaryOperator<MatcherScores> noMatchScoreMerger, IntentMatcher... intentMatchers)
  {
    return compose(noMatchScoreMerger, Arrays.asList(intentMatchers));
  }
}
