/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core.bot;

import java.util.List;
import java.util.Objects;

import com.rabidgremlin.mutters.core.Intent;
import com.rabidgremlin.mutters.core.MatcherScores;

/**
 * A response from an IntentBot. Extends BotResponse to include the matched
 * intent and the scores of the matching.
 * 
 * @author rabidgremlin
 *
 */
public class IntentBotResponse extends BotResponse
{
  /** The matched intent. */
  private final Intent matchedIntent;

  /** The scores generated by the intent matcher. */
  private final MatcherScores matchingScores;

  /**
   * Constructor
   * 
   * @param response       The response text.
   * @param hint           Any hint text for the client to display.
   * @param askResponse    True if this the bot is expecting a further response
   *                       from the user.
   * @param attachments    The attachments for the response.
   * @param quickReplies   List of suggested replies that the user could use.
   * @param matchedIntent  The matched intent.
   * @param matchingScores The scores generated by the intent matching.
   */
  public IntentBotResponse(String response, String hint, boolean askResponse, List<BotResponseAttachment> attachments,
      List<String> quickReplies, Intent matchedIntent, MatcherScores matchingScores)
  {
    super(response, hint, askResponse, attachments, quickReplies);
    this.matchedIntent = Objects.requireNonNull(matchedIntent);
    this.matchingScores = Objects.requireNonNull(matchingScores);
  }

  /**
   * Gets the matched intent.
   * 
   * @return The matched intent.
   */
  public Intent getMatchedIntent()
  {
    return matchedIntent;
  }

  /**
   * Gets the scores generated by the intent matcher.
   * 
   * @return The scores generated by the intent matcher.
   */
  public MatcherScores getMatchingScores()
  {
    return matchingScores;
  }

}
