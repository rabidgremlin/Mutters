package com.rabidgremlin.mutters.bot.ink.functions;

import com.rabidgremlin.mutters.bot.ink.functions.orderbot.OrderInkBot;
import com.rabidgremlin.mutters.bot.ink.functions.orderbot.OrderInkBotConfiguration;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.bot.BotResponse;
import com.rabidgremlin.mutters.core.session.Session;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class TestRepromptQuickRepliesAndHint
{
  private static OrderInkBot orderBot = new OrderInkBot(new OrderInkBotConfiguration());
    
  @Test
  public void givenFailedMatchShouldIncludeRepromptHintQuickRelies() throws Exception
  {
    Session session = new Session();
    Context context = new Context();

    // do the order so value is set
    BotResponse response = orderBot.respond(session, context, "Order a widget");

    // ask for status which should use long term current order as context
    response = orderBot.respond(session, context, "What is the status of my order");
    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("For order 123456 ?"));
    assertThat(response.getHint(), is("Yes or No"));
    assertThat(response.getQuickReplies(), hasItems("Yes", "No"));

    response =  orderBot.respond(session, context, "Hmm...");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("For order 123456 ? Please answer Yes or No."));
    assertThat(response.getHint(), is("Yes or No"));
    assertThat(response.getQuickReplies(), hasItems("Yes", "No"));

  }

}
