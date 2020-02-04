/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.bot.ink;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.session.Session;

/**
 * A ResponseGenerator that uses a list of reprompts to tell the user that the
 * bot didn't understand them. Will pick a random phrase from the supplied list.
 * If in the bot was in the middle of a conversation than the last query will be
 * appended to the phrase.
 * 
 * @author jack
 *
 */
public class DefaultResponseRepromptGenerator implements RepromptGenerator
{
  /** The list of phrases. */
  private final List<String> defaultResponses;

  /** Random for default responses. */
  private final Random rand = new Random();

  /**
   * Creates the DefaultResponseRepromptGenerator with a generic "Pardon?" phrase.
   */
  public DefaultResponseRepromptGenerator()
  {
    this("Pardon?");
  }

  /**
   * Creates the DefaultResponseRepromptGenerator with the supplied list of
   * phrases.
   * 
   * @param defaultResponses The list of phrases to use as reprompts.
   */
  public DefaultResponseRepromptGenerator(String... defaultResponses)
  {
    this(Arrays.asList(defaultResponses));
  }

  /**
   * Creates the DefaultResponseRepromptGenerator with the supplied list of
   * phrases.
   * 
   * @param defaultResponses The list of phrases to use as reprompts.
   */
  public DefaultResponseRepromptGenerator(List<String> defaultResponses)
  {
    this.defaultResponses = Objects.requireNonNull(defaultResponses);
  }

  @Override
  public CurrentResponse generateReprompt(Session session, Context context, String messageText, IntentMatch intentMatch,
      CurrentResponse currentResponse)
  {
    // choose a default response
    String defaultResponse = defaultResponses.get(rand.nextInt(defaultResponses.size()));

    // grab the last response we sent to the user
    String lastResponse = InkBotSessionUtils.getLastPrompt(session);

    // do we have a last response ? If so add it to default response to create
    // reprompt
    if (lastResponse != null)
    {
      currentResponse.setResponseText(defaultResponse + " " + lastResponse);
    }
    else
    {
      // no last response so just use default response
      currentResponse.setResponseText(defaultResponse);
    }

    return currentResponse;
  }
}
