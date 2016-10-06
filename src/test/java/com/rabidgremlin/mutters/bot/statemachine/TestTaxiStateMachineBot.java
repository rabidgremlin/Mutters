package com.rabidgremlin.mutters.bot.statemachine;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.rabidgremlin.mutters.bot.BotException;
import com.rabidgremlin.mutters.bot.BotResponse;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.session.Session;
import com.rabidgremlin.mutters.util.SessionUtils;

import java.util.List;
import java.util.Arrays;

public class TestTaxiStateMachineBot
{
  private static TaxiStateMachineBot taxiBot;

  @BeforeClass
  public static void setUpBot()
  {
    taxiBot = new TaxiStateMachineBot();
  }

  @Test
  public void testOrderTaxiWithAddress()
    throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "Send a taxi to 56 Kilm Steet");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Taxi 1e1f is on its way"));
    assertThat(response.isAskResponse(), is(false));
  }

  @Test
  public void testOrderTaxiWithOutAddress()
    throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "Order me a taxi");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("What is the pick up address ?"));
    assertThat(response.isAskResponse(), is(true));

    response = taxiBot.respond(session, context, "136 River Road");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Taxi 1983 is on its way"));
    assertThat(response.isAskResponse(), is(false));
  }

  @Test
  public void testCancelTaxi()
    throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "Cancel my cab order");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Your taxi has been cancelled"));
    assertThat(response.isAskResponse(), is(false));
  }

  @Test
  public void testTaxiStatus()
    throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "Where is my ride ?");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Your taxi is about 7 minutes away"));
    assertThat(response.isAskResponse(), is(false));
  }
}
