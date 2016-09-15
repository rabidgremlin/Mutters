package com.rabidgremlin.mutters.bot.ink;

import com.rabidgremlin.mutters.bot.Bot;
import com.rabidgremlin.mutters.bot.BotResponse;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.session.Session;

public abstract class AbstractInkBot implements Bot
{

	@Override
	public BotResponse respond(Session session, Context context, String messageText)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public abstract IntentMatcher setUpIntents();
	public abstract String getStoryJson();

}
