package com.rabidgremlin.mutters.core;

import java.util.HashMap;

public class TemplatedUtteranceMatch
{

	private boolean matched;
	private HashMap<Slot, SlotMatch> slotMatches;

	public TemplatedUtteranceMatch(boolean matched)
	{
		this.matched = matched;
		this.slotMatches = new HashMap<Slot, SlotMatch>();
	}

	public TemplatedUtteranceMatch(boolean matched, HashMap<Slot, SlotMatch> slotMatches)
	{
		this.matched = matched;
		this.slotMatches = slotMatches;
	}

	public boolean isMatched()
	{
		return matched;
	}

	public HashMap<Slot, SlotMatch> getSlotMatches()
	{
		return slotMatches;
	}

	@Override
	public String toString()
	{
		return "UtteranceMatch [matched=" + matched + ", slotMatches=" + slotMatches + "]";
	}
	
	

}
