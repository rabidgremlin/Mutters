/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.generate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * This class generates a list of utterances, given a template string. Can be
 * used to generate lots of options to match on given only one string.
 * 
 * Groups of options are enclosed in ~'s and separated by |
 * 
 * For example, given a template string of:
 * 
 * ``` ~what|what's|what is~ ~the|~ time ~in|at~ {Place} ```
 * 
 * The generator will generate 12 different utterances, include:
 * 
 * ``` what the time in {Place} what's the time in {Place} what is the time in
 * {Place} what is the time at {Place} what is time at {Place} etc ```
 * 
 * 
 * @see com.rabidgremlin.mutters.generate.TestUtteranceGenerator
 * 
 * @author rabidgremlin
 *
 */
public class UtteranceGenerator
{
  private final Pattern matchPattern = Pattern.compile("~(.*?)~|(\\S+)");

  public List<String> generate(String template)
  {
    Matcher match = matchPattern.matcher(template);

    ArrayList<ArrayList<String>> lists = new ArrayList<>();

    while (match.find())
    {
      String group = match.group();
      // System.out.println(group);

      if (group.startsWith("~") && group.endsWith("~"))
      {
        String[] words = group.substring(1, group.length() - 1).split("\\|", -1);
        lists.add(new ArrayList<>(Arrays.asList(words)));
      }
      else
      {
        lists.add(new ArrayList<>(Arrays.asList(group)));
      }

    }

    // arrgggh need this crap because Java generics suck and you cannot create
    // arrays of generics
    // also type erasure...
    List<String>[] listArray = new ArrayList[lists.size()];
    for (int loop = 0; loop < lists.size(); loop++)
    {
      listArray[loop] = lists.get(loop);
    }

    List<List<String>> templates = Product.generate(listArray);

    // System.out.println(templates);

    List<String> utterances = new ArrayList<>();
    for (List<String> templateParts : templates)
    {
      utterances.add(StringUtils.join(templateParts, " ").trim().replaceAll(" +", " "));
    }

    return utterances;
  }

}
