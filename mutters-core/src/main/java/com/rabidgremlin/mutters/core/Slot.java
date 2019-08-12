package com.rabidgremlin.mutters.core;

/**
 * Base class for all slots. Slots are used to extract data from a user's utterance.
 * 
 * @author rabidgremlin
 *
 */
public abstract class Slot
{
  /**
   * This method returns a slot match if the slot matches the given token.
   * 
   * @param token The token to match against.
   * @param context The user's context. Provides data to help with slot matching.
   * @return A SlotMatch if the slot was matched or null if there was no match.
   */
  public abstract SlotMatch match(String token, Context context);

  /**
   * This method returns the name of the slot.
   * 
   * @return The name of the slot.
   */
  public abstract String getName();

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   * 
   */
  @Override
  public int hashCode()
  {
    // HACK HACK assumes name match means equals. need to do this for SlotMatches list but is dog dog
    // ugly
    final int prime = 31;
    int result = 1;
    result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(Object obj)
   * 
   */
  @Override
  public boolean equals(Object obj)
  {
    // HACK HACK assumes name match means equals. need to do this for SlotMatches list but is dog dog
    // ugly
    if (this == obj)
    {
      return true;
    }
    if (obj == null)
    {
      return false;
    }
    if (!(obj instanceof Slot))
    {
      return false;
    }

    Slot other = (Slot) obj;
    if (getName() == null)
    {
      if (other.getName() != null)
      {
        return false;
      }
    }
    else
    {
      if (!getName().equals(other.getName()))
      {
        return false;
      }
    }

    return true;
  }
}
