package com.rabidgremlin.mutters.examples.mathbot;

import com.rabidgremlin.mutters.bot.AbstractBot;
import com.rabidgremlin.mutters.core.Intent;
import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.core.NumberSlot;
import com.rabidgremlin.mutters.core.Utterance;
import com.rabidgremlin.mutters.state.Guard;
import com.rabidgremlin.mutters.state.PreEventAction;
import com.rabidgremlin.mutters.state.State;
import com.rabidgremlin.mutters.state.StateMachine;

public class MathBot extends AbstractBot
{

	@Override
	public IntentMatcher setUpIntents()
	{
		IntentMatcher matcher = new IntentMatcher();

		matcher.addIntent(createAdditionIntent());
		matcher.addIntent(createNumberIntent());
		matcher.addIntent(createGenerateGraphIntent());

		return matcher;
	}
	
	
	private Intent createAdditionIntent()
	{
		Intent additionIntent = new Intent("Addition");

		additionIntent.addUtterance(new Utterance("What's {number1} + {number2}"));
		additionIntent.addUtterance(new Utterance("What's {number1} plus {number2}"));
		additionIntent.addUtterance(new Utterance("What is {number1} + {number2}"));
		additionIntent.addUtterance(new Utterance("What is {number1} plus {number2}"));
		additionIntent.addUtterance(new Utterance("Whats {number1} + {number2}"));
		additionIntent.addUtterance(new Utterance("Whats {number1} plus {number2}"));
		additionIntent.addUtterance(new Utterance("Add {number1} and {number2}"));
		additionIntent.addUtterance(new Utterance("Add {number1} to {number2}"));
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

	@Override
	public StateMachine setUpStateMachine()
	{
		StateMachine stateMachine = new StateMachine();

		State startState = new StartState();
		stateMachine.setStartState(startState);

		State addNumbersState = new AddNumbersState();
		State secondNumberState = new SecondNumberState();
		State generateGraphState = new GenerateGraphState(stateMachine);

		Guard haveTwoNumbersGuard = new HaveTwoNumbersGuard();

		PreEventAction setNumberAsFirstNumber = new SetNumberAsFirstNumber();
		PreEventAction setNumberAsSecondNumber = new SetNumberAsSecondNumber();

		stateMachine.addTransition("Addition", startState, addNumbersState, haveTwoNumbersGuard);
		stateMachine.addTransition("Addition", startState, secondNumberState);

		stateMachine.addPreEventAction("Number", secondNumberState, setNumberAsSecondNumber);
		stateMachine.addTransition("Number", secondNumberState, addNumbersState, haveTwoNumbersGuard);

		stateMachine.addPreEventAction("Number", startState, setNumberAsFirstNumber);
		stateMachine.addTransition("Number", startState, secondNumberState);

		stateMachine.addTransition("GenerateGraph", startState, generateGraphState);

		return stateMachine;
	}

}
