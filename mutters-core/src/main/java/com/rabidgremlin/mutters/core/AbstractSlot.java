/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core;

import java.util.Objects;

/**
 * Base class for creating slots. Slots are used to extract data from a user's
 * utterance.
 * 
 * @author rabidgremlin
 * @author wilmol
 */
public abstract class AbstractSlot<T> implements Slot<T>
{
  private final String name;

  protected AbstractSlot(String name)
  {
    this.name = Objects.requireNonNull(name);
  }

  @Override
  public final String getName()
  {
    return name;
  }

  @Override
  public final boolean equals(Object o)
  {
    // HACK HACK assumes name match means equals. need to do this for SlotMatches
    // list but is dog dog ugly
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }
    Slot<?> that = (Slot<?>) o;
    return Objects.equals(getName(), that.getName());
  }

  @Override
  public final int hashCode()
  {
    // HACK HACK assumes name match means equals. need to do this for SlotMatches
    // list but is dog dog ugly
    return Objects.hash(getName());
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + " [name=" + name + "]";
  }
}
