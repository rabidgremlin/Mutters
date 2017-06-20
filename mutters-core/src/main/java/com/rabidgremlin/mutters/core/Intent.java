package com.rabidgremlin.mutters.core;

import java.util.Collection;
import java.util.Collections;

/**
 * This class represents an intent. Each intent has a unique name and zero or more Slots that are used to extract
 * entities out of a user's input.
 * 
 * @author rabidgremlin
 *
 */
public class Intent
{
  /** The name of the intent. */
  protected String name;

  /** The slots for the intent. */
  protected Slots slots = new Slots();

  /**
   * Constructor.
   * 
   * @param name The name of the intent.
   */
  public Intent(String name)
  {
    this.name = name;
  }

  /**
   * Returns the name of the intent.
   * 
   * @return The name of the intent.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Adds a slot to the intent.
   * 
   * @param slot The slot to add.
   */
  public void addSlot(Slot slot)
  {
    slots.add(slot);
  }

  /**
   * Returns the slots for the intent.
   * 
   * @return The slots for the intent.
   */
  public Collection<Slot> getSlots()
  {
    return Collections.unmodifiableCollection(slots.getSlots());
  }

}
