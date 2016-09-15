package com.rabidgremlin.mutters.bot.ink;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.rabidgremlin.mutters.bot.statemachine.AbstractStateMachineBot;
import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.examples.mathbot.StartState;
import com.rabidgremlin.mutters.ml.MLIntent;
import com.rabidgremlin.mutters.ml.MLIntentMatcher;
import com.rabidgremlin.mutters.slots.LiteralSlot;
import com.rabidgremlin.mutters.state.Guard;
import com.rabidgremlin.mutters.state.State;
import com.rabidgremlin.mutters.state.StateMachine;

public class TaxiInkBot extends AbstractInkBot
{

	@Override
	public IntentMatcher setUpIntents()
	{
		MLIntentMatcher matcher = new MLIntentMatcher("models/en-cat-taxi-intents.bin");
		matcher.addSlotModel("Address", "models/en-ner-address.bin");

		MLIntent intent = new MLIntent("OrderTaxi");
		intent.addSlot(new LiteralSlot("Address"));
		matcher.addIntent(intent);

		intent = new MLIntent("CancelTaxi");
		matcher.addIntent(intent);

		intent = new MLIntent("WhereTaxi");
		matcher.addIntent(intent);

		intent = new MLIntent("GaveAddress");
		intent.addSlot(new LiteralSlot("Address"));
		matcher.addIntent(intent);

		return matcher;
	}

	@Override
	public String getStoryJson()
	{
		try
		{
			InputStream inkJsonStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("taxibot.ink.json");
			
			return IOUtils.toString(inkJsonStream,"UTF-8").replace('\uFEFF', ' ');
		}
		catch (Exception e)
		{
			throw new IllegalStateException("Failed to load ink json.", e);
		}
	}

}
