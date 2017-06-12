package com.rabidgremlin.mutters.bot.ink;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.rabidgremlin.mutters.bot.BotException;
import com.rabidgremlin.mutters.bot.BotResponse;
import com.rabidgremlin.mutters.bot.BotResponseAttachment;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.session.Session;

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
    assertThat(response.getAttachments(), is(nullValue()));
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
    assertThat(response.getAttachments(), is(nullValue()));
  }

  @Test
  public void testHintAndReprompt()
    throws BotException
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
    assertThat(response.getResponse(), is("Where would you like to be picked up ?"));
    assertThat(response.isAskResponse(), is(true));
    assertThat(response.getHint(), is("123 Someplace Rd"));

    response = taxiBot.respond(session, context, "136 River Road");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Taxi 1983 is on its way"));
    assertThat(response.isAskResponse(), is(false));
    assertThat(response.getHint(), is(nullValue()));
  }

  @Test
  public void testAttachment()
    throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "Send a taxi to 56 Kilm Steet");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Taxi 1e1f is on its way"));
    assertThat(response.isAskResponse(), is(false));    
    assertThat(response.getAttachments(), is(notNullValue()));
    
    assertThat(response.getAttachments().size(), is(1));
    BotResponseAttachment attachment = response.getAttachments().get(0);    
    assertThat(attachment.getType(), is("link"));
    assertThat(attachment.getParameters().get("url"), is("http://trackcab.example.com/t/1e1f"));
    assertThat(attachment.getParameters().get("title"), is("Track your taxi here"));
  }
  
  
  @Test
  public void testStop()
    throws BotException
  {
    Session session = new Session();
    Context context = new Context();
    
    BotResponse response = taxiBot.respond(session, context, "Order me a taxi");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("What is the pick up address ?"));
    assertThat(response.isAskResponse(), is(true));

    response = taxiBot.respond(session, context, "Stop");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Ok"));
    assertThat(response.isAskResponse(), is(false));
    assertThat(response.getAttachments(), is(nullValue()));
  }
  
  @Test
  public void testHelp()
    throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "help");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), startsWith("I can help you order a taxi or"));
    assertThat(response.isAskResponse(), is(false));
    assertThat(response.getAttachments(), is(nullValue()));
  }
  
  @Test
  public void testNoPardonFollowedByPardon()
    throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "The sky is blue");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Pardon?"));
    assertThat(response.isAskResponse(), is(true));
    
    response = taxiBot.respond(session, context, "and roses are red");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Pardon?"));
    assertThat(response.isAskResponse(), is(true));
    
    response = taxiBot.respond(session, context, "pigs don't fly");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Pardon?"));
    assertThat(response.isAskResponse(), is(true));
  }
  
  @Test
  public void testNoPardonFollowedByPardonSecondCase()
    throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    // valid but out of sequence answer
    BotResponse response = taxiBot.respond(session, context, "I like pink");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Pardon?"));
    assertThat(response.isAskResponse(), is(true));
    
    // then a nonsense reply
    response = taxiBot.respond(session, context, "and roses are red");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Pardon?"));
    assertThat(response.isAskResponse(), is(true));
    
    // then a nonsense reply
    response = taxiBot.respond(session, context, "pigs don't fly");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Pardon?"));
    assertThat(response.isAskResponse(), is(true));
  }
  
  @Test
  public void testParagraphsPreserved()
    throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "help");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), startsWith("I can help you order a taxi or find out the location of your current taxi.\nTry say "));
    assertThat(response.isAskResponse(), is(false));    
  }
  
  @Test
  public void testSessionEndClearsReprompts()
    throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "Where is my ride ?");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Your taxi is about 7 minutes away"));
    assertThat(response.isAskResponse(), is(false));
    
    response = taxiBot.respond(session, context, "and roses are red");
    assertThat(response.getResponse(), is("Pardon?"));
    assertThat(response.isAskResponse(), is(true));
  }
  
  @Test
  public void testQuickReplies()
    throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "Send a taxi to 56 Kilm Steet");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Taxi 1e1f is on its way"));
    assertThat(response.isAskResponse(), is(false));    
    assertThat(response.getQuickReplies(), is(notNullValue()));
    
    assertThat(response.getQuickReplies().size(), is(2));
    List<String> quickReplies = response.getQuickReplies();    
    assertThat(quickReplies.get(0), is("Where is my taxi?"));
    assertThat(quickReplies.get(1), is("Cancel my taxi"));    
  }
}
