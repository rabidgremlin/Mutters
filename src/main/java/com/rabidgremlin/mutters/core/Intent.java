package com.rabidgremlin.mutters.core;

import java.util.Collection;

public abstract class Intent
{
  protected String name;

  protected Slots slots = new Slots();

  public Intent(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public void addSlot(Slot slot)
  {
    slots.add(slot);
  }

  public Collection<Slot> getSlots()
  {
    return slots.getSlots();
  }

}
