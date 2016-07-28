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
	// private HashMap<Integer, String> slotNames = new HashMap<Integer,
	// String>();
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
				regexStr += Pattern.quote(token) + " ";
			}
		}

		//System.out.println(regexStr);
		matchPattern = Pattern.compile(regexStr.trim(), Pattern.CASE_INSENSITIVE);
	}

	public List<String> getTokens() {
		return tokens;
	}

	// NOTE input should be cleaned
	public UtteranceMatch matches(String input, Slots slots, Context context) {


		Matcher match = matchPattern.matcher(input);

		if (!match.find()) {
			return new UtteranceMatch(false);
		}

		UtteranceMatch theMatch = new UtteranceMatch(true);

		for (String slotName : slotNames) {
			Slot slot = slots.getSlot(slotName);

			if (slot == null) {
				throw new IllegalStateException(
						"Cannot find slot '" + slotName + " reference by utterace '" + template + "'");
			}

			SlotMatch slotMatch = slot.match(match.group(slotName),context);

			if (slotMatch != null) {
				theMatch.getSlotMatches().put(slot, slotMatch);
			} else {
				return new UtteranceMatch(false);
			}
		}

		return theMatch;		
	}

	@Override
	public String toString() {
		return "Utterance [template=" + template + ", tokens=" + tokens + "]";
	}

}
