package com.rabidgremlin.mutters.bot;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.session.Session;

/**
 * Interface implemented by all bots.
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
   * @return The response from the bot.
   */
  BotResponse respond(Session session, Context context, String messageText);

}
