/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A Slot that is a combination of two other slots. Useful if you need different
 * approaches for matching a slot. Class will stop on the match returned by a
 * slot.
 *
 * @author rabidgremlin
 * @author wilmol
 * @see #compose(String, Slot...)
 */
public class CompoundSlot<T> extends AbstractSlot<T>
{
  /** The first slot to try match against. */
  private final Slot<? extends T> firstSlot;

  /** The second slot to try match against. */
  private final Slot<? extends T> secondSlot;

  /**
   * Constructor.
   *
   * @param name       The name of the slot.
   * @param firstSlot  The first slot to try match with.
   * @param secondSlot The second slot to try match against,
   */
  public CompoundSlot(String name, Slot<? extends T> firstSlot, Slot<? extends T> secondSlot)
  {
    super(name);
    this.firstSlot = Objects.requireNonNull(firstSlot);
    this.secondSlot = Objects.requireNonNull(secondSlot);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Optional<SlotMatch<T>> match(String token, Context context)
  {
    Optional<? extends SlotMatch<? extends T>> match = firstSlot.match(token, context);
    if (!match.isPresent())
    {
      match = secondSlot.match(token, context);
    }
    // this cast is safe because SlotMatch and Optional are read only
    return (Optional<SlotMatch<T>>) match;
  }

  /**
   * Static factory for composing {@link Slot}s using {@link CompoundSlot}.
   *
   * @param name  composed slot name
   * @param slots slots to compose
   * @return composed slot
   */
  public static Slot<?> compose(String name, List<Slot<?>> slots)
  {
    return slots.stream().reduce((firstSlot, secondSlot) -> new CompoundSlot<>(name, firstSlot, secondSlot))
        .orElseThrow(() -> new IllegalArgumentException("No slots provided."));
  }

  /**
   * Static factory for composing {@link Slot}s using {@link CompoundSlot}.
   *
   * @param name  composed slot name
   * @param slots slots to compose
   * @return composed slot
   */
  public static Slot<?> compose(String name, Slot<?>... slots)
  {
    return compose(name, Arrays.asList(slots));
  }
}
