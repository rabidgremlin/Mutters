/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core.bot;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.session.Session;

/**
 * This is the interface implemented by bots that use intent matching as their
 * core processing logic.
 * 
 * @author rabidgremlin
 *
 */
public interface IntentBot extends Bot
{
  /** See {@link Bot#respond(Session, Context, String)} */
  @Override
  IntentBotResponse respond(Session session, Context context, String messageText) throws BotException;
}
