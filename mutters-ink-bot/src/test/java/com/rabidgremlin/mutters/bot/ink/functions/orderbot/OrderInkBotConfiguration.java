/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.bot.ink.functions.orderbot;

import java.util.List;

import com.rabidgremlin.mutters.bot.ink.DefaultResponseRepromptGenerator;
import com.rabidgremlin.mutters.bot.ink.InkBotConfiguration;
import com.rabidgremlin.mutters.bot.ink.InkBotFunction;
import com.rabidgremlin.mutters.bot.ink.RepromptGenerator;
import com.rabidgremlin.mutters.bot.ink.StoryUtils;
import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.templated.SimpleTokenizer;
import com.rabidgremlin.mutters.templated.TemplatedIntent;
import com.rabidgremlin.mutters.templated.TemplatedIntentMatcher;

public class OrderInkBotConfiguration implements InkBotConfiguration
{

  @Override
  public IntentMatcher getIntentMatcher()
  {
    SimpleTokenizer tokenizer = new SimpleTokenizer();

    TemplatedIntentMatcher matcher = new TemplatedIntentMatcher(tokenizer);

    TemplatedIntent createOrderIntent = matcher.addIntent("CreateOrderIntent");
    createOrderIntent.addUtterance("Order a widget");

    TemplatedIntent checkStatusIntent = matcher.addIntent("CheckStatusIntent");
    checkStatusIntent.addUtterance("What is the status of my order");

    TemplatedIntent getPriceIntent = matcher.addIntent("GetPriceIntent");
    getPriceIntent.addUtterance("Can you give me a price");

    TemplatedIntent yesIntent = matcher.addIntent("YesIntent");
    yesIntent.addUtterance("Yes");

    TemplatedIntent noIntent = matcher.addIntent("NoIntent");
    noIntent.addUtterance("No");

    return matcher;
  }

  @Override
  public String getStoryJson()
  {
    return StoryUtils.loadStoryJsonFromClassPath("orderbot.ink.json");
  }

  @Override
  public List<InkBotFunction> getInkFunctions()
  {
    return null;
  }

  @Override
  public List<GlobalIntent> getGlobalIntents()
  {
    return null;
  }

  @Override
  public ConfusedKnot getConfusedKnot()
  {
    return null;
  }

  @Override
  public RepromptGenerator getRepromptGenerator()
  {
    return new DefaultResponseRepromptGenerator(new String[] { "Sorry I didn't catch that." });
  }

}
