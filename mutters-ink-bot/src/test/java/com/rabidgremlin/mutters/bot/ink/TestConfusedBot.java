/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.bot.ink;

import static com.google.common.truth.Truth.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.bot.BotException;
import com.rabidgremlin.mutters.core.bot.BotResponse;
import com.rabidgremlin.mutters.core.session.Session;

class TestConfusedBot
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

  @BeforeEach
  void setUp()
  {
    botWithConfusedKnot = new TaxiInkBot(new TaxiBotWithConfusedKnotConfig());
    botWithoutConfusedKnot = new TaxiInkBot(new TaxiBotWithoutConfusedKnotConfig());
    botWithConfusedKnotWithReprompts = new TaxiInkBot(new TaxiBotWithConfusedKnotWithRepromptsConfig());
  }

  @Test
  void testNoConfusedKnot() throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = botWithoutConfusedKnot.respond(session, context, "Order me a taxi");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("What is the pick up address ?");
    assertThat(response.isAskResponse()).isTrue();

    response = botWithoutConfusedKnot.respond(session, context, "etretret ret");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("Where would you like to be picked up ?");
    assertThat(response.isAskResponse()).isTrue();

    response = botWithoutConfusedKnot.respond(session, context, "eeeetttt");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("Where would you like to be picked up ?");
    assertThat(response.isAskResponse()).isTrue();

    response = botWithoutConfusedKnot.respond(session, context, "Where is my cab ?");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("Where would you like to be picked up ?");
    assertThat(response.isAskResponse()).isTrue();

    response = botWithoutConfusedKnot.respond(session, context, "ewewew");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("Where would you like to be picked up ?");
    assertThat(response.isAskResponse()).isTrue();
  }

  @Test
  void testBasicConfusedKnot() throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = botWithConfusedKnot.respond(session, context, "Order me a taxi");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("What is the pick up address ?");
    assertThat(response.isAskResponse()).isTrue();

    response = botWithConfusedKnot.respond(session, context, "etretret ret");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("Where would you like to be picked up ?");
    assertThat(response.isAskResponse()).isTrue();

    response = botWithConfusedKnot.respond(session, context, "Where is my cab ?");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).startsWith("I'm sorry I'm not understanding you at all");
    assertThat(response.isAskResponse()).isFalse();
  }

  @Test
  void testStopConfusion() throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = botWithConfusedKnot.respond(session, context, "Order me a taxi");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("What is the pick up address ?");
    assertThat(response.isAskResponse()).isTrue();

    response = botWithConfusedKnot.respond(session, context, "etretret ret");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("Where would you like to be picked up ?");
    assertThat(response.isAskResponse()).isTrue();

    response = botWithConfusedKnot.respond(session, context, "136 River Road");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).startsWith("Taxi 1983 is on its way");
    assertThat(response.isAskResponse()).isFalse();
  }

  @Test
  void testConfusedKnotWithReprompts() throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = botWithConfusedKnotWithReprompts.respond(session, context, "Order me a taxi");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("What is the pick up address ?");
    assertThat(response.isAskResponse()).isTrue();

    response = botWithConfusedKnotWithReprompts.respond(session, context, "skibidi whop");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("Where would you like to be picked up ?");
    assertThat(response.isAskResponse()).isTrue();

    response = botWithConfusedKnotWithReprompts.respond(session, context, "Where is my cab ?");

    assertThat(response).isNotNull();
    assertThat(response.getResponse())
        .startsWith("I'm struggling with that one. Do you want me to call our service line for you?");
    assertThat(response.isAskResponse()).isTrue();

    assertThat(InkBotSessionUtils.getReprompt(session)).isEqualTo("Would you like me to call our service line?");

    assertThat(response.getHint()).isEqualTo("Yes or no");

    assertThat(response.getQuickReplies()).hasSize(2);
    List<String> quickReplies = response.getQuickReplies();
    assertThat(quickReplies.get(0)).isEqualTo("Yes");
    assertThat(quickReplies.get(1)).isEqualTo("No");
  }
}
