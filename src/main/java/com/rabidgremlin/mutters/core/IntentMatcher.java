package com.rabidgremlin.mutters.core;

import java.util.Set;

/**
 * This is the interface implemented by any intent matcher. It should take an user's utterance (and a context) and
 * returns an intent match.
 * 
 * @author rabidgremlin
 *
 */
public interface IntentMatcher
{
  /**
   * This returns the best intent match for the user's utterance. Can return null to indicate no good match found.
   * 
   * @param utterance The user's utterance.
   * @param context The user's context, helps with extracting data from the user's utterance.
   * @param expectedIntents Set of intent names that we expect to match on. Matcher will only return a match from this
   *          set. Can be null if matcher should match against any intent.
   * @return The best intent match or null if no good match.
   */
  IntentMatch match(String utterance, Context context, Set<String> expectedIntents);

}
