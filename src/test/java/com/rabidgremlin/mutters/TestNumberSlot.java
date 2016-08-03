package com.rabidgremlin.mutters;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

import com.rabidgremlin.mutters.core.CleanedInput;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.InputCleaner;
import com.rabidgremlin.mutters.core.LiteralSlot;
import com.rabidgremlin.mutters.core.NumberSlot;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.Slots;
import com.rabidgremlin.mutters.core.Utterance;
import com.rabidgremlin.mutters.core.UtteranceMatch;
import com.rabidgremlin.mutters.util.Utils;

public class TestNumberSlot
{

	@Test
	public void testBasicWordMatch()
	{
		Utterance utterance = new Utterance("The balance is {number}");

		CleanedInput input = InputCleaner.cleanInput("The balance is One hundred two thousand and thirty four");
		Slots slots = new Slots();
		Context context = new Context();

		NumberSlot slot = new NumberSlot("number");
		slots.add(slot);

		UtteranceMatch match = utterance.matches(input, slots, context);

		assertThat(match, is(notNullValue()));
		assertThat(match.isMatched(), is(true));
		assertThat(match.getSlotMatches().size(), is(1));

		SlotMatch slotMatch = match.getSlotMatches().get(slot);
		assertThat(slotMatch, is(notNullValue()));
		assertThat(slotMatch.getOrginalValue(), is("One hundred two thousand and thirty four"));
		assertThat(slotMatch.getValue(), is(102034l));
	}

	@Test
	public void testWordStringToNumber()
	{
		NumberSlot slot = new NumberSlot("test");
		Number result = slot.wordStringToNumber("Three hundred fifty two thousand two hundred and sixty one");

		assertThat(result, is(notNullValue()));
		assertThat(result, is(352261l));

		result = slot.wordStringToNumber("Three hundred and bad");
		assertThat(result, is(nullValue()));
	}

	@Test
	public void testBasicNumberMatch()
	{
		Utterance utterance = new Utterance("The balance is {number}");

		CleanedInput input = InputCleaner.cleanInput("The balance is 123");
		Slots slots = new Slots();
		Context context = new Context();

		NumberSlot slot = new NumberSlot("number");
		slots.add(slot);

		UtteranceMatch match = utterance.matches(input, slots, context);

		assertThat(match, is(notNullValue()));
		assertThat(match.isMatched(), is(true));
		assertThat(match.getSlotMatches().size(), is(1));

		SlotMatch slotMatch = match.getSlotMatches().get(slot);
		assertThat(slotMatch, is(notNullValue()));
		assertThat(slotMatch.getOrginalValue(), is("123"));
		assertThat(slotMatch.getValue(), is(123l));
	}

	@Test
	public void testBasicDecimalMatch()
	{
		Utterance utterance = new Utterance("The balance is {number}");

		CleanedInput input = InputCleaner.cleanInput("The balance is 546.12");
		Slots slots = new Slots();
		Context context = new Context();

		NumberSlot slot = new NumberSlot("number");
		slots.add(slot);

		UtteranceMatch match = utterance.matches(input, slots, context);

		assertThat(match, is(notNullValue()));
		assertThat(match.isMatched(), is(true));
		assertThat(match.getSlotMatches().size(), is(1));

		SlotMatch slotMatch = match.getSlotMatches().get(slot);
		assertThat(slotMatch, is(notNullValue()));
		assertThat(slotMatch.getOrginalValue(), is("546.12"));
		assertThat(slotMatch.getValue(), is(546.12));
	}

}
