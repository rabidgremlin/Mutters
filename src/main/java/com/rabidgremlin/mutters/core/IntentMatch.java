package com.rabidgremlin.mutters.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class IntentMatch {

	private Intent intent;
	private HashMap<Slot, SlotMatch> slotMatches;

	public IntentMatch(Intent intent, HashMap<Slot, SlotMatch> slotMatches) {

		this.intent = intent;
		this.slotMatches = slotMatches;
	}

	public Intent getIntent() {
		return intent;
	}

	public Map<Slot, SlotMatch> getSlotMatches() {
		return Collections.unmodifiableMap(slotMatches);
	}

	public SlotMatch getSlotMatch(String slotName) {
		for (SlotMatch match : slotMatches.values()) {
			if (match.getSlot().getName().equalsIgnoreCase(slotName)) {
				return match;
			}
		}
		return null;
	}

}
