package com.rabidgremlin.mutters;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.TimeZone;

import org.joda.time.LocalTime;
import org.junit.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.Slots;
import com.rabidgremlin.mutters.slots.TimeSlot;
import com.rabidgremlin.mutters.templated.SimpleTokenizer;
import com.rabidgremlin.mutters.templated.TemplatedUtterance;
import com.rabidgremlin.mutters.templated.TemplatedUtteranceMatch;

public class TestTimeSlot
{
  private SimpleTokenizer tokenizer = new SimpleTokenizer();

  @Test
  public void testBasicMatch()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("at {time}"));

    String[] input = tokenizer.tokenize("at 6:45am");
    Slots slots = new Slots();
    Context context = new Context();

    TimeSlot slot = new TimeSlot("time");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match, is(notNullValue()));
    assertThat(match.isMatched(), is(true));
    assertThat(match.getSlotMatches().size(), is(1));

    SlotMatch slotMatch = match.getSlotMatches().get(slot);
    assertThat(slotMatch, is(notNullValue()));
    assertThat(slotMatch.getOrginalValue(), is("6:45am"));
    assertThat(slotMatch.getValue(), is(new LocalTime(6, 45)));
  }

  @Test
  public void testMatchWithTimeZone()
  {
    TemplatedUtterance utterance = new TemplatedUtterance(tokenizer.tokenize("at {time}"));

    String[] input = tokenizer.tokenize("at 6:45am");
    Slots slots = new Slots();
    Context context = new Context();
    context.setTimeZone(TimeZone.getTimeZone("Africa/Johannesburg"));

    TimeSlot slot = new TimeSlot("time");
    slots.add(slot);

    TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

    assertThat(match, is(notNullValue()));
    assertThat(match.isMatched(), is(true));
    assertThat(match.getSlotMatches().size(), is(1));

    SlotMatch slotMatch = match.getSlotMatches().get(slot);
    assertThat(slotMatch, is(notNullValue()));
    assertThat(slotMatch.getOrginalValue(), is("6:45am"));
    assertThat(slotMatch.getValue(), is(new LocalTime(6, 45)));
  }

}
