package com.rabidgremlin.mutters;

import java.util.HashMap;

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

	public HashMap<Slot, SlotMatch> getSlotMatches() {
		return slotMatches;
	}

}
