package com.rabidgremlin.mutters.bot.ink;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.rabidgremlin.mutters.bot.BotResponse;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.session.Session;
import com.rabidgremlin.mutters.templated.TemplatedIntentMatcher;

/**
 * Test class for default response.
 * 
 * @author rabidgremlin
 *
 */
public class TestDefaultResponses
{
  private AbstractInkBot testBot;

  @Before
  public void setUpBot()
  {
    testBot = new AbstractInkBot()
    {
      @Override
      public IntentMatcher setUpIntents()
      {
        // return empty matcher
        return new TemplatedIntentMatcher();
      }

      @Override
      public void setUpFunctions()
      {
        // no functions
      }

      @Override
      public String getStoryJson()
      {
        // use taxi but ink but no intents so all good
        return loadStoryJsonFromClassPath("taxibot.ink.json");
      }
    };
  }

  @Test
  public void testDefaultDefaultResponse()
    throws Exception
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = testBot.respond(session, context, "Why is the sky blue ?");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Pardon?"));
    assertThat(response.isAskResponse(), is(true));
  }

  @Test
  public void testCustomDefaultResponse()
    throws Exception
  {
    Session session = new Session();
    Context context = new Context();

    testBot.setDefaultResponses(new String[]{ "Response A", "Response B", "Response C" });

    int responseACount = 0;
    int responseBCount = 0;
    int responseCCount = 0;

    for (int loop = 0; loop < 50; loop++)
    {
      BotResponse response = testBot.respond(session, context, "Why is the sky blue ?");

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
