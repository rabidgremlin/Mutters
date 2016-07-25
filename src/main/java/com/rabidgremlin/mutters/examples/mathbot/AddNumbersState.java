package com.rabidgremlin.mutters.examples.mathbot;

import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.session.Session;
import com.rabidgremlin.mutters.state.IntentResponse;
import com.rabidgremlin.mutters.state.State;

public class AddNumbersState extends State {

	public AddNumbersState() {
		super("AddNumbersState");
	}

	@Override
	public IntentResponse execute(IntentMatch intentMatch, Session session) {
		
		
		Integer num1 = (Integer) intentMatch.getSlotMatch("number1").getValue();
		Integer num2 = (Integer) intentMatch.getSlotMatch("number2").getValue();
		
		
		return IntentResponse.newTellResponse(String.format("%d + %d is %d", num1, num2, num1 + num2));
	}

}
