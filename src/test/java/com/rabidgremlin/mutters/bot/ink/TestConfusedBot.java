package com.rabidgremlin.mutters.bot.ink;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rabidgremlin.mutters.bot.BotException;
import com.rabidgremlin.mutters.bot.BotResponse;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.session.Session;

public class TestConfusedBot
{
  private TaxiInkBot taxiBot;

  @Before
  public void setUp()
  {
    taxiBot = new TaxiInkBot();    
    taxiBot.setConfusedKnot(2, "confused_bot");
  }
  

  @Test
  public void testNoConfusedKnot()
    throws BotException
  {
    // unset confused knot for this test
    taxiBot.setConfusedKnot(-1, null);
    
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "Order me a taxi");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("What is the pick up address ?"));
    assertThat(response.isAskResponse(), is(true));

    response = taxiBot.respond(session, context, "etretret ret");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Where would you like to be picked up ?"));
    assertThat(response.isAskResponse(), is(true));
    
    response = taxiBot.respond(session, context, "eeeetttt");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Where would you like to be picked up ?"));
    assertThat(response.isAskResponse(), is(true));
    
    response = taxiBot.respond(session, context, "Where is my cab ?");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Where would you like to be picked up ?"));
    assertThat(response.isAskResponse(), is(true));
    
    response = taxiBot.respond(session, context, "ewewew");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Where would you like to be picked up ?"));
    assertThat(response.isAskResponse(), is(true));
  }
  
  
  @Test
  public void testBasicConfusedKnot()
    throws BotException
  { 
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "Order me a taxi");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("What is the pick up address ?"));
    assertThat(response.isAskResponse(), is(true));

    response = taxiBot.respond(session, context, "etretret ret");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Where would you like to be picked up ?"));
    assertThat(response.isAskResponse(), is(true));
    
    response = taxiBot.respond(session, context, "Where is my cab ?");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), startsWith("I'm sorry I'm not understanding you at all"));
    assertThat(response.isAskResponse(), is(false));
  }


}
