/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters;

import static com.google.common.truth.Truth.assertThat;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.Slots;
import com.rabidgremlin.mutters.slots.DateSlot;
import com.rabidgremlin.mutters.slots.DateTimeSlot;
import com.rabidgremlin.mutters.templated.SimpleTokenizer;
import com.rabidgremlin.mutters.templated.TemplatedUtterance;
import com.rabidgremlin.mutters.templated.TemplatedUtteranceMatch;

class TestDateTimeSlot
{
  private final SimpleTokenizer tokenizer = new SimpleTokenizer();

  @Test
  void testBasicMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("for the {datetime}"));

    String[] input = tokenizer.tokenize("for the 30th May 1974 at 10pm");
    Slots slots = new Slots();
    Context context = new Context();

    DateTimeSlot slot = new DateTimeSlot("datetime");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isTrue();
    assertThat(match.getSlotMatches()).hasSize(1);

    SlotMatch<?> slotMatch = match.getSlotMatches().get(slot);
    assertThat(slotMatch).isNotNull();
    assertThat(slotMatch.getOriginalValue()).isEqualTo("30th May 1974 at 10pm");
    assertThat(slotMatch.getValue()).isEqualTo(ZonedDateTime.of(1974, 5, 30, 22, 0, 0, 0, ZoneId.systemDefault()));
  }

  @Test
  void testMatchWithTimeZone()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("for the {datetime}"));

    String[] input = tokenizer.tokenize("for the 30th May 1974 at 10pm");
    Slots slots = new Slots();
    Context context = new Context();
    context.setTimeZone(TimeZone.getTimeZone("Africa/Johannesburg"));

    DateTimeSlot slot = new DateTimeSlot("datetime");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isTrue();
    assertThat(match.getSlotMatches()).hasSize(1);

    SlotMatch<?> slotMatch = match.getSlotMatches().get(slot);
    assertThat(slotMatch).isNotNull();
    assertThat(slotMatch.getOriginalValue()).isEqualTo("30th May 1974 at 10pm");
    assertThat(slotMatch.getValue())
        .isEqualTo(ZonedDateTime.of(1974, 5, 30, 22, 0, 0, 0, context.getTimeZone().toZoneId()));
  }

  @Test
  void testLastWeek()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("Give me the report for {date}"));

    String[] input = tokenizer.tokenize("Give me the report for last week");
    Slots slots = new Slots();
    Context context = new Context();

    DateSlot slot = new DateSlot("date");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isTrue();
    assertThat(match.getSlotMatches()).hasSize(1);

    SlotMatch<?> slotMatch = match.getSlotMatches().get(slot);
    assertThat(slotMatch).isNotNull();
    assertThat(slotMatch.getOriginalValue()).isEqualTo("last week");

    LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);

    assertThat(slotMatch.getValue()).isEqualTo(sevenDaysAgo);
  }

}
