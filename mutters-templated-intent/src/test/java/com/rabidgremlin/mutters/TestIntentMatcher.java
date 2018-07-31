package com.rabidgremlin.mutters;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.slots.CustomSlot;
import com.rabidgremlin.mutters.slots.DefaultValueSlot;
import com.rabidgremlin.mutters.slots.LiteralSlot;
import com.rabidgremlin.mutters.slots.NumberSlot;
import com.rabidgremlin.mutters.templated.SimpleTokenizer;
import com.rabidgremlin.mutters.templated.TemplatedIntent;
import com.rabidgremlin.mutters.templated.TemplatedIntentMatcher;
import com.rabidgremlin.mutters.templated.TemplatedUtterance;

public class TestIntentMatcher
{
  private SimpleTokenizer tokenizer = new SimpleTokenizer();

  @Test
  public void testBasicMatching()
  {
    TemplatedIntentMatcher matcher = new TemplatedIntentMatcher(tokenizer);
    TemplatedIntent additionIntent = matcher.addIntent("Addition");

    additionIntent.addUtterance("What's {number1} + {number2}");
    additionIntent.addUtterance("What is {number1} + {number2}");
    additionIntent.addUtterance("Add {number1} and {number2}");
    additionIntent.addUtterance("{number1} plus {number2}");

    NumberSlot number1 = new NumberSlot("number1");
    NumberSlot number2 = new NumberSlot("number2");

    additionIntent.addSlot(number1);
    additionIntent.addSlot(number2);

    IntentMatch intentMatch = matcher.match("What is 1 + 5", new Context(), null, null);

    assertThat(intentMatch, is(notNullValue()));
    assertThat(intentMatch.getIntent(), is(additionIntent));
    assertThat(intentMatch.getSlotMatches().size(), is(2));

    SlotMatch number1Match = intentMatch.getSlotMatches().get(number1);
    assertThat(number1Match, is(notNullValue()));
    assertThat(number1Match.getValue(), is(1L));

    SlotMatch number2Match = intentMatch.getSlotMatches().get(number2);
    assertThat(number2Match, is(notNullValue()));
    assertThat(number2Match.getValue(), is(5L));

  }

  @Test
  public void testBrokenMatch()
  {
    TemplatedIntentMatcher matcher = new TemplatedIntentMatcher(tokenizer);
    TemplatedIntent intent = matcher.addIntent("Hello");

    intent.addUtterance("hello");
    intent.addUtterance("hi");
    intent.addUtterance("hiya");

    IntentMatch intentMatch = matcher.match("book this flight", new Context(), null, null);

    assertThat(intentMatch, is(nullValue()));
  }

  @Test
  public void testBrokenAirportMatch()
  {
    TemplatedIntentMatcher matcher = new TemplatedIntentMatcher(tokenizer);        
    TemplatedIntent intent = matcher.addIntent("GetAirport");
    
    intent.addUtterance("{Airport}");

    CustomSlot airportSlot = new CustomSlot("Airport", new String[]{ "NEWCASTLE" });
    intent.addSlot(airportSlot);
    

    IntentMatch intentMatch = matcher.match("next friday", new Context(), null, null);

    assertThat(intentMatch, is(nullValue()));
  }


  @Test
  public void testHandlingOfEmptyOrTokenizedOutInputs()
  {
    TemplatedIntentMatcher matcher = new TemplatedIntentMatcher(tokenizer);        
    TemplatedIntent intent = matcher.addIntent("AcceptAnything");
    
    intent.addUtterance("{AnyThing}");

    LiteralSlot slot = new LiteralSlot("AnyThing");
    intent.addSlot(slot);
    
    IntentMatch intentMatch = matcher.match("Bob", new Context(), null, null);
    assertThat(intentMatch, is(notNullValue()));

    intentMatch = matcher.match("", new Context(), null, null);
    assertThat(intentMatch, is(nullValue()));

    intentMatch = matcher.match(" ", new Context(), null, null);
    assertThat(intentMatch, is(nullValue()));

    intentMatch = matcher.match("?", new Context(), null, null);
    assertThat(intentMatch, is(nullValue()));

    intentMatch = matcher.match("...", new Context(), null, null);
    assertThat(intentMatch, is(nullValue()));

    intentMatch = matcher.match(" ?", new Context(), null, null);
    assertThat(intentMatch, is(nullValue()));
  }
  
  // slot that matches a colour or defaults to black
  class ColorsSlot extends CustomSlot implements DefaultValueSlot
  {
	  public ColorsSlot()
	  {
		  super("Color",new String[] {"Red","Green","Blue","White"});
	  }

	  @Override
	  public Object getDefaultValue() 
	  {
	    return "Black";
	  } 
  }
  
  @Test
  public void testDefaultValueHandling()
  {
	  TemplatedIntentMatcher matcher = new TemplatedIntentMatcher(tokenizer);
	  
	  TemplatedIntent intent = matcher.addIntent("FavColourIntent");
	  
	  ColorsSlot colorSlot = new ColorsSlot();
	  intent.addSlot(colorSlot);
	  
	  intent.addUtterance("My favourite color is {Color}");
	  intent.addUtterance("{Color} is my favourite");
	  intent.addUtterance("I have a favourite color"); // utterance without a slot, should default to Black
	  
	  IntentMatch intentMatch = matcher.match("My favourite color is green", new Context(), null, null);
	  assertThat(intentMatch, is(notNullValue()));
	  assertThat(intentMatch.getSlotMatches().size(), is(1));
	  SlotMatch colourMatch = intentMatch.getSlotMatches().get(colorSlot);
	  assertThat(colourMatch, is(notNullValue()));
	  assertThat(colourMatch.getValue(), is("Green"));
	  
	  intentMatch = matcher.match("Red is my favourite", new Context(), null, null);
	  assertThat(intentMatch, is(notNullValue()));
	  assertThat(intentMatch.getSlotMatches().size(), is(1));
	  colourMatch = intentMatch.getSlotMatches().get(colorSlot);
	  assertThat(colourMatch, is(notNullValue()));
	  assertThat(colourMatch.getValue(), is("Red"));
	  
	  
	  intentMatch = matcher.match("I have a favourite color", new Context(), null, null);
	  assertThat(intentMatch, is(notNullValue()));
	  assertThat(intentMatch.getSlotMatches().size(), is(1));
	  colourMatch = intentMatch.getSlotMatches().get(colorSlot);
	  assertThat(colourMatch, is(notNullValue()));
	  assertThat(colourMatch.getValue(), is("Black"));
	  
  }

}
