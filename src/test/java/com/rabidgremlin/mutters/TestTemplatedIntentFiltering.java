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
import com.rabidgremlin.mutters.slots.CustomSlot;
import com.rabidgremlin.mutters.slots.NumberSlot;
import com.rabidgremlin.mutters.templated.TemplatedIntent;
import com.rabidgremlin.mutters.templated.TemplatedIntentMatcher;
import com.rabidgremlin.mutters.templated.TemplatedUtterance;


public class TestTemplatedIntentFiltering
{
  TemplatedIntentMatcher matcher;
  
  @Before
  public void  setUpMatcher()
  {
    matcher = new TemplatedIntentMatcher();
    
    TemplatedIntent intent = new TemplatedIntent("HelloIntent");
    intent.addUtterance(new TemplatedUtterance("hello"));
    intent.addUtterance(new TemplatedUtterance("hi"));
    intent.addUtterance(new TemplatedUtterance("hiya"));    
    matcher.addIntent(intent);
    
    intent = new TemplatedIntent("GoodbyeIntent");
    intent.addUtterance(new TemplatedUtterance("goodbye"));
    intent.addUtterance(new TemplatedUtterance("bye"));        
    matcher.addIntent(intent);
  }
  
  
  
  @Test
  public void testNoFiltering()
  {
    // should match on hello intent
    IntentMatch intentMatch = matcher.match("hello", new Context(), null, null);

    assertThat(intentMatch, is(notNullValue()));
    assertThat(intentMatch.getIntent(), is(notNullValue()));
    assertThat(intentMatch.getIntent().getName(), is("HelloIntent"));
    
    // should match on goodbye intent
    intentMatch = matcher.match("bye", new Context(), null, null);

    assertThat(intentMatch, is(notNullValue()));
    assertThat(intentMatch.getIntent(), is(notNullValue()));
    assertThat(intentMatch.getIntent().getName(), is("GoodbyeIntent"));
  }
  
  @Test
  public void testFiltering()
  {
    HashSet<String> expectedIntents = new HashSet<String>();
    expectedIntents.add("HelloIntent");
    
    // should match on hello intent
    IntentMatch intentMatch = matcher.match("hello", new Context(), expectedIntents, null);

    assertThat(intentMatch, is(notNullValue()));
    assertThat(intentMatch.getIntent(), is(notNullValue()));
    assertThat(intentMatch.getIntent().getName(), is("HelloIntent"));
    
    // should not match on goodbye intent (its not in expected intents)
    intentMatch = matcher.match("bye", new Context(), expectedIntents, null);

    assertThat(intentMatch, is(nullValue()));    
  }
}
