/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.bot.ink;

import java.util.List;

import com.rabidgremlin.mutters.core.IntentMatcher;

/**
 * This interface must be implemented by configuration objects passed to a
 * InkBot. The class provides all the configuration needed by the bot.
 * 
 * @author rabidgremlin.
 *
 */
public interface InkBotConfiguration
{
  /**
   * This method should create and populate an IntentMatcher. This IntentMatcher
   * will be used by the bot to determine what a user said to the bot. The names
   * of the Intents returned by the IntentMatcher should match choices in the Ink
   * story for the bot to work.
   * 
   * @return The IntentMatcher for the bot to use.
   */
  public IntentMatcher getIntentMatcher();

  /**
   * This method should load and return the compiled Ink story in JSON format.
   * This is the Ink story that the bot will use to figure out how to respond to a
   * user's message.
   * 
   * @return The compiled story as a JSON string.
   */
  public String getStoryJson();

  /**
   * This method should set up and return a list of InkBotFunctions for the bot.
   * 
   * @return The list of functions for the bot or null if there are no functions.
   */
  public List<InkBotFunction> getInkFunctions();

  /**
   * This method should return a list of global intents for the bot. These intents
   * are matched on before any handled by the bot's IntentMatcher. Useful for
   * implementing global commands such as 'help'.
   * 
   * @return The list of global intents for the bot or null if there are no global
   *         intents.
   */
  public List<GlobalIntent> getGlobalIntents();

  /**
   * This method should returned the confused knot for the bot. If this is
   * configured then the bot will jump to this ink not after it fails to match on
   * a user's utterance n times.
   * 
   * @return The confused knot info or null if the bot does not have one.
   */
  public ConfusedKnot getConfusedKnot();

  /**
   * Holder class for global intents.
   * 
   * @author rabidgremlin
   *
   */
  public class GlobalIntent
  {
    private final String intentName;

    private final String knotName;

    public GlobalIntent(String intentName, String knotName)
    {

      this.intentName = intentName;
      this.knotName = knotName;
    }

    public String getIntentName()
    {
      return intentName;
    }

    public String getKnotName()
    {
      return knotName;
    }
  }

  /**
   * Holder class for confused knot info.
   * 
   * @author rabidgremlin
   *
   */
  public class ConfusedKnot
  {
    private final int maxAttemptsBeforeConfused;

    private final String confusedKnotName;

    public ConfusedKnot(int maxAttemptsBeforeConfused, String confusedKnotName)
    {

      this.maxAttemptsBeforeConfused = maxAttemptsBeforeConfused;
      this.confusedKnotName = confusedKnotName;
    }

    public int getMaxAttemptsBeforeConfused()
    {
      return maxAttemptsBeforeConfused;
    }

    public String getConfusedKnotName()
    {
      return confusedKnotName;
    }

  }

  /**
   * This should return a reprompt generator that will be used by the bot to
   * generate a reprompt if it doesn't understand what the user said. This is only
   * used if no reprompt has been defined by a SET_REPROMPT ink function.
   * 
   * @return The reprompt generator for the bot.
   */
  public RepromptGenerator getRepromptGenerator();
}
