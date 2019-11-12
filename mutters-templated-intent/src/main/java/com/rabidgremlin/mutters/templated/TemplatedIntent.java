/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.templated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.Intent;
import com.rabidgremlin.mutters.core.Slot;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.Tokenizer;
import com.rabidgremlin.mutters.slots.DefaultValueSlot;

/**
 * This is an intent that matches based on an utterance template.
 * 
 * One or more utterance templates can be added to the intent. An utterance
 * template can include slot identifiers (in curly braces) which are used to
 * extract the slots added to the intent.
 * 
 * @author rabidgremlin
 *
 */
public class TemplatedIntent extends Intent
{
  private Logger log = LoggerFactory.getLogger(TemplatedIntent.class);

  private List<TemplatedUtterance> utterances = new ArrayList<TemplatedUtterance>();

  private Tokenizer tokenizer;

  /**
   * Constructor.
   * 
   * @param name      The name of the intent.
   * @param tokenizer The tokenizer to use when parsing the utterance templates.
   *                  Should be the same one used to parse the input passed to the
   *                  matches method.
   */
  public TemplatedIntent(String name, Tokenizer tokenizer)
  {
    super(name);
    this.tokenizer = tokenizer;
  }

  /**
   * Adds an utterance template to the intent.
   * 
   * @param utteranceTemplate The utterance template.
   * @return The new TemplatedUtterance.
   */
  public TemplatedUtterance addUtterance(String utteranceTemplate)
  {
    TemplatedUtterance newUtterance = new TemplatedUtterance(tokenizer.tokenize(utteranceTemplate));
    utterances.add(newUtterance);
    return newUtterance;
  }

  /**
   * Adds a list of utterance templates.
   * 
   * @param utteranceTemplates The list of utterance templates.
   * @return The list of new TemplatedUtterances.
   */
  public List<TemplatedUtterance> addUtterances(List<String> utteranceTemplates)
  {
    List<TemplatedUtterance> newUtterances = new ArrayList<>();

    for (String utteranceTemplate : utteranceTemplates)
    {
      newUtterances.add(addUtterance(utteranceTemplate));
    }

    return newUtterances;
  }

  /**
   * Processes the supplied input and attempts to match against the utterance
   * templates for the intent.
   * 
   * @param input   The user's input.
   * @param context The user's context.
   * @return The templated utterance match.
   */
  public TemplatedUtteranceMatch matches(String[] input, Context context)
  {
    for (TemplatedUtterance utterance : utterances)
    {
      TemplatedUtteranceMatch match = utterance.matches(input, slots, context);
      if (match.isMatched())
      {
        // matched utterance didn't fill in all the slots so check for default values
        if (match.getSlotMatches().size() != slots.getSlots().size())
        {
          // grab all the matched slots
          HashMap<Slot, SlotMatch> matchedSlots = match.getSlotMatches();

          // loop through each slot
          for (Slot slot : slots.getSlots())
          {
            // does slot have default value and no match ?
            if (!matchedSlots.containsKey(slot) && slot instanceof DefaultValueSlot)
            {
              // yep create a slot match with default value
              Object defaultValue = ((DefaultValueSlot) slot).getDefaultValue();
              matchedSlots.put(slot,
                  new SlotMatch(slot, defaultValue == null ? "" : defaultValue.toString(), defaultValue));
            }
          }
        }

        return match;
      }
    }

    return new TemplatedUtteranceMatch(false);
  }

  public List<TemplatedUtterance> getUtterances()
  {
    return Collections.unmodifiableList(utterances);
  }
}
