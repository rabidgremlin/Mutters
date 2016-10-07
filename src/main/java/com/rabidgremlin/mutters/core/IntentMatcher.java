package com.rabidgremlin.mutters.core;

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
   * @return The best intent match or null if no good match.
   */
  IntentMatch match(String utterance, Context context);

}
