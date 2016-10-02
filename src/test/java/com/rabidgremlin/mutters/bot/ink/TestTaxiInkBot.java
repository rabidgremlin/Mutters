package com.rabidgremlin.mutters.bot.ink;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.rabidgremlin.mutters.bot.BotResponse;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.session.Session;
import com.rabidgremlin.mutters.util.SessionUtils;

import java.util.List;
import java.util.Arrays;

public class TestTaxiInkBot
{
  private static TaxiInkBot taxiBot;

  @BeforeClass
  public static void setUpBot()
  {
    taxiBot = new TaxiInkBot();
  }

  @Test
  public void testOrderTaxiWithAddress()
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
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "Where is my ride ?");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Your taxi is about 7 minutes away"));
    assertThat(response.isAskResponse(), is(false));
  }

  @Test
  public void testHintAndReprompt()
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "Order me a taxi");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("What is the pick up address ?"));
    assertThat(response.isAskResponse(), is(true));
    assertThat(response.getHint(), is("123 Someplace Rd"));

    // random non address response to test we get reprompt and hint
    response = taxiBot.respond(session, context, "no");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Where would you like to be picked up ?"));
    assertThat(response.isAskResponse(), is(true));
    assertThat(response.getHint(), is("123 Someplace Rd"));

    // another random non address response to test we get reprompt and hint
    response = taxiBot.respond(session, context, "yes");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Pardon? Where would you like to be picked up ?"));
    assertThat(response.isAskResponse(), is(true));
    assertThat(response.getHint(), is("123 Someplace Rd"));

    response = taxiBot.respond(session, context, "136 River Road");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Taxi 1983 is on its way"));
    assertThat(response.isAskResponse(), is(false));
    assertThat(response.getHint(), is(nullValue()));
  }

  @Test
  public void testActionUrl()
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "Send a taxi to 56 Kilm Steet");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Taxi 1e1f is on its way"));
    assertThat(response.isAskResponse(), is(false));
    assertThat(response.getAction(), is("OPEN_URL"));
    assertThat(response.getActionParams().get("url"), is("http://trackcab.example.com/t/1e1f"));
  }
}
