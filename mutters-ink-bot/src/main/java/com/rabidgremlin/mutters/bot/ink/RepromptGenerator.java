/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.bot.ink;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.session.Session;

/**
 * This interface must be implemented by reprompt generators. These generators
 * are used to generate the bot's response when no reprompt has been defined
 * (via ::SET_REPROMPT) and the bot has failed to understand what the user has
 * said.
 * 
 * @author rabidgremlin
 *
 */
public interface RepromptGenerator
{
  /**
   * Generates a reprompt response from the bot. Passes in current state
   * information so that the reprompt can be based on that.
   * 
   * @param session         The user's session.
   * @param context         The user's context.
   * @param messageText     The original message text from the user.
   * @param intentMatch     The last intent match. Normally a NoIntentMatch but
   *                        allows matching scores to be used for reprompt
   *                        generation.
   * @param currentResponse The current response. Has attachements, quick replies
   *                        etc that were generated during processing.
   * @return A CurrentResponse containing the payload to return to the user.
   */
  CurrentResponse generateReprompt(Session session, Context context, String messageText, IntentMatch intentMatch,
      CurrentResponse currentResponse);
}
