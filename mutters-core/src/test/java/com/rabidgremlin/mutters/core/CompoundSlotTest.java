/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

/**
 * Created by wilmol on 2019-08-19 at 12:35.
 */
class CompoundSlotTest
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
  void canConstructWithSameType()
  {
    CompoundSlot<String> compoundSlot = new CompoundSlot<>("compound", stringSlot, stringSlot);
    Optional<SlotMatch<String>> match = compoundSlot.match("hi", null);
    assertThat(match).isPresent();
    assertThat(match.get().getValue()).isEqualTo("hi");
    assertThat(match.get().getSlot()).isEqualTo(stringSlot);
  }

  @Test
  void canConstructWithDifferentTypes()
  {
    CompoundSlot<Number> compoundSlot = new CompoundSlot<>("compound", integerSlot, doubleSlot);
    Optional<SlotMatch<Number>> match = compoundSlot.match("123", null);
    assertThat(match).isPresent();
    assertThat(match.get().getValue()).isEqualTo(123);
    assertThat(match.get().getSlot()).isEqualTo(integerSlot);
  }

  @Test
  void mustTrySecondSlotIfFirstFailsToMatch()
  {
    CompoundSlot<Number> compoundSlot = new CompoundSlot<>("compound", doubleSlot, integerSlot);
    Optional<SlotMatch<Number>> match = compoundSlot.match("123", null);
    assertThat(match).isPresent();
    assertThat(match.get().getValue()).isEqualTo(123);
    assertThat(match.get().getSlot()).isEqualTo(integerSlot);
  }

  @Test
  void testComposeStaticFactoryVarargs()
  {
    Slot<?> composed = CompoundSlot.compose("composed", doubleSlot, integerSlot, stringSlot);
    assertThat(composed).isNotNull();
    Optional<? extends SlotMatch<?>> match = composed.match("123", null);
    assertThat(match).isPresent();
    assertThat(match.get().getValue()).isEqualTo(123);
    assertThat(match.get().getSlot()).isEqualTo(integerSlot);
  }

  @Test
  void testComposeStaticFactoryArray()
  {
    Slot<?>[] slots = { doubleSlot, integerSlot, stringSlot };
    Slot<?> composed = CompoundSlot.compose("composed", slots);
    assertThat(composed).isNotNull();
    Optional<? extends SlotMatch<?>> match = composed.match("123", null);
    assertThat(match).isPresent();
    assertThat(match.get().getValue()).isEqualTo(123);
    assertThat(match.get().getSlot()).isEqualTo(integerSlot);
  }

  @Test
  void testComposeStaticFactoryList()
  {
    List<Slot<?>> slots = Arrays.asList(doubleSlot, integerSlot, stringSlot);
    Slot<?> composed = CompoundSlot.compose("composed", slots);
    assertThat(composed).isNotNull();
    Optional<? extends SlotMatch<?>> match = composed.match("123", null);
    assertThat(match).isPresent();
    assertThat(match.get().getValue()).isEqualTo(123);
    assertThat(match.get().getSlot()).isEqualTo(integerSlot);
  }

  @Test
  void testComposeStaticFactoryRejectsEmptyVarargs()
  {
    IllegalArgumentException expected = assertThrows(IllegalArgumentException.class,
        () -> CompoundSlot.compose("composed"));
    assertThat(expected).hasMessageThat().isEqualTo("No slots provided.");
  }

  @Test
  void testComposeStaticFactoryRejectsEmptyArray()
  {
    Slot<?>[] slots = {};
    IllegalArgumentException expected = assertThrows(IllegalArgumentException.class,
        () -> CompoundSlot.compose("composed", slots));
    assertThat(expected).hasMessageThat().isEqualTo("No slots provided.");
  }

  @Test
  void testComposeStaticFactoryRejectsEmptyList()
  {
    List<Slot<?>> slots = Collections.emptyList();
    IllegalArgumentException expected = assertThrows(IllegalArgumentException.class,
        () -> CompoundSlot.compose("composed", slots));
    assertThat(expected).hasMessageThat().isEqualTo("No slots provided.");
  }
}
