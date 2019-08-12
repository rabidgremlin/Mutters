package com.rabidgremlin.mutters.bot.ink;

import java.util.List;

import com.rabidgremlin.mutters.core.IntentMatcher;

/**
 * This interface must be implemented by configuration objects passed to a InkBot. The class provides all the
 * configuration needed by the bot.
 * 
 * @author rabidgremlin.
 *
 */
public interface InkBotConfiguration
{
  /**
   * This method should create and populate an IntentMatcher. This IntentMatcher will be used by the bot to determine
   * what a user said to the bot. The names of the Intents returned by the IntentMatcher should match choices in the Ink
   * story for the bot to work.
   * 
   * @return The IntentMatcher for the bot to use.
   */
  public IntentMatcher getIntentMatcher();

  /**
   * This method should load and return the compiled Ink story in JSON format. This is the Ink story that the bot will
   * use to figure out how to respond to a user's message.
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
   * This method should return a list of global intents for the bot. These intents are matched on before any handled by
   * the bot's IntentMatcher. Useful for implementing global commands such as 'help'.
   * 
   * @return The list of global intents for the bot or null if there are no global intents.
   */
  public List<GlobalIntent> getGlobalIntents();

  /**
   * This method should returned the confused knot for the bot. If this is configured then the bot will jump to this ink
   * not after it fails to match on a user's utterance n times.
   * 
   * @return The confused knot info or null if the bot does not have one.
   */
  public ConfusedKnot getConfusedKnot();

  /**
   * This method returns a list of default phrases for the bot to say when it fails to understand a user's utterance. A
   * random default phrase will be picked. A default phrase will only be used if there is no current reprompt.
   * 
   * @return The list of default phrases or null if the bot doesn't have any custom default phrases.
   */
  public List<String> getDefaultResponses();

  /**
   * Holder class for global intents.
   * 
   * @author rabidgremlin
   *
   */
  public class GlobalIntent
  {
    private String intentName;

    private String knotName;

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
    private int maxAttemptsBeforeConfused;

    private String confusedKnotName;

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

}
