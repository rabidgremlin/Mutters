package com.rabidgremlin.mutters;

import java.util.Arrays;
import java.util.List;

public class Utils {

	private Utils()
	{
		// do nothing
	}
	
	public static List<String> tokenize(String text)
	{
		 return Arrays.asList(text.split(
					"[ \\n\\r\\t,\\!`\\(\\)\\[\\]:;\\\"\\?\\/\\\\\\<\\-\\+\\=>]+"));
	}
}
