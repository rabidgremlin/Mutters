/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.opennlp.ner;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.Intent;
import com.rabidgremlin.mutters.core.Slot;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.SlotMatcher;
import com.rabidgremlin.mutters.core.Tokenizer;
import com.rabidgremlin.mutters.slots.DefaultValueSlot;

/**
 * Implements a SlotMatcher that uses OpenNLP's NER framework.
 *
 * @author rabidgremlin
 *
 */
public class OpenNLPSlotMatcher implements SlotMatcher
{
  /** Logger. */
  private final Logger log = LoggerFactory.getLogger(OpenNLPSlotMatcher.class);

  /** Map of NER models. */
  private final HashMap<String, TokenNameFinderModel> nerModels = new HashMap<>();

  /** Map of slot models. These share the NER models. */
  private final HashMap<String, TokenNameFinderModel> slotModels = new HashMap<>();

  /** The tokenizer to use. */
  private final Tokenizer tokenizer;

  /**
   * Constructor. Allows tokenizer to be supplied because NER can use case etc as
   * cues, so may require different tokenizer than used for intent matching.
   *
   * @param tokenizer The tokenizer to use on an utterance for NER.
   */
  public OpenNLPSlotMatcher(Tokenizer tokenizer)
  {
    this.tokenizer = tokenizer;
  }

  /**
   * This set the NER model to use for a slot.
   *
   * @param slotName The name of the slot. Should match the name of slots on
   *                 intents added to the matcher.
   * @param nerModel The file name of the NER model. This file must be on the
   *                 classpath.
   */
  public void addSlotModel(String slotName, String nerModel)
  {
    TokenNameFinderModel tnfModel = nerModels.get(nerModel);
    if (tnfModel == null)
    {
      try
      {
        URL modelUrl = Thread.currentThread().getContextClassLoader().getResource(nerModel);
        tnfModel = new TokenNameFinderModel(modelUrl);
        nerModels.put(nerModel, tnfModel);
      }
      catch (Exception e)
      {
        throw new IllegalArgumentException("Unable to load NER model", e);
      }
    }

    slotModels.put(slotName.toLowerCase(), tnfModel);
  }

  @Override
  public Map<Slot<?>, SlotMatch<?>> match(Context context, Intent intent, String utterance)
  {
    String[] utteranceTokens = tokenizer.tokenize(utterance);

    HashMap<Slot<?>, SlotMatch<?>> matchedSlots = new HashMap<>();

    for (Slot<?> slot : intent.getSlots())
    {
      log.debug("Looking for Slot {}", slot.getName());

      TokenNameFinderModel tnfModel = slotModels.get(slot.getName().toLowerCase());
      if (tnfModel == null)
      {
        log.warn("Could not find NER model for slot {}", slot.getName());
        continue;
      }

      NameFinderME nameFinder = new NameFinderME(tnfModel);
      Span[] spans = nameFinder.find(utteranceTokens);

      boolean slotMatched = false;
      if (spans.length > 0)
      {
        String[] matches = Span.spansToStrings(spans, utteranceTokens);

        log.debug("Matching for {} against {}", slot.getName(), matches);

        // TODO what to do with multi matches?
        Optional<? extends SlotMatch<?>> match = slot.match(matches[0], context);
        if (match.isPresent())
        {
          matchedSlots.put(slot, match.get());
          slotMatched = true;
          log.debug("Match found {}", match);
        }
        else
        {
          log.debug("No Match found slot: {} text: {} ", slot.getName(), matches);
        }
      }

      if (!slotMatched && slot instanceof DefaultValueSlot)
      {
        DefaultValueSlot<?> defaultValueSlot = (DefaultValueSlot<?>) slot;
        Object defaultValue = defaultValueSlot.getDefaultValue();
        // capture wildcard with helper method
        matchedSlots.put(slot, newSlotMatch(defaultValueSlot, utterance));
        log.debug("No Match found slot: {} Using default value: {} ", slot.getName(), defaultValue);
      }
      else
      {
        log.debug("Did not find slot {} utteranceTokens {} ", slot.getName(), utteranceTokens);
      }
    }
    return matchedSlots;
  }

  private static <T> SlotMatch<T> newSlotMatch(DefaultValueSlot<T> defaultValueSlot, String utterance)
  {
    T defaultValue = defaultValueSlot.getDefaultValue();
    return new SlotMatch<>(defaultValueSlot, utterance, defaultValue);
  }
}
