/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Set;

import org.junit.Test;

public class TestCompoundIntentMatcher
{

  static class AlwaysMatcher implements IntentMatcher
  {
    @Override
    public IntentMatch match(String utterance, Context context, Set<String> expectedIntents)
    {
      return new IntentMatch(new Intent("AlwaysIntent"), new HashMap<Slot, SlotMatch>(), utterance,
          new MatcherScores());
    }
  }

  static class NeverMatcher implements IntentMatcher
  {
    @Override
    public IntentMatch match(String utterance, Context context, Set<String> expectedIntents)
    {
      return new NoIntentMatch(utterance);
    }
  }

  @Test
  public void shouldIgnoreSecondMatcherIfFirstMatcherMatches()
  {
    AlwaysMatcher alwaysMatcher = new AlwaysMatcher();
    NeverMatcher neverMatcher = new NeverMatcher();

    CompoundIntentMatcher compoundMatcher = new CompoundIntentMatcher(alwaysMatcher, neverMatcher);

    IntentMatch match = compoundMatcher.match("The rain stays mainly in the plains", new Context(), null);

    assertThat(match, is(notNullValue()));
    assertThat(match.matched(), is(true));
  }

  @Test
  public void shouldUseSecondMatcherIfFirstFailsToMatch()
  {
    AlwaysMatcher alwaysMatcher = new AlwaysMatcher();
    NeverMatcher neverMatcher = new NeverMatcher();

    CompoundIntentMatcher compoundMatcher = new CompoundIntentMatcher(neverMatcher, alwaysMatcher);

    IntentMatch match = compoundMatcher.match("The rain stays mainly in the plains", new Context(), null);

    assertThat(match, is(notNullValue()));
    assertThat(match.matched(), is(true));
  }

  @Test
  public void shouldReturnNoMatchIfBothMatchersFail()
  {
    NeverMatcher neverMatcher1 = new NeverMatcher();
    NeverMatcher neverMatcher2 = new NeverMatcher();

    CompoundIntentMatcher compoundMatcher = new CompoundIntentMatcher(neverMatcher1, neverMatcher2);

    IntentMatch match = compoundMatcher.match("The rain stays mainly in the plains", new Context(), null);

    assertThat(match, is(notNullValue()));
    assertThat(match.matched(), is(false));
  }

}
