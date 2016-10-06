package com.rabidgremlin.mutters.bot.ink;

import java.util.Map;

/**
 * Simple class to hold data for current response.
 * 
 * @author rabidgremlin
 *
 */
public class CurrentResponse
{
  private String responseText;

  private String hint;

  private String reprompt = null;

  private String reponseAction = null;

  private Map<String, Object> responseActionParams = null;

  private boolean askResponse = true;

  @Override
  public String toString()
  {
    return "CurrentResponse [responseText=" + responseText + ", hint=" + hint + ", reprompt="
        + reprompt + ", reponseAction=" + reponseAction
        + ", responseActionParams=" + responseActionParams
        + ", askResponse=" + askResponse + "]";
  }

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

  public String getReponseAction()
  {
    return reponseAction;
  }

  public void setReponseAction(String reponseAction)
  {
    this.reponseAction = reponseAction;
  }

  public Map<String, Object> getResponseActionParams()
  {
    return responseActionParams;
  }

  public void setResponseActionParams(Map<String, Object> responseActionParams)
  {
    this.responseActionParams = responseActionParams;
  }

  public boolean isAskResponse()
  {
    return askResponse;
  }

  public void setAskResponse(boolean askResponse)
  {
    this.askResponse = askResponse;
  }

}
