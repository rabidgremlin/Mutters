package com.rabidgremlin.mutters.core;

public interface Slot {

	public SlotMatch match(String token, Context context);

	public String getName();

}
