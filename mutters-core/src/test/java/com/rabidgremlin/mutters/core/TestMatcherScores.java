/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.OptionalDouble;

import org.junit.Test;

public class TestMatcherScores
{

  @Test
  public void shouldBeEmptyIfCreatedWithNoScores()
  {
    MatcherScores emptyScores = new MatcherScores();
    assertThat(emptyScores.isEmpty(), is(true));

    emptyScores.addScore("TestIntent", 1.0);
    assertThat(emptyScores.isEmpty(), is(false));
  }

  @Test
  public void shouldReturnTheBestScore()
  {
    MatcherScores scores = new MatcherScores();

    assertThat(scores.getBestScore(), is(OptionalDouble.empty()));

    scores.addScore("TestIntent", 0.56);
    scores.addScore("TestIntent2", 0.16);
    scores.addScore("TestIntent3", 0.99);

    assertThat(scores.getBestScore().getAsDouble(), is(0.99));
  }

  @Test
  public void shouldHandleMultipleIntentsForScore()
  {
    MatcherScores scores = new MatcherScores();

    assertThat(scores.getBestScore(), is(OptionalDouble.empty()));

    scores.addScore("TestIntent", 0.56);
    scores.addScore("TestIntent3", 0.16);
    scores.addScore("TestIntent2", 0.16);

    assertThat(scores.getScores().size(), is(2));
    assertThat(scores.getScores().get(0.16).size(), is(2));
  }

  @Test
  public void shouldSortIntentsForSameScore()
  {
    MatcherScores scores = new MatcherScores();

    assertThat(scores.getBestScore(), is(OptionalDouble.empty()));

    scores.addScore("TestIntent2", 0.16);
    scores.addScore("TestIntent3", 0.16);
    scores.addScore("TestIntent0", 0.16);

    assertThat(scores.getScores().size(), is(1));
    assertThat(scores.getScores().get(0.16).size(), is(3));
    assertThat(scores.getScores().get(0.16).first(), is("TestIntent0"));
    assertThat(scores.getScores().get(0.16).last(), is("TestIntent3"));
  }

}
