package com.rabidgremlin.mutters.state;

public class IntentResponse {

	private boolean sessionEnded;
	private String response;

	public static IntentResponse newAskResponse(String response) {
		return new IntentResponse(false, response);
	}

	public static IntentResponse newTellResponse(String response) {
		return new IntentResponse(true, response);
	}

	public IntentResponse(boolean sessionEnded, String response) {

		this.sessionEnded = sessionEnded;
		this.response = response;
	}

	public boolean isSessionEnded() {
		return sessionEnded;
	}

	public String getResponse() {
		return response;
	}

}
