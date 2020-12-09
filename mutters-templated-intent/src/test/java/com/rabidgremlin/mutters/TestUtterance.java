/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters;

import static com.google.common.truth.Truth.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.Slots;
import com.rabidgremlin.mutters.slots.CustomSlot;
import com.rabidgremlin.mutters.templated.SimpleTokenizer;
import com.rabidgremlin.mutters.templated.TemplatedIntent;
import com.rabidgremlin.mutters.templated.TemplatedUtterance;
import com.rabidgremlin.mutters.templated.TemplatedUtteranceMatch;

class TestUtterance
{
  private final SimpleTokenizer tokenizer = new SimpleTokenizer();

  @Test
  void testSimpleMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("What's the time"));

    String[] input = tokenizer.tokenize("What's the time");
    Slots slots = new Slots();
    Context context = new Context();

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isTrue();
    assertThat(match.getSlotMatches()).hasSize(0);
  }

  @Test
  void testSimpleNotMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("This is that and that is this"));

    Slots slots = new Slots();
    Context context = new Context();

    assertNotMatch(utterance.matches(tokenizer.tokenize("This is really not all that"), slots, context));
    assertNotMatch(utterance.matches(tokenizer.tokenize("This is that and that is"), slots, context));
    assertNotMatch(utterance.matches(tokenizer.tokenize("This is that and that is that"), slots, context));
    assertNotMatch(utterance.matches(tokenizer.tokenize("This is that and that is this or that"), slots, context));
  }

  @Test
  void testSimpleSlotMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("I like {Color}"));

    String[] input = tokenizer.tokenize("I Like red");
    Slots slots = new Slots();
    Context context = new Context();

    CustomSlot color = new CustomSlot("Color", "Green", "blue", "Red");
    slots.add(color);

    // System.out.println(color);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isTrue();
    assertThat(match.getSlotMatches()).hasSize(1);

    SlotMatch<?> colorMatch = match.getSlotMatches().get(color);
    assertThat(colorMatch).isNotNull();
    assertThat(colorMatch.getOriginalValue()).isEqualTo("red");
    assertThat(colorMatch.getValue()).isEqualTo("Red");
  }

  @Test
  void testSimpleNotSlotMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("I like {Color}"));

    Slots slots = new Slots();
    Context context = new Context();

    CustomSlot color = new CustomSlot("Color", "Green", "blue", "Red");
    slots.add(color);

    assertNotMatch(utterance.matches(tokenizer.tokenize("I Like pink"), slots, context));
    assertNotMatch(utterance.matches(tokenizer.tokenize("I Like red hair"), slots, context));
    assertNotMatch(utterance.matches(tokenizer.tokenize("I Like red green"), slots, context));
    assertNotMatch(utterance.matches(tokenizer.tokenize("I Like   "), slots, context));
  }

  private void assertNotMatch(TemplatedUtteranceMatch match)
  {
    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isFalse();
    assertThat(match.getSlotMatches()).hasSize(0);
  }

  @Test
  void testMultiSlotMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("I like {Color} and {Food}"));

    String[] input = tokenizer.tokenize("I like red and grapes");
    Slots slots = new Slots();
    Context context = new Context();

    CustomSlot color = new CustomSlot("Color", "Green", "blue", "Red");
    slots.add(color);

    CustomSlot food = new CustomSlot("Food", "grapes", "biscuits", "lollipops");
    slots.add(food);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isTrue();
    assertThat(match.getSlotMatches()).hasSize(2);

    SlotMatch<?> colorMatch = match.getSlotMatches().get(color);
    assertThat(colorMatch).isNotNull();
    assertThat(colorMatch.getOriginalValue()).isEqualTo("red");
    assertThat(colorMatch.getValue()).isEqualTo("Red");

    SlotMatch<?> foodMatch = match.getSlotMatches().get(food);
    assertThat(foodMatch).isNotNull();
    assertThat(foodMatch.getOriginalValue()).isEqualTo("grapes");
    assertThat(foodMatch.getValue()).isEqualTo("grapes");
  }

  @Test
  void testMultiSlotNotMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("I like {Color} and {Food}"));

    Slots slots = new Slots();
    Context context = new Context();

    CustomSlot color = new CustomSlot("Color", "Green", "blue", "Red");
    slots.add(color);

    CustomSlot food = new CustomSlot("Food", "grapes", "biscuits", "lollipops");
    slots.add(food);

    assertNotMatch(utterance.matches(tokenizer.tokenize("I like red and burgers"), slots, context));
    assertNotMatch(utterance.matches(tokenizer.tokenize("I like white and biscuits"), slots, context));
    assertNotMatch(utterance.matches(tokenizer.tokenize("I like red and grapes a lot"), slots, context));
    assertNotMatch(utterance.matches(tokenizer.tokenize("I like red"), slots, context));
    assertNotMatch(utterance.matches(tokenizer.tokenize("I like red and"), slots, context));
    assertNotMatch(utterance.matches(tokenizer.tokenize("I like and grapes"), slots, context));
  }

  @Test
  void testMultiWordSlotMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("What is the time in {City}"));

    String[] input = tokenizer.tokenize("What is the time in San francisco");
    Slots slots = new Slots();
    Context context = new Context();

    CustomSlot color = new CustomSlot("City", "Wellington", "San Francisco", "Auckland");
    slots.add(color);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isTrue();
    assertThat(match.getSlotMatches()).hasSize(1);

    SlotMatch<?> colorMatch = match.getSlotMatches().get(color);
    assertThat(colorMatch).isNotNull();
    assertThat(colorMatch.getOriginalValue()).isEqualTo("San francisco");
    assertThat(colorMatch.getValue()).isEqualTo("San Francisco");
  }

  // TODO refactor out to a separate Intent test class
  @Test
  void testSingleWordMatch()
  {
    TemplatedIntent intent = new TemplatedIntent("YesIntent", tokenizer);

    // intent.addUtterance(new Utterance("yes"));
    // intent.addUtterance(new Utterance("yes please"));
    // intent.addUtterance(new Utterance("yes thanks"));
    // intent.addUtterance(new Utterance("yes thank you"));
    // intent.addUtterance(new Utterance("yep"));
    // intent.addUtterance(new Utterance("Y"));
    // intent.addUtterance(new Utterance("Ok"));
    intent.addUtterance("K");

    String[] input = tokenizer.tokenize("Kia Ora");
    Context context = new Context();

    TemplatedUtteranceMatch match = intent.matches(input, context);
    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isFalse();
  }

  @Test
  void testMultiSlotGreedyMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("I like {Color} and {Food}"));

    String[] input = tokenizer.tokenize("I like red and grapes and bananas");
    Slots slots = new Slots();
    Context context = new Context();

    CustomSlot color = new CustomSlot("Color", "Green", "blue", "Red", "Red and Grapes");
    slots.add(color);

    CustomSlot food = new CustomSlot("Food", "bananas", "biscuits", "lollipops");
    slots.add(food);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isTrue();
    assertThat(match.getSlotMatches()).hasSize(2);

    SlotMatch<?> colorMatch = match.getSlotMatches().get(color);
    assertThat(colorMatch).isNotNull();
    assertThat(colorMatch.getOriginalValue()).isEqualTo("red and grapes");
    assertThat(colorMatch.getValue()).isEqualTo("Red and Grapes");

    SlotMatch<?> foodMatch = match.getSlotMatches().get(food);
    assertThat(foodMatch).isNotNull();
    assertThat(foodMatch.getOriginalValue()).isEqualTo("bananas");
    assertThat(foodMatch.getValue()).isEqualTo("bananas");
  }

  @Test
  void testSlotOnlyMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("{City} {Color} {Food}"));

    Slots slots = new Slots();
    Context context = new Context();

    CustomSlot city = new CustomSlot("City", "Wellington", "San Francisco", "Auckland");
    slots.add(city);

    CustomSlot color = new CustomSlot("Color", "Green", "blue", "Red");
    slots.add(color);

    CustomSlot food = new CustomSlot("Food", "bananas", "biscuits", "lollipops");
    slots.add(food);

    TemplatedUtteranceMatch match = utterance.matches(tokenizer.tokenize("san francisco red bananas "), slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isTrue();
    assertThat(match.getSlotMatches()).hasSize(3);

    SlotMatch<?> cityMatch = match.getSlotMatches().get(city);
    assertThat(cityMatch).isNotNull();
    assertThat(cityMatch.getOriginalValue()).isEqualTo("san francisco");
    assertThat(cityMatch.getValue()).isEqualTo("San Francisco");

    SlotMatch<?> colorMatch = match.getSlotMatches().get(color);
    assertThat(colorMatch).isNotNull();
    assertThat(colorMatch.getOriginalValue()).isEqualTo("red");
    assertThat(colorMatch.getValue()).isEqualTo("Red");

    SlotMatch<?> foodMatch = match.getSlotMatches().get(food);
    assertThat(foodMatch).isNotNull();
    assertThat(foodMatch.getOriginalValue()).isEqualTo("bananas");
    assertThat(foodMatch.getValue()).isEqualTo("bananas");
  }

  @Test
  void shouldReturnCompleteListOfExpectedSlots()
  {
    TemplatedUtterance teplatedUtterance = new TemplatedUtterance(
        tokenizer.tokenize("I like {Color} and {Food} and {color} food"));

    List<String> expectedSlotNames = teplatedUtterance.getExpectedSlotNames();
    assertThat(expectedSlotNames).isNotNull();
    assertThat(expectedSlotNames).hasSize(2);
    assertThat(expectedSlotNames.contains("color")).isTrue();
    assertThat(expectedSlotNames.contains("food")).isTrue();
  }
}
