package com.rabidgremlin.mutters.util;

import java.util.Arrays;
import java.util.List;

public class Utils
{

	private Utils()
	{
		// do nothing
	}

	public static List<String> tokenize(String text)
	{
		// return Arrays.asList(text.split(
		// "[ \\n\\r\\t,\\!`\\(\\)\\[\\]:;\\\"\\?\\/\\\\\\<\\-\\+\\=>]+"));

		// TODO fix this
		return Arrays.asList(text.split("\\s+"));
	}

	public static String cleanInput(String inputString)
	{
		List<String> inputTokens = tokenize(inputString);

		// yuck fix this !
		String input = "";
		for (String token : inputTokens)
		{
			input += token + " ";
		}

		return input.trim();
	}

}
