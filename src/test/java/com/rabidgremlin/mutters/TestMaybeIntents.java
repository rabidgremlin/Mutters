package com.rabidgremlin.mutters;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.ml.MLIntent;
import com.rabidgremlin.mutters.ml.MLIntentMatcher;

/**
 * Test class for maybe intents.
 * 
 * @author rabidgremlin
 *
 */
public class TestMaybeIntents
{
  private MLIntentMatcher matcher;

  @Before
  public void setUpMatcher()
  {
    matcher = new MLIntentMatcher("models/en-cat-taxi-intents.bin", 0.7f, 0.25f);

    matcher.addIntent(new MLIntent("OrderTaxi"));
    matcher.addIntent(new MLIntent("WhereTaxi"));

  }

  @Test
  public void testBestMatchWorks()
  {
    // should match on order taxi intent
    IntentMatch intentMatch = matcher.match("Order a taxi", new Context(), null, null);

    assertThat(intentMatch, is(notNullValue()));
    assertThat(intentMatch.getIntent(), is(notNullValue()));
    assertThat(intentMatch.getIntent().getName(), is("OrderTaxi"));
  }

  @Test
  public void testMaybeMatchWithNoExpectedIntents()
  {
    // should match on maybe order taxi
    IntentMatch intentMatch = matcher.match("Order", new Context(), null, null);

    assertThat(intentMatch, is(notNullValue()));
    assertThat(intentMatch.getIntent(), is(notNullValue()));
    assertThat(intentMatch.getIntent().getName(), is("MaybeOrderTaxi"));
  }

  @Test
  public void testMaybeMatchWithExpectedIntents()
  {
    HashSet<String> expectedIntents = new HashSet<String>();
    expectedIntents.add("OrderTaxi");
    expectedIntents.add("MaybeOrderTaxi");

    // should match on maybe order taxi
    IntentMatch intentMatch = matcher.match("Order", new Context(), expectedIntents, null);

    assertThat(intentMatch, is(notNullValue()));
    assertThat(intentMatch.getIntent(), is(notNullValue()));
    assertThat(intentMatch.getIntent().getName(), is("MaybeOrderTaxi"));
  }

  @Test
  public void testMaybeMatchWithExpectedIntentsAndMaybeIntentNotInList()
  {
    HashSet<String> expectedIntents = new HashSet<String>();
    expectedIntents.add("OrderTaxi");

    // should not match
    IntentMatch intentMatch = matcher.match("Order", new Context(), expectedIntents, null);

    assertThat(intentMatch, is(nullValue()));
  }

}
