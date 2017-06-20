package com.rabidgremlin.mutters.bot.ink;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.bot.BotException;
import com.rabidgremlin.mutters.core.bot.BotResponse;
import com.rabidgremlin.mutters.core.session.Session;
import com.rabidgremlin.mutters.core.util.SessionUtils;

public class TestBadStoryState
{
  private static TaxiInkBot taxiBot;

  // copied from SessionUtils logic
  private static final String INK_STORY_STATE_KEY = SessionUtils.SLOT_PREFIX + "0987654321STORYSTATE1234567890";

  @BeforeClass
  public static void setUpBot()
  {
    taxiBot = new TaxiInkBot(new TaxiInkBotConfiguration());
  }

  @Test
  public void testCorruptStoryState()
    throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    // do a call that results in state
    BotResponse response = taxiBot.respond(session, context, "Order me a taxi");

    String stateJson = (String) session.getAttribute(INK_STORY_STATE_KEY);
    assertThat(stateJson, is(notNullValue()));

    // corrupt the state
    session.setAttribute(INK_STORY_STATE_KEY, "{junk}");

    try
    {
      // this call should fail
      response = taxiBot.respond(session, context, "136 River Road");
      fail("Exception should have been thrown");
    }
    catch (BotException e)
    {
      // check that cause was BadInkStoryState
      if (!e.getCause().getClass().equals(BadInkStoryState.class))
      {
        fail("BadInkStoryState exception expected");
      }
    }

  }
}
