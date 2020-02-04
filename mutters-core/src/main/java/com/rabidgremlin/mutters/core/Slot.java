/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core;

import java.util.Optional;

/**
 * Interface defining the {@code Slot} type.
 *
 * @param <T> the corresponding {@link SlotMatch} type
 * @author wilmol
 * @see AbstractSlot
 */
public interface Slot<T>
{

  /**
   * This method returns a slot match if the slot matches the given token.
   *
   * @param token   The token to match against.
   * @param context The user's context. Provides data to help with slot matching.
   * @return A SlotMatch if the slot was matched or {@code Optional.empty} if
   *         there was no match.
   */
  Optional<SlotMatch<T>> match(String token, Context context);

  /**
   * This method returns the name of the slot.
   *
   * @return The name of the slot.
   */
  String getName();
}
