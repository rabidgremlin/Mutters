package com.rabidgremlin.mutters.core;

/**
 * This class holds the details of a slot match.
 * 
 * @author rabidgremlin
 *
 */
public class SlotMatch
{
  /** The slot that was matched. */
  private Slot slot;

  /** The original value that was used to matched on. */
  private String orginalValue;

  /** The value that was matched. */
  private Object value;

  /**
   * Constructor.
   * 
   * @param slot The slot that was matched.
   * @param orginalValue The original value that was used to match on.
   * @param value The value that was matched.
   */
  public SlotMatch(Slot slot, String orginalValue, Object value)
  {
    this.slot = slot;
    this.orginalValue = orginalValue;
    this.value = value;
  }

  /**
   * Gets the slot that was matched on.
   * 
   * @return The slot that was matched on.
   */
  public Slot getSlot()
  {
    return slot;
  }

  /**
   * Gets the original value that was matched on.
   * 
   * @return The original value that was matched on.
   */
  public String getOrginalValue()
  {
    return orginalValue;
  }

  /**
   * Gets the value that was matched on.
   * 
   * @return the value that was matched on.
   */
  public Object getValue()
  {
    return value;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.String#toString()
   * 
   */
  @Override
  public String toString()
  {
    return "SlotMatch [slot=" + slot + ", orginalValue=" + orginalValue + ", value=" + value + "]";
  }

}
