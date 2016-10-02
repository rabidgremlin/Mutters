package com.rabidgremlin.mutters.examples.mathbot;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.rabidgremlin.mutters.bot.BotResponse;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.session.Session;

public class MathBotExample
{

  Session session;

  MathBot mathBot;

  Context context;

  public MathBotExample()
  {
    mathBot = new MathBot();
    session = new Session();
    context = new Context();
  }

  public void run()
    throws Exception
  {
    BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));

    System.out.println("Hi, I'm MathBot\nYou can ask me to add two numbers together.");

    System.out.print("> ");
    String input = null;
    while ((input = inReader.readLine()) != null)
    {
      BotResponse botResponse = mathBot.respond(session, context, input);
      System.out.println(botResponse.getResponse());

      System.out.print("> ");
    }

  }

  public static void main(String[] args)
  {
    try
    {
      MathBotExample mathBot = new MathBotExample();

      mathBot.run();

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

  }

}
