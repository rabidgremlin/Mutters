package com.rabidgremlin.mutters.examples.mathbot;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.rabidgremlin.mutters.bot.BotResponse;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.Intent;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.core.NumberSlot;
import com.rabidgremlin.mutters.core.Utterance;
import com.rabidgremlin.mutters.session.Session;
import com.rabidgremlin.mutters.state.IntentResponse;

public class MathBotExample
{

	Session session;
	MathBot mathBot;
	Context context;

	public MathBotExample()
	{
		mathBot = new MathBot();
		session = new Session();
	}

	public void run() throws Exception
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
