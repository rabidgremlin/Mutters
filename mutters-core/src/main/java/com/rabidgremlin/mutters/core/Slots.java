package com.rabidgremlin.mutters.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * Helper class for managing a map of slots.
 * 
 * @author rabidgremlin
 *
 */
public class Slots
{
  /** The map of slots. */
  private HashMap<String, Slot> slots = new HashMap<String, Slot>();

  /**
   * Adds a slot to the map.
   * 
   * @param slot The slot to add.
   */
  public void add(Slot slot)
  {
    slots.put(slot.getName().toLowerCase(), slot);
  }

  /**
   * Gets the specified slot from the map.
   * 
   * @param name The name of the slot.
   * @return The name of the slot or null if the slot does not exist in the map.
   */
  public Slot getSlot(String name)
  {
    return slots.get(name.toLowerCase());
  }

  /**
   * Returns the slots in the map.
   * 
   * @return The slots in the map.
   */
  public Collection<Slot> getSlots()
  {
    return Collections.unmodifiableCollection(slots.values());
  }

}
