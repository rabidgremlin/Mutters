package com.rabidgremlin.mutters.bot.ink.functions;

import com.bladecoder.ink.runtime.Story;
import com.rabidgremlin.mutters.bot.ink.CurrentResponse;
import com.rabidgremlin.mutters.bot.ink.InkBotFunction;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.session.Session;

/**
 * This class implements the ADD_QUICK_REPLY Ink bot function. It is added by default to the InkBot. The ADD_QUICK_REPLY
 * function allows the bot to pass a list of quick reply options back to the client. Depending on the chat interface these
 * options can be displayed as quick menu options so that a user does not have to enter a response manually.
 * 
 * Multiple quick reply functions can be called to build up a list of quick replies. 
 * 
 * ``` 
 * I can help you manage your taxi service. Try say something like 'Order me a cab' or 'Where is my taxi'. 
 * ::ADD_QUICK_REPLY Order me a cab
 * ::ADD_QUICK_REPLY Where is my taxi
 * ```
 * 
 * 
 * @author rabidgremlin
 *
 */
public class AddQuickReplyFunction
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
    return "ADD_QUICK_REPLY";
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
    currentResponse.addResponseQuickReply(param);
  }

}
