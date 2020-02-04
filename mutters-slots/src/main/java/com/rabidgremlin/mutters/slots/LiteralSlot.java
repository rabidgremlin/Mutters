/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.slots;

import java.util.Optional;

import com.rabidgremlin.mutters.core.AbstractSlot;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.SlotMatch;

/**
 * Slot that handles a string literal. Included for completeness. Always
 * matches.
 *
 */
public class LiteralSlot extends AbstractSlot<String>
{

  public LiteralSlot(String name)
  {
    super(name);
  }

  @Override
  public Optional<SlotMatch<String>> match(String token, Context context)
  {
    return Optional.of(new SlotMatch<>(this, token, token.toLowerCase()));
  }
}
