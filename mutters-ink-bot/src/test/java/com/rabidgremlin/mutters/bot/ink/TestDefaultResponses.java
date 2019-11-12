/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.bot.ink;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.core.bot.BotResponse;
import com.rabidgremlin.mutters.core.session.Session;
import com.rabidgremlin.mutters.templated.SimpleTokenizer;
import com.rabidgremlin.mutters.templated.TemplatedIntentMatcher;

/**
 * Test class for default response.
 * 
 * @author rabidgremlin
 *
 */
public class TestDefaultResponses
{
  private InkBot<BotWithDefaultDefaultResponses> testBotWithDefaultDefaultPhrases;
  private InkBot<BotWithDefaultDefaultResponses> testBotWithTestDefaultPhrases;

  static class BotWithDefaultDefaultResponses implements InkBotConfiguration
  {

    @Override
    public String getStoryJson()
    {
      // use taxi but ink but no intents so all good
      return StoryUtils.loadStoryJsonFromClassPath("taxibot.ink.json");
    }

    @Override
    public IntentMatcher getIntentMatcher()
    {
      // return empty matcher
      return new TemplatedIntentMatcher(new SimpleTokenizer());
    }

    @Override
    public List<InkBotFunction> getInkFunctions()
    {
      // no functions
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
      return new DefaultResponseRepromptGenerator();
    }
  }

  static class BotWithTestDefaultResponses extends BotWithDefaultDefaultResponses
  {
    @Override
    public RepromptGenerator getRepromptGenerator()
    {
      return new DefaultResponseRepromptGenerator(new String[] { "Response A", "Response B", "Response C" });
    }
  }

  static class TestBot extends InkBot<BotWithDefaultDefaultResponses>
  {
    public TestBot(BotWithDefaultDefaultResponses configuration)
    {
      super(configuration);
    }
  }

  @Before
  public void setUpBot()
  {
    testBotWithDefaultDefaultPhrases = new TestBot(new BotWithDefaultDefaultResponses());
    testBotWithTestDefaultPhrases = new TestBot(new BotWithTestDefaultResponses());
  }

  @Test
  public void testDefaultDefaultResponse() throws Exception
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = testBotWithDefaultDefaultPhrases.respond(session, context, "Why is the sky blue ?");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Pardon?"));
    assertThat(response.isAskResponse(), is(true));
  }

  @Test
  public void testCustomDefaultResponse() throws Exception
  {
    Session session = new Session();
    Context context = new Context();

    int responseACount = 0;
    int responseBCount = 0;
    int responseCCount = 0;

    for (int loop = 0; loop < 50; loop++)
    {
      BotResponse response = testBotWithTestDefaultPhrases.respond(session, context, "Why is the sky blue ?");

      assertThat(response, is(notNullValue()));
      assertThat(response.isAskResponse(), is(true));

      if (response.getResponse().equals("Response A"))
      {
        responseACount++;
      }

      if (response.getResponse().equals("Response B"))
      {
        responseBCount++;
      }

      if (response.getResponse().equals("Response C"))
      {
        responseCCount++;
      }
    }

    assertTrue("Did not get any Response As", responseACount > 0);
    assertTrue("Did not get any Response Bs", responseBCount > 0);
    assertTrue("Did not get any Response Cs", responseCCount > 0);
  }

}
