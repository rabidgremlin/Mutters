/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.state;

import java.util.Collections;
import java.util.List;

import com.rabidgremlin.mutters.core.bot.BotResponseAttachment;

public class IntentResponse
{

  private final boolean sessionEnded;

  private final String response;

  private final String reprompt;

  private final String hint;

  private final List<BotResponseAttachment> attachments;

  private final List<String> quickReplies;

  public static IntentResponse newAskResponse(String response)
  {
    return new IntentResponse(false, response, null, null, null, null);
  }

  public static IntentResponse newAskResponse(String response, String reprompt)
  {
    return new IntentResponse(false, response, reprompt, null, null, null);
  }

  public static IntentResponse newAskResponse(String response, String reprompt, String hint)
  {
    return new IntentResponse(false, response, reprompt, hint, null, null);
  }

  public static IntentResponse newAskResponse(String response, List<BotResponseAttachment> attachments)
  {
    return new IntentResponse(false, response, null, null, attachments, null);
  }

  public static IntentResponse newAskResponse(String response, String reprompt, String hint,
      List<BotResponseAttachment> attachments)
  {
    return new IntentResponse(false, response, null, null, attachments, null);
  }

  public static IntentResponse newTellResponse(String response)
  {
    return new IntentResponse(true, response, null, null, null, null);
  }

  public static IntentResponse newTellResponse(String response, List<BotResponseAttachment> attachments)
  {
    return new IntentResponse(true, response, null, null, attachments, null);
  }

  public IntentResponse(boolean sessionEnded, String response, String reprompt, String hint,
      List<BotResponseAttachment> attachments, List<String> quickReplies)
  {
    this.sessionEnded = sessionEnded;
    this.response = response;
    this.reprompt = reprompt;
    this.hint = hint;
    this.attachments = attachments;
    this.quickReplies = quickReplies;
  }

  public boolean isSessionEnded()
  {
    return sessionEnded;
  }

  public String getResponse()
  {
    return response;
  }

  public List<BotResponseAttachment> getAttachments()
  {
    if (attachments == null)
    {
      return null;
    }

    return Collections.unmodifiableList(attachments);
  }

  public String getReprompt()
  {
    return reprompt;
  }

  public String getHint()
  {
    return hint;
  }

  public List<String> getQuickReplies()
  {
    if (quickReplies == null)
    {
      return null;
    }

    return Collections.unmodifiableList(quickReplies);
  }

}
