package com.rabidgremlin.mutters.core;

import java.util.HashMap;

public class UtteranceMatch {

	private boolean matched;
	private HashMap<Slot, SlotMatch> slotMatches;

	public UtteranceMatch(boolean matched)
	{
		this.matched = matched;
		this.slotMatches = new HashMap<Slot, SlotMatch>();
	}
	
	
	public UtteranceMatch(boolean matched, HashMap<Slot, SlotMatch> slotMatches) {
		this.matched = matched;
		this.slotMatches = slotMatches;
	}

	public boolean isMatched() {
		return matched;
	}

	public HashMap<Slot, SlotMatch> getSlotMatches() {
		return slotMatches;
	}

}
