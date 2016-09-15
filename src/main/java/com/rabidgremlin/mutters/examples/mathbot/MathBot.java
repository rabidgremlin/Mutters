package com.rabidgremlin.mutters.examples.mathbot;

import com.rabidgremlin.mutters.bot.statemachine.AbstractStateMachineBot;
import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.slots.NumberSlot;
import com.rabidgremlin.mutters.state.Guard;
import com.rabidgremlin.mutters.state.PreEventAction;
import com.rabidgremlin.mutters.state.State;
import com.rabidgremlin.mutters.state.StateMachine;
import com.rabidgremlin.mutters.templated.TemplatedIntent;
import com.rabidgremlin.mutters.templated.TemplatedIntentMatcher;
import com.rabidgremlin.mutters.templated.TemplatedUtterance;

public class MathBot extends AbstractStateMachineBot
{

	@Override
	public IntentMatcher setUpIntents()
	{
		TemplatedIntentMatcher matcher = new TemplatedIntentMatcher();

		matcher.addIntent(createAdditionIntent());
		matcher.addIntent(createNumberIntent());
		matcher.addIntent(createGenerateGraphIntent());

		return matcher;
	}
	
	
	private TemplatedIntent createAdditionIntent()
	{
		TemplatedIntent additionIntent = new TemplatedIntent("Addition");

		additionIntent.addUtterance(new TemplatedUtterance("What's {number1} + {number2}"));
		additionIntent.addUtterance(new TemplatedUtterance("What's {number1} plus {number2}"));
		additionIntent.addUtterance(new TemplatedUtterance("What is {number1} + {number2}"));
		additionIntent.addUtterance(new TemplatedUtterance("What is {number1} plus {number2}"));
		additionIntent.addUtterance(new TemplatedUtterance("Whats {number1} + {number2}"));
		additionIntent.addUtterance(new TemplatedUtterance("Whats {number1} plus {number2}"));
		additionIntent.addUtterance(new TemplatedUtterance("Add {number1} and {number2}"));
		additionIntent.addUtterance(new TemplatedUtterance("Add {number1} to {number2}"));
		additionIntent.addUtterance(new TemplatedUtterance("{number1} plus {number2}"));
		additionIntent.addUtterance(new TemplatedUtterance("{number1} + {number2}"));		
		// TODO tweak Utterance so it can handle tokens not separated by spaces
		// additionIntent.addUtterance(new Utterance("{number1}+{number2}"));
		additionIntent.addUtterance(new TemplatedUtterance("Add {number1}"));
		

		NumberSlot number1 = new NumberSlot("number1");
		NumberSlot number2 = new NumberSlot("number2");

		additionIntent.addSlot(number1);
		additionIntent.addSlot(number2);

		return additionIntent;
	}

	private TemplatedIntent createNumberIntent()
	{
		TemplatedIntent numberIntent = new TemplatedIntent("Number");

		numberIntent.addUtterance(new TemplatedUtterance("{number}"));

		NumberSlot number = new NumberSlot("number");
		numberIntent.addSlot(number);

		return numberIntent;
	}

	private TemplatedIntent createGenerateGraphIntent()
	{
		TemplatedIntent graphIntent = new TemplatedIntent("GenerateGraph");

		graphIntent.addUtterance(new TemplatedUtterance("dump graph"));
		graphIntent.addUtterance(new TemplatedUtterance("show graph"));
		graphIntent.addUtterance(new TemplatedUtterance("graph"));

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
