/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core;

import java.util.Objects;

/**
 * This class holds the details of a slot match.
 *
 * @param <T> the type of the value held by this slot match
 * @author rabidgremlin
 */
public final class SlotMatch<T>
{
  /** The slot that was matched. */
  private final Slot<T> slot;

  /** The original value that was used to matched on. */
  private final String originalValue;

  /** The value that was matched. */
  private final T value;

  /**
   * Constructor.
   * 
   * @param slot          The slot that was matched.
   * @param originalValue The original value that was used to match on.
   * @param value         The value that was matched.
   */
  public SlotMatch(Slot<T> slot, String originalValue, T value)
  {
    this.slot = Objects.requireNonNull(slot);
    this.originalValue = Objects.requireNonNull(originalValue);
    this.value = Objects.requireNonNull(value);
  }

  /**
   * Gets the slot that was matched on.
   * 
   * @return The slot that was matched on.
   */
  public Slot<T> getSlot()
  {
    return slot;
  }

  /**
   * Gets the original value that was matched on.
   * 
   * @return The original value that was matched on.
   */
  public String getOriginalValue()
  {
    return originalValue;
  }

  /**
   * Gets the value that was matched on.
   * 
   * @return the value that was matched on.
   */
  public T getValue()
  {
    return value;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }
    SlotMatch<?> slotMatch = (SlotMatch<?>) o;
    return Objects.equals(slot, slotMatch.slot) && Objects.equals(originalValue, slotMatch.originalValue)
        && Objects.equals(value, slotMatch.value);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(slot, originalValue, value);
  }

  @Override
  public String toString()
  {
    return "SlotMatch [slot=" + slot + ", originalValue=" + originalValue + ", value=" + value + "]";
  }
}
