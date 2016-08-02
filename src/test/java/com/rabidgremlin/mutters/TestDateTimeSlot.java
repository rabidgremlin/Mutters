package com.rabidgremlin.mutters;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.DateSlot;
import com.rabidgremlin.mutters.core.DateTimeSlot;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.Slots;
import com.rabidgremlin.mutters.core.Utterance;
import com.rabidgremlin.mutters.core.UtteranceMatch;
import com.rabidgremlin.mutters.util.Utils;

public class TestDateTimeSlot
{

	@Test
	public void testBasicMatch()
	{
		Utterance utterance = new Utterance("for the {datetime}");

		String input = Utils.cleanInput("for the 30th May 1974 at 10pm");
		Slots slots = new Slots();
		Context context = new Context();

		DateTimeSlot slot = new DateTimeSlot("datetime");
		slots.add(slot);

		UtteranceMatch match = utterance.matches(input, slots, context);

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
		Utterance utterance = new Utterance("for the {datetime}");

		String input = Utils.cleanInput("for the 30th May 1974 at 10pm");
		Slots slots = new Slots();
		Context context = new Context();
		context.setTimeZone(TimeZone.getTimeZone("Africa/Johannesburg"));

		DateTimeSlot slot = new DateTimeSlot("datetime");
		slots.add(slot);

		UtteranceMatch match = utterance.matches(input, slots, context);

		assertThat(match, is(notNullValue()));
		assertThat(match.isMatched(), is(true));
		assertThat(match.getSlotMatches().size(), is(1));

		SlotMatch slotMatch = match.getSlotMatches().get(slot);
		assertThat(slotMatch, is(notNullValue()));
		assertThat(slotMatch.getOrginalValue(), is("30th May 1974 at 10pm"));
		assertThat(slotMatch.getValue(), is(new DateTime(1974, 5, 30, 22, 0, 0, DateTimeZone.forTimeZone(context.getTimeZone()))));
	}

	@Test
	public void testLastWeek()
	{
		Utterance utterance = new Utterance("Give me the report for {date}");

		String input = Utils.cleanInput("Give me the report for last week");
		Slots slots = new Slots();
		Context context = new Context();

		DateSlot slot = new DateSlot("date");
		slots.add(slot);

		UtteranceMatch match = utterance.matches(input, slots, context);

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
