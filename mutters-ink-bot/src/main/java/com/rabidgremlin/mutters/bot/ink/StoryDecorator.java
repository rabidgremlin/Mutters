/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.bot.ink;

import com.bladecoder.ink.runtime.Story;

/**
 * This is a wrapper class that overrides the {@link #bindExternalFunction}
 * method so that we can bind external functions without unbinding them first.
 * 
 * @author Joe Dai
 */
public class StoryDecorator extends Story
{

  public StoryDecorator(String jsonString) throws Exception
  {
    super(jsonString);
  }

  /**
   * Binds the given external function. The function with the same name will be
   * unbound first.
   */
  @Override
  public void bindExternalFunction(String funcName, ExternalFunction<?> func) throws Exception
  {
    try
    {
      super.unbindExternalFunction(funcName);
    }
    catch (Exception e)
    {
      if (!e.getMessage().contains("has not been bound"))
      {
        // ignore the "not bound" precondition, only rethrow if the function was bound
        throw e;
      }
    }
    super.bindExternalFunction(funcName, func);
  }
}
