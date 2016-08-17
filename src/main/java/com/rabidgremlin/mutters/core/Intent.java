package com.rabidgremlin.mutters.core;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Intent
{
	private Logger log = LoggerFactory.getLogger(Intent.class);
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
	
	public void addUtterances(List<Utterance> utterances)
	{
		utterances.addAll(utterances);
	}

	public UtteranceMatch matches(CleanedInput input, Context context)
	{
		log.debug("------------- Intent: {} Input: {}", name,input);
		for (Utterance utterance : utterances)
		{
			log.debug("       Matching to {} ", utterance.getTemplate());
			UtteranceMatch match = utterance.matches(input, slots, context);
			if (match.isMatched())
			{
				log.debug("------------ Matched to {} match: {} -------------", utterance.getTemplate(), match);
				return match;
			}
		}

		log.debug("------------ No Match to {} -------------", name);
		return new UtteranceMatch(false);
	}
}
