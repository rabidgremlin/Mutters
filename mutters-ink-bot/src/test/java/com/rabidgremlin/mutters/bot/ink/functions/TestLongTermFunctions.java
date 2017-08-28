package com.rabidgremlin.mutters.bot.ink.functions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.rabidgremlin.mutters.bot.ink.functions.orderbot.OrderInkBot;
import com.rabidgremlin.mutters.bot.ink.functions.orderbot.OrderInkBotConfiguration;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.bot.BotResponse;
import com.rabidgremlin.mutters.core.session.Session;

public class TestLongTermFunctions
{
  private static OrderInkBot orderBot = new OrderInkBot(new OrderInkBotConfiguration());
    
  @Test
  public void testSetLongTermFunctionInScript() throws Exception
  {
    Session session = new Session();
    Context context = new Context();
    
    BotResponse response = orderBot.respond(session, context, "Order a widget");
    
    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Your order 123456 has been created!"));
    assertThat(session.getLongTermAttribute("currentorder"), is("123456"));
  }
  
  @Test
  public void testGetLongTermFunctionInScript() throws Exception
  {
    Session session = new Session();
    Context context = new Context();
    
    // do the order so value is set
    BotResponse response = orderBot.respond(session, context, "Order a widget");
    
    // ask for status which should use long term current order as context
    response = orderBot.respond(session, context, "What is the status of my order");
    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("For order 123456 ?"));
    assertThat(session.getLongTermAttribute("currentorder"), is("123456"));
  }
  
  @Test
  public void testDeleteLongTermFunctionInScript() throws Exception
  {
    Session session = new Session();
    Context context = new Context();
    
    // order, then answer no when bot confirms on current order
    BotResponse response = orderBot.respond(session, context, "Order a widget");
    response = orderBot.respond(session, context, "What is the status of my order");
    response = orderBot.respond(session, context, "No");
    
    // check that we are prompted for order number and that long term current order has been unset
    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("What is the order number of the order you want to check the status of ?"));
    assertThat(session.getLongTermAttribute("currentorder"), is(nullValue()));
  }
  
  @Test
  public void testGetLongTermFunctionInScriptNoValue() throws Exception
  {
    Session session = new Session();
    Context context = new Context();
    
    // ask for status of order with out any prior conversation
    BotResponse response = orderBot.respond(session, context, "What is the status of my order");
    
    // should go down route of prompting for order number
    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("What is the order number of the order you want to check the status of ?"));
    assertThat(session.getLongTermAttribute("currentorder"), is(nullValue()));    
  }
}
