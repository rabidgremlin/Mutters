package com.rabidgremlin.mutters.slots;

import java.util.HashMap;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.Slot;
import com.rabidgremlin.mutters.core.SlotMatch;

/**
 * A CustomSlot that supports default values.
 * 
 * @author rabidgremlin
 *
 */
 public class CustomSlotWithDefaultValue
    extends CustomSlot
    implements DefaultValueSlot
{
  /** The default value to return */
  private Object defaultValue;

  /**
   * Constructor.
   * 
   * @param name The name of the slot.
   * @param optionValueMap A map of possible input values mapped to output values.   
   * @param defaultValue The default value.
   */
  public CustomSlotWithDefaultValue(String name, HashMap<String, String> optionValueMap, Object defaultValue)
  {
    super(name, optionValueMap);
    this.defaultValue = defaultValue;
  }

  /**
   * Constructor.
   * 
   * @param name The name of the slot.   
   * @param options The list of expected options.
   * @param defaultValue The default value.
   */
  public CustomSlotWithDefaultValue(String name, String[] options, Object defaultValue)
  {
    super(name, options);
    this.defaultValue = defaultValue;
  }

  @Override
  public Object getDefaultValue()
  {    
    return defaultValue;
  }

}