/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.Slots;
import com.rabidgremlin.mutters.slots.CustomSlot;
import com.rabidgremlin.mutters.templated.SimpleTokenizer;
import com.rabidgremlin.mutters.templated.TemplatedIntent;
import com.rabidgremlin.mutters.templated.TemplatedUtterance;
import com.rabidgremlin.mutters.templated.TemplatedUtteranceMatch;

public class TestUtterance
{
  private SimpleTokenizer tokenizer = new SimpleTokenizer();

  @Test
  public void testSimpleMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("What's the time"));

    String[] input = tokenizer.tokenize("What's the time");
    Slots slots = new Slots();
    Context context = new Context();

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match, is(notNullValue()));
    assertThat(match.isMatched(), is(true));
    assertThat(match.getSlotMatches().size(), is(0));
  }

  @Test
  public void testSimpleNotMatch()
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
  public void testSimpleSlotMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("I like {Color}"));

    String[] input = tokenizer.tokenize("I Like red");
    Slots slots = new Slots();
    Context context = new Context();

    CustomSlot color = new CustomSlot("Color", new String[] { "Green", "blue", "Red" });
    slots.add(color);

    // System.out.println(color);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match, is(notNullValue()));
    assertThat(match.isMatched(), is(true));
    assertThat(match.getSlotMatches().size(), is(1));

    SlotMatch colorMatch = match.getSlotMatches().get(color);
    assertThat(colorMatch, is(notNullValue()));
    assertThat(colorMatch.getOrginalValue(), is("red"));
    assertThat(colorMatch.getValue(), is("Red"));
  }

  @Test
  public void testSimpleNotSlotMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("I like {Color}"));

    Slots slots = new Slots();
    Context context = new Context();

    CustomSlot color = new CustomSlot("Color", new String[] { "Green", "blue", "Red" });
    slots.add(color);

    assertNotMatch(utterance.matches(tokenizer.tokenize("I Like pink"), slots, context));
    assertNotMatch(utterance.matches(tokenizer.tokenize("I Like red hair"), slots, context));
    assertNotMatch(utterance.matches(tokenizer.tokenize("I Like red green"), slots, context));
    assertNotMatch(utterance.matches(tokenizer.tokenize("I Like   "), slots, context));
  }

  private void assertNotMatch(TemplatedUtteranceMatch match)
  {
    assertThat(match, is(notNullValue()));
    assertThat(match.isMatched(), is(false));
    assertThat(match.getSlotMatches().size(), is(0));
  }

  @Test
  public void testMultiSlotMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("I like {Color} and {Food}"));

    String[] input = tokenizer.tokenize("I like red and grapes");
    Slots slots = new Slots();
    Context context = new Context();

    CustomSlot color = new CustomSlot("Color", new String[] { "Green", "blue", "Red" });
    slots.add(color);

    CustomSlot food = new CustomSlot("Food", new String[] { "grapes", "biscuits", "lollipops" });
    slots.add(food);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match, is(notNullValue()));
    assertThat(match.isMatched(), is(true));
    assertThat(match.getSlotMatches().size(), is(2));

    SlotMatch colorMatch = match.getSlotMatches().get(color);
    assertThat(colorMatch, is(notNullValue()));
    assertThat(colorMatch.getOrginalValue(), is("red"));
    assertThat(colorMatch.getValue(), is("Red"));

    SlotMatch foodMatch = match.getSlotMatches().get(food);
    assertThat(foodMatch, is(notNullValue()));
    assertThat(foodMatch.getOrginalValue(), is("grapes"));
    assertThat(foodMatch.getValue(), is("grapes"));
  }

  @Test
  public void testMultiSlotNotMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("I like {Color} and {Food}"));

    Slots slots = new Slots();
    Context context = new Context();

    CustomSlot color = new CustomSlot("Color", new String[] { "Green", "blue", "Red" });
    slots.add(color);

    CustomSlot food = new CustomSlot("Food", new String[] { "grapes", "biscuits", "lollipops" });
    slots.add(food);

    assertNotMatch(utterance.matches(tokenizer.tokenize("I like red and burgers"), slots, context));
    assertNotMatch(utterance.matches(tokenizer.tokenize("I like white and biscuits"), slots, context));
    assertNotMatch(utterance.matches(tokenizer.tokenize("I like red and grapes a lot"), slots, context));
    assertNotMatch(utterance.matches(tokenizer.tokenize("I like red"), slots, context));
    assertNotMatch(utterance.matches(tokenizer.tokenize("I like red and"), slots, context));
    assertNotMatch(utterance.matches(tokenizer.tokenize("I like and grapes"), slots, context));
  }

  @Test
  public void testMultiWordSlotMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("What is the time in {City}"));

    String[] input = tokenizer.tokenize("What is the time in San francisco");
    Slots slots = new Slots();
    Context context = new Context();

    CustomSlot color = new CustomSlot("City", new String[] { "Wellington", "San Francisco", "Auckland" });
    slots.add(color);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match, is(notNullValue()));
    assertThat(match.isMatched(), is(true));
    assertThat(match.getSlotMatches().size(), is(1));

    SlotMatch colorMatch = match.getSlotMatches().get(color);
    assertThat(colorMatch, is(notNullValue()));
    assertThat(colorMatch.getOrginalValue(), is("San francisco"));
    assertThat(colorMatch.getValue(), is("San Francisco"));
  }

  // TODO refactor out to a separate Intent test class
  @Test
  public void testSingleWordMatch()
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
    assertThat(match, is(notNullValue()));
    assertThat(match.isMatched(), is(false));
  }

  @Test
  public void testMultiSlotGreedyMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("I like {Color} and {Food}"));

    String[] input = tokenizer.tokenize("I like red and grapes and bananas");
    Slots slots = new Slots();
    Context context = new Context();

    CustomSlot color = new CustomSlot("Color", new String[] { "Green", "blue", "Red", "Red and Grapes" });
    slots.add(color);

    CustomSlot food = new CustomSlot("Food", new String[] { "bananas", "biscuits", "lollipops" });
    slots.add(food);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match, is(notNullValue()));
    assertThat(match.isMatched(), is(true));
    assertThat(match.getSlotMatches().size(), is(2));

    SlotMatch colorMatch = match.getSlotMatches().get(color);
    assertThat(colorMatch, is(notNullValue()));
    assertThat(colorMatch.getOrginalValue(), is("red and grapes"));
    assertThat(colorMatch.getValue(), is("Red and Grapes"));

    SlotMatch foodMatch = match.getSlotMatches().get(food);
    assertThat(foodMatch, is(notNullValue()));
    assertThat(foodMatch.getOrginalValue(), is("bananas"));
    assertThat(foodMatch.getValue(), is("bananas"));
  }

  @Test
  public void testSlotOnlyMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("{City} {Color} {Food}"));

    Slots slots = new Slots();
    Context context = new Context();

    CustomSlot city = new CustomSlot("City", new String[] { "Wellington", "San Francisco", "Auckland" });
    slots.add(city);

    CustomSlot color = new CustomSlot("Color", new String[] { "Green", "blue", "Red" });
    slots.add(color);

    CustomSlot food = new CustomSlot("Food", new String[] { "bananas", "biscuits", "lollipops" });
    slots.add(food);

    TemplatedUtteranceMatch match = utterance.matches(tokenizer.tokenize("san francisco red bananas "), slots, context);

    assertThat(match, is(notNullValue()));
    assertThat(match.isMatched(), is(true));
    assertThat(match.getSlotMatches().size(), is(3));

    SlotMatch cityMatch = match.getSlotMatches().get(city);
    assertThat(cityMatch, is(notNullValue()));
    assertThat(cityMatch.getOrginalValue(), is("san francisco"));
    assertThat(cityMatch.getValue(), is("San Francisco"));

    SlotMatch colorMatch = match.getSlotMatches().get(color);
    assertThat(colorMatch, is(notNullValue()));
    assertThat(colorMatch.getOrginalValue(), is("red"));
    assertThat(colorMatch.getValue(), is("Red"));

    SlotMatch foodMatch = match.getSlotMatches().get(food);
    assertThat(foodMatch, is(notNullValue()));
    assertThat(foodMatch.getOrginalValue(), is("bananas"));
    assertThat(foodMatch.getValue(), is("bananas"));
  }

}
