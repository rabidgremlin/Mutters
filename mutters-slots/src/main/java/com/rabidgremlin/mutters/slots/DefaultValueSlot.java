/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.slots;

/**
 * An interface for a slot that has a default value, for cases when no slot
 * value is found.
 *
 * @author foskic
 *
 */
public interface DefaultValueSlot
{

  Object getDefaultValue();

}
