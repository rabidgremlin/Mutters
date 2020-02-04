/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.slots;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.rabidgremlin.mutters.core.AbstractSlot;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.SlotMatch;

/**
 * This slot maps a number of possible inputs to a list of expected values.
 * 
 * @author rabidgremlin
 *
 */
public class CustomSlot extends AbstractSlot<String>
{
  /** Map of options. */
  private final Map<String, String> options = new HashMap<>();

  /**
   * Constructor.
   *
   * @param name    The name of the slot.
   * @param options The list of expected options.
   */
  public CustomSlot(String name, List<String> options)
  {
    super(name);
    for (String option : options)
    {
      this.options.put(option.toLowerCase(), option);
    }
  }

  /**
   * Constructor.
   * 
   * @param name    The name of the slot.
   * @param options The list of expected options.
   */
  public CustomSlot(String name, String... options)
  {
    this(name, Arrays.asList(options));
  }

  /**
   * Constructor.
   * 
   * @param name           The name of the slot.
   * @param optionValueMap A map of possible input values mapped to output values.
   */
  public CustomSlot(String name, Map<String, String> optionValueMap)
  {
    super(name);
    for (Entry<String, String> entry : optionValueMap.entrySet())
    {
      options.put(entry.getKey().toLowerCase(), entry.getValue());
    }
  }

  @Override
  public Optional<SlotMatch<String>> match(String token, Context context)
  {
    String id = token.toLowerCase();
    return Optional.ofNullable(options.get(id)).map(option -> new SlotMatch<>(this, token, option));
  }

  @Override
  public String toString()
  {
    return "CustomSlot [name=" + getName() + ", options=" + options + "]";
  }
}
