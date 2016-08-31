package com.rabidgremlin.mutters.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class IntentMatch
{

	private Intent intent;
	private HashMap<Slot, SlotMatch> slotMatches;
	private String utterance;

	public IntentMatch(Intent intent, HashMap<Slot, SlotMatch> slotMatches, String utterance)
	{

		this.intent = intent;
		this.slotMatches = slotMatches;
		this.utterance = utterance;
	}

	public Intent getIntent()
	{
		return intent;
	}

	public Map<Slot, SlotMatch> getSlotMatches()
	{
		return Collections.unmodifiableMap(slotMatches);
	}

	public SlotMatch getSlotMatch(String slotName)
	{
		for (SlotMatch match : slotMatches.values())
		{
			if (match.getSlot().getName().equalsIgnoreCase(slotName))
			{
				return match;
			}
		}
		return null;
	}
	
	public void removeSlotMatch(String slotName)
	{
		SlotMatch match = getSlotMatch(slotName);
		if (match != null)
		{
			slotMatches.remove(match.getSlot());
		}
	}

	public String getUtterance()
	{
		return utterance;
	}

}
