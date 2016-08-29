package com.rabidgremlin.mutters.templated;

import java.util.HashMap;

import com.rabidgremlin.mutters.core.Slot;
import com.rabidgremlin.mutters.core.SlotMatch;

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
