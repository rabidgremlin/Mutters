package com.rabidgremlin.mutters.bot.ink;

import com.bladecoder.ink.runtime.Story;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.ml.MLIntent;
import com.rabidgremlin.mutters.ml.MLIntentMatcher;
import com.rabidgremlin.mutters.session.Session;
import com.rabidgremlin.mutters.slots.LiteralSlot;
import com.rabidgremlin.mutters.util.SessionUtils;

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
		return loadStoryJsonFromClassPath("taxibot.ink.json");
	}

	@Override
	public void setUpFunctions()
	{		
		addFunction("ORDER_TAXI", new InkBotFunction()
		{
			@Override
			public void execute(CurrentResponse currentResponse, Session session, IntentMatch intentMatch, Story story, String param)
			{	
				try
				{
					story.getVariablesState().set("taxiNo", Integer.toHexString(SessionUtils.getStringFromSlotOrSession(intentMatch, session, "address", "").hashCode()).substring(0, 4));
				}
				catch (Exception e)
				{					
					throw new RuntimeException("Unable to set taxi no",e);
				}
			}
		}); 	
	}

}
