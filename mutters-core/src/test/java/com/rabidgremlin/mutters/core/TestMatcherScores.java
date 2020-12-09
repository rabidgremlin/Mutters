/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;

import org.junit.jupiter.api.Test;

class TestMatcherScores
{

  @Test
  void shouldBeEmptyIfCreatedWithNoScores()
  {
    MatcherScores emptyScores = new MatcherScores();
    assertThat(emptyScores.isEmpty()).isTrue();

    emptyScores.addScore("TestIntent", 1.0);
    assertThat(emptyScores.isEmpty()).isFalse();
  }

  @Test
  void shouldReturnTheBestScore()
  {
    MatcherScores scores = new MatcherScores();

    assertThat(scores.getBestScore()).isEmpty();

    scores.addScore("TestIntent", 0.56);
    scores.addScore("TestIntent2", 0.16);
    scores.addScore("TestIntent3", 0.99);

    assertThat(scores.getBestScore()).hasValue(0.99);
  }

  @Test
  void shouldHandleMultipleIntentsForScore()
  {
    MatcherScores scores = new MatcherScores();

    assertThat(scores.getBestScore()).isEmpty();

    scores.addScore("TestIntent", 0.56);
    scores.addScore("TestIntent3", 0.16);
    scores.addScore("TestIntent2", 0.16);

    assertThat(scores.getScores()).hasSize(2);
    assertThat(scores.getScores().get(0.16)).hasSize(2);
  }

  @Test
  void shouldSortIntentsForSameScore()
  {
    MatcherScores scores = new MatcherScores();

    assertThat(scores.getBestScore()).isEmpty();

    scores.addScore("TestIntent2", 0.16);
    scores.addScore("TestIntent3", 0.16);
    scores.addScore("TestIntent0", 0.16);

    assertThat(scores.getScores()).hasSize(1);
    assertThat(scores.getScores().get(0.16)).hasSize(3);
    assertThat(scores.getScores().get(0.16).first()).isEqualTo("TestIntent0");
    assertThat(scores.getScores().get(0.16).last()).isEqualTo("TestIntent3");
  }

}
