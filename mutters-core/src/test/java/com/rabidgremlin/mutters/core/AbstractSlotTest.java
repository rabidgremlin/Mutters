/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core;

import static com.google.common.truth.Truth.assertThat;

import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.Test;

/**
 * @author wilmol
 */
class AbstractSlotTest
{
  private static final class TestSlot extends AbstractSlot<String>
  {
    protected TestSlot(String name)
    {
      super(name);
    }

    @Override
    public Optional<SlotMatch<String>> match(String token, Context context)
    {
      return Optional.empty();
    }
  }

  private static final class TestSlot2 extends AbstractSlot<String>
  {
    protected TestSlot2(String name)
    {
      super(name);
    }

    @Override
    public Optional<SlotMatch<String>> match(String token, Context context)
    {
      return Optional.empty();
    }
  }

  @Test
  void testGetName()
  {
    Slot<String> slot = new TestSlot("my-slot");
    assertThat(slot.getName()).isEqualTo("my-slot");
  }

  @Test
  void testToString()
  {
    Slot<String> slot = new TestSlot("my-slot");
    assertThat(slot.toString()).isEqualTo("TestSlot [name=my-slot]");
  }

  @Test
  void testEquals()
  {
    Slot<String> slot = new TestSlot("my-slot");
    Slot<String> slotWithSameName = new TestSlot("my-slot");
    Slot<String> slotWithDifferentName = new TestSlot("my-slot-2");
    Slot<String> slotWithSameNameDifferentClass = new TestSlot2("my-slot");

    assertThat(slot).isEqualTo(slotWithSameName);
    assertThat(slot).isNotEqualTo(slotWithDifferentName);

    assertThat(slot).isEqualTo(slotWithSameNameDifferentClass);
    assertThat(slot).isEqualTo(slot);
    assertThat(slot).isNotEqualTo(1);
  }

  @Test
  void testHashCode()
  {
    Slot<String> slot = new TestSlot("my-slot");
    assertThat(slot.hashCode()).isEqualTo(Objects.hash("my-slot"));
  }
}
