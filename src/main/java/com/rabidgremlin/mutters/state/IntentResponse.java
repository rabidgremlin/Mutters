package com.rabidgremlin.mutters.state;

import java.util.Collections;
import java.util.Map;

public class IntentResponse
{

  private boolean sessionEnded;

  private String response;

  private String reprompt;

  private String hint;

  private String action;

  private Map<String, Object> actionParams;

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

  public static IntentResponse newAskResponse(String response, String action,
    Map<String, Object> actionParams)
  {
    return new IntentResponse(false, response, null, null, action, actionParams);
  }

  public static IntentResponse newAskResponse(String response, String reprompt, String hint,
    String action, Map<String, Object> actionParams)
  {
    return new IntentResponse(false, response, null, null, action, actionParams);
  }

  public static IntentResponse newTellResponse(String response)
  {
    return new IntentResponse(true, response, null, null, null, null);
  }

  public static IntentResponse newTellResponse(String response, String action,
    Map<String, Object> actionParams)
  {
    return new IntentResponse(true, response, null, null, action, actionParams);
  }

  public IntentResponse(boolean sessionEnded, String response, String reprompt, String hint,
    String action, Map<String, Object> actionParams)
  {

    this.sessionEnded = sessionEnded;
    this.response = response;
    this.reprompt = reprompt;
    this.hint = hint;
    this.action = action;
    this.actionParams = actionParams;
  }

  public boolean isSessionEnded()
  {
    return sessionEnded;
  }

  public String getResponse()
  {
    return response;
  }

  public String getAction()
  {
    return action;
  }

  public Map<String, Object> getActionParams()
  {
    if (actionParams == null)
    {
      return null;
    }

    return Collections.unmodifiableMap(actionParams);
  }

  public String getReprompt()
  {
    return reprompt;
  }

  public String getHint()
  {
    return hint;
  }

}
