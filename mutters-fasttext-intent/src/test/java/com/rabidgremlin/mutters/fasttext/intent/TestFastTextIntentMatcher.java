package com.rabidgremlin.mutters.fasttext.intent;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.net.URL;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.Intent;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.SlotMatcher;
import com.rabidgremlin.mutters.slots.LiteralSlot;
import com.rabidgremlin.mutters.templated.SimpleTokenizer;

// based on training data from https://github.com/mlehman/nlp-intent-toolkit

public class TestFastTextIntentMatcher
{
  private static FastTextIntentMatcher intentMatcher;


  @BeforeClass
  public static void setUp()
  {
    SlotMatcher slotMatcher = mock(SlotMatcher.class);
    
    URL modelUrl = Thread.currentThread().getContextClassLoader()
        .getResource("models/fasttext-weather-model.bin");
    assertThat(modelUrl, is(notNullValue()));
    
    
    intentMatcher = new FastTextIntentMatcher(modelUrl, new SimpleTokenizer(true), slotMatcher, 0.85f,-1);
    
    Intent intent = new Intent("CurrentWeatherIntent");    
    intentMatcher.addIntent(intent);

    intent = new Intent("FiveDayForecastIntent");
    intentMatcher.addIntent(intent);

    intent = new Intent("HourlyForecastIntent");
    intentMatcher.addIntent(intent);
  }

  @Test
  public void testMatchingOfPhraseInTestData()
  {
    Context context = new Context();
    IntentMatch intentMatch =  intentMatcher.match("how hot is it", context, null, null);
    
    assertThat(intentMatch, is(notNullValue()));
    
    Intent intent = intentMatch.getIntent();
    assertThat(intent,is(notNullValue()));
    assertThat(intent.getName(),is("CurrentWeatherIntent"));    
  }
  
  @Test
  public void testBasicMatching()
  {
    Context context = new Context();
    IntentMatch intentMatch =  intentMatcher.match("will it rain tomorrow", context, null, null);
    
    assertThat(intentMatch, is(notNullValue()));
    
    Intent intent = intentMatch.getIntent();
    assertThat(intent,is(notNullValue()));
    assertThat(intent.getName(),is("FiveDayForecastIntent"));    
  }
  
  @Test
  public void testNoMatch()
  {
    Context context = new Context();
    IntentMatch intentMatch =  intentMatcher.match("the hovercraft is full of eels", context, null, null);
    
    assertThat(intentMatch, is(nullValue()));
   
  }

}
