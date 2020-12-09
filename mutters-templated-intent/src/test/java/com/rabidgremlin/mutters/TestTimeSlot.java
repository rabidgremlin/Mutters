/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters;

import static com.google.common.truth.Truth.assertThat;

import java.time.LocalTime;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.Slots;
import com.rabidgremlin.mutters.slots.TimeSlot;
import com.rabidgremlin.mutters.templated.SimpleTokenizer;
import com.rabidgremlin.mutters.templated.TemplatedUtterance;
import com.rabidgremlin.mutters.templated.TemplatedUtteranceMatch;

class TestTimeSlot
{
  private final SimpleTokenizer tokenizer = new SimpleTokenizer();

  @Test
  void testBasicMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("at {time}"));

    String[] input = tokenizer.tokenize("at 6:45am");
    Slots slots = new Slots();
    Context context = new Context();

    TimeSlot slot = new TimeSlot("time");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isTrue();
    assertThat(match.getSlotMatches()).hasSize(1);

    SlotMatch<?> slotMatch = match.getSlotMatches().get(slot);
    assertThat(slotMatch).isNotNull();
    assertThat(slotMatch.getOriginalValue()).isEqualTo("6:45am");
    assertThat(slotMatch.getValue()).isEqualTo(LocalTime.of(6, 45));
  }

  @Test
  void testMatchWithTimeZone()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("at {time}"));

    String[] input = tokenizer.tokenize("at 6:45am");
    Slots slots = new Slots();
    Context context = new Context();
    context.setTimeZone(TimeZone.getTimeZone("Africa/Johannesburg"));

    TimeSlot slot = new TimeSlot("time");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isTrue();
    assertThat(match.getSlotMatches()).hasSize(1);

    SlotMatch<?> slotMatch = match.getSlotMatches().get(slot);
    assertThat(slotMatch).isNotNull();
    assertThat(slotMatch.getOriginalValue()).isEqualTo("6:45am");
    assertThat(slotMatch.getValue()).isEqualTo(LocalTime.of(6, 45));
  }

}
