/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.bot.ink.functions;

import com.bladecoder.ink.runtime.Story;
import com.rabidgremlin.mutters.bot.ink.CurrentResponse;
import com.rabidgremlin.mutters.bot.ink.InkBotFunction;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.session.Session;

/**
 * This function sets the value of a long term session attribute. These
 * attributes are not removed at the end of a conversation so they can be used
 * to share context between conversations in the same session.
 * 
 * For example in your Ink script you could have: ``` Your order {order_number}
 * has been placed! ::SET_LONG_TERM_ATTR name::currentorder
 * value::{order_number} ```
 * 
 * 
 * @author rabidgremlin
 *
 */
public class SetLongTermAttributeFunction implements InkBotFunction
{

  /*
   * (non-Javadoc)
   * 
   * @see com.rabidgremlin.mutters.bot.ink.InkBotFunction#getFunctionName()
   */
  @Override
  public String getFunctionName()
  {
    return "SET_LONG_TERM_ATTR";
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
      throw new IllegalArgumentException("Missing name and value values for SET_LONG_TERM_ATTR");
    }

    String name = details.getFunctionParams().get("name");
    if (name == null)
    {
      throw new IllegalArgumentException("Missing name value for SET_LONG_TERM_ATTR");
    }

    String value = details.getFunctionParams().get("value");
    if (value == null)
    {
      throw new IllegalArgumentException("Missing value value for SET_LONG_TERM_ATTR");
    }

    session.setLongTermAttribute(name, value);
  }

}
