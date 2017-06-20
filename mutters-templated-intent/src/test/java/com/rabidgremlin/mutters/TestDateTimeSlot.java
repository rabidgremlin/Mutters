package com.rabidgremlin.mutters;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.Slots;
import com.rabidgremlin.mutters.slots.DateSlot;
import com.rabidgremlin.mutters.slots.DateTimeSlot;
import com.rabidgremlin.mutters.templated.SimpleTokenizer;
import com.rabidgremlin.mutters.templated.TemplatedUtterance;
import com.rabidgremlin.mutters.templated.TemplatedUtteranceMatch;

public class TestDateTimeSlot
{
  private SimpleTokenizer tokenizer = new SimpleTokenizer();

  @Test
  public void testBasicMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("for the {datetime}"));

    String[] input = tokenizer.tokenize("for the 30th May 1974 at 10pm");
    Slots slots = new Slots();
    Context context = new Context();

    DateTimeSlot slot = new DateTimeSlot("datetime");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match, is(notNullValue()));
    assertThat(match.isMatched(), is(true));
    assertThat(match.getSlotMatches().size(), is(1));

    SlotMatch slotMatch = match.getSlotMatches().get(slot);
    assertThat(slotMatch, is(notNullValue()));
    assertThat(slotMatch.getOrginalValue(), is("30th May 1974 at 10pm"));
    assertThat(slotMatch.getValue(), is(new DateTime(1974, 5, 30, 22, 0, 0)));
  }

  @Test
  public void testMatchWithTimeZone()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("for the {datetime}"));

    String[] input = tokenizer.tokenize("for the 30th May 1974 at 10pm");
    Slots slots = new Slots();
    Context context = new Context();
    context.setTimeZone(TimeZone.getTimeZone("Africa/Johannesburg"));

    DateTimeSlot slot = new DateTimeSlot("datetime");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match, is(notNullValue()));
    assertThat(match.isMatched(), is(true));
    assertThat(match.getSlotMatches().size(), is(1));

    SlotMatch slotMatch = match.getSlotMatches().get(slot);
    assertThat(slotMatch, is(notNullValue()));
    assertThat(slotMatch.getOrginalValue(), is("30th May 1974 at 10pm"));
    assertThat(slotMatch.getValue(),
        is(new DateTime(1974, 5, 30, 22, 0, 0, DateTimeZone.forTimeZone(context.getTimeZone()))));
  }

  @Test
  public void testLastWeek()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("Give me the report for {date}"));

    String[] input = tokenizer.tokenize("Give me the report for last week");
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
    assertThat(slotMatch.getOrginalValue(), is("last week"));

    LocalDate sevenDaysAgo = new LocalDate().minusDays(7);

    assertThat(slotMatch.getValue(), is(sevenDaysAgo));
  }

}
