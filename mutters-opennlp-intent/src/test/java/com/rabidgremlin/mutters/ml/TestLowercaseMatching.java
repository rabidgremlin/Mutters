package com.rabidgremlin.mutters.ml;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.IntentMatch;

public class TestLowercaseMatching
{

  @Test
  public void testCaseSensitiveDefaultMatching()
  {
    MLIntentMatcher matcher = new MLIntentMatcher("models/en-cat-taxi-intents.bin", 0.75f, -1, false);
    matcher.addIntent(new MLIntent("Shouting"));

    assertThat(matcher, is(notNullValue()));

    IntentMatch match = matcher.match("I AM SHOUTING", new Context(), null, null);
    assertThat(match, is(nullValue()));

    match = matcher.match("I am Shouting", new Context(), null, null);
    assertThat(match, is(nullValue()));

    match = matcher.match("i am shouting", new Context(), null, null);
    assertThat(match, is(notNullValue()));
    assertThat(match.getIntent().getName(), is("Shouting"));
  }

  @Test
  public void testLowercaseIntentMatching()
  {
    MLIntentMatcher matcher = new MLIntentMatcher("models/en-cat-taxi-intents.bin", 0.75f, -1, true);
    matcher.addIntent(new MLIntent("Shouting"));

    assertThat(matcher, is(notNullValue()));

    IntentMatch match = matcher.match("I AM SHOUTING", new Context(), null, null);
    assertThat(match, is(notNullValue()));
    assertThat(match.getIntent().getName(), is("Shouting"));

    match = matcher.match("I am Shouting", new Context(), null, null);
    assertThat(match, is(notNullValue()));
    assertThat(match.getIntent().getName(), is("Shouting"));

    match = matcher.match("i am shouting", new Context(), null, null);
    assertThat(match, is(notNullValue()));
    assertThat(match.getIntent().getName(), is("Shouting"));
  }

}
