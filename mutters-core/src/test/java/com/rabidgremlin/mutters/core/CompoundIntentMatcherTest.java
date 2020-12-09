/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.BinaryOperator;

import org.junit.jupiter.api.Test;

class CompoundIntentMatcherTest
{

  /**
   * Always matches with scores={1.0=[AlwaysIntent]}.
   */
  static class AlwaysMatcher implements IntentMatcher
  {
    @Override
    public IntentMatch match(String utterance, Context context, Set<String> expectedIntents)
    {
      return new IntentMatch(new Intent("AlwaysIntent"), new HashMap<>(), utterance,
          new MatcherScores().addScore("AlwaysIntent", 1.0));
    }
  }

  /**
   * Never matches with scores={}.
   */
  static class NeverMatcher implements IntentMatcher
  {
    @Override
    public IntentMatch match(String utterance, Context context, Set<String> expectedIntents)
    {
      return new NoIntentMatch(utterance);
    }
  }

  /**
   * Never matches with scores={0.5=[IntentA], 0.3=[IntentB], 0.2=[IntentC]}.
   */
  static class AbcNeverMatcher implements IntentMatcher
  {
    @Override
    public IntentMatch match(String utterance, Context context, Set<String> expectedIntents)
    {
      return new NoIntentMatch(utterance,
          new MatcherScores().addScore("IntentA", 0.5).addScore("IntentB", 0.3).addScore("IntentC", 0.2));
    }
  }

  /**
   * Never matches with scores={0.5=[IntentA], 0.4=[IntentB], 0.1=[IntentD]}.
   */
  static class AbdNeverMatcher implements IntentMatcher
  {
    @Override
    public IntentMatch match(String utterance, Context context, Set<String> expectedIntents)
    {
      return new NoIntentMatch(utterance,
          new MatcherScores().addScore("IntentA", 0.5).addScore("IntentB", 0.4).addScore("IntentD", 0.1));
    }
  }

  /**
   * Merges scores by naively dumping all the scores into one MatcherScores.
   */
  static class NaiveScoresMerger implements BinaryOperator<MatcherScores>
  {
    @Override
    public MatcherScores apply(MatcherScores a, MatcherScores b)
    {
      for (Map.Entry<Double, SortedSet<String>> score : b.getScores().entrySet())
      {
        for (String intent : score.getValue())
        {
          a.addScore(intent, score.getKey());
        }
      }
      return a;
    }
  }

  @Test
  void shouldIgnoreSecondMatcherIfFirstMatcherMatches()
  {
    AlwaysMatcher alwaysMatcher = new AlwaysMatcher();
    NeverMatcher neverMatcher = new NeverMatcher();

    CompoundIntentMatcher compoundMatcher = new CompoundIntentMatcher(alwaysMatcher, neverMatcher);

    IntentMatch match = compoundMatcher.match("The rain stays mainly in the plains", new Context(), null);

    assertThat(match).isNotNull();
    assertThat(match.matched()).isTrue();
  }

  @Test
  void shouldUseSecondMatcherIfFirstFailsToMatch()
  {
    AlwaysMatcher alwaysMatcher = new AlwaysMatcher();
    NeverMatcher neverMatcher = new NeverMatcher();

    CompoundIntentMatcher compoundMatcher = new CompoundIntentMatcher(neverMatcher, alwaysMatcher);

    IntentMatch match = compoundMatcher.match("The rain stays mainly in the plains", new Context(), null);

    assertThat(match).isNotNull();
    assertThat(match.matched()).isTrue();
  }

  @Test
  void shouldReturnNoMatchIfBothMatchersFail()
  {
    NeverMatcher neverMatcher1 = new NeverMatcher();
    NeverMatcher neverMatcher2 = new NeverMatcher();

    CompoundIntentMatcher compoundMatcher = new CompoundIntentMatcher(neverMatcher1, neverMatcher2);

    IntentMatch match = compoundMatcher.match("The rain stays mainly in the plains", new Context(), null);

    assertThat(match).isNotNull();
    assertThat(match.matched()).isFalse();
  }

  @Test
  void shouldReturnOnlyMatchedScoresIfLastMatcherMatches()
  {
    // Given
    AbcNeverMatcher abcMatcher = new AbcNeverMatcher();
    AlwaysMatcher alwaysMatcher = new AlwaysMatcher();
    IntentMatcher intentMatcher = CompoundIntentMatcher.compose(abcMatcher, alwaysMatcher);

    // When
    IntentMatch match = intentMatcher.match("The rain stays mainly in the plains", new Context(), null);

    // Then
    assertThat(match).isNotNull();
    assertThat(match.matched()).isTrue();
    assertThat(match.getMatcherScores()).isEqualTo(new MatcherScores().addScore("AlwaysIntent", 1.0));
  }

  @Test
  void shouldReturnOnlyMatchedScoresIfFirstMatcherMatches()
  {
    // Given
    AlwaysMatcher alwaysMatcher = new AlwaysMatcher();
    AbcNeverMatcher abcMatcher = new AbcNeverMatcher();
    IntentMatcher intentMatcher = CompoundIntentMatcher.compose(abcMatcher, alwaysMatcher);

    // When
    IntentMatch match = intentMatcher.match("The rain stays mainly in the plains", new Context(), null);

    // Then
    assertThat(match).isNotNull();
    assertThat(match.matched()).isTrue();
    assertThat(match.getMatcherScores()).isEqualTo(new MatcherScores().addScore("AlwaysIntent", 1.0));
  }

