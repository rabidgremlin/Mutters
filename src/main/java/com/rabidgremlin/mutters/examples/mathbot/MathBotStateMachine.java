package com.rabidgremlin.mutters.examples.mathbot;

import com.rabidgremlin.mutters.state.Guard;
import com.rabidgremlin.mutters.state.PreEventAction;
import com.rabidgremlin.mutters.state.State;
import com.rabidgremlin.mutters.state.StateMachine;

public class MathBotStateMachine extends StateMachine {

	public MathBotStateMachine() {
		State startState = new StartState();
		setStartState(startState);

		State addNumbersState = new AddNumbersState();
		State secondNumberState = new SecondNumberState();
		State generateGraphState = new GenerateGraphState(this);

		Guard haveTwoNumbersGuard = new HaveTwoNumbersGuard();
		
		PreEventAction setNumberAsFirstNumber = new SetNumberAsFirstNumber();
		PreEventAction setNumberAsSecondNumber = new SetNumberAsSecondNumber();
		

		this.addTransition("Addition", startState, addNumbersState,haveTwoNumbersGuard);
		this.addTransition("Addition", startState, secondNumberState);
				
		this.addPreEventAction("Number", secondNumberState, setNumberAsSecondNumber);
		this.addTransition("Number", secondNumberState, addNumbersState, haveTwoNumbersGuard);
		
		this.addPreEventAction("Number", startState, setNumberAsFirstNumber);
		this.addTransition("Number", startState, secondNumberState);

		this.addTransition("GenerateGraph", startState, generateGraphState);
	}

}
