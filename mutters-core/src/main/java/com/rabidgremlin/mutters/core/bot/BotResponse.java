/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core.bot;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This class holds a response from a bot.
 * 
 * Apart from the response text the bot can also return hints, attachments (for
 * rich responses) and quick replies.
 * 
 * @author rabidgremlin
 *
 */
public class BotResponse
{
  /** The response text. */
  private final String response;

  /** Any hint text for the client to display. */
  private final String hint;

  /** True if this the bot is expecting a further response from the user. */
  private final boolean askResponse;

  /** The attachments for the response. */
  private final List<BotResponseAttachment> attachments;

  /** List of suggested replies that the user could use. */
  private final List<String> quickReplies;

  /**
   * Constructor.
   * 
   * @param response     The response text.
   * @param hint         Any hint text for the client to display.
   * @param askResponse  True if this the bot is expecting a further response from
   *                     the user.
   * @param attachments  The attachments for the response.
   * @param quickReplies List of suggested replies that the user could use.
   */
  public BotResponse(String response, String hint, boolean askResponse, List<BotResponseAttachment> attachments,
      List<String> quickReplies)
  {
    this.response = Objects.requireNonNull(response);
    this.hint = hint;
    this.askResponse = askResponse;
    this.attachments = attachments;
    this.quickReplies = quickReplies;
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
   * This returns true if the bot is expecting a further response from the user.
   * Allows a client to adjust its UI appropriately.
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
   * Returns the current hint for the expected response from the user. Can be used
   * by a client to provide a visual prompt.
   * 
   * @return The hint or null if no hint.
   */
  public String getHint()
  {
    return hint;
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
