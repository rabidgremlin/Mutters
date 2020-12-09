/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters;

import static com.google.common.truth.Truth.assertThat;

import java.util.SortedMap;
import java.util.SortedSet;

import org.junit.jupiter.api.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.slots.CustomSlot;
import com.rabidgremlin.mutters.slots.DefaultValueSlot;
import com.rabidgremlin.mutters.slots.LiteralSlot;
import com.rabidgremlin.mutters.slots.NumberSlot;
import com.rabidgremlin.mutters.templated.SimpleTokenizer;
import com.rabidgremlin.mutters.templated.TemplatedIntent;
import com.rabidgremlin.mutters.templated.TemplatedIntentMatcher;

class TestIntentMatcher
{
  private final SimpleTokenizer tokenizer = new SimpleTokenizer();

  @Test
  void testBasicMatching()
  {
    TemplatedIntentMatcher matcher = new TemplatedIntentMatcher(tokenizer);
    TemplatedIntent additionIntent = matcher.addIntent("Addition");

    additionIntent.addUtterance("What's {number1} + {number2}");
    additionIntent.addUtterance("What is {number1} + {number2}");
    additionIntent.addUtterance("Add {number1} and {number2}");
    additionIntent.addUtterance("{number1} plus {number2}");

    NumberSlot number1 = new NumberSlot("number1");
    NumberSlot number2 = new NumberSlot("number2");

    additionIntent.addSlot(number1);
    additionIntent.addSlot(number2);

    IntentMatch intentMatch = matcher.match("What is 1 + 5", new Context(), null);

    assertThat(intentMatch).isNotNull();
    assertThat(intentMatch.getIntent()).isEqualTo(additionIntent);
    assertThat(intentMatch.getSlotMatches()).hasSize(2);

    SlotMatch<?> number1Match = intentMatch.getSlotMatches().get(number1);
    assertThat(number1Match).isNotNull();
    assertThat(number1Match.getValue()).isEqualTo(1L);

    SlotMatch<?> number2Match = intentMatch.getSlotMatches().get(number2);
    assertThat(number2Match).isNotNull();
    assertThat(number2Match.getValue()).isEqualTo(5L);

  }

  @Test
  void testBrokenMatch()
  {
    TemplatedIntentMatcher matcher = new TemplatedIntentMatcher(tokenizer);
    TemplatedIntent intent = matcher.addIntent("Hello");

    intent.addUtterance("hello");
    intent.addUtterance("hi");
    intent.addUtterance("hiya");

    IntentMatch intentMatch = matcher.match("book this flight", new Context(), null);

    assertThat(intentMatch).isNotNull();
    assertThat(intentMatch.matched()).isFalse();
  }

  @Test
  void testBrokenAirportMatch()
  {
    TemplatedIntentMatcher matcher = new TemplatedIntentMatcher(tokenizer);
    TemplatedIntent intent = matcher.addIntent("GetAirport");

    intent.addUtterance("{Airport}");

    CustomSlot airportSlot = new CustomSlot("Airport", new String[] { "NEWCASTLE" });
    intent.addSlot(airportSlot);

    IntentMatch intentMatch = matcher.match("next friday", new Context(), null);

    assertThat(intentMatch).isNotNull();
    assertThat(intentMatch.matched()).isFalse();
  }

