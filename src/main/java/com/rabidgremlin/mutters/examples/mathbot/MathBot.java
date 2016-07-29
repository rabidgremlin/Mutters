package com.rabidgremlin.mutters.examples.mathbot;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.Intent;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.core.NumberSlot;
import com.rabidgremlin.mutters.core.Utterance;
import com.rabidgremlin.mutters.session.Session;
import com.rabidgremlin.mutters.state.IntentResponse;

public class MathBot
{

	IntentMatcher matcher;
	Session session;
	MathBotStateMachine stateMachine;
	Context context;

	public MathBot()
	{
		setUpIntents();
	}

	private void setUpIntents()
	{
		session = new Session();
		context = new Context();

		matcher = new IntentMatcher();
		matcher.addIntent(createAdditionIntent());
		matcher.addIntent(createNumberIntent());
		matcher.addIntent(createGenerateGraphIntent());

		stateMachine = new MathBotStateMachine();
	}

	private Intent createAdditionIntent()
	{
		Intent additionIntent = new Intent("Addition");

		additionIntent.addUtterance(new Utterance("What's {number1} + {number2}"));
		additionIntent.addUtterance(new Utterance("What is {number1} + {number2}"));
		additionIntent.addUtterance(new Utterance("Whats {number1} + {number2}"));
		additionIntent.addUtterance(new Utterance("Add {number1} and {number2}"));
		additionIntent.addUtterance(new Utterance("{number1} plus {number2}"));
		additionIntent.addUtterance(new Utterance("{number1} + {number2}"));
		// TODO tweak Utterance so it can handle tokens not separated by spaces
		// additionIntent.addUtterance(new Utterance("{number1}+{number2}"));
		additionIntent.addUtterance(new Utterance("Add {number1}"));

		NumberSlot number1 = new NumberSlot("number1");
		NumberSlot number2 = new NumberSlot("number2");

		additionIntent.addSlot(number1);
		additionIntent.addSlot(number2);

		return additionIntent;
	}

	private Intent createNumberIntent()
	{
		Intent numberIntent = new Intent("Number");

		numberIntent.addUtterance(new Utterance("{number}"));

		NumberSlot number = new NumberSlot("number");
		numberIntent.addSlot(number);

		return numberIntent;
	}

	private Intent createGenerateGraphIntent()
	{
		Intent graphIntent = new Intent("GenerateGraph");

		graphIntent.addUtterance(new Utterance("dump graph"));
		graphIntent.addUtterance(new Utterance("show graph"));
		graphIntent.addUtterance(new Utterance("graph"));

		return graphIntent;
	}

	public void run() throws Exception
	{
		BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Hi, I'm MathBot\nYou can ask me to add two numbers together.");

		System.out.print("> ");
		String input = null;
		while ((input = inReader.readLine()) != null)
		{
			IntentMatch intentMatch = matcher.match(input, context);

			if (intentMatch != null)
			{
				IntentResponse response = stateMachine.trigger(intentMatch, session);
				System.out.println(response.getResponse());

				if (response.isSessionEnded())
				{
					session = new Session();
				}

			}
			else
			{
				System.out.println("Pardon?");
			}

			// System.out.println("You said '" + input + "'");
			System.out.print("> ");
		}

	}

	public static void main(String[] args)
	{
		try
		{
			MathBot mathBot = new MathBot();

			mathBot.run();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
