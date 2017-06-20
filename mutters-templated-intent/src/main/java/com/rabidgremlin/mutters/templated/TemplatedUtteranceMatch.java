package com.rabidgremlin.mutters.templated;

import java.util.HashMap;

import com.rabidgremlin.mutters.core.Slot;
import com.rabidgremlin.mutters.core.SlotMatch;

/**
 * This holds the details of an templated utterance match.
 * 
 * @author rabidgremlin
 *
 */
public class TemplatedUtteranceMatch
{
  /** Flag to indicate if template was matched. */
  private boolean matched;

  /** Map of slots that were matched. */
  private HashMap<Slot, SlotMatch> slotMatches;

  /**
   * Constructor.
   * 
   * @param matched Indicates if template was matched or not.
   */
  public TemplatedUtteranceMatch(boolean matched)
  {
    this.matched = matched;
    this.slotMatches = new HashMap<Slot, SlotMatch>();
  }

  /**
   * Constructor.
   * 
   * @param matched Indicates if template was matched or not.
   * @param slotMatches Map of slot matches.
   */
  public TemplatedUtteranceMatch(boolean matched, HashMap<Slot, SlotMatch> slotMatches)
  {
    this.matched = matched;
    this.slotMatches = slotMatches;
  }

  /**
   * Returns true of the templated utterance was matched on.
   * 
   * @return True if templated utterance was matched on.
   */
  public boolean isMatched()
  {
    return matched;
  }

  /**
   * Returns the map of slot matches.
   * 
   * @return Map of slot matches. Will be empty if no slot matches.
   */
  public HashMap<Slot, SlotMatch> getSlotMatches()
  {
    return slotMatches;
  }

  @Override
  public String toString()
  {
    return "UtteranceMatch [matched=" + matched + ", slotMatches=" + slotMatches + "]";
  }

}