  @Test
  void testHandlingOfEmptyOrTokenizedOutInputs()
  {
    TemplatedIntentMatcher matcher = new TemplatedIntentMatcher(tokenizer);
    TemplatedIntent intent = matcher.addIntent("AcceptAnything");

    intent.addUtterance("{AnyThing}");

    LiteralSlot slot = new LiteralSlot("AnyThing");
    intent.addSlot(slot);

    IntentMatch intentMatch = matcher.match("Bob", new Context(), null);
    assertThat(intentMatch).isNotNull();

    intentMatch = matcher.match("", new Context(), null);
    assertThat(intentMatch).isNotNull();
    assertThat(intentMatch.matched()).isFalse();

    intentMatch = matcher.match(" ", new Context(), null);
    assertThat(intentMatch).isNotNull();
    assertThat(intentMatch.matched()).isFalse();

    intentMatch = matcher.match("?", new Context(), null);
    assertThat(intentMatch).isNotNull();
    assertThat(intentMatch.matched()).isFalse();

    intentMatch = matcher.match("...", new Context(), null);
    assertThat(intentMatch).isNotNull();
    assertThat(intentMatch.matched()).isFalse();

    intentMatch = matcher.match(" ?", new Context(), null);
    assertThat(intentMatch).isNotNull();
    assertThat(intentMatch.matched()).isFalse();
  }

  // slot that matches a colour or defaults to black
  static class ColorsSlot extends CustomSlot implements DefaultValueSlot<String>
  {
    ColorsSlot()
    {
      super("Color", "Red", "Green", "Blue", "White");
    }

    @Override
    public String getDefaultValue()
    {
      return "Black";
    }
  }

  @Test
  void testDefaultValueHandling()
  {
    TemplatedIntentMatcher matcher = new TemplatedIntentMatcher(tokenizer);

    TemplatedIntent intent = matcher.addIntent("FavColourIntent");

    ColorsSlot colorSlot = new ColorsSlot();
    intent.addSlot(colorSlot);

    intent.addUtterance("My favourite color is {Color}");
    intent.addUtterance("{Color} is my favourite");
    intent.addUtterance("I have a favourite color"); // utterance without a
                                                     // slot, should default to
                                                     // Black

    IntentMatch intentMatch = matcher.match("My favourite color is green", new Context(), null);
    assertThat(intentMatch).isNotNull();
    assertThat(intentMatch.getSlotMatches()).hasSize(1);
    SlotMatch<?> colourMatch = intentMatch.getSlotMatches().get(colorSlot);
    assertThat(colourMatch).isNotNull();
    assertThat(colourMatch.getValue()).isEqualTo("Green");

    intentMatch = matcher.match("Red is my favourite", new Context(), null);
    assertThat(intentMatch).isNotNull();
    assertThat(intentMatch.getSlotMatches()).hasSize(1);
    colourMatch = intentMatch.getSlotMatches().get(colorSlot);
    assertThat(colourMatch).isNotNull();
    assertThat(colourMatch.getValue()).isEqualTo("Red");

    intentMatch = matcher.match("I have a favourite color", new Context(), null);
    assertThat(intentMatch).isNotNull();
    assertThat(intentMatch.getSlotMatches()).hasSize(1);
    colourMatch = intentMatch.getSlotMatches().get(colorSlot);
    assertThat(colourMatch).isNotNull();
    assertThat(colourMatch.getValue()).isEqualTo("Black");
  }

  @Test
  void testThatScoringIsReturnedForSuccessfulMatch()
  {
    TemplatedIntentMatcher matcher = new TemplatedIntentMatcher(tokenizer);
    TemplatedIntent intent = matcher.addIntent("Hello");

    intent.addUtterance("hello");
    intent.addUtterance("hi");
    intent.addUtterance("hiya");

    IntentMatch intentMatch = matcher.match("hello", new Context(), null);

    // check that we matched on hello
    assertThat(intentMatch).isNotNull();
    assertThat(intentMatch.getIntent().getName()).isEqualTo("Hello");

    // check that we got a scores
    assertThat(intentMatch.getMatcherScores()).isNotNull();
    SortedMap<Double, SortedSet<String>> scores = intentMatch.getMatcherScores().getScores();

    // we should only have one score
    assertThat(scores.keySet()).hasSize(1);

    // score should be 100% (1.0)
    assertThat(scores.firstKey()).isEqualTo(1.0);

    // it should be for the Hello intent
    assertThat(scores.get(scores.firstKey())).contains("Hello");
  }

}
