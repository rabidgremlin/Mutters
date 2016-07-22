package com.rabidgremlin.mutters;

public interface Slot {
	
	public SlotMatch match(String token);
	public String getName();

}