  @Test
  void testComposeMoreThan2Matchers()
  {
    // Given
    AlwaysMatcher alwaysMatcher = new AlwaysMatcher();
    AbcNeverMatcher abcMatcher = new AbcNeverMatcher();
    AbdNeverMatcher abdMatcher = new AbdNeverMatcher();
    IntentMatcher intentMatcher = CompoundIntentMatcher.compose(abcMatcher, abdMatcher, alwaysMatcher);

    // When
    IntentMatch match = intentMatcher.match("The rain stays mainly in the plains", new Context(), null);

    // Then
    assertThat(match).isNotNull();
    assertThat(match.matched()).isTrue();
    assertThat(match.getMatcherScores()).isEqualTo(new MatcherScores().addScore("AlwaysIntent", 1.0));
  }

  @Test
  void testComposeMoreThan2MatchersWithCustomNoMatchScoreMergingBehaviour()
  {
    // Given
    AbcNeverMatcher abcMatcher = new AbcNeverMatcher();
    AbdNeverMatcher abdNeverMatcher = new AbdNeverMatcher();
    NeverMatcher neverMatcher = new NeverMatcher();
    IntentMatcher intentMatcher = CompoundIntentMatcher.compose(new NaiveScoresMerger(), abcMatcher, abdNeverMatcher,
        neverMatcher);

    // When
    IntentMatch match = intentMatcher.match("The rain stays mainly in the plains", new Context(), null);

    // Then
    assertThat(match).isNotNull();
    assertThat(match.matched()).isFalse();
    assertThat(match.getMatcherScores()).isEqualTo(new MatcherScores().addScore("IntentA", 0.5).addScore("IntentB", 0.4)
        .addScore("IntentB", 0.3).addScore("IntentC", 0.2).addScore("IntentD", 0.1));
  }

  @Test
  void testConstructWithListOfMatchers()
  {
    // Given
    List<IntentMatcher> intentMatchers = Arrays.asList(new AbcNeverMatcher(), new AbdNeverMatcher(),
        new AlwaysMatcher());

    // When
    IntentMatcher intentMatcher = CompoundIntentMatcher.compose(intentMatchers);

    // Then
    IntentMatch match = intentMatcher.match("The rain stays mainly in the plains", new Context(), null);
    assertThat(match).isNotNull();
    assertThat(match.matched()).isTrue();
    assertThat(match.getIntent().getName()).isEqualTo("AlwaysIntent");
  }

  @Test
  void testConstructWithArrayOfMatchers()
  {
    // Given
    IntentMatcher[] intentMatchers = { new AbcNeverMatcher(), new AbdNeverMatcher(), new AlwaysMatcher() };

    // When
    IntentMatcher intentMatcher = CompoundIntentMatcher.compose(intentMatchers);

    // Then
    IntentMatch match = intentMatcher.match("The rain stays mainly in the plains", new Context(), null);
    assertThat(match).isNotNull();
    assertThat(match.matched()).isTrue();
    assertThat(match.getIntent().getName()).isEqualTo("AlwaysIntent");
  }

  @Test
  void testConstructWithListOfMatchersCustomNoMatchMergeStrategy()
  {
    // Given
    List<IntentMatcher> intentMatchers = Arrays.asList(new AbcNeverMatcher(), new AbdNeverMatcher(),
        new NeverMatcher());
    NaiveScoresMerger naiveScoresMerger = new NaiveScoresMerger();

    // When
    IntentMatcher intentMatcher = CompoundIntentMatcher.compose(naiveScoresMerger, intentMatchers);

    // Then
    IntentMatch match = intentMatcher.match("The rain stays mainly in the plains", new Context(), null);
    assertThat(match).isNotNull();
    assertThat(match.matched()).isFalse();
    assertThat(match.getMatcherScores()).isEqualTo(new MatcherScores().addScore("IntentA", 0.5).addScore("IntentB", 0.4)
        .addScore("IntentB", 0.3).addScore("IntentC", 0.2).addScore("IntentD", 0.1));
  }

  @Test
  void testConstructWithArrayOfMatchersCustomNoMatchMergeStrategy()
  {
    // Given
    IntentMatcher[] intentMatchers = { new AbcNeverMatcher(), new AbdNeverMatcher(), new NeverMatcher() };
    NaiveScoresMerger naiveScoresMerger = new NaiveScoresMerger();

    // When
    IntentMatcher intentMatcher = CompoundIntentMatcher.compose(naiveScoresMerger, intentMatchers);

    // Then
    IntentMatch match = intentMatcher.match("The rain stays mainly in the plains", new Context(), null);
    assertThat(match).isNotNull();
    assertThat(match.matched()).isFalse();
    assertThat(match.getMatcherScores()).isEqualTo(new MatcherScores().addScore("IntentA", 0.5).addScore("IntentB", 0.4)
        .addScore("IntentB", 0.3).addScore("IntentC", 0.2).addScore("IntentD", 0.1));
  }

  @Test
  void testConstructWithListOfMatchersRejectsEmptyList()
  {
    // Given
    List<IntentMatcher> intentMatchers = Collections.emptyList();

    // When
    IllegalArgumentException expected = assertThrows(IllegalArgumentException.class,
        () -> CompoundIntentMatcher.compose(intentMatchers));

    // Then
    assertThat(expected).hasMessageThat().isEqualTo("No intent matchers provided.");
  }

  @Test
  void testConstructWithArrayOfMatchersRejectsEmptyArray()
  {
    // Given
    IntentMatcher[] intentMatchers = {};

    // When
    IllegalArgumentException expected = assertThrows(IllegalArgumentException.class,
        () -> CompoundIntentMatcher.compose(intentMatchers));

    // Then
    assertThat(expected).hasMessageThat().isEqualTo("No intent matchers provided.");
  }
}
