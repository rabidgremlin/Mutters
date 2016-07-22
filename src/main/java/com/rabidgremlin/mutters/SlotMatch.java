package com.rabidgremlin.mutters;

public class SlotMatch {

	private Slot slot;
	private String orginalValue;
	private Object value;

	public SlotMatch(Slot slot, String orginalValue, Object value) {
		this.slot = slot;
		this.orginalValue = orginalValue;
		this.value = value;
	}

	public Slot getSlot() {
		return slot;
	}

	public String getOrginalValue() {
		return orginalValue;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "SlotMatch [slot=" + slot + ", orginalValue=" + orginalValue
				+ ", value=" + value + "]";
	}

}
