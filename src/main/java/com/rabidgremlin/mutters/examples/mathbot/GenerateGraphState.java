package com.rabidgremlin.mutters.examples.mathbot;

import java.io.StringWriter;

import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.session.Session;
import com.rabidgremlin.mutters.state.IntentResponse;
import com.rabidgremlin.mutters.state.State;
import com.rabidgremlin.mutters.state.StateMachine;

public class GenerateGraphState extends State {

	private StateMachine myStateMachine;

	public GenerateGraphState(StateMachine myStateMachine) {
		super("GenerateGraphState");
		this.myStateMachine = myStateMachine;
	}

	@Override
	public IntentResponse execute(IntentMatch intentMatch, Session session) {

		try {
			StringWriter writer = new StringWriter();
			myStateMachine.dump(writer);

			return IntentResponse.newTellResponse(String.format("Cut and past the following into http://sandbox.kidstrythisathome.com/erdos/\n\n%s", writer.toString()));
		} catch (Exception e) {
			return IntentResponse.newTellResponse("Unable to genearte graph: " + e.getMessage());
		}
	}

}
