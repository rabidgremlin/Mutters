/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core;

import java.util.Objects;

/**
 * A Slot that is a combination of two other slots. Useful if you need different
 * approaches for matching a slot. Class will stop on the match returned by a
 * slot.
 * 
 * @author rabidgremlin
 *
 */
public class CompoundSlot extends Slot
{
  /** The name of the compound slot. */
  private String name;

  /** The first slost to true match against. */
  private Slot firstSlot;

  /** The second slot to try match against. */
  private Slot secondSlot;

  /**
   * Constructor.
   * 
   * @param name       The name of the slot.
   * @param firstSlot  The first slot to try match with.
   * @param secondSlot The second slot to try match against,
   */
  public CompoundSlot(String name, Slot firstSlot, Slot secondSlot)
  {
    this.name = Objects.requireNonNull(name);
    this.firstSlot = Objects.requireNonNull(firstSlot);
    this.secondSlot = Objects.requireNonNull(secondSlot);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.rabidgremlin.mutters.core#match(String token, Context context)
   * 
   */
  @Override
  public SlotMatch match(String token, Context context)
  {
    SlotMatch match = firstSlot.match(token, context);
    if (match == null)
    {
      match = secondSlot.match(token, context);
    }

    return match;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.rabidgremlin.mutters.core#getName()
   * 
   */
  @Override
  public String getName()
  {
    return name;
  }

}
