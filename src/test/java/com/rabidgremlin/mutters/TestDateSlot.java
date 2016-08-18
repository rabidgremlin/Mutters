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
import com.rabidgremlin.mutters.core.DateSlot;
import com.rabidgremlin.mutters.core.DateTimeSlot;
import com.rabidgremlin.mutters.core.InputCleaner;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.Slots;
import com.rabidgremlin.mutters.core.Utterance;
import com.rabidgremlin.mutters.core.UtteranceMatch;
import com.rabidgremlin.mutters.util.Utils;

public class TestDateSlot
{

	@Test
	public void testBasicMatch()
	{
		Utterance utterance = new Utterance("for the {date}");

		CleanedInput input = InputCleaner.cleanInput("for the 30th May 1974");
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
		assertThat(slotMatch.getOrginalValue(), is("30th May 1974"));
		assertThat(slotMatch.getValue(), is(new LocalDate(1974, 5, 30)));
	}

	@Test
	public void testMatchWithTimeZone()
	{
		Utterance utterance = new Utterance("for the {date}");

		CleanedInput input = InputCleaner.cleanInput("for the 30th May 1974");
		Slots slots = new Slots();
		Context context = new Context();
		context.setTimeZone(TimeZone.getTimeZone("Africa/Johannesburg"));

		DateSlot slot = new DateSlot("date");
		slots.add(slot);

		UtteranceMatch match = utterance.matches(input, slots, context);

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
		Utterance utterance = new Utterance("{date}");

		CleanedInput input = InputCleaner.cleanInput("10pm");
		Slots slots = new Slots();
		Context context = new Context();

		DateSlot slot = new DateSlot("date");
		slots.add(slot);

		UtteranceMatch match = utterance.matches(input, slots, context);

		assertThat(match, is(notNullValue()));
		assertThat(match.isMatched(), is(false));
	}

	@Test
	public void testNZDate()
	{
		Utterance utterance = new Utterance("{date}");

		CleanedInput input = InputCleaner.cleanInput("2 Aug");
		Slots slots = new Slots();
		Context context = new Context();

		DateSlot slot = new DateSlot("date");
		slots.add(slot);

		UtteranceMatch match = utterance.matches(input, slots, context);

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
		Utterance utterance = new Utterance("{date}");

		CleanedInput input = InputCleaner.cleanInput("20/5/2016");
		Slots slots = new Slots();
		Context context = new Context();

		DateSlot slot = new DateSlot("date");
		slots.add(slot);

		UtteranceMatch match = utterance.matches(input, slots, context);

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
		Utterance utterance = new Utterance("{date}");

		CleanedInput input = InputCleaner.cleanInput("today");
		Slots slots = new Slots();
		Context context = new Context();

		DateSlot slot = new DateSlot("date");
		slots.add(slot);

		UtteranceMatch match = utterance.matches(input, slots, context);

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
		Utterance utterance = new Utterance("{date}");

		CleanedInput input = InputCleaner.cleanInput("tomorrow");
		Slots slots = new Slots();
		Context context = new Context();

		DateSlot slot = new DateSlot("date");
		slots.add(slot);

		UtteranceMatch match = utterance.matches(input, slots, context);

		assertThat(match, is(notNullValue()));
		assertThat(match.isMatched(), is(true));

		SlotMatch slotMatch = match.getSlotMatches().get(slot);
		LocalDate dateMatch = (LocalDate) slotMatch.getValue();

		LocalDate tommorrow = new LocalDate().plusDays(1);
		assertThat(dateMatch, is(tommorrow));
	}

}
