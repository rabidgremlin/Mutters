package com.rabidgremlin.mutters.bot.ink;

import java.util.HashMap;

import com.bladecoder.ink.runtime.Story;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.session.Session;

/**
 * This class implements the SET_ACTION Ink bot function. It is added by default to the AbstractInkBot. The SET_ACTION
 * function allows the bot to pass actions to the client that the user is using to talk to the bot with. It could be
 * used to pass button lists or URL opening requests or any other such actions.
 * 
 * For example in the ink script you could have:
 * 
 * ``` 
 * I'm opening a website with that information... 
 * :SET_ACTION OPEN_URL url:https:\/\/en.wikipedia.org/wiki/Chatbot
 * ```
 * 
 * This function uses the first word as the action name that is returned by the bot. The rest of the line is assumed to
 * be name-value pairs which are returned as action parameters by the bot.
 * 
 * @author rabidgremlin
 *
 */
public class SetActionFunction
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
    return "SET_ACTION";
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
    // HACK HACK doesn't handle spaces in name value pairs
    // OPEN_URL url:http:\/\/trackcab.example.com/t/{taxiNo}
    String trimmedLine = param.trim();
    String actionName = trimmedLine.split(" ")[0].substring(0).trim();
    String[] nameValues = trimmedLine.substring(actionName.length() + 1).trim().split(" ");

    currentResponse.setReponseAction(actionName);
    HashMap<String, Object> paramsMap = new HashMap<String, Object>();
    currentResponse.setResponseActionParams(paramsMap);

    for (String nameValue : nameValues)
    {
      String name = nameValue.split(":")[0].substring(0).trim();
      String value = nameValue.substring(name.length() + 1).trim();

      paramsMap.put(name, value);
    }
  }

}
