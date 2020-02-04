/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.bot.ink;

import java.util.ArrayList;
import java.util.List;

import com.rabidgremlin.mutters.core.bot.BotResponseAttachment;

/**
 * Simple class to hold data for current response.
 * 
 * @author rabidgremlin
 *
 */
public class CurrentResponse
{
  /** The text of the response. */
  private String responseText;

  /** The hint for the response. */
  private String hint;

  /** The reprompt for the response. */
  private String reprompt = null;

  /** The attachments for the response. */
  private List<BotResponseAttachment> responseAttachments;

  /** List of suggested replies that the user could use. */
  private List<String> responseQuickReplies;

  private boolean askResponse = true;

  public String getResponseText()
  {
    return responseText;
  }

  public void setResponseText(String responseText)
  {
    this.responseText = responseText;
  }

  public String getHint()
  {
    return hint;
  }

  public void setHint(String hint)
  {
    this.hint = hint;
  }

  public String getReprompt()
  {
    return reprompt;
  }

  public void setReprompt(String reprompt)
  {
    this.reprompt = reprompt;
  }

  public boolean isAskResponse()
  {
    return askResponse;
  }

  public void setAskResponse(boolean askResponse)
  {
    this.askResponse = askResponse;
  }

  public List<BotResponseAttachment> getResponseAttachments()
  {
    return responseAttachments;
  }

  public void setResponseAttachments(List<BotResponseAttachment> responseAttachments)
  {
    this.responseAttachments = responseAttachments;
  }

  public List<String> getResponseQuickReplies()
  {
    return responseQuickReplies;
  }

  public void setResponseQuickReplies(List<String> responseQuickReplies)
  {
    this.responseQuickReplies = responseQuickReplies;
  }

  public void addResponseAttachement(BotResponseAttachment attachment)
  {
    if (responseAttachments == null)
    {
      responseAttachments = new ArrayList<>();
    }

    responseAttachments.add(attachment);
  }

  public void addResponseQuickReply(String quickReply)
  {
    if (responseQuickReplies == null)
    {
      responseQuickReplies = new ArrayList<>();
    }

    responseQuickReplies.add(quickReply);
  }

  @Override
  public String toString()
  {
    return "CurrentResponse [responseText=" + responseText + ", hint=" + hint + ", reprompt=" + reprompt
        + ", responseAttachments=" + responseAttachments + ", responseQuickReplies=" + responseQuickReplies
        + ", askResponse=" + askResponse + "]";
  }

}
