package com.rabidgremlin.mutters.session;

import java.util.HashMap;

public class Session {

	private HashMap<String, Object> attributes = new HashMap<String, Object>();

	public Object getAttribute(String attributeName)
	{
	   return attributes.get(attributeName);
	}
	
	public void setAttribute(String attributeName,Object value)
	{
	   attributes.put(attributeName, value);
	}
}
