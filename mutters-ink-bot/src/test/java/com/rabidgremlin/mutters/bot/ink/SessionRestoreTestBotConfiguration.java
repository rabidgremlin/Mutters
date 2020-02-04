/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.bot.ink;

import java.util.List;

import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.templated.SimpleTokenizer;
import com.rabidgremlin.mutters.templated.TemplatedIntent;
import com.rabidgremlin.mutters.templated.TemplatedIntentMatcher;

public class SessionRestoreTestBotConfiguration implements InkBotConfiguration
{
  private final String inkJsonFileName;

  public SessionRestoreTestBotConfiguration(String inkJsonFileName)
  {
    this.inkJsonFileName = inkJsonFileName;
  }

  @Override
  public IntentMatcher getIntentMatcher()
  {
    SimpleTokenizer tokenizer = new SimpleTokenizer();

    TemplatedIntentMatcher matcher = new TemplatedIntentMatcher(tokenizer);

    TemplatedIntent oneIntent = matcher.addIntent("OneIntent");
    oneIntent.addUtterance("one");

    TemplatedIntent twoIntent = matcher.addIntent("TwoIntent");
    twoIntent.addUtterance("two");

    TemplatedIntent threeIntent = matcher.addIntent("ThreeIntent");
    threeIntent.addUtterance("three");

    TemplatedIntent newIntent = matcher.addIntent("NewIntent");
    newIntent.addUtterance("new");

    return matcher;
  }

  @Override
  public String getStoryJson()
  {
    return StoryUtils.loadStoryJsonFromClassPath(inkJsonFileName);
  }

  @Override
  public List<InkBotFunction> getInkFunctions()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<GlobalIntent> getGlobalIntents()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ConfusedKnot getConfusedKnot()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public RepromptGenerator getRepromptGenerator()
  {
    return new DefaultResponseRepromptGenerator();
  }

}
