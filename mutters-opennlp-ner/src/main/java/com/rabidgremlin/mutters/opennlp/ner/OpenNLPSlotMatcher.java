package com.rabidgremlin.mutters.opennlp.ner;

import java.net.URL;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.Intent;
import com.rabidgremlin.mutters.core.Slot;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.SlotMatcher;
import com.rabidgremlin.mutters.core.Tokenizer;
import com.rabidgremlin.mutters.slots.DefaultValueSlot;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

/**
 * Implements a SlotMatcher that uses OpenNLP's NER framework.
 *
 * @author rabidgremlin
 *
 */
public class OpenNLPSlotMatcher
        implements SlotMatcher
{
  /** Logger. */
  private Logger log = LoggerFactory.getLogger(OpenNLPSlotMatcher.class);

  /** Map of NER models. */
  private HashMap<String, TokenNameFinderModel> nerModels = new HashMap<String, TokenNameFinderModel>();

  /** Map of slot models. These share the NER models. */
  private HashMap<String, TokenNameFinderModel> slotModels = new HashMap<String, TokenNameFinderModel>();

  /** The tokenizer to use. */
  private Tokenizer tokenizer;

  /**
   * Constructor. Allows tokenizer to be supplied because NER can use case etc as cues, so may require different
   * tokenizer than used for intent matching.
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
   * @param slotName The name of the slot. Should match the name of slots on intents added to the matcher.
   * @param nerModel The file name of the NER model. This file must be on the classpath.
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
      }
      catch (Exception e)
      {
        throw new IllegalArgumentException("Unable to load NER model", e);
      }
    }

    slotModels.put(slotName.toLowerCase(), tnfModel);
  }

  @Override
  public HashMap<Slot, SlotMatch> match(Context context, Intent intent, String utterance)
  {
    String[] utteranceTokens = tokenizer.tokenize(utterance);

    HashMap<Slot, SlotMatch> matchedSlots = new HashMap<Slot, SlotMatch>();

    for (Slot slot : intent.getSlots())
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
        SlotMatch match = slot.match(matches[0], context);
        if (match != null)
        {
          matchedSlots.put(slot, match);
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
        Object defaultValue = ((DefaultValueSlot) slot).getDefaultValue();
        matchedSlots.put(slot, new SlotMatch(slot, utterance, defaultValue));
        log.debug("No Match found slot: {} Using default value: {} ", slot.getName(), defaultValue);
      }
      else
      {
        log.debug("Did not find slot {} utteranceTokens {} ", slot.getName(), utteranceTokens);
      }
    }
    return matchedSlots;
  }
}
