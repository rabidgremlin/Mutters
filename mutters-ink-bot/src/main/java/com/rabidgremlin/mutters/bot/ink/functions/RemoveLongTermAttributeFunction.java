/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.bot.ink.functions;

import com.bladecoder.ink.runtime.Story;
import com.rabidgremlin.mutters.bot.ink.CurrentResponse;
import com.rabidgremlin.mutters.bot.ink.InkBotFunction;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.session.Session;

/**
 * This function removes the specified long term session attribute from the
 * session. These attributes are not removed at the end of a conversation so
 * they can be used to share context between conversations in the same session.
 * 
 * For example in your Ink script you could have: ``` VAR current_order = ""
 * 
 * === check_order_status === ::GET_LONG_TERM_ATTR name::currentorder
 * var::current_order For order {current_order} ? + YesIntent ->
 * display_order_details + NoIntent ::REMOVE_LONG_TERM_ATTR name::currentorder
 * -> get_order_number -> END ```
 * 
 * 
 * @author rabidgremlin
 *
 */
public class RemoveLongTermAttributeFunction implements InkBotFunction
{

  /*
   * (non-Javadoc)
   * 
   * @see com.rabidgremlin.mutters.bot.ink.InkBotFunction#getFunctionName()
   */
  @Override
  public String getFunctionName()
  {
    return "REMOVE_LONG_TERM_ATTR";
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
    FunctionDetails details = FunctionHelper.parseFunctionString(param);

    if (details.getFunctionParams() == null)
    {
      throw new IllegalArgumentException("Missing name and value values for REMOVE_LONG_TERM_ATTR");
    }

    String name = details.getFunctionParams().get("name");
    if (name == null)
    {
      throw new IllegalArgumentException("Missing name value for REMOVE_LONG_TERM_ATTR");
    }

    session.removeLongTermAttribute(name);
  }

}
