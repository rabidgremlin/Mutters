package com.rabidgremlin.mutters.core;

import java.util.HashMap;

/**
 * This is the interface implemented by any slot matcher. It should take an user's utterance, an intent and a context
 * and return a map of slot matches.
 * 
 * If no slot matches are found it should return an empty map.
 * 
 * @author rabidgremlin
 * 
 */
public interface SlotMatcher
{
  /**
   * This method returns any slot matches for the supplied context, intent and utterance.
   * 
   * @param context The user's context. Used by slots for timezone and locale specific processing.
   * @param intent The intent to extract slots for.
   * @param utterance The user's utterance to extract slots from.
   * @return A map of slot matches or an empty map if there were no matches.
   */
  public HashMap<Slot, SlotMatch> match(Context context, Intent intent, String utterance);
}
