package com.rabidgremlin.mutters;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import com.rabidgremlin.mutters.core.CleanedInput;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.InputCleaner;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.Slots;
import com.rabidgremlin.mutters.slots.CustomSlot;
import com.rabidgremlin.mutters.templated.TemplatedIntent;
import com.rabidgremlin.mutters.templated.TemplatedUtterance;
import com.rabidgremlin.mutters.templated.TemplatedUtteranceMatch;
import com.rabidgremlin.mutters.util.Utils;

public class TestUtterance
{

	@Test
	public void testSimpleMatch()
	{
		TemplatedUtterance utterance = new TemplatedUtterance("What's the time");

		CleanedInput input = InputCleaner.cleanInput("What's the time");
		Slots slots = new Slots();
		Context context = new Context();

		TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

		assertThat(match, is(notNullValue()));
		assertThat(match.isMatched(), is(true));
		assertThat(match.getSlotMatches().size(), is(0));
	}

	@Test
	public void testSimpleNotMatch()
	{
		TemplatedUtterance utterance = new TemplatedUtterance("This is that and that is this");

		CleanedInput input = InputCleaner.cleanInput("This is really not all that");
		Slots slots = new Slots();
		Context context = new Context();

		TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

		assertThat(match, is(notNullValue()));
		assertThat(match.isMatched(), is(false));
		assertThat(match.getSlotMatches().size(), is(0));
	}

	@Test
	public void testSimpleSlotMatch()
	{
		TemplatedUtterance utterance = new TemplatedUtterance("I like {Color}");

		CleanedInput input = InputCleaner.cleanInput("I Like red");
		Slots slots = new Slots();
		Context context = new Context();

		CustomSlot color = new CustomSlot("Color", new String[] { "Green", "blue", "Red" });
		slots.add(color);

		// System.out.println(color);

		TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

		assertThat(match, is(notNullValue()));
		assertThat(match.isMatched(), is(true));
		assertThat(match.getSlotMatches().size(), is(1));

		SlotMatch colorMatch = match.getSlotMatches().get(color);
		assertThat(colorMatch, is(notNullValue()));
		assertThat(colorMatch.getOrginalValue(), is("red"));
		assertThat(colorMatch.getValue(), is("Red"));
	}

	@Test
	public void testSimpleNotSlotMatch()
	{
		TemplatedUtterance utterance = new TemplatedUtterance("I like {Color}");

		CleanedInput input = InputCleaner.cleanInput("I Like pink");
		Slots slots = new Slots();
		Context context = new Context();

		CustomSlot color = new CustomSlot("Color", new String[] { "Green", "blue", "Red" });
		slots.add(color);

		TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

		assertThat(match, is(notNullValue()));
		assertThat(match.isMatched(), is(false));
		assertThat(match.getSlotMatches().size(), is(0));
	}

	@Test
	public void testMultiSlotMatch()
	{
		TemplatedUtterance utterance = new TemplatedUtterance("I like {Color} and {Food}");

		CleanedInput input = InputCleaner.cleanInput("I like red and grapes");
		Slots slots = new Slots();
		Context context = new Context();

		CustomSlot color = new CustomSlot("Color", new String[] { "Green", "blue", "Red" });
		slots.add(color);

		CustomSlot food = new CustomSlot("Food", new String[] { "grapes", "biscuits", "lollipops" });
		slots.add(food);

		TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

		assertThat(match, is(notNullValue()));
		assertThat(match.isMatched(), is(true));
		assertThat(match.getSlotMatches().size(), is(2));

		SlotMatch colorMatch = match.getSlotMatches().get(color);
		assertThat(colorMatch, is(notNullValue()));
		assertThat(colorMatch.getOrginalValue(), is("red"));
		assertThat(colorMatch.getValue(), is("Red"));

		SlotMatch foodMatch = match.getSlotMatches().get(food);
		assertThat(foodMatch, is(notNullValue()));
		assertThat(foodMatch.getOrginalValue(), is("grapes"));
		assertThat(foodMatch.getValue(), is("grapes"));
	}

	@Test
	public void testMultiSlotNotMatch()
	{
		TemplatedUtterance utterance = new TemplatedUtterance("I like {Color} and {Food}");

		CleanedInput input = InputCleaner.cleanInput("I like red and burgers");
		Slots slots = new Slots();
		Context context = new Context();

		CustomSlot color = new CustomSlot("Color", new String[] { "Green", "blue", "Red" });
		slots.add(color);

		CustomSlot food = new CustomSlot("Food", new String[] { "grapes", "biscuits", "lollipops" });
		slots.add(food);

		TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

		assertThat(match, is(notNullValue()));
		assertThat(match.isMatched(), is(false));
		assertThat(match.getSlotMatches().size(), is(0));
	}

	@Test
	public void testMultiWordSlotMatch()
	{
		TemplatedUtterance utterance = new TemplatedUtterance("What is the time in {City}");

		CleanedInput input = InputCleaner.cleanInput("What is the time in San francisco");
		Slots slots = new Slots();
		Context context = new Context();

		CustomSlot color = new CustomSlot("City", new String[] { "Wellington", "San Francisco", "Auckland" });
		slots.add(color);

		TemplatedUtteranceMatch match = utterance.matches(input, slots, context);

		assertThat(match, is(notNullValue()));
		assertThat(match.isMatched(), is(true));
		assertThat(match.getSlotMatches().size(), is(1));

		SlotMatch colorMatch = match.getSlotMatches().get(color);
		assertThat(colorMatch, is(notNullValue()));
		assertThat(colorMatch.getOrginalValue(), is("San francisco"));
		assertThat(colorMatch.getValue(), is("San Francisco"));
	}

	//TODO refactor out to a separate Intent test class
	@Test
	public void testSingleWordMatch()
	{
		TemplatedIntent intent = new TemplatedIntent("YesIntent");

//		intent.addUtterance(new Utterance("yes"));
//		intent.addUtterance(new Utterance("yes please"));
//		intent.addUtterance(new Utterance("yes thanks"));
//		intent.addUtterance(new Utterance("yes thank you"));
//		intent.addUtterance(new Utterance("yep"));
//		intent.addUtterance(new Utterance("Y"));
//		intent.addUtterance(new Utterance("Ok"));
		intent.addUtterance(new TemplatedUtterance("K"));
		
		CleanedInput input = InputCleaner.cleanInput("Kia Ora");		
		Context context = new Context();
		
		TemplatedUtteranceMatch match = intent.matches(input, context);
		assertThat(match, is(notNullValue()));
		assertThat(match.isMatched(), is(false));
	}
}
