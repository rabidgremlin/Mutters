/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.bot.ink;

import java.util.List;
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
  private String[] defaultResponses;

  /** Random for default reponses. */
  private Random rand = new Random();

  /**
   * Creates the DefaultResponseRepromptGenerator with a generic "Pardon?" phrase.
   * 
   */
  public DefaultResponseRepromptGenerator()
  {
    this.defaultResponses = new String[] { "Pardon?" };
  }

  /**
   * Creates the DefaultResponseRepromptGenerator with the supplied list of
   * phrases.
   * 
   * @param defaultResponses The list of phrases to use as reprompts.
   */
  public DefaultResponseRepromptGenerator(String[] defaultResponses)
  {
    this.defaultResponses = defaultResponses.clone();
  }

  /**
   * Creates the DefaultResponseRepromptGenerator with the supplied list of
   * phrases.
   * 
   * @param defaultResponses The list of phrases to use as reprompts.
   */
  public DefaultResponseRepromptGenerator(List<String> defaultResponses)
  {
    this.defaultResponses = defaultResponses.toArray(new String[0]);
  }

  @Override
  public CurrentResponse generateReprompt(Session session, Context context, String messageText, IntentMatch intentMatch,
      CurrentResponse currentResponse)
  {
    // choose a default response
    String defaultResponse = defaultResponses[rand.nextInt(defaultResponses.length)];

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
