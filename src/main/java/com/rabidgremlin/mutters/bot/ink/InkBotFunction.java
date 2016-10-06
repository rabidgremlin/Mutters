package com.rabidgremlin.mutters.bot.ink;

import com.bladecoder.ink.runtime.Story;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.session.Session;

/**
 * Interface to be implemented by any functions added to the bot. Note these are not true ink functions but rather
 * triggered by a line of response text starting with a :
 * 
 * @see AbstractInkBot#addFunction
 * @author rabidgremlin
 *
 */
public interface InkBotFunction
{
  /**
   * Returns the name of the function. Should be uppercase and NOT include the leading :
   * 
   * @return The name of the function.
   */
  String getFunctionName();

  /**
   * Called when a function is found in the ink story response.
   * 
   * @param currentResponse The current state of the response.
   * @param session The current user's session.
   * @param intentMatch The intent that was matched.
   * @param story The current ink story.
   * @param param The param that was passed to the function. This is all the text after the function identifier.
   */
  void execute(CurrentResponse currentResponse, Session session, IntentMatch intentMatch, Story story, String param);

}
