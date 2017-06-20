package com.rabidgremlin.mutters.bot.ink;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.junit.BeforeClass;
import org.junit.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.bot.BotException;
import com.rabidgremlin.mutters.core.bot.BotResponse;
import com.rabidgremlin.mutters.core.session.Session;

/**
 * Test that debug values are populated as expected by ink based bot.
 *
 */
public class TestDebugValues
{
  private static TaxiInkBot taxiBot;

  @BeforeClass
  public static void setUpBot()
  {
    taxiBot = new TaxiInkBot(new TaxiInkBotConfiguration());
  }

  @Test
  public void testDebugValuesAreSet()
    throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response = taxiBot.respond(session, context, "Send a taxi to 56 Kilm Steet");

    assertThat(response, is(notNullValue()));
    Map<String, Object> debugValues = response.getDebugValues();
    assertThat(debugValues, is(notNullValue()));
    assertThat(debugValues.get(InkBot.DK_MATCHED_INTENT), is("OrderTaxi"));

    @SuppressWarnings("unchecked")
    SortedMap<Double, Set<String>> matchingScores = (SortedMap<Double, Set<String>>) debugValues.get(InkBot.DK_INTENT_MATCHING_SCORES);

    assertThat(matchingScores, is(notNullValue()));
    Set<String> bestIntents = matchingScores.get(matchingScores.lastKey());
    assertThat(bestIntents.size(), is(1));
    assertThat(bestIntents, hasItems("OrderTaxi"));
  }
}
