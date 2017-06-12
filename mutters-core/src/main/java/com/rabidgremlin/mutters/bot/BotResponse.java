package com.rabidgremlin.mutters.bot;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class holds a response from a bot.
 * 
 * Apart from the response test the bot can also return hints, attachments (for rich responses) and debug values.
 * 
 * @author rabidgremlin
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

  /** The attachments for the response. */
  private List<BotResponseAttachment> attachments;

  /** List of suggested replies that the user could use. */
  private List<String> quickReplies;

  /** Map of debug values. Added by particular bot implementation. */
  private Map<String, Object> debugValues;

  /**
   * Constructor.
   * 
   * @param response The response text.
   * @param hint Any hint text for the client to display.
   * @param askResponse True if this the bot is expecting a further response from the user.
   * @param attachments The attachments for the response.
   * @param quickReplies List of suggested replies that the user could use.
   * @param debugValues Map of debug values. Specific to bot implementation.
   */
  public BotResponse(String response, String hint, boolean askResponse,
    List<BotResponseAttachment> attachments, List<String> quickReplies, Map<String, Object> debugValues)
  {
    this.response = response;
    this.hint = hint;
    this.askResponse = askResponse;
    this.attachments = attachments;
    this.quickReplies = quickReplies;
    this.debugValues = debugValues;
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
   * Returns the attachments for this response.
   * 
   * @return The attachments or null.
   */
  public List<BotResponseAttachment> getAttachments()
  {
    if (attachments == null)
    {
      return null;
    }

    return Collections.unmodifiableList(attachments);
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

  /**
   * Returns the debug values for this response.
   * 
   * @return The debug values or null.
   */
  public Map<String, Object> getDebugValues()
  {
    if (debugValues == null)
    {
      return null;
    }

    return Collections.unmodifiableMap(debugValues);
  }

  /**
   * Returns the quick replies for this response.
   * 
   * @return The quick replies or null.
   */
  public List<String> getQuickReplies()
  {
    if (quickReplies == null)
    {
      return null;
    }

    return Collections.unmodifiableList(quickReplies);
  }

}
