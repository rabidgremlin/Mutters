/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.opennlp.ner;

import java.util.Optional;

import com.rabidgremlin.mutters.core.AbstractSlot;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.slots.DefaultValueSlot;

public class TestSlot extends AbstractSlot<String> implements DefaultValueSlot<String>
{

  public TestSlot(String name)
  {
    super(name);
  }

  @Override
  public String getDefaultValue()
  {
    return "Default value";
  }

  @Override
  public Optional<SlotMatch<String>> match(String token, Context context)
  {
    return Optional.empty();
  }
}
