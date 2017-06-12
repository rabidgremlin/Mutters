package com.rabidgremlin.mutters.bot.ink.functions;

import com.bladecoder.ink.runtime.Story;
import com.rabidgremlin.mutters.bot.ink.CurrentResponse;
import com.rabidgremlin.mutters.bot.ink.InkBotFunction;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.session.Session;

/**
 * This class implements the SET_REPROMPT Ink bot function. It is added by default to the AbstractInkBot. The
 * SET_REPROMPT function allows an ink script writer to define a reprompt with more information or different phrasing
 * from the original. The bot will use this reprompt text rather the its default response if it is unable to understand
 * what the user has said.
 * 
 * For example in the ink script you could have:
 * 
 * ``` 
 * Which date ? 
 * ::SET_REPROMPT On which date would you like the delivery to take place? 
 * ```
 * 
 * @author rabidgremlin
 *
 */
public class SetRepromptFunction
    implements InkBotFunction
{

  /*
   * (non-Javadoc)
   * 
   * @see com.rabidgremlin.mutters.bot.ink.InkBotFunction#getFunctionName()
   */
  @Override
  public String getFunctionName()
  {
    return "SET_REPROMPT";
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.rabidgremlin.mutters.bot.ink.InkBotFunction#respondexecute(CurrentResponse currentResponse, Session
   * session, IntentMatch intentMatch, Story story, String param)
   */
  @Override
  public void execute(CurrentResponse currentResponse, Session session, IntentMatch intentMatch,
    Story story, String param)
  {
    currentResponse.setReprompt(param);
  }

}
