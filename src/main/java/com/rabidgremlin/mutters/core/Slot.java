package com.rabidgremlin.mutters.core;

public interface Slot {
	
	public SlotMatch match(String token);
	public String getName();

}
