package com.rabidgremlin.mutters.bot.ink;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rabidgremlin.mutters.bot.ink.InkBotConfiguration.ConfusedKnot;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.bot.BotException;
import com.rabidgremlin.mutters.core.bot.BotResponse;
import com.rabidgremlin.mutters.core.session.Session;

public class TestConfusedBot
{
  private TaxiInkBot botWithConfusedKnot;
  private TaxiInkBot botWithoutConfusedKnot;
  
  
  class TaxiBotWithConfusedKnotConfig extends TaxiInkBotConfiguration
  {
    @Override
    public ConfusedKnot getConfusedKnot()
    {
      return new ConfusedKnot(2, "confused_bot");
    }    
  }
  
  class TaxiBotWithoutConfusedKnotConfig extends TaxiInkBotConfiguration
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
  }
  

  @Test
  public void testNoConfusedKnot()
    throws BotException
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
  public void testBasicConfusedKnot()
    throws BotException
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
  public void testStopConfusion()
    throws BotException
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


}
