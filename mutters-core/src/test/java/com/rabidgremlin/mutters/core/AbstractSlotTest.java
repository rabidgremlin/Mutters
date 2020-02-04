/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

import java.util.Objects;
import java.util.Optional;

import org.junit.Test;

/**
 * @author wilmol
 */
public class AbstractSlotTest
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

  @Test
  public void testGetName()
  {
    Slot<String> slot = new TestSlot("my-slot");
    assertThat(slot.getName(), is("my-slot"));
  }

  @Test
  public void testToString()
  {
    Slot<String> slot = new TestSlot("my-slot");
    assertThat(slot.toString(), is("TestSlot [name=my-slot]"));
  }

  @Test
  public void testEquals()
  {
    Slot<String> slot = new TestSlot("my-slot");
    Slot<String> slotWithSameName = new TestSlot("my-slot");
    Slot<String> slotWithDifferentName = new TestSlot("my-slot-2");
    assertEquals(slot, slotWithSameName);
    assertNotEquals(slot, slotWithDifferentName);
  }

  @Test
  public void testHashCode()
  {
    Slot<String> slot = new TestSlot("my-slot");
    assertEquals(slot.hashCode(), Objects.hash("my-slot"));
  }
}
