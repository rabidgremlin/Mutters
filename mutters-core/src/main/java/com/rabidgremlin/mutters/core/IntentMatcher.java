/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core;

import java.util.Set;

/**
 * This is the interface implemented by any intent matcher. It should take an
 * user's utterance (and a context) and return an intent match.
 * 
 * A NoIntentMatch be returned by an IntentMatcher to indicate that there was
 * not a match.
 * 
 * @author rabidgremlin
 *
 */
public interface IntentMatcher
{
  /**
   * This returns the best intent match for the user's utterance. Can return a
   * NoIntentMatch to indicate no good match was found.
   * 
   * @param utterance       The user's utterance.
   * @param context         The user's context, helps with extracting data from
   *                        the user's utterance.
   * @param expectedIntents Set of intent names that we expect to match on.
   *                        Matcher will only return a match from this set. Can be
   *                        null if matcher should match against any intent.
   * @return The best intent match or NoIntentMatch if no good match was found.
   */
  IntentMatch match(String utterance, Context context, Set<String> expectedIntents);

}
