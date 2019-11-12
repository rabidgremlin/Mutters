/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core;

import java.util.HashMap;

/**
 * An IntentMatch used to indicate no good match was found by an IntentMatcher.
 * 
 * @author rabidgremlin
 *
 */
public class NoIntentMatch extends IntentMatch
{
  /**
   * Creates a NoIntentMatch with empty matcher scores.
   * 
   * @param utterance The utterance that did not match.
   * 
   */
  public NoIntentMatch(String utterance)
  {
    super(Intent.none(), new HashMap<Slot, SlotMatch>(), utterance, new MatcherScores());
  }

  /**
   * Creates a NoIntentMatch with the specified matcher scores. Allows further
   * processing to be done based on the scores even if no match was found.
   * 
   * @param utterance     The utterance that did not match.
   * @param matcherScores The matcher scores.
   */
  public NoIntentMatch(String utterance, MatcherScores matcherScores)
  {
    super(Intent.none(), new HashMap<Slot, SlotMatch>(), utterance, matcherScores);
  }
}
