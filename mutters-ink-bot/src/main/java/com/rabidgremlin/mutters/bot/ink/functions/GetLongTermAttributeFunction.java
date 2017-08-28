package com.rabidgremlin.mutters.bot.ink.functions;

import com.bladecoder.ink.runtime.Story;
import com.rabidgremlin.mutters.bot.ink.CurrentResponse;
import com.rabidgremlin.mutters.bot.ink.InkBotFunction;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.session.Session;

/**
 * This function gets the value of a long term session attribute. These attributes are not removed at the end of a
 * conversation so they can be used to share context between conversations in the same session.
 * 
 * For example in your Ink script you could have:
 * ```
 * VAR current_order = ""
 * 
 * === check_order_status ===
 * ::GET_LONG_TERM_ATTR name::currentorder var::current_order
 * {
 *   - current_order == "":
 *     -> get_order_number_for_status_check // this would prompt for order number and store in current_order then jump to check_order_status
 *   - else:
 *     -> check_order_status  // retrieves and displays status for order number in current_order
 * }
 * -> END
 * ``` 
 * 
 * Note: If there is no long term attribute with the specified name in the session then the specified Ink variable
 * will be set to "".  
 * 
 * 
 * @author rabidgremlin
 *
 */
public class GetLongTermAttributeFunction
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
    return "GET_LONG_TERM_ATTR";
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
    FunctionDetails details = FunctionHelper.parseFunctionString(param);

    if (details.getFunctionParams() == null)
    {
      throw new IllegalArgumentException("Missing name and variable value for GET_LONG_TERM_ATTR");
    }

    String name = details.getFunctionParams().get("name");
    if (name == null)
    {
      throw new IllegalArgumentException("Missing name value for GET_LONG_TERM_ATTR");
    }

    String var = details.getFunctionParams().get("var");
    if (var == null)
    {
      throw new IllegalArgumentException("Missing var value for GET_LONG_TERM_ATTR");
    }
    
    try
    {
      Object value =  session.getLongTermAttribute(name);
      if (value == null)
      {
        story.getVariablesState().set(var, "");
      }
      else
      {
        story.getVariablesState().set(var, value);
      }
    }
    catch(Exception e)
    {
      throw new RuntimeException("Failed to get long term attribute",e);  
    }    
  }

}
