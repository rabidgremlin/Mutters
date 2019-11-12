/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.bot.ink;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.bot.BotException;
import com.rabidgremlin.mutters.core.bot.BotResponse;
import com.rabidgremlin.mutters.core.session.Session;

public class TestConfusedBot
{
  private TaxiInkBot botWithConfusedKnot;
  private TaxiInkBot botWithoutConfusedKnot;
  private TaxiInkBot botWithConfusedKnotWithReprompts;

  static class TaxiBotWithConfusedKnotConfig extends TaxiInkBotConfiguration
  {
    @Override
    public ConfusedKnot getConfusedKnot()
    {
      return new ConfusedKnot(2, "confused_bot");
    }
  }

  static class TaxiBotWithConfusedKnotWithRepromptsConfig extends TaxiInkBotConfiguration
  {
    @Override
    public ConfusedKnot getConfusedKnot()
    {
      return new ConfusedKnot(2, "confused_bot_with_handover");
    }
  }

  static class TaxiBotWithoutConfusedKnotConfig extends TaxiInkBotConfiguration
  {
    @Override
    public ConfusedKnot getConfusedKnot()
    {
      return null;
    }
  }

  @Before
  public void setUp()
  {
    botWithConfusedKnot = new TaxiInkBot(new TaxiBotWithConfusedKnotConfig());
    botWithoutConfusedKnot = new TaxiInkBot(new TaxiBotWithoutConfusedKnotConfig());
    botWithConfusedKnotWithReprompts = new TaxiInkBot(new TaxiBotWithConfusedKnotWithRepromptsConfig());
  }

  @Test
  public void testNoConfusedKnot() throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = botWithoutConfusedKnot.respond(session, context, "Order me a taxi");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("What is the pick up address ?"));
    assertThat(response.isAskResponse(), is(true));

    response = botWithoutConfusedKnot.respond(session, context, "etretret ret");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Where would you like to be picked up ?"));
    assertThat(response.isAskResponse(), is(true));

    response = botWithoutConfusedKnot.respond(session, context, "eeeetttt");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Where would you like to be picked up ?"));
    assertThat(response.isAskResponse(), is(true));

    response = botWithoutConfusedKnot.respond(session, context, "Where is my cab ?");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Where would you like to be picked up ?"));
    assertThat(response.isAskResponse(), is(true));

    response = botWithoutConfusedKnot.respond(session, context, "ewewew");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Where would you like to be picked up ?"));
    assertThat(response.isAskResponse(), is(true));
  }

  @Test
  public void testBasicConfusedKnot() throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = botWithConfusedKnot.respond(session, context, "Order me a taxi");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("What is the pick up address ?"));
    assertThat(response.isAskResponse(), is(true));

    response = botWithConfusedKnot.respond(session, context, "etretret ret");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Where would you like to be picked up ?"));
    assertThat(response.isAskResponse(), is(true));

    response = botWithConfusedKnot.respond(session, context, "Where is my cab ?");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), startsWith("I'm sorry I'm not understanding you at all"));
    assertThat(response.isAskResponse(), is(false));
  }

  @Test
  public void testStopConfusion() throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = botWithConfusedKnot.respond(session, context, "Order me a taxi");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("What is the pick up address ?"));
    assertThat(response.isAskResponse(), is(true));

    response = botWithConfusedKnot.respond(session, context, "etretret ret");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Where would you like to be picked up ?"));
    assertThat(response.isAskResponse(), is(true));

    response = botWithConfusedKnot.respond(session, context, "136 River Road");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), startsWith("Taxi 1983 is on its way"));
    assertThat(response.isAskResponse(), is(false));
  }

  @Test
  public void testConfusedKnotWithReprompts() throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = botWithConfusedKnotWithReprompts.respond(session, context, "Order me a taxi");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("What is the pick up address ?"));
    assertThat(response.isAskResponse(), is(true));

    response = botWithConfusedKnotWithReprompts.respond(session, context, "skibidi whop");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Where would you like to be picked up ?"));
    assertThat(response.isAskResponse(), is(true));

    response = botWithConfusedKnotWithReprompts.respond(session, context, "Where is my cab ?");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(),
        startsWith("I'm struggling with that one. Do you want me to call our service line for you?"));
    assertThat(response.isAskResponse(), is(true));

    assertThat(InkBotSessionUtils.getReprompt(session), is("Would you like me to call our service line?"));

    assertThat(response.getHint(), is("Yes or no"));

    assertThat(response.getQuickReplies().size(), is(2));
    List<String> quickReplies = response.getQuickReplies();
    assertThat(quickReplies.get(0), is("Yes"));
    assertThat(quickReplies.get(1), is("No"));
  }
}