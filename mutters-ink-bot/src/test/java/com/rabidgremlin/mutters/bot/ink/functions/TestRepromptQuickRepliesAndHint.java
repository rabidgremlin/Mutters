/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.bot.ink.functions;

import static org.hamcrest.CoreMatchers.hasItems;
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

    response = orderBot.respond(session, context, "Hmm...");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("For order 123456 ? Please answer Yes or No."));
    assertThat(response.getHint(), is("Yes or No"));
    assertThat(response.getQuickReplies(), hasItems("Yes", "No"));

    response = orderBot.respond(session, context, "What?");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("For order 123456 ? Please answer Yes or No."));
    assertThat(response.getHint(), is("Yes or No"));
    assertThat(response.getQuickReplies(), hasItems("Yes", "No"));

  }

  @Test
  public void givenFailedMatchShouldClearRepromptHintQuickReliesAfterSuccessfulMatch() throws Exception
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

    response = orderBot.respond(session, context, "Hmm...");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("For order 123456 ? Please answer Yes or No."));
    assertThat(response.getHint(), is("Yes or No"));
    assertThat(response.getQuickReplies(), hasItems("Yes", "No"));

    response = orderBot.respond(session, context, "No");

    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("What is the order number of the order you want to check the status of ?"));
    assertThat(response.getHint(), is(nullValue()));
    assertThat(response.getQuickReplies(), is(nullValue()));
  }

  @Test
  public void givenNoMatchingPhraseAtStartOfConversationShouldReturnDefaultResponse() throws Exception
  {
    Session session = new Session();
    Context context = new Context();

    // Say something the bot won't understand
    BotResponse response = orderBot.respond(session, context, "The sky is bright yellow");

    // check that we got default prompt
    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Sorry I didn't catch that."));
  }

  @Test
  public void givenNoMatchingPhraseWhenNoRepromptAvailableShouldReturnDefaultResponsePlusOrginalPrompt()
      throws Exception
  {
    Session session = new Session();
    Context context = new Context();

    // Start pricing conversation
    BotResponse response = orderBot.respond(session, context, "Can you give me a price");
    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Do you want the price for blue widgets ?"));

    // say something other then yes or no
    response = orderBot.respond(session, context, "for green widgets");

    // check that we get default response + original prompt
    assertThat(response, is(notNullValue()));
    assertThat(response.getResponse(), is("Sorry I didn't catch that. Do you want the price for blue widgets ?"));
  }

}
