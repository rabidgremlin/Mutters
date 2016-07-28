package com.rabidgremlin.mutters;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import com.rabidgremlin.mutters.core.CustomSlot;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.Slots;
import com.rabidgremlin.mutters.core.Utterance;
import com.rabidgremlin.mutters.core.UtteranceMatch;
import com.rabidgremlin.mutters.util.Utils;

public class TestUtterance {

	@Test
	public void testTokens() {
		Utterance utterance = new Utterance("What's the time");
		List<String> tokens = utterance.getTokens();
		assertNotNull(tokens);
		assertThat(tokens.size(), is(3));
		
		assertThat(tokens.get(0),is("What's"));
		assertThat(tokens.get(1),is("the"));
		assertThat(tokens.get(2),is("time"));		
	}
	
	
	@Test
	public void testSimpleMatch() {
		Utterance utterance = new Utterance("What's the time");
		
		List<String> inputTokens = Utils.tokenize("What's the time");
		Slots slots = new Slots();
		
		UtteranceMatch match = utterance.matches(inputTokens, slots);		
		
		assertThat(match,is(notNullValue()));
		assertThat(match.isMatched(), is(true));
		assertThat(match.getSlotMatches().size(), is(0));			
	}
	
	@Test
	public void testSimpleNotMatch() {
		Utterance utterance = new Utterance("This is that and that is this");
		
		List<String> inputTokens = Utils.tokenize("This is really not all that");
		Slots slots = new Slots();
		
		UtteranceMatch match = utterance.matches(inputTokens, slots);		
		
		assertThat(match,is(notNullValue()));
		assertThat(match.isMatched(), is(false));
		assertThat(match.getSlotMatches().size(), is(0));			
	}

	
	@Test
	public void testSimpleSlotMatch() {
		Utterance utterance = new Utterance("I like {Color}");
		
		List<String> inputTokens = Utils.tokenize("I Like red");
		Slots slots = new Slots();
		
		CustomSlot color = new CustomSlot("Color", new String[]{"Green","blue","Red"});
		slots.add(color);
		
		UtteranceMatch match = utterance.matches(inputTokens, slots);		
		
		assertThat(match,is(notNullValue()));
		assertThat(match.isMatched(), is(true));
		assertThat(match.getSlotMatches().size(), is(1));
		
		SlotMatch colorMatch = match.getSlotMatches().get(color);
		assertThat(colorMatch,is(notNullValue()));		
		assertThat(colorMatch.getOrginalValue(), is("red"));
		assertThat(colorMatch.getValue(), is("Red"));
	}
	
	@Test
	public void testSimpleNotSlotMatch() {
		Utterance utterance = new Utterance("I like {Color}");
		
		List<String> inputTokens = Utils.tokenize("I Like pink");
		Slots slots = new Slots();
		
		CustomSlot color = new CustomSlot("Color", new String[]{"Green","blue","Red"});
		slots.add(color);
		
		UtteranceMatch match = utterance.matches(inputTokens, slots);		
		
		assertThat(match,is(notNullValue()));
		assertThat(match.isMatched(), is(false));
		assertThat(match.getSlotMatches().size(), is(0));
	}
	
	@Test
	public void testMultiSlotMatch() {
		Utterance utterance = new Utterance("I like {Color} and {Food}");
		
		List<String> inputTokens = Utils.tokenize("I like red and grapes");
		Slots slots = new Slots();
		
		CustomSlot color = new CustomSlot("Color", new String[]{"Green","blue","Red"});
		slots.add(color);
		
		CustomSlot food = new CustomSlot("Food", new String[]{"grapes","biscuits","lollipops"});
		slots.add(food);
		
		UtteranceMatch match = utterance.matches(inputTokens, slots);		
		
		assertThat(match,is(notNullValue()));
		assertThat(match.isMatched(), is(true));
		assertThat(match.getSlotMatches().size(), is(2));
		
		SlotMatch colorMatch = match.getSlotMatches().get(color);
		assertThat(colorMatch,is(notNullValue()));		
		assertThat(colorMatch.getOrginalValue(), is("red"));
		assertThat(colorMatch.getValue(), is("Red"));
		
		SlotMatch foodMatch = match.getSlotMatches().get(food);
		assertThat(foodMatch,is(notNullValue()));		
		assertThat(foodMatch.getOrginalValue(), is("grapes"));
		assertThat(foodMatch.getValue(), is("grapes"));
	}
	
	@Test
	public void testMultiSlotNotMatch() {
		Utterance utterance = new Utterance("I like {Color} and {Food}");
		
		List<String> inputTokens = Utils.tokenize("I like red and burgers");
		Slots slots = new Slots();
		
		CustomSlot color = new CustomSlot("Color", new String[]{"Green","blue","Red"});
		slots.add(color);
		
		CustomSlot food = new CustomSlot("Food", new String[]{"grapes","biscuits","lollipops"});
		slots.add(food);
		
		UtteranceMatch match = utterance.matches(inputTokens, slots);		
		
		assertThat(match,is(notNullValue()));
		assertThat(match.isMatched(), is(false));
		assertThat(match.getSlotMatches().size(), is(0));
	}
	
	@Test
	public void testMultiWordSlotMatch() {
		Utterance utterance = new Utterance("What is the time in {City}");
		
		List<String> inputTokens = Utils.tokenize("What is the time in San francisco");
		Slots slots = new Slots();
		
		CustomSlot color = new CustomSlot("City", new String[]{"Wellington","San Francisco","Auckland"});
		slots.add(color);
		
		UtteranceMatch match = utterance.matches(inputTokens, slots);		
		
		assertThat(match,is(notNullValue()));
		assertThat(match.isMatched(), is(true));
		assertThat(match.getSlotMatches().size(), is(1));
		
		SlotMatch colorMatch = match.getSlotMatches().get(color);
		assertThat(colorMatch,is(notNullValue()));		
		assertThat(colorMatch.getOrginalValue(), is("San francisco"));
		assertThat(colorMatch.getValue(), is("San Francisco"));
	}
}
