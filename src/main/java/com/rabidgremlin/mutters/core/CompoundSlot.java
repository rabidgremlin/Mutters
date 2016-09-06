package com.rabidgremlin.mutters.core;

public class CompoundSlot extends Slot
{
	private String name;
	private Slot firstSlot;
	private Slot secondSlot;

	public CompoundSlot(String name, Slot firstSlot, Slot secondSlot)
	{
		this.name = name;
		this.firstSlot = firstSlot;
		this.secondSlot = secondSlot;
	}

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

	@Override
	public String getName()
	{
		return name;
	}

}
