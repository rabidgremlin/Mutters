package com.rabidgremlin.mutters;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.IsCloseTo.*;
import static org.junit.Assert.*;

import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;

import com.rabidgremlin.mutters.core.CleanedInput;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.DateSlot;
import com.rabidgremlin.mutters.core.DateTimeSlot;
import com.rabidgremlin.mutters.core.InputCleaner;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.Slots;
import com.rabidgremlin.mutters.core.TimeSlot;
import com.rabidgremlin.mutters.core.Utterance;
import com.rabidgremlin.mutters.core.UtteranceMatch;
import com.rabidgremlin.mutters.util.Utils;

public class TestTimeSlot
{

	@Test
	public void testBasicMatch()
	{
		Utterance utterance = new Utterance("at {time}");

		CleanedInput input = InputCleaner.cleanInput("at 6:45am");
		Slots slots = new Slots();
		Context context = new Context();

		TimeSlot slot = new TimeSlot("time");
		slots.add(slot);

		UtteranceMatch match = utterance.matches(input, slots, context);

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
		Utterance utterance = new Utterance("at {time}");

		CleanedInput input = InputCleaner.cleanInput("at 6:45am");
		Slots slots = new Slots();
		Context context = new Context();
		context.setTimeZone(TimeZone.getTimeZone("Africa/Johannesburg"));

		TimeSlot slot = new TimeSlot("time");
		slots.add(slot);

		UtteranceMatch match = utterance.matches(input, slots, context);

		assertThat(match, is(notNullValue()));
		assertThat(match.isMatched(), is(true));
		assertThat(match.getSlotMatches().size(), is(1));

		SlotMatch slotMatch = match.getSlotMatches().get(slot);
		assertThat(slotMatch, is(notNullValue()));
		assertThat(slotMatch.getOrginalValue(), is("6:45am"));
		assertThat(slotMatch.getValue(), is(new LocalTime(6, 45)));
	}

}
