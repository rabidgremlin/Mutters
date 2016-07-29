package com.rabidgremlin.mutters;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.LiteralSlot;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.Slots;
import com.rabidgremlin.mutters.core.Utterance;
import com.rabidgremlin.mutters.core.UtteranceMatch;
import com.rabidgremlin.mutters.util.Utils;

public class TestLiteralSlot
{

	@Test
	public void testBasicMatch()
	{
		Utterance utterance = new Utterance("My name is {name}");

		String input = Utils.cleanInput("My Name is Kilroy Jones");
		Slots slots = new Slots();
		Context context = new Context();

		LiteralSlot slot = new LiteralSlot("name");
		slots.add(slot);

		UtteranceMatch match = utterance.matches(input, slots, context);

		assertThat(match, is(notNullValue()));
		assertThat(match.isMatched(), is(true));
		assertThat(match.getSlotMatches().size(), is(1));

		SlotMatch slotMatch = match.getSlotMatches().get(slot);
		assertThat(slotMatch, is(notNullValue()));
		assertThat(slotMatch.getOrginalValue(), is("Kilroy Jones"));
		assertThat(slotMatch.getValue(), is("kilroy jones"));
	}

	@Test
	public void testMidUtteranceMatch()
	{
		Utterance utterance = new Utterance("The {something} is good");

		String input = Utils.cleanInput("The pinot noir is good");
		Slots slots = new Slots();
		Context context = new Context();

		LiteralSlot slot = new LiteralSlot("something");
		slots.add(slot);

		UtteranceMatch match = utterance.matches(input, slots, context);

		assertThat(match, is(notNullValue()));
		assertThat(match.isMatched(), is(true));
		assertThat(match.getSlotMatches().size(), is(1));

		SlotMatch slotMatch = match.getSlotMatches().get(slot);
		assertThat(slotMatch, is(notNullValue()));
		assertThat(slotMatch.getOrginalValue(), is("pinot noir"));
		assertThat(slotMatch.getValue(), is("pinot noir"));
	}

}
