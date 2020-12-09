/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.bot.ink;

import static com.google.common.truth.Truth.assertThat;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.MatcherScores;
import com.rabidgremlin.mutters.core.bot.BotException;
import com.rabidgremlin.mutters.core.bot.IntentBotResponse;
import com.rabidgremlin.mutters.core.session.Session;

/**
 * Test that matched intent name and matching scores are set
 *
 */
class TestMatchingDataIsSet
{
  private static TaxiInkBot taxiBot;

  @BeforeAll
  static void setUpBot()
  {
    taxiBot = new TaxiInkBot(new TaxiInkBotConfiguration());
  }

  @Test
  void testDebugValuesAreSet() throws BotException
  {
    Session session = new Session();
    Context context = new Context();

    IntentBotResponse response = taxiBot.respond(session, context, "Send a taxi to 56 Kilm Steet");

    assertThat(response).isNotNull();
    assertThat(response.getMatchedIntent()).isNotNull();
    assertThat(response.getMatchedIntent().getName()).isEqualTo("OrderTaxi");

    MatcherScores matchingScores = response.getMatchingScores();

    assertThat(matchingScores).isNotNull();
    Set<String> bestIntents = matchingScores.getScores().get(matchingScores.getScores().lastKey());
    assertThat(bestIntents).hasSize(1);
    assertThat(bestIntents).contains("OrderTaxi");
  }
}
