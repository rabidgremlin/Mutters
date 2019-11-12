/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.templated;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.Slot;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.Slots;

/**
 * This class handles the matching of an utterance against an utterance
 * template.
 * 
 * It also handles the extraction slot data.
 * 
 * @author rabidgremlin
 * @author dmap
 *
 */
public class TemplatedUtterance
{
  private static enum PartType
  {
    SIMPLE, SLOT
  }

  private static class MatchPart
  {
    private final PartType type;
    private final String value;

    public MatchPart(final PartType type, final String value)
    {
      this.type = type;
      this.value = value;
    }

  }

  private static class SlotMatchRegion
  {
    private final String slotName;
    private final int from, to;

    public SlotMatchRegion(final String slotName, final int from, final int to)
    {
      this.slotName = slotName;
      this.from = from;
      this.to = to;
    }
  }

  private String template;

  private HashSet<String> slotNames = new HashSet<String>();

  private MatchPart[] templateParts;

  public TemplatedUtterance(String[] tokenizedTemplate)
  {
    this.template = StringUtils.join(tokenizedTemplate, ' ');

    this.templateParts = new MatchPart[tokenizedTemplate.length];
    for (int i = 0; i < tokenizedTemplate.length; i++)
    {
      final String token = tokenizedTemplate[i];
      if (token.startsWith("{") && token.endsWith("}"))
      {
        String slotName = token.substring(1, token.length() - 1);
        templateParts[i] = new MatchPart(PartType.SLOT, slotName);
        slotNames.add(slotName);
      }
      else
      {
        templateParts[i] = new MatchPart(PartType.SIMPLE, token);
      }
    }
  }

  public String getTemplate()
  {
    return template;
  }

  public TemplatedUtteranceMatch matches(String[] tokenizedUtterance, Slots slots, Context context)
  {
    final List<SlotMatchRegion> slotMatches = new ArrayList<>();
    if (!match(tokenizedUtterance, 0, this.templateParts, 0, slotMatches))
    {
      return new TemplatedUtteranceMatch(false);
    }

    TemplatedUtteranceMatch theMatch = new TemplatedUtteranceMatch(true);

    for (SlotMatchRegion region : slotMatches)
    {
      Slot slot = slots.getSlot(region.slotName);

      if (slot == null)
      {
        throw new IllegalStateException(
            "Cannot find slot '" + region.slotName + " reference by utterance '" + template + "'");
      }

      List<String> orginalTokens = new ArrayList<>();
      for (int loop = region.from; loop < region.to; loop++)
      {
        orginalTokens.add(tokenizedUtterance[loop]);
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

  private boolean match(String[] tokens, int tokenIndex, MatchPart[] parts, int partIndex,
      final List<SlotMatchRegion> slotMatches)
  {
    if (partIndex == parts.length)
    {
      // we have matched all parts, make sure there are no tokens remaining
      return tokenIndex == tokens.length;
    }
    else if ((tokens.length - tokenIndex) < (parts.length - partIndex))
    {
      // there are not enough tokens left to match all the remaining parts
      return false;
    }

    final MatchPart part = parts[partIndex];
    switch (part.type)
    {
      case SIMPLE:
        if (part.value.equalsIgnoreCase(tokens[tokenIndex]))
        {
          return match(tokens, tokenIndex + 1, parts, partIndex + 1, slotMatches);
        }
        else
        {
          return false;
        }
      case SLOT:
        // to match the original greedy regex we must also use a greedy match
        for (int i = tokens.length; i > tokenIndex; i--)
        {
          if (match(tokens, i, parts, partIndex + 1, slotMatches))
          {
            slotMatches.add(new SlotMatchRegion(part.value, tokenIndex, i));
            return true;
          }
        }
        return false;
      default:
        return false;
    }
  }

  @Override
  public String toString()
  {
    return "Utterance [template=" + template + "]";
  }

}
