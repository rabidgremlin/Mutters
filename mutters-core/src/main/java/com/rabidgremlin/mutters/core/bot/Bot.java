package com.rabidgremlin.mutters.core.bot;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.session.Session;

/**
 * Interface implemented by all bots. 
 * 
 * Note: bots should be designed to be thread-safe.
 * 
 * @author rabidgremlin
 *
 */
public interface Bot
{
  /**
   * Processes a message from a user and returns a response.
   * 
   * @param session The current session for the user.
   * @param context The current context for the user.
   * @param messageText The message text from the user.
   * @throws BotException Thrown if the bot has had a failure during processing.
   * @return The response from the bot.
   */
  BotResponse respond(Session session, Context context, String messageText)
    throws BotException;

}
