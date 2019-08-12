package com.rabidgremlin.mutters.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class holds the details of an intent match.
 * 
 * @author rabidgremlin
 *
 */
public class IntentMatch
{
  /** The intent that was matched. */
  private Intent intent;

  /** Map of slots that were matched. */
  private HashMap<Slot, SlotMatch> slotMatches;

  /** The utterance that was matched against. */
  private String utterance;

  /**
   * Constructor.
   * 
   * @param intent The intent that was matched.
   * @param slotMatches The slots that were matched.
   * @param utterance The utterance that was matched against.
   */
  public IntentMatch(Intent intent, HashMap<Slot, SlotMatch> slotMatches, String utterance)
  {

    this.intent = intent;
    this.slotMatches = slotMatches;
    this.utterance = utterance;
  }

  /**
   * Returns the Intent that was matched.
   * 
   * @return The intent that was matched.
   */
  public Intent getIntent()
  {
    return intent;
  }

  /**
   * Returns the slots that were matched.
   * 
   * @return Map of the slots that were matched.
   */
  public Map<Slot, SlotMatch> getSlotMatches()
  {
    return Collections.unmodifiableMap(slotMatches);
  }

  /**
   * Returns the specified slot match if the slot was matched.
   * 
   * @param slotName The name of the slot to return.
   * @return The slot match or null if the slot was not matched.
   */
  public SlotMatch getSlotMatch(String slotName)
  {
    for (SlotMatch match : slotMatches.values())
    {
      if (match.getSlot().getName().equalsIgnoreCase(slotName))
      {
        return match;
      }
    }
    return null;
  }

  /**
   * Removes the specified slot match from the intent match.
   * 
   * @param slotName The name of the slot to remove the match for.
   */
  public void removeSlotMatch(String slotName)
  {
    SlotMatch match = getSlotMatch(slotName);
    if (match != null)
    {
      slotMatches.remove(match.getSlot());
    }
  }

  /**
   * Returns the utterance that was matched against.
   * 
   * @return The utterance that was matched against.
   */
  public String getUtterance()
  {
    return utterance;
  }

}
