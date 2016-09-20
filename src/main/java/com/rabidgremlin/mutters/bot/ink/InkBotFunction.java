package com.rabidgremlin.mutters.bot.ink;

import com.bladecoder.ink.runtime.Story;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.session.Session;

public interface InkBotFunction
{
	void execute(CurrentResponse currentResponse, Session session, IntentMatch intentMatch, Story story, String param);
}
