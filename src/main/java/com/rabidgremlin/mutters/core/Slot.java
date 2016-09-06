package com.rabidgremlin.mutters.core;

import com.rabidgremlin.mutters.slots.CustomSlot;

public abstract class Slot
{

	public abstract SlotMatch match(String token, Context context);

	public abstract String getName();

	// HACK HACK assumes name match means equals. need to do this for SlotMatches list but is dog dog ugly
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		return result;
	}

	// HACK HACK assumes name match means equals. need to do this for SlotMatches list but is dog dog ugly
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomSlot other = (CustomSlot) obj;
		if (getName() == null)
		{
			if (other.getName() != null)
				return false;
		}
		else
		{
			if (!getName().equals(other.getName()))
				return false;
		}

		return true;
	}
}
