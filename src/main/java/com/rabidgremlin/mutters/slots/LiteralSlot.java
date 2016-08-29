package com.rabidgremlin.mutters.slots;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.Slot;
import com.rabidgremlin.mutters.core.SlotMatch;

/**
 * Slot that handles a string literal. Included for completeness. Always matches.
 *
 */
public class LiteralSlot implements Slot
{

	private String name;

	public LiteralSlot(String name)
	{
		this.name = name;
	}

	@Override
	public SlotMatch match(String token, Context context)
	{

		return new SlotMatch(this, token, token.toLowerCase());

	}

	@Override
	public String getName()
	{
		return name;
	}

}
