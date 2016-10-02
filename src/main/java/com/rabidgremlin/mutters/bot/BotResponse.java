package com.rabidgremlin.mutters.bot;

import java.util.Collections;
import java.util.Map;

/**
 * This class holds a response from a bot.
 * 
 * @author rabidgremlin
 *
 */
/**
 * @author ackej1
 *
 */
/**
 * @author ackej1
 *
 */
public class BotResponse
{
  /** The response text. */
  private String response;

  /** Any hint text for the client to display. */
  private String hint;

  /** True if this the bot is expecting a further response from the user. */
  private boolean askResponse;

  /** Any action that the client should take. eg opening a URL, displaying a button etc */
  private String action;

  /** The parameters for the action. */
  private Map<String, Object> actionParams;

  /**
   * Constructor.
   * 
   * @param response The response text.
   * @param hint Any hint text for the client to display.
   * @param askResponse True if this the bot is expecting a further response from the user.
   * @param action Any action that the client should take. eg opening a URL, displaying a button etc
   * @param actionParams The parameters for the action.
   */
  public BotResponse(String response, String hint, boolean askResponse, String action, Map<String, Object> actionParams)
  {
    this.response = response;
    this.hint = hint;
    this.askResponse = askResponse;
    this.action = action;
    this.actionParams = actionParams;
  }

  /**
   * This returns the response text from the bot.
   * 
   * @return The response text.
   */
  public String getResponse()
  {
    return response;
  }

  /**
   * This returns true if the bot is expecting a further response from the user. Allows a client to adjust its UI
   * appropriately.
   * 
   * @return True if a further response is expected.
   */
  public boolean isAskResponse()
  {
    return askResponse;
  }

  /**
   * Returns an action for the client to perform. This is any unique string for example OPEN_URL.
   * 
   * @return The action for the client or null if no action.
   */
  public String getAction()
  {
    return action;
  }

  /**
   * Returns the parameters for the action.
   * 
   * @return The action parameters or null.
   */
  public Map<String, Object> getActionParams()
  {
    if (actionParams == null)
    {
      return null;
    }

    return Collections.unmodifiableMap(actionParams);
  }

  /**
   * Returns the current hint for the expected response from the user. Can be used by a client to provide a visual
   * prompt.
   * 
   * @return The hint or null if no hint.
   */
  public String getHint()
  {
    return hint;
  }

}
