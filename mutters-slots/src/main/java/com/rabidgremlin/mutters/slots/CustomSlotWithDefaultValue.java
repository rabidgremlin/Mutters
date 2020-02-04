/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.slots;

import java.util.List;
import java.util.Map;

/**
 * A CustomSlot that supports default values.
 *
 * @author rabidgremlin
 *
 */
public class CustomSlotWithDefaultValue extends CustomSlot implements DefaultValueSlot<String>
{
  /** The default value to return */
  private final String defaultValue;

  /**
   * Constructor.
   *
   * @param name           The name of the slot.
   * @param optionValueMap A map of possible input values mapped to output values.
   * @param defaultValue   The default value.
   */
  public CustomSlotWithDefaultValue(String name, Map<String, String> optionValueMap, String defaultValue)
  {
    super(name, optionValueMap);
    this.defaultValue = defaultValue;
  }

  /**
   * Constructor.
   *
   * @param name         The name of the slot.
   * @param options      The list of expected options.
   * @param defaultValue The default value.
   */
  public CustomSlotWithDefaultValue(String name, List<String> options, String defaultValue)
  {
    super(name, options);
    this.defaultValue = defaultValue;
  }

  /**
   * Constructor.
   *
   * @param name         The name of the slot.
   * @param options      The list of expected options.
   * @param defaultValue The default value.
   */
  public CustomSlotWithDefaultValue(String name, String[] options, String defaultValue)
  {
    super(name, options);
    this.defaultValue = defaultValue;
  }

  @Override
  public String getDefaultValue()
  {
    return defaultValue;
  }
}
