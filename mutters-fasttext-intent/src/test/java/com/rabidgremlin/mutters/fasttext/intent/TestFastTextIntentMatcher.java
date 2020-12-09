/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.fasttext.intent;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;

import java.net.URL;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.Intent;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.SlotMatcher;
import com.rabidgremlin.mutters.templated.SimpleTokenizer;

// based on training data from https://github.com/mlehman/nlp-intent-toolkit

class TestFastTextIntentMatcher
{
  private static FastTextIntentMatcher intentMatcher;

  @BeforeAll
  static void setUp()
  {
    URL modelUrl = Thread.currentThread().getContextClassLoader().getResource("models/fasttext-weather-model.bin");
    assertThat(modelUrl).isNotNull();

    intentMatcher = new FastTextIntentMatcher(modelUrl, new SimpleTokenizer(true), mock(SlotMatcher.class), 0.85f, -1);

    Intent intent = new Intent("CurrentWeatherIntent");
    intentMatcher.addIntent(intent);

    intent = new Intent("FiveDayForecastIntent");
    intentMatcher.addIntent(intent);

    intent = new Intent("HourlyForecastIntent");
    intentMatcher.addIntent(intent);
  }

  @Test
  void testMatchingOfPhraseInTestData()
  {
    Context context = new Context();
    IntentMatch intentMatch = intentMatcher.match("how hot is it", context, null);

    assertThat(intentMatch).isNotNull();

    Intent intent = intentMatch.getIntent();
    assertThat(intent).isNotNull();
    assertThat(intent.getName()).isEqualTo("CurrentWeatherIntent");
  }

  @Test
  void testBasicMatching()
  {
    Context context = new Context();
    IntentMatch intentMatch = intentMatcher.match("will it rain tomorrow", context, null);

    assertThat(intentMatch).isNotNull();

    Intent intent = intentMatch.getIntent();
    assertThat(intent).isNotNull();
    assertThat(intent.getName()).isEqualTo("FiveDayForecastIntent");
  }

  @Test
  void testNoMatch()
  {
    Context context = new Context();
    IntentMatch intentMatch = intentMatcher.match("the hovercraft is full of eels", context, null);

    assertThat(intentMatch).isNotNull();
    assertThat(intentMatch.matched()).isFalse();
  }

}
