package com.rabidgremlin.mutters.bot.statemachine;

import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.state.StateMachine;

/**
 * This interface must be implemented by configuration objects passed to a StateMachineBot. The class provides all the
 * configuration needed by the bot.
 * 
 * @author rabidgremlin.
 *
 */
public interface StateMachineBotConfiguration
{
  /**
   * This method should create and populate an IntentMatcher. This IntentMatcher will be used by the bot to determine
   * what a user said to the bot.
   * 
   * @return The IntentMatcher for the bot to use.
   */
  public IntentMatcher getIntentMatcher();

  /**
   * This method should create the StateMachine used by the bot to track the state of the user's conversation.
   * 
   * @return The StateMachine for the bot to use.
   */
  public StateMachine getStateMachine();

  /**
   * Sets the default response for the bot. This is the bot's response if it doesn't understand what was said.
   * 
   * return The default bot response.
   */
  public String getDefaultResponse();

}
