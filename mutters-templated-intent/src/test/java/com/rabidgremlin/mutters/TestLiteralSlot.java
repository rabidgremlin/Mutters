/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.Slots;
import com.rabidgremlin.mutters.slots.LiteralSlot;
import com.rabidgremlin.mutters.templated.SimpleTokenizer;
import com.rabidgremlin.mutters.templated.TemplatedUtterance;
import com.rabidgremlin.mutters.templated.TemplatedUtteranceMatch;

class TestLiteralSlot
{
  private final SimpleTokenizer tokenizer = new SimpleTokenizer();

  @Test
  void testBasicMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("My name is {name}"));

    String[] input = tokenizer.tokenize("My Name is Kilroy Jones");
    Slots slots = new Slots();
    Context context = new Context();

    LiteralSlot slot = new LiteralSlot("name");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isTrue();
    assertThat(match.getSlotMatches()).hasSize(1);

    SlotMatch<?> slotMatch = match.getSlotMatches().get(slot);
    assertThat(slotMatch).isNotNull();
    assertThat(slotMatch.getOriginalValue()).isEqualTo("Kilroy Jones");
    assertThat(slotMatch.getValue()).isEqualTo("kilroy jones");
  }

  @Test
  void testMidUtteranceMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("The {something} is good"));

    String[] input = tokenizer.tokenize("The pinot noir is good");
    Slots slots = new Slots();
    Context context = new Context();

    LiteralSlot slot = new LiteralSlot("something");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isTrue();
    assertThat(match.getSlotMatches()).hasSize(1);

    SlotMatch<?> slotMatch = match.getSlotMatches().get(slot);
    assertThat(slotMatch).isNotNull();
    assertThat(slotMatch.getOriginalValue()).isEqualTo("pinot noir");
    assertThat(slotMatch.getValue()).isEqualTo("pinot noir");
  }

}
