package com.rabidgremlin.mutters;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import com.rabidgremlin.mutters.core.Intent;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.core.NumberSlot;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.Utterance;
import com.rabidgremlin.mutters.core.Context;

public class TestIntentMatcher
{

	@Test
	public void testBasicMatching()
	{
		Intent additionIntent = new Intent("Addition");

		additionIntent.addUtterance(new Utterance("What's {number1} + {number2}"));
		additionIntent.addUtterance(new Utterance("What is {number1} + {number2}"));
		additionIntent.addUtterance(new Utterance("Add {number1} and {number2}"));
		additionIntent.addUtterance(new Utterance("{number1} plus {number2}"));

		NumberSlot number1 = new NumberSlot("number1");
		NumberSlot number2 = new NumberSlot("number2");

		additionIntent.addSlot(number1);
		additionIntent.addSlot(number2);

		IntentMatcher matcher = new IntentMatcher();
		matcher.addIntent(additionIntent);

		IntentMatch intentMatch = matcher.match("What is 1 + 5", new Context());

		assertThat(intentMatch, is(notNullValue()));
		assertThat(intentMatch.getIntent(), is(additionIntent));
		assertThat(intentMatch.getSlotMatches().size(), is(2));

		SlotMatch number1Match = intentMatch.getSlotMatches().get(number1);
		assertThat(number1Match, is(notNullValue()));
		assertThat(number1Match.getValue(), is(1l));

		SlotMatch number2Match = intentMatch.getSlotMatches().get(number2);
		assertThat(number2Match, is(notNullValue()));
		assertThat(number2Match.getValue(), is(5l));

	}

}
