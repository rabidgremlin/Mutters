package com.rabidgremlin.mutters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utterance {

	private String template;
	private List<String> tokens;
	private Map<Integer,String> slotNames = new HashMap<Integer, String>();

	public Utterance(String template) {
		tokens = Utils.tokenize(template);

		for (int loop=0; loop < tokens.size(); loop++)
		{
			String token = tokens.get(loop);
			if (token.startsWith("{") && token.endsWith("}"))
		    {
			  slotNames.put(loop, token.substring(1,token.length()-1));	
			}
		}
	}

	public List<String> getTokens() {
		return tokens;
	}

	public UtteranceMatch matches(List<String> inputTokens,
			Slots slots) {
		
		// TODO: need to refactor this check when we have tokens matching multiple slots
		// i.e like dates or number strings
		if (inputTokens.size() != tokens.size())
		{
			return new UtteranceMatch(false);
		}
		
		UtteranceMatch theMatch = new UtteranceMatch(true);
		
		for (int loop=0; loop < inputTokens.size(); loop++)
		{
			if (!inputTokens.get(loop).equalsIgnoreCase(tokens.get(loop)))
			{
				String slotName = slotNames.get(loop);
				
				if (slotName != null)
				{
					Slot slot = slots.getSlot(slotName);
					if (slot != null)
					{
						SlotMatch slotMatch = slot.match(inputTokens.get(loop));
						if (slotMatch != null)
						{
							theMatch.getSlotMatches().put(slot, slotMatch);
						}
						else
						{
							return new UtteranceMatch(false);
						}
					}
					else
					{
						return new UtteranceMatch(false);
					}
				}
				else
				{
					return new UtteranceMatch(false);	
				}			
				
			}
		}
		
		return theMatch;
	}

	@Override
	public String toString() {
		return "Utterance [template=" + template + ", tokens=" + tokens + "]";
	}

}
