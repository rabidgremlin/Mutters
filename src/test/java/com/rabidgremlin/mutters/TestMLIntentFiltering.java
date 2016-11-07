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
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.ml.MLIntent;
import com.rabidgremlin.mutters.ml.MLIntentMatcher;
import com.rabidgremlin.mutters.slots.CustomSlot;
import com.rabidgremlin.mutters.slots.NumberSlot;
import com.rabidgremlin.mutters.templated.TemplatedIntent;
import com.rabidgremlin.mutters.templated.TemplatedIntentMatcher;
import com.rabidgremlin.mutters.templated.TemplatedUtterance;


public class TestMLIntentFiltering
{
  MLIntentMatcher matcher;
  
  @Before
  public void  setUpMatcher()
  {
    matcher = new MLIntentMatcher("models/en-cat-taxi-intents.bin");
    
    matcher.addIntent(new MLIntent("OrderTaxi"));
    matcher.addIntent(new MLIntent("WhereTaxi"));
    
  }
  
  
  
  @Test
  public void testNoFiltering()
  {
    // should match on order intent
    IntentMatch intentMatch = matcher.match("Order a taxi", new Context(), null);

    assertThat(intentMatch, is(notNullValue()));
    assertThat(intentMatch.getIntent(), is(notNullValue()));
    assertThat(intentMatch.getIntent().getName(), is("OrderTaxi"));
    
    // should match on where intent
    intentMatch = matcher.match("How far away is my taxi ?", new Context(), null);

    assertThat(intentMatch, is(notNullValue()));
    assertThat(intentMatch.getIntent(), is(notNullValue()));
    assertThat(intentMatch.getIntent().getName(), is("WhereTaxi"));
  }
  
  @Test
  public void testFiltering()
  {
    HashSet<String> expectedIntents = new HashSet<String>();
    expectedIntents.add("OrderTaxi");
    
    // should match on order intent
    IntentMatch intentMatch = matcher.match("Order a taxi", new Context(), expectedIntents);

    assertThat(intentMatch, is(notNullValue()));
    assertThat(intentMatch.getIntent(), is(notNullValue()));
    assertThat(intentMatch.getIntent().getName(), is("OrderTaxi"));
    
    // should not match on where intent (its not in expected intents)
    intentMatch = matcher.match("How far away is my taxi ?", new Context(), expectedIntents);

    assertThat(intentMatch, is(nullValue()));        
  }
}
