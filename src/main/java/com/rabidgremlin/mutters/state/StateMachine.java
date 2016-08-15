package com.rabidgremlin.mutters.state;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.rabidgremlin.mutters.core.Intent;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.session.Session;

public class StateMachine
{

	private HashMap<String, State> states = new HashMap<String, State>();
	private State startState;
	private HashMap<String, List<Transition>> transitionMap = new HashMap<String, List<Transition>>();
	private HashSet<String> handledIntents = new HashSet<String>();
	private HashMap<String, PreEventAction> preEventActions = new HashMap<String, PreEventAction>();

	class Transition
	{
		public State toState;
		public Guard guard;

		public Transition(State toState, Guard guard)
		{
			this.toState = toState;
			this.guard = guard;
		}
	}

	public void setStartState(State state)
	{
		this.startState = state;
	}

	private void addState(State state)
	{
		states.put(state.getName(), state);
		if (startState == null)
		{
			startState = state;
		}
	}

	private String makeTransitionKey(String intentName, State state)
	{
		return intentName + '-' + state.getName();
	}

	private String makeGlobalTransitionKey(String intentName)
	{
		return intentName + "-*";
	}

	public void addTransition(String intentName, State fromState, State toState)
	{
		addTransition(intentName, fromState, toState, null);
	}

	public void addPreEventAction(String intentName, State fromState, PreEventAction preEventAction)
	{
		preEventActions.put(makeTransitionKey(intentName, fromState), preEventAction);
	}

	public void addTransition(String intentName, State fromState, State toState, Guard guard)
	{
		if (!states.containsKey(fromState.getName()))
		{
			addState(fromState);
		}

		if (!states.containsKey(toState.getName()))
		{
			addState(toState);
		}

		String key = makeTransitionKey(intentName, fromState);
		List<Transition> transitionList = transitionMap.get(key);
		if (transitionList == null)
		{
			transitionList = new ArrayList<Transition>();
			transitionMap.put(key, transitionList);
		}

		transitionList.add(new Transition(toState, guard));

		handledIntents.add(intentName);
	}

	public void addGlobalTransition(String intentName, State toState)
	{
		addGlobalTransition(intentName, toState, null);
	}

	public void addGlobalTransition(String intentName, State toState, Guard guard)
	{

		if (!states.containsKey(toState.getName()))
		{
			addState(toState);
		}

		String key = makeGlobalTransitionKey(intentName);
		List<Transition> transitionList = transitionMap.get(key);
		if (transitionList == null)
		{
			transitionList = new ArrayList<Transition>();
			transitionMap.put(key, transitionList);
		}

		transitionList.add(new Transition(toState, guard));

		handledIntents.add(intentName);
	}

	public IntentResponse trigger(final IntentMatch match, final Session session)
	{

		State currentState = startState;

		String currentStateName = (String) session.getAttribute("STATE_MACHINE_JLA1974_currentStateName");
		if (currentStateName != null)
		{
			currentState = states.get(currentStateName);
			if (currentState == null)
			{
				throw new IllegalStateException("Illegal current state in session:" + currentStateName);
			}
		}

		Intent intent = match.getIntent();
		String intentName = (intent != null) ? intent.getName() : null;

		if (intentName == null)
		{
			throw new IllegalArgumentException("Request missing intent." + match.toString());
		}

		String key = makeTransitionKey(intentName, currentState);

		// see if we have a pre-evet action and execute it if we have it
		PreEventAction preEventAction = preEventActions.get(key);
		if (preEventAction != null)
		{
			preEventAction.execute(match, session);
		}

		List<Transition> transitionToStateList = transitionMap.get(key);

		if (transitionToStateList == null)
		{
			key = makeGlobalTransitionKey(intentName);
			transitionToStateList = transitionMap.get(key);

			if (transitionToStateList == null)
			{
				throw new IllegalStateException("Could not find state to transition to. Intent: " + intentName + " Current State: " + currentState);
			}
		}

		State transitionToState = null;

		// find first matching to state, checking guards
		for (Transition transition : transitionToStateList)
		{
			if (transition.guard == null)
			{
				transitionToState = transition.toState;
				break;
			}
			else
			{
				if (transition.guard.passes(match, session))
				{
					transitionToState = transition.toState;
					break;
				}
			}
		}

		// didn't find any matching states
		if (transitionToState == null)
		{
			throw new IllegalStateException("Could not find state to transition to. Failed all guards. Intent: " + intentName + " Current State: " + currentState);
		}

		IntentResponse response = transitionToState.execute(match, session);

		session.setAttribute("STATE_MACHINE_JLA1974_currentStateName", transitionToState.name);

		return response;
	}

	public boolean canHandleRequest(final IntentMatch request)
	{
		return request.getIntent() != null && handledIntents.contains(request.getIntent().getName());
	}

	public void dump(Writer writer) throws IOException
	{
		// dummy state for global transitions
		final State anyState = new State("<<ANY>>")
		{

			@Override
			public IntentResponse execute(IntentMatch intentMatch, Session session)
			{
				return null;
			}
		};

		PrintWriter out = new PrintWriter(writer);

		out.println("digraph g {");

		out.println("rankdir=LR;");
		out.println("overlap=false;");
		out.println("nodesep=0.25;");

		for (State state : states.values())
		{
			out.print(state.getName());
			out.println(";");
		}

		for (String key : transitionMap.keySet())
		{
			String[] splitKey = key.split("-");
			String intent = splitKey[0];
			State fromState;

			// handle global transitions
			if (splitKey[1].equals("*"))
			{
				fromState = anyState;
			}
			else
			{
				fromState = states.get(splitKey[1]);
			}

			List<Transition> transitionToStateList = transitionMap.get(key);

			for (Transition transition : transitionToStateList)
			{
				out.print(fromState.getName());
				out.print(" -> ");
				out.print(transition.toState.getName());
				out.print(" [label=\"");
				out.print(intent);

				if (transition.guard != null)
				{
					out.print(" [");
					out.print(transition.guard.getDescription());
					out.print("]");
				}

				out.println("\"];");
			}

		}

		out.println("}");
		out.flush();
	}
}
