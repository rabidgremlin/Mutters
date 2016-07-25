package com.rabidgremlin.mutters.examples.mathbot;

import com.rabidgremlin.mutters.state.State;
import com.rabidgremlin.mutters.state.StateMachine;

public class MathBotStateMachine extends StateMachine {

	public MathBotStateMachine() {
		State startState = new StartState();
		setStartState(startState);

		State addNumbersState = new AddNumbersState();
		State generateGraphState = new GenerateGraphState(this);

		this.addTransition("Addition", startState, addNumbersState);
		this.addTransition("GenerateGraph", startState, generateGraphState);
	}

}
