/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.bot.ink;

import static com.google.common.truth.Truth.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.bot.BotException;
import com.rabidgremlin.mutters.core.bot.BotResponse;
import com.rabidgremlin.mutters.core.bot.BotResponseAttachment;
import com.rabidgremlin.mutters.core.session.Session;

class TestTaxiInkBot
{
  private static TaxiInkBot taxiBot;

  @BeforeAll
  static void setUpBot()
  {
    taxiBot = new TaxiInkBot(new TaxiInkBotConfiguration());
  }

  @Test
  void testOrderTaxiWithAddress() throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "Send a taxi to 56 Kilm Steet");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("Taxi 1e1f is on its way");
    assertThat(response.isAskResponse()).isFalse();
  }

  @Test
  void testOrderTaxiWithOutAddress() throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "Order me a taxi");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("What is the pick up address ?");
    assertThat(response.isAskResponse()).isTrue();

    response = taxiBot.respond(session, context, "136 River Road");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("Taxi 1983 is on its way");
    assertThat(response.isAskResponse()).isFalse();
  }

  @Test
  void testCancelTaxi() throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "Cancel my cab order");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("Your taxi has been cancelled");
    assertThat(response.isAskResponse()).isFalse();
    assertThat(response.getAttachments()).isNull();
  }

  @Test
  void testTaxiStatus() throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "Where is my ride ?");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("Your taxi is about 7 minutes away");
    assertThat(response.isAskResponse()).isFalse();
    assertThat(response.getAttachments()).isNull();
  }

  @Test
  void testHintAndReprompt() throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "Order me a taxi");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("What is the pick up address ?");
    assertThat(response.isAskResponse()).isTrue();
    assertThat(response.getHint()).isEqualTo("123 Someplace Rd");

    // random non address response to test we get reprompt and hint
    response = taxiBot.respond(session, context, "no");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("Where would you like to be picked up ?");
    assertThat(response.isAskResponse()).isTrue();
    assertThat(response.getHint()).isEqualTo("123 Someplace Rd");

    // another random non address response to test we get reprompt and hint
    response = taxiBot.respond(session, context, "yes");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("Where would you like to be picked up ?");
    assertThat(response.isAskResponse()).isTrue();
    assertThat(response.getHint()).isEqualTo("123 Someplace Rd");

    response = taxiBot.respond(session, context, "136 River Road");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("Taxi 1983 is on its way");
    assertThat(response.isAskResponse()).isFalse();
    assertThat(response.getHint()).isNull();
  }

  @Test
  void testAttachment() throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "Send a taxi to 56 Kilm Steet");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("Taxi 1e1f is on its way");
    assertThat(response.isAskResponse()).isFalse();
    assertThat(response.getAttachments()).isNotNull();

    assertThat(response.getAttachments()).hasSize(1);
    BotResponseAttachment attachment = response.getAttachments().get(0);
    assertThat(attachment.getType()).isEqualTo("link");
    assertThat(attachment.getParameters().get("url")).isEqualTo("http://trackcab.example.com/t/1e1f");
    assertThat(attachment.getParameters().get("title")).isEqualTo("Track your taxi here");
  }

  @Test
  void testStop() throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "Order me a taxi");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("What is the pick up address ?");
    assertThat(response.isAskResponse()).isTrue();

    response = taxiBot.respond(session, context, "Stop");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("Ok");
    assertThat(response.isAskResponse()).isFalse();
    assertThat(response.getAttachments()).isNull();
  }

  @Test
  void testHelp() throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "help");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).startsWith("I can help you order a taxi or");
    assertThat(response.isAskResponse()).isFalse();
    assertThat(response.getAttachments()).isNull();
  }

  @Test
  void testNoPardonFollowedByPardon() throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "The sky is blue");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("Pardon?");
    assertThat(response.isAskResponse()).isTrue();

    response = taxiBot.respond(session, context, "and roses are red");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("Pardon?");
    assertThat(response.isAskResponse()).isTrue();

    response = taxiBot.respond(session, context, "pigs don't fly");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("Pardon?");
    assertThat(response.isAskResponse()).isTrue();
  }

  @Test
  void testNoPardonFollowedByPardonSecondCase() throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    // valid but out of sequence answer
    BotResponse response = taxiBot.respond(session, context, "I like pink");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("Pardon?");
    assertThat(response.isAskResponse()).isTrue();

    // then a nonsense reply
    response = taxiBot.respond(session, context, "and roses are red");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("Pardon?");
    assertThat(response.isAskResponse()).isTrue();

    // then a nonsense reply
    response = taxiBot.respond(session, context, "pigs don't fly");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("Pardon?");
    assertThat(response.isAskResponse()).isTrue();
  }

  @Test
  void testParagraphsPreserved() throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "help");

    assertThat(response).isNotNull();
    assertThat(response.getResponse())
        .startsWith("I can help you order a taxi or find out the location of your current taxi.\nTry say ");
    assertThat(response.isAskResponse()).isFalse();
  }

  @Test
  void testSessionEndClearsReprompts() throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "Where is my ride ?");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("Your taxi is about 7 minutes away");
    assertThat(response.isAskResponse()).isFalse();

    response = taxiBot.respond(session, context, "and roses are red");
    assertThat(response.getResponse()).isEqualTo("Pardon?");
    assertThat(response.isAskResponse()).isTrue();
  }

  @Test
  void testQuickReplies() throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "Send a taxi to 56 Kilm Steet");

    assertThat(response).isNotNull();
    assertThat(response.getResponse()).isEqualTo("Taxi 1e1f is on its way");
    assertThat(response.isAskResponse()).isFalse();
    assertThat(response.getQuickReplies()).isNotNull();

    assertThat(response.getQuickReplies()).hasSize(2);
    List<String> quickReplies = response.getQuickReplies();
    assertThat(quickReplies.get(0)).isEqualTo("Where is my taxi?");
    assertThat(quickReplies.get(1)).isEqualTo("Cancel my taxi");
  }
}
