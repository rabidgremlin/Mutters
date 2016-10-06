package com.rabidgremlin.mutters.bot.ink;

import java.util.HashMap;

import com.bladecoder.ink.runtime.Story;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.session.Session;

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
  public void execute(CurrentResponse currentResponse, Session session, IntentMatch intentMatch, Story story, String param)
  {
    // HACK HACK doesn't handle spaces in name value pairs
    // OPEN_URL url:http:\/\/trackcab.example.com/t/{taxiNo}
    String trimmedLine = param.trim();
    String actionName = trimmedLine.split(" ")[0].substring(0).trim();
    String[] nameValues = trimmedLine.substring(actionName.length() + 1).trim().split(" ");

    currentResponse.reponseAction = actionName;
    currentResponse.responseActionParams = new HashMap<String, Object>();

    for (String nameValue : nameValues)
    {
      String name = nameValue.split(":")[0].substring(0).trim();
      String value = nameValue.substring(name.length() + 1).trim();

      currentResponse.responseActionParams.put(name, value);
    }
  }

}
