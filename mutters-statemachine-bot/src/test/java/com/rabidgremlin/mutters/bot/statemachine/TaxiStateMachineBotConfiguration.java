package com.rabidgremlin.mutters.bot.statemachine;

import com.rabidgremlin.mutters.core.Intent;
import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.opennlp.intent.OpenNLPIntentMatcher;
import com.rabidgremlin.mutters.opennlp.intent.OpenNLPTokenizer;
import com.rabidgremlin.mutters.opennlp.ner.OpenNLPSlotMatcher;
import com.rabidgremlin.mutters.slots.LiteralSlot;
import com.rabidgremlin.mutters.state.Guard;
import com.rabidgremlin.mutters.state.State;
import com.rabidgremlin.mutters.state.StateMachine;

import opennlp.tools.tokenize.WhitespaceTokenizer;

public class TaxiStateMachineBotConfiguration
    implements StateMachineBotConfiguration
{

  @Override
  public IntentMatcher getIntentMatcher()
  {
 // model was built with OpenNLP whitespace tokenizer
    OpenNLPTokenizer tokenizer = new OpenNLPTokenizer(WhitespaceTokenizer.INSTANCE);
    
    // use Open NLP NER for slot matching
    OpenNLPSlotMatcher slotMatcher = new OpenNLPSlotMatcher(tokenizer);
    slotMatcher.addSlotModel("Address", "models/en-ner-address.bin");    

    // create intent matcher
    OpenNLPIntentMatcher matcher = new OpenNLPIntentMatcher("models/en-cat-taxi-intents.bin", tokenizer, slotMatcher);

    Intent intent = new Intent("OrderTaxi");
    intent.addSlot(new LiteralSlot("Address"));
    matcher.addIntent(intent);

    intent = new Intent("CancelTaxi");
    matcher.addIntent(intent);

    intent = new Intent("WhereTaxi");
    matcher.addIntent(intent);

    intent = new Intent("GaveAddress");
    intent.addSlot(new LiteralSlot("Address"));
    matcher.addIntent(intent);

    return matcher;
  }

  @Override
  public StateMachine getStateMachine()
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

  @Override
  public String getDefaultResponse()
  {

    return null;
  }
}
