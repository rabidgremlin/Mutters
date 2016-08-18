package com.rabidgremlin.mutters.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.rabidgremlin.mutters.util.Utils;

public class IntentMatcher
{

	private List<Intent> intents = new ArrayList<Intent>();

	public void addIntent(Intent intent)
	{
		intents.add(intent);
	}

	public IntentMatch match(String utterance, Context context)
	{

		CleanedInput cleanedUtterance = InputCleaner.cleanInput(utterance);

		for (Intent intent : intents)
		{
			UtteranceMatch utteranceMatch = intent.matches(cleanedUtterance, context);
			if (utteranceMatch.isMatched())
			{
				return new IntentMatch(intent, utteranceMatch.getSlotMatches());
			}
		}

		return null;
	}
	
	public List<Intent> getIntents()
	{
		return Collections.unmodifiableList(intents);
	}

}
