package com.rabidgremlin.mutters.bot;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.session.Session;

public interface Bot
{

	BotResponse respond(Session session, Context context, String messageText);

}