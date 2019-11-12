/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.bot.ink.functions;

import com.bladecoder.ink.runtime.Story;
import com.rabidgremlin.mutters.bot.ink.CurrentResponse;
import com.rabidgremlin.mutters.bot.ink.InkBotFunction;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.session.Session;

/**
 * This class implements the SET_HINT Ink bot function. It is added by default
 * to the InkBot. The SET_HINT function allows the bot to pass hints to the
 * user's client about how the bot expects them to respond. The user's client
 * can then display the hint as an appropriate visual cue.
 * 
 * For example in the ink script you could have:
 * 
 * ``` Which date ? ::SET_HINT dd/mm or next friday ```
 * 
 * @author rabidgremlin
 *
 */
public class SetHintFunction implements InkBotFunction
{

  /*
   * (non-Javadoc)
   * 
   * @see com.rabidgremlin.mutters.bot.ink.InkBotFunction#getFunctionName()
   */
  @Override
  public String getFunctionName()
  {
    return "SET_HINT";
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.rabidgremlin.mutters.bot.ink.InkBotFunction#respondexecute(
   * CurrentResponse currentResponse, Session session, IntentMatch intentMatch,
   * Story story, String param)
   */
  @Override
  public void execute(CurrentResponse currentResponse, Session session, IntentMatch intentMatch, Story story,
      String param)
  {
    currentResponse.setHint(param);
  }

}
