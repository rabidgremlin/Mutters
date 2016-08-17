package com.rabidgremlin.mutters.generate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.rabidgremlin.mutters.core.Utterance;

public class UtteranceGenerator
{
	private Pattern matchPattern = Pattern.compile("~(.*?)~|(\\S+)");

	public List<Utterance> generate(String template)
	{
		Matcher match = matchPattern.matcher(template);

		ArrayList<ArrayList<String>> lists = new ArrayList<ArrayList<String>>();

		while (match.find())
		{
			String group = match.group();
			// System.out.println(group);

			if (group.startsWith("~") && group.endsWith("~"))
			{
				String[] words = group.substring(1, group.length() - 1).split("\\|", -1);
				lists.add(new ArrayList(Arrays.asList(words)));
			}
			else
			{
				lists.add(new ArrayList(Arrays.asList(group)));
			}

		}

		// arrgggh need this crap because Java generics suck and you cannot create arrays of generics also type erasure...
		List[] listArray = new ArrayList[lists.size()];
		for (int loop = 0; loop < lists.size(); loop++)
		{
			listArray[loop] = lists.get(loop);
		}

		List<List<String>> templates = Product.generate(listArray);

		// System.out.println(templates);

		List<Utterance> utterances = new ArrayList<Utterance>();
		for (List<String> templateParts : templates)
		{
			utterances.add(new Utterance(StringUtils.join(templateParts, " ")));
		}

		return utterances;
	}

}
