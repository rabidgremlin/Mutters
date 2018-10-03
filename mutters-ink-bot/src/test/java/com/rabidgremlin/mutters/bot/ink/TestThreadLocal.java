package com.rabidgremlin.mutters.bot.ink;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.bot.BotException;
import com.rabidgremlin.mutters.core.bot.BotResponse;
import com.rabidgremlin.mutters.core.session.Session;

public class TestThreadLocal
{

  private static final int TEST_ROUNDS = 10;

  private static TaxiInkBot taxiBot;

  private List<Runnable> testCases = new ArrayList<>();

  @BeforeClass
  public static void setUpBot()
  {
    taxiBot = new TaxiInkBot(new TaxiInkBotConfiguration());
  }

  @Test
  public void testThreadLocal()
  {
    addSimpleCases();
    addInteractiveCases();

    ExecutorService executor = Executors.newFixedThreadPool(2);
    for (int i = 0; i < TEST_ROUNDS; i++)
    {
      for (Runnable testCase : testCases)
      {
        executor.execute(testCase);
      }
    }

    executor.shutdown();
    while (!executor.isTerminated())
    {
    }
    System.out.println("Finished all threads");
  }

  private void addSimpleCases()
  {
    testCases.add(new TestCase(taxiBot, "Send a taxi to 56 Kilm Steet", "Taxi 1e1f is on its way"));
    testCases.add(new TestCase(taxiBot, "Order me a taxi", "What is the pick up address ?"));
    testCases.add(new TestCase(taxiBot, "Cancel my cab order", "Your taxi has been cancelled"));
    testCases.add(new TestCase(taxiBot, "Where is my ride ?", "Your taxi is about 7 minutes away"));
    testCases.add(new TestCase(taxiBot, "The sky is blue", "Pardon?"));
    testCases.add(new TestCase(taxiBot, "pigs don't fly", "Pardon?"));
  }

  private void addInteractiveCases()
  {
    List<Pair<String, String>> phrasesAndResponses = new ArrayList<>();
    phrasesAndResponses.add(new ImmutablePair<String, String>("Order me a taxi", "What is the pick up address ?"));
    phrasesAndResponses.add(new ImmutablePair<String, String>("136 River Road", "Taxi 1983 is on its way"));
    testCases.add(new TestCase(taxiBot, phrasesAndResponses));
  }
}

class TestCase
    implements Runnable
{

  private TaxiInkBot bot;

  private List<Pair<String, String>> phrasesAndResponses = new ArrayList<>();

  public TestCase(TaxiInkBot bot, String phrase, String expectedResponse)
  {
    this.bot = bot;
    Pair<String, String> pair = new ImmutablePair<>(phrase, expectedResponse);
    phrasesAndResponses.add(pair);
  }

  public TestCase(TaxiInkBot bot, List<Pair<String, String>> phrasesAndResponses)
  {
    this.bot = bot;
    this.phrasesAndResponses = phrasesAndResponses;
  }

  @Override
  public void run()
  {
    Session session = new Session();
    Context context = new Context();

    BotResponse response;
    try
    {
      for (Pair<String, String> pair : phrasesAndResponses)
      {
        response = bot.respond(session, context, pair.getKey());
        assertThat(response, is(notNullValue()));
        assertThat(response.getResponse(), is(pair.getValue()));
      }
    }
    catch (BotException e)
    {
      e.printStackTrace();
    }
  }
}
