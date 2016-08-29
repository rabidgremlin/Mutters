package com.rabidgremlin.mutters.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class Slots
{

	private HashMap<String, Slot> slots = new HashMap<String, Slot>();

	public void add(Slot slot)
	{
		slots.put(slot.getName().toLowerCase(), slot);
	}

	public Slot getSlot(String name)
	{
		return slots.get(name.toLowerCase());
	}
	
	public Collection<Slot> getSlots()
	{
		return Collections.unmodifiableCollection(slots.values());
	}

}
