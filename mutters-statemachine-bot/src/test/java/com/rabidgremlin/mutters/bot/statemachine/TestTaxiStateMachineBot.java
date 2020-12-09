/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.bot.statemachine;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.bot.BotException;
import com.rabidgremlin.mutters.core.bot.BotResponse;
import com.rabidgremlin.mutters.core.session.Session;

class TestTaxiStateMachineBot
{
  private static TaxiStateMachineBot taxiBot;

  @BeforeAll
  static void setUpBot()
  {
    taxiBot = new TaxiStateMachineBot(new TaxiStateMachineBotConfiguration());
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
  }
}
