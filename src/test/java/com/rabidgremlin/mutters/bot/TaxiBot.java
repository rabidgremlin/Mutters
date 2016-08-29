package com.rabidgremlin.mutters.bot;

import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.examples.mathbot.StartState;
import com.rabidgremlin.mutters.ml.MLIntent;
import com.rabidgremlin.mutters.ml.MLIntentMatcher;
import com.rabidgremlin.mutters.state.Guard;
import com.rabidgremlin.mutters.state.State;
import com.rabidgremlin.mutters.state.StateMachine;

public class TaxiBot extends AbstractBot
{

	@Override
	public IntentMatcher setUpIntents()
	{
		MLIntentMatcher matcher = new MLIntentMatcher("models/en-cat-taxi-intents.bin");
		matcher.addSlotModel("Address", "models/en-ner-address.bin");
		
		MLIntent intent = new MLIntent("OrderTaxi");
		intent.addSlot("Address");
		matcher.addIntent(intent);		
		
		intent = new MLIntent("CancelTaxi");
		matcher.addIntent(intent);
		
		intent = new MLIntent("WhereTaxi");
		matcher.addIntent(intent);
		
		intent = new MLIntent("GaveAddress");
		intent.addSlot("Address");
		matcher.addIntent(intent);
		
		return matcher;
	}

	@Override
	public StateMachine setUpStateMachine()
	{
		StateMachine stateMachine = new StateMachine();

		State startState = new StartState();
		stateMachine.setStartState(startState);
		
		State orderTaxi = new OrderTaxiState();
		State cancelTaxi = new CancelTaxiState();
		State taxiStatus = new TaxiStatusState();
		State getAddress = new GetAddressState();
		
		Guard haveAddress = new HaveAddressGuard();
		
		stateMachine.addTransition("OrderTaxi", startState, orderTaxi, haveAddress);
		stateMachine.addTransition("OrderTaxi", startState, getAddress);
		
		stateMachine.addTransition("GaveAddress", getAddress, orderTaxi, haveAddress);
		
		stateMachine.addTransition("CancelTaxi", startState, cancelTaxi);
		stateMachine.addTransition("WhereTaxi", startState, taxiStatus);
		
		
		return stateMachine;
	}

}
