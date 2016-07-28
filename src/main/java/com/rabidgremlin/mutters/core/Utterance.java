package com.rabidgremlin.mutters.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.rabidgremlin.mutters.util.Utils;

public class Utterance {

	private String template;
	private List<String> tokens;
	//private HashMap<Integer, String> slotNames = new HashMap<Integer, String>();
	private HashSet<String> slotNames = new HashSet<String>();

	private Pattern matchPattern;

	public Utterance(String template) {
		this.template = template;
		tokens = Utils.tokenize(template);

		String regexStr = "";

		for (int loop = 0; loop < tokens.size(); loop++) {
			String token = tokens.get(loop);
			if (token.startsWith("{") && token.endsWith("}")) {
				String slotName = token.substring(1, token.length() - 1);

				regexStr += "(?<" + slotName + ">.*) ";

				slotNames.add(slotName);
			} else {
				regexStr += token + " ";
			}
		}

		matchPattern = Pattern.compile(regexStr.trim(), Pattern.CASE_INSENSITIVE);
	}

	public List<String> getTokens() {
		return tokens;
	}

	public UtteranceMatch matches(List<String> inputTokens, Slots slots) {
		
		// yuck fix this !
		String input = "";
		for (String token: inputTokens)
		{
			input += token + " ";
		}
		
		input = input.trim();
		
				
		Matcher  match = matchPattern.matcher(input);
		
		if (!match.find())
		{
			return new UtteranceMatch(false);
		}
		
		
		UtteranceMatch theMatch = new UtteranceMatch(true);
		
		for(String slotName: slotNames)
		{
			Slot slot = slots.getSlot(slotName);	
			
			if (slot == null)
			{
				throw new IllegalStateException("Cannot find slot '" + slotName + " reference by utterace '" +template +"'");
			}
			
			SlotMatch slotMatch = slot.match(match.group(slotName)); 
			
			if (slotMatch != null) {
				theMatch.getSlotMatches().put(slot, slotMatch);
			} else {
				return new UtteranceMatch(false);
			}
		}
		
		
		return theMatch;
		
		
		/*

		// TODO: need to refactor this check when we have tokens matching
		// multiple slots
		// i.e like dates or number strings
		if (inputTokens.size() != tokens.size()) {
			return new UtteranceMatch(false);
		}

		UtteranceMatch theMatch = new UtteranceMatch(true);

		for (int loop = 0; loop < inputTokens.size(); loop++) {
			if (!inputTokens.get(loop).equalsIgnoreCase(tokens.get(loop))) {
				String slotName = slotNames.get(loop);

				if (slotName != null) {
					Slot slot = slots.getSlot(slotName);
					if (slot != null) {
						SlotMatch slotMatch = slot.match(inputTokens.get(loop));
						if (slotMatch != null) {
							theMatch.getSlotMatches().put(slot, slotMatch);
						} else {
							return new UtteranceMatch(false);
						}
					} else {
						return new UtteranceMatch(false);
					}
				} else {
					return new UtteranceMatch(false);
				}

			}
		}

		return theMatch;*/
	}

	@Override
	public String toString() {
		return "Utterance [template=" + template + ", tokens=" + tokens + "]";
	}

}
