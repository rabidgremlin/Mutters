package com.rabidgremlin.mutters.core;

import java.util.ArrayList;
import java.util.List;

import com.rabidgremlin.mutters.util.Utils;

public class IntentMatcher {

	private List<Intent> intents = new ArrayList<Intent>();

	public void addIntent(Intent intent) {
		intents.add(intent);
	}

	public IntentMatch match(String utterance) {
		return match(Utils.tokenize(utterance));
	}

	public IntentMatch match(List<String> utteranceTokens) {

		for (Intent intent : intents) {
			UtteranceMatch utteranceMatch = intent.matches(utteranceTokens);
			if (utteranceMatch.isMatched()) {
				return new IntentMatch(intent, utteranceMatch.getSlotMatches());
			}
		}

		return null;
	}

}
