/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

/**
 * Created by wilmol on 2019-08-19 at 12:35.
 */
public class CompoundSlotTest
{

  private static final Slot<String> stringSlot = new Slot<String>()
  {
    @Override
    public Optional<SlotMatch<String>> match(String token, Context context)
    {
      return Optional.of(new SlotMatch<>(this, token, token));
    }

    @Override
    public String getName()
    {
      return null;
    }
  };

  private static final Slot<Integer> integerSlot = new Slot<Integer>()
  {
    @Override
    public Optional<SlotMatch<Integer>> match(String token, Context context)
    {
      return Optional.of(new SlotMatch<>(this, token, Integer.parseInt(token)));
    }

    @Override
    public String getName()
    {
      return null;
    }
  };

  private static final Slot<Double> doubleSlot = new Slot<Double>()
  {
    @Override
    public Optional<SlotMatch<Double>> match(String token, Context context)
    {
      return Optional.empty();
    }

    @Override
    public String getName()
    {
      return null;
    }
  };

  @Test
  public void canConstructWithSameType()
  {
    CompoundSlot<String> compoundSlot = new CompoundSlot<>("compound", stringSlot, stringSlot);
    Optional<SlotMatch<String>> match = compoundSlot.match("hi", null);
    assertTrue(match.isPresent());
    assertEquals(match.get().getValue(), "hi");
    assertEquals(match.get().getSlot(), stringSlot);
  }

  @Test
  public void canConstructWithDifferentTypes()
  {
    CompoundSlot<Number> compoundSlot = new CompoundSlot<>("compound", integerSlot, doubleSlot);
    Optional<SlotMatch<Number>> match = compoundSlot.match("123", null);
    assertTrue(match.isPresent());
    assertEquals(match.get().getValue(), 123);
    assertEquals(match.get().getSlot(), integerSlot);
  }

  @Test
  public void mustTrySecondSlotIfFirstFailsToMatch()
  {
    CompoundSlot<Number> compoundSlot = new CompoundSlot<>("compound", doubleSlot, integerSlot);
    Optional<SlotMatch<Number>> match = compoundSlot.match("123", null);
    assertTrue(match.isPresent());
    assertEquals(match.get().getValue(), 123);
    assertEquals(match.get().getSlot(), integerSlot);
  }

  @Test
  public void testComposeStaticFactoryVarargs()
  {
    Slot<?> composed = CompoundSlot.compose("composed", doubleSlot, integerSlot, stringSlot);
    assertNotNull(composed);
    Optional<? extends SlotMatch<?>> match = composed.match("123", null);
    assertTrue(match.isPresent());
    assertEquals(match.get().getValue(), 123);
    assertEquals(match.get().getSlot(), integerSlot);
  }

  @Test
  public void testComposeStaticFactoryArray()
  {
    Slot<?>[] slots = { doubleSlot, integerSlot, stringSlot };
    Slot<?> composed = CompoundSlot.compose("composed", slots);
    assertNotNull(composed);
    Optional<? extends SlotMatch<?>> match = composed.match("123", null);
    assertTrue(match.isPresent());
    assertEquals(match.get().getValue(), 123);
    assertEquals(match.get().getSlot(), integerSlot);
  }

  @Test
  public void testComposeStaticFactoryList()
  {
    List<Slot<?>> slots = Arrays.asList(doubleSlot, integerSlot, stringSlot);
    Slot<?> composed = CompoundSlot.compose("composed", slots);
    assertNotNull(composed);
    Optional<? extends SlotMatch<?>> match = composed.match("123", null);
    assertTrue(match.isPresent());
    assertEquals(match.get().getValue(), 123);
    assertEquals(match.get().getSlot(), integerSlot);
  }

  @Test
  public void testComposeStaticFactoryRejectsEmptyVarargs()
  {
    try
    {
      Slot<?> composed = CompoundSlot.compose("composed");
      fail();
    }
    catch (IllegalArgumentException expected)
    {
      assertThat(expected.getMessage(), is("No slots provided."));
    }
  }

  @Test
  public void testComposeStaticFactoryRejectsEmptyArray()
  {
    try
    {
      Slot<?>[] slots = {};
      CompoundSlot.compose("composed", slots);
      fail();
    }
    catch (IllegalArgumentException expected)
    {
      assertThat(expected.getMessage(), is("No slots provided."));
    }
  }

  @Test
  public void testComposeStaticFactoryRejectsEmptyList()
  {
    try
    {
      List<Slot<?>> slots = Collections.emptyList();
      CompoundSlot.compose("composed", slots);
      fail();
    }
    catch (IllegalArgumentException expected)
    {
      assertThat(expected.getMessage(), is("No slots provided."));
    }
  }
}
