package com.rabidgremlin.mutters.templated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.Slot;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.Slots;

/**
 * This class handles the matching of an utterance against an utterance template.
 * 
 * It also handles the extraction slot data.
 * 
 * @author rabidgremlin
 *
 */
public class TemplatedUtterance
{
  private String template;

  private HashSet<String> slotNames = new HashSet<String>();

  private Pattern matchPattern;

  public TemplatedUtterance(String[] tokenizedTemplate)
  {
    this.template = StringUtils.join(tokenizedTemplate, ' ');

    String regexStr = "^";

    for (String token : tokenizedTemplate)
    {
      if (token.startsWith("{") && token.endsWith("}"))
      {
        String slotName = token.substring(1, token.length() - 1);

        regexStr += "(?<" + slotName + ">.*) ";

        slotNames.add(slotName);
      }
      else
      {
        regexStr += Pattern.quote(token) + " ";
      }
    }

    // System.out.println(regexStr);
    matchPattern = Pattern.compile(regexStr.trim() + "$", Pattern.CASE_INSENSITIVE);
  }

  public String getTemplate()
  {
    return template;
  }

  public TemplatedUtteranceMatch matches(String[] tokenizedUtterance, Slots slots, Context context)
  {
    List<String> input = Arrays.asList(tokenizedUtterance);

    String inputString = StringUtils.join(input, ' ');

    Matcher match = matchPattern.matcher(inputString);

    if (!match.find())
    {
      return new TemplatedUtteranceMatch(false);
    }

    TemplatedUtteranceMatch theMatch = new TemplatedUtteranceMatch(true);

    for (String slotName : slotNames)
    {
      Slot slot = slots.getSlot(slotName);

      if (slot == null)
      {
        throw new IllegalStateException(
            "Cannot find slot '" + slotName + " reference by utterace '" + template + "'");
      }

      String cleanedMatchString = match.group(slotName);
      List<String> matchedTokens = Arrays.asList(cleanedMatchString.split(" "));

      int matchPos = Collections.indexOfSubList(input, matchedTokens);
      if (matchPos == -1)
      {
        throw new IllegalStateException(
            "Was unable to find '" + cleanedMatchString + "' in '" + inputString + "'");
      }

      List<String> orginalTokens = new ArrayList<>();
      for (int loop = matchPos; loop < matchedTokens.size() + matchPos; loop++)
      {
        orginalTokens.add(input.get(loop));
      }

      SlotMatch slotMatch = slot.match(StringUtils.join(orginalTokens, ' '), context);

      if (slotMatch != null)
      {
        theMatch.getSlotMatches().put(slot, slotMatch);
      }
      else
      {
        return new TemplatedUtteranceMatch(false);
      }
    }

    return theMatch;
  }

  @Override
  public String toString()
  {
    return "Utterance [template=" + template + "]";
  }

}
