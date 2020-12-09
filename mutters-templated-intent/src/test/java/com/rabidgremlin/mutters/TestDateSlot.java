/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters;

import static com.google.common.truth.Truth.assertThat;

import java.time.LocalDate;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.Slots;
import com.rabidgremlin.mutters.slots.DateSlot;
import com.rabidgremlin.mutters.templated.SimpleTokenizer;
import com.rabidgremlin.mutters.templated.TemplatedUtterance;
import com.rabidgremlin.mutters.templated.TemplatedUtteranceMatch;

class TestDateSlot
{
  private final SimpleTokenizer tokenizer = new SimpleTokenizer();

  @Test
  void testBasicMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("for the {date}"));

    String[] input = tokenizer.tokenize("for the 30th May 1974");
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
    assertThat(slotMatch.getOriginalValue()).isEqualTo("30th May 1974");
    assertThat(slotMatch.getValue()).isEqualTo(LocalDate.of(1974, 5, 30));
  }

  @Test
  void testMatchWithTimeZone()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("for the {date}"));

    String[] input = tokenizer.tokenize("for the 30th May 1974");
    Slots slots = new Slots();
    Context context = new Context();
    context.setTimeZone(TimeZone.getTimeZone("Africa/Johannesburg"));

    DateSlot slot = new DateSlot("date");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isTrue();
    assertThat(match.getSlotMatches()).hasSize(1);

    SlotMatch<?> slotMatch = match.getSlotMatches().get(slot);
    assertThat(slotMatch).isNotNull();
    assertThat(slotMatch.getOriginalValue()).isEqualTo("30th May 1974");
    assertThat(slotMatch.getValue()).isEqualTo(LocalDate.of(1974, 5, 30));
  }

  @Test
  void testDontMatchOnJustTime()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("{date}"));

    String[] input = tokenizer.tokenize("10pm");
    Slots slots = new Slots();
    Context context = new Context();

    DateSlot slot = new DateSlot("date");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isFalse();
  }

  @Test
  void testNZDateFullNumeric()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("{date}"));

    String[] input = tokenizer.tokenize("20/5/2016");
    Slots slots = new Slots();
    Context context = new Context();
    context.setLocale(new Locale("en", "NZ"));

    DateSlot slot = new DateSlot("date");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isTrue();

    SlotMatch<?> slotMatch = match.getSlotMatches().get(slot);
    LocalDate dateMatch = (LocalDate) slotMatch.getValue();
    assertThat(dateMatch.getDayOfMonth()).isEqualTo(20);
    assertThat(dateMatch.getMonth().getValue()).isEqualTo(5);
    assertThat(dateMatch.getYear()).isEqualTo(2016);
  }

  @Test
  void testNZDateShortYear()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("{date}"));

    String[] input = tokenizer.tokenize("20/5/16");
    Slots slots = new Slots();
    Context context = new Context();
    context.setLocale(new Locale("en", "NZ"));

    DateSlot slot = new DateSlot("date");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isTrue();

    SlotMatch<?> slotMatch = match.getSlotMatches().get(slot);
    LocalDate dateMatch = (LocalDate) slotMatch.getValue();
    assertThat(dateMatch.getDayOfMonth()).isEqualTo(20);
    assertThat(dateMatch.getMonth().getValue()).isEqualTo(5);
    assertThat(dateMatch.getYear()).isEqualTo(2016);
  }

  @Test
  void testNZDateDayMonthOnly()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("{date}"));

    String[] input = tokenizer.tokenize("20/5");
    Slots slots = new Slots();
    Context context = new Context();
    context.setLocale(new Locale("en", "NZ"));

    DateSlot slot = new DateSlot("date");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isTrue();

    SlotMatch<?> slotMatch = match.getSlotMatches().get(slot);
    LocalDate dateMatch = (LocalDate) slotMatch.getValue();
    assertThat(dateMatch.getDayOfMonth()).isEqualTo(20);
    assertThat(dateMatch.getMonth().getValue()).isEqualTo(5);
    assertThat(dateMatch.getYear()).isEqualTo(LocalDate.now().getYear());
  }

  @Test
  void testNZDateDayMonthAsTextOnly()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("{date}"));

    String[] input = tokenizer.tokenize("1 dec");
    Slots slots = new Slots();
    Context context = new Context();
    context.setLocale(new Locale("en", "NZ"));

    DateSlot slot = new DateSlot("date");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isTrue();

    SlotMatch<?> slotMatch = match.getSlotMatches().get(slot);
    LocalDate dateMatch = (LocalDate) slotMatch.getValue();
    assertThat(dateMatch.getDayOfMonth()).isEqualTo(1);
    assertThat(dateMatch.getMonth().getValue()).isEqualTo(12);
    assertThat(dateMatch.getYear()).isEqualTo(LocalDate.now().getYear());
  }

  // TODO handle different TZ in context
  @Test
  void testToday()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("{date}"));

    String[] input = tokenizer.tokenize("today");
    Slots slots = new Slots();
    Context context = new Context();

    DateSlot slot = new DateSlot("date");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isTrue();

    SlotMatch<?> slotMatch = match.getSlotMatches().get(slot);
    LocalDate dateMatch = (LocalDate) slotMatch.getValue();

    LocalDate today = LocalDate.now();
    assertThat(dateMatch).isEqualTo(today);
  }

  // TODO handle different TZ in context
  @Test
  void testTomorrow()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("{date}"));

    String[] input = tokenizer.tokenize("tomorrow");
    Slots slots = new Slots();
    Context context = new Context();

    DateSlot slot = new DateSlot("date");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isTrue();

    SlotMatch<?> slotMatch = match.getSlotMatches().get(slot);
    LocalDate dateMatch = (LocalDate) slotMatch.getValue();

    LocalDate tommorrow = LocalDate.now().plusDays(1);
    assertThat(dateMatch).isEqualTo(tommorrow);
  }

  @Test
  void testMismatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("{date}"));

    String[] input = tokenizer.tokenize("book auckland to wellington for friday");
    Slots slots = new Slots();
    Context context = new Context();

    DateSlot slot = new DateSlot("date");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match).isNotNull();
    assertThat(match.isMatched()).isFalse();
  }

}
