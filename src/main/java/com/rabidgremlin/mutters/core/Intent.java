package com.rabidgremlin.mutters.core;

import java.util.ArrayList;
import java.util.List;

public class Intent
{

	private String name;
	private Slots slots = new Slots();
	private List<Utterance> utterances = new ArrayList<Utterance>();

	public Intent(String name)
	{
		super();
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void addSlot(Slot slot)
	{
		slots.add(slot);
	}

	public void addUtterance(Utterance utterance)
	{
		utterances.add(utterance);
	}

	public UtteranceMatch matches(String input, Context context)
	{

		for (Utterance utterance : utterances)
		{
			UtteranceMatch match = utterance.matches(input, slots, context);
			if (match.isMatched())
			{
				return match;
			}
		}

		return new UtteranceMatch(false);
	}
}
