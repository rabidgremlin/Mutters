package com.rabidgremlin.mutters;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.rabidgremlin.mutters.input.CleanedInput;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.input.InputCleaner;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.Slots;
import com.rabidgremlin.mutters.slots.NumberSlot;
import com.rabidgremlin.mutters.templated.TemplatedUtterance;
import com.rabidgremlin.mutters.templated.TemplatedUtteranceMatch;

public class TestNumberSlot
{

  @Test
  public void testBasicWordMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance("The balance is {number}");

    CleanedInput input = InputCleaner
        .cleanInput("The balance is One hundred two thousand and thirty four");
    Slots slots = new Slots();
    Context context = new Context();

    NumberSlot slot = new NumberSlot("number");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match, is(notNullValue()));
    assertThat(match.isMatched(), is(true));
    assertThat(match.getSlotMatches().size(), is(1));

    SlotMatch slotMatch = match.getSlotMatches().get(slot);
    assertThat(slotMatch, is(notNullValue()));
    assertThat(slotMatch.getOrginalValue(), is("One hundred two thousand and thirty four"));
    assertThat(slotMatch.getValue(), is(102034L));
  }

  @Test
  public void testWordStringToNumber()
  {
    NumberSlot slot = new NumberSlot("test");
    Number result = slot
        .wordStringToNumber("Three hundred fifty two thousand two hundred and sixty one");

    assertThat(result, is(notNullValue()));
    assertThat(result, is(352261L));

    result = slot.wordStringToNumber("Three hundred and bad");
    assertThat(result, is(nullValue()));
  }

  @Test
  public void testBasicNumberMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance("The balance is {number}");

    CleanedInput input = InputCleaner.cleanInput("The balance is 123");
    Slots slots = new Slots();
    Context context = new Context();

    NumberSlot slot = new NumberSlot("number");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match, is(notNullValue()));
    assertThat(match.isMatched(), is(true));
    assertThat(match.getSlotMatches().size(), is(1));

    SlotMatch slotMatch = match.getSlotMatches().get(slot);
    assertThat(slotMatch, is(notNullValue()));
    assertThat(slotMatch.getOrginalValue(), is("123"));
    assertThat(slotMatch.getValue(), is(123L));
  }

  @Test
  public void testBasicDecimalMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance("The balance is {number}");

    CleanedInput input = InputCleaner.cleanInput("The balance is 546.12");
    Slots slots = new Slots();
    Context context = new Context();

    NumberSlot slot = new NumberSlot("number");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match, is(notNullValue()));
    assertThat(match.isMatched(), is(true));
    assertThat(match.getSlotMatches().size(), is(1));

    SlotMatch slotMatch = match.getSlotMatches().get(slot);
    assertThat(slotMatch, is(notNullValue()));
    assertThat(slotMatch.getOrginalValue(), is("546.12"));
    assertThat(slotMatch.getValue(), is(546.12));
  }

}
