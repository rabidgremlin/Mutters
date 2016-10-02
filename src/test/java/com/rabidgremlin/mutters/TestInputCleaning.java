package com.rabidgremlin.mutters;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

import com.rabidgremlin.mutters.core.CleanedInput;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.InputCleaner;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.Slots;
import com.rabidgremlin.mutters.slots.LiteralSlot;
import com.rabidgremlin.mutters.templated.TemplatedUtterance;
import com.rabidgremlin.mutters.templated.TemplatedUtteranceMatch;
import com.rabidgremlin.mutters.util.Utils;

public class TestInputCleaning
{

  // TODO make this a real test
  @Test
  public void testBasicCleaning()
  {
    CleanedInput cleanedInput = InputCleaner.cleanInput("What's the time");
    cleanedInput = InputCleaner.cleanInput("What is the time");
    cleanedInput = InputCleaner.cleanInput("Whats the time");
    cleanedInput = InputCleaner.cleanInput("Whats teh time");
    cleanedInput = InputCleaner.cleanInput("Wats the time");
    cleanedInput = InputCleaner.cleanInput("Whats the tim");
    cleanedInput = InputCleaner.cleanInput("Whats the tiem");

    cleanedInput = InputCleaner.cleanInput("san francisco");
    cleanedInput = InputCleaner.cleanInput("San Franscisoco");
    cleanedInput = InputCleaner.cleanInput("Sun Fransisco");

    cleanedInput = InputCleaner.cleanInput("Auckland");
    cleanedInput = InputCleaner.cleanInput("Awkland");
    cleanedInput = InputCleaner.cleanInput("Oakland");

    cleanedInput = InputCleaner.cleanInput("I like {Color}");
    cleanedInput = InputCleaner.cleanInput("I like {Color} and {Food}");
    cleanedInput = InputCleaner.cleanInput("I like red and burgers");
    cleanedInput = InputCleaner.cleanInput("I like bright pink and fish and chips");

  }

}
