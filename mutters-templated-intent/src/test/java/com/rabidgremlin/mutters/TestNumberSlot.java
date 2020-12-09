/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.Slots;
import com.rabidgremlin.mutters.slots.NumberSlot;
import com.rabidgremlin.mutters.templated.SimpleTokenizer;
import com.rabidgremlin.mutters.templated.TemplatedUtterance;
import com.rabidgremlin.mutters.templated.TemplatedUtteranceMatch;

class TestNumberSlot
{
  private final SimpleTokenizer tokenizer = new SimpleTokenizer();

  @Test
  void testBasicWordMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("The balance is {number}"));

    String[] input = tokenizer.tokenize("The balance is One hundred two thousand and thirty four");
    Slots slots = new Slots();
    Context context = new Context();

    NumberSlot slot = new NumberSlot("number");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isTrue();
    assertThat(match.getSlotMatches()).hasSize(1);

    SlotMatch<?> slotMatch = match.getSlotMatches().get(slot);
    assertThat(slotMatch).isNotNull();
    assertThat(slotMatch.getOriginalValue()).isEqualTo("One hundred two thousand and thirty four");
    assertThat(slotMatch.getValue()).isEqualTo(102034L);
  }

  @Test
  void testWordStringToNumber()
  {
    NumberSlot slot = new NumberSlot("test");
    Number result = slot.wordStringToNumber("Three hundred fifty two thousand two hundred and sixty one");

    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(352261L);

    result = slot.wordStringToNumber("Three hundred and bad");
    assertThat(result).isNull();
  }

  @Test
  void testBasicNumberMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("The balance is {number}"));

    String[] input = tokenizer.tokenize("The balance is 123");
    Slots slots = new Slots();
    Context context = new Context();

    NumberSlot slot = new NumberSlot("number");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isTrue();
    assertThat(match.getSlotMatches()).hasSize(1);

    SlotMatch<?> slotMatch = match.getSlotMatches().get(slot);
    assertThat(slotMatch).isNotNull();
    assertThat(slotMatch.getOriginalValue()).isEqualTo("123");
    assertThat(slotMatch.getValue()).isEqualTo(123L);
  }

  @Test
  void testBasicDecimalMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("The balance is {number}"));

    String[] input = tokenizer.tokenize("The balance is 546.12");
    Slots slots = new Slots();
    Context context = new Context();

    NumberSlot slot = new NumberSlot("number");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isTrue();
    assertThat(match.getSlotMatches()).hasSize(1);

    SlotMatch<?> slotMatch = match.getSlotMatches().get(slot);
    assertThat(slotMatch).isNotNull();
    assertThat(slotMatch.getOriginalValue()).isEqualTo("546.12");
    assertThat(slotMatch.getValue()).isEqualTo(546.12);
  }

}
