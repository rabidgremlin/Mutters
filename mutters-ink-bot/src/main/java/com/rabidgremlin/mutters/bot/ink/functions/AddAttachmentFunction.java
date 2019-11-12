/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.bot.ink.functions;

import com.bladecoder.ink.runtime.Story;
import com.rabidgremlin.mutters.bot.ink.CurrentResponse;
import com.rabidgremlin.mutters.bot.ink.InkBotFunction;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.bot.BotResponseAttachment;
import com.rabidgremlin.mutters.core.session.Session;

/**
 * This class implements the ADD_ATTACHMENT Ink bot function. It is added by
 * default to the InkBot. The ADD_ATTACHMENT function allows the bot to pass
 * attachments to the client that the user is using to talk to the bot with. It
 * could be used to pass hyper-links, images or button lists.
 * 
 * For example in the ink script you could have the following hyperlink
 * attachment that might get rendered as a button:
 * 
 * ``` I've found a website with that information... ::ADD_ATTACHMENT type::link
 * url::https:\/\/en.wikipedia.org/wiki/Chatbot title::Here is the link ```
 * 
 * The type parameter is a required parameter. Other parameters are parsed and
 * pass along.
 * 
 * @author rabidgremlin
 *
 */
public class AddAttachmentFunction implements InkBotFunction
{

  /*
   * (non-Javadoc)
   * 
   * @see com.rabidgremlin.mutters.bot.ink.InkBotFunction#getFunctionName()
   */
  @Override
  public String getFunctionName()
  {
    return "ADD_ATTACHMENT";
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

    if (details.getFunctionParams() == null || details.getFunctionParams().get("type") == null)
    {
      throw new IllegalArgumentException("Missing type value for ADD_ATTACHMENT function.");
    }

    BotResponseAttachment attachment = new BotResponseAttachment(details.getFunctionParams().get("type"));

    for (String key : details.getFunctionParams().keySet())
    {
      if (!key.equals("type"))
      {
        attachment.addParameters(key, details.getFunctionParams().get(key));
      }
    }

    currentResponse.addResponseAttachement(attachment);
  }

}
