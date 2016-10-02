package com.rabidgremlin.mutters;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.IsCloseTo.*;
import static org.junit.Assert.*;

import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.Test;

import com.rabidgremlin.mutters.core.CleanedInput;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.InputCleaner;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.Slots;
import com.rabidgremlin.mutters.slots.DateSlot;
import com.rabidgremlin.mutters.slots.DateTimeSlot;
import com.rabidgremlin.mutters.templated.TemplatedUtterance;
import com.rabidgremlin.mutters.templated.TemplatedUtteranceMatch;
import com.rabidgremlin.mutters.util.Utils;

public class TestDateSlot
{

  @Test
  public void testBasicMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance("for the {date}");

    CleanedInput input = InputCleaner.cleanInput("for the 30th May 1974");
    Slots slots = new Slots();
    Context context = new Context();

    DateSlot slot = new DateSlot("date");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match, is(notNullValue()));
    assertThat(match.isMatched(), is(true));
    assertThat(match.getSlotMatches().size(), is(1));

    SlotMatch slotMatch = match.getSlotMatches().get(slot);
    assertThat(slotMatch, is(notNullValue()));
    assertThat(slotMatch.getOrginalValue(), is("30th May 1974"));
    assertThat(slotMatch.getValue(), is(new LocalDate(1974, 5, 30)));
  }

  @Test
  public void testMatchWithTimeZone()
  {
    TemplatedUtterance utterance = new TemplatedUtterance("for the {date}");

    CleanedInput input = InputCleaner.cleanInput("for the 30th May 1974");
    Slots slots = new Slots();
    Context context = new Context();
    context.setTimeZone(TimeZone.getTimeZone("Africa/Johannesburg"));

    DateSlot slot = new DateSlot("date");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match, is(notNullValue()));
    assertThat(match.isMatched(), is(true));
    assertThat(match.getSlotMatches().size(), is(1));

    SlotMatch slotMatch = match.getSlotMatches().get(slot);
    assertThat(slotMatch, is(notNullValue()));
    assertThat(slotMatch.getOrginalValue(), is("30th May 1974"));
    assertThat(slotMatch.getValue(), is(new LocalDate(1974, 5, 30)));
  }

  @Test
  public void testDontMatchOnJustTime()
  {
    TemplatedUtterance utterance = new TemplatedUtterance("{date}");

    CleanedInput input = InputCleaner.cleanInput("10pm");
    Slots slots = new Slots();
    Context context = new Context();

    DateSlot slot = new DateSlot("date");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match, is(notNullValue()));
    assertThat(match.isMatched(), is(false));
  }

  @Test
  public void testNZDate()
  {
    TemplatedUtterance utterance = new TemplatedUtterance("{date}");

    CleanedInput input = InputCleaner.cleanInput("2 Aug");
    Slots slots = new Slots();
    Context context = new Context();

    DateSlot slot = new DateSlot("date");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match, is(notNullValue()));
    assertThat(match.isMatched(), is(true));

    SlotMatch slotMatch = match.getSlotMatches().get(slot);
    LocalDate dateMatch = (LocalDate) slotMatch.getValue();
    assertThat(dateMatch.getDayOfMonth(), is(2));
    assertThat(dateMatch.getMonthOfYear(), is(8));
  }

  @Test
  public void testNZDate2()
  {
    TemplatedUtterance utterance = new TemplatedUtterance("{date}");

    CleanedInput input = InputCleaner.cleanInput("20/5/2016");
    Slots slots = new Slots();
    Context context = new Context();

    DateSlot slot = new DateSlot("date");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match, is(notNullValue()));
    assertThat(match.isMatched(), is(true));

    SlotMatch slotMatch = match.getSlotMatches().get(slot);
    LocalDate dateMatch = (LocalDate) slotMatch.getValue();
    assertThat(dateMatch.getDayOfMonth(), is(20));
    assertThat(dateMatch.getMonthOfYear(), is(5));
    assertThat(dateMatch.getYear(), is(2016));
  }

  // TODO handle different TZ in context
  @Test
  public void testToday()
  {
    TemplatedUtterance utterance = new TemplatedUtterance("{date}");

    CleanedInput input = InputCleaner.cleanInput("today");
    Slots slots = new Slots();
    Context context = new Context();

    DateSlot slot = new DateSlot("date");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match, is(notNullValue()));
    assertThat(match.isMatched(), is(true));

    SlotMatch slotMatch = match.getSlotMatches().get(slot);
    LocalDate dateMatch = (LocalDate) slotMatch.getValue();

    LocalDate today = new LocalDate();
    assertThat(dateMatch, is(today));
  }

  // TODO handle different TZ in context
  @Test
  public void testTomorrow()
  {
    TemplatedUtterance utterance = new TemplatedUtterance("{date}");

    CleanedInput input = InputCleaner.cleanInput("tomorrow");
    Slots slots = new Slots();
    Context context = new Context();

    DateSlot slot = new DateSlot("date");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match, is(notNullValue()));
    assertThat(match.isMatched(), is(true));

    SlotMatch slotMatch = match.getSlotMatches().get(slot);
    LocalDate dateMatch = (LocalDate) slotMatch.getValue();

    LocalDate tommorrow = new LocalDate().plusDays(1);
    assertThat(dateMatch, is(tommorrow));
  }

  @Test
  public void testMismatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance("{date}");

    CleanedInput input = InputCleaner.cleanInput("book auckland to wellington for friday");
    Slots slots = new Slots();
    Context context = new Context();

    DateSlot slot = new DateSlot("date");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match, is(notNullValue()));
    assertThat(match.isMatched(), is(false));
  }

}
