package com.rabidgremlin.mutters.ml;

import java.net.URL;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.core.Slot;
import com.rabidgremlin.mutters.core.SlotMatch;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;

public class MLIntentMatcher
    implements IntentMatcher
{
  private Logger log = LoggerFactory.getLogger(MLIntentMatcher.class);

  private DoccatModel model;

  private HashMap<String, MLIntent> intents = new HashMap<String, MLIntent>();

  private HashMap<String, TokenNameFinderModel> nerModels = new HashMap<String, TokenNameFinderModel>();

  private HashMap<String, TokenNameFinderModel> slotModels = new HashMap<String, TokenNameFinderModel>();

  private static final float MIN_MATCH_SCORE = 0.75f;

  private float minMatchScore;

  public MLIntentMatcher(String intentModel)
  {
    this(intentModel, MIN_MATCH_SCORE);
  }

  public MLIntentMatcher(String intentModel, float minMatchScore)
  {
    this.minMatchScore = minMatchScore;
    try
    {
      URL modelUrl = Thread.currentThread().getContextClassLoader().getResource(intentModel);
      model = new DoccatModel(modelUrl);
    }
    catch (Exception e)
    {
      throw new IllegalArgumentException("Unable to load intent model", e);
    }
  }

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

  public void addIntent(MLIntent intent)
  {
    intents.put(intent.getName().toUpperCase(), intent);
  }

  @Override
  public IntentMatch match(String utterance, Context context)
  {
    // TODO look into thread safety of DocumentCategorizerME
    DocumentCategorizerME intentCategorizer = new DocumentCategorizerME(model);

    // TODO extract match probablity and filter out low ones...
    SortedMap<Double, Set<String>> scoredCats = intentCategorizer.sortedScoreMap(utterance);
    log.info("Sorted scores were: {}", scoredCats);

    double bestScore = scoredCats.lastKey();
    String category = (String) scoredCats.get(bestScore).toArray()[0];
    log.info("Best Match was:" + category);

    if (bestScore < minMatchScore)
    {
      log.info("Best score for {} lower then minMatchScore of {}. Failing match.", category, minMatchScore);
      return null;
    }

    MLIntent bestIntent = intents.get(category.toUpperCase());
    if (bestIntent == null)
    {
      return null;
    }

    String[] tokens = SimpleTokenizer.INSTANCE.tokenize(utterance);

    HashMap<Slot, SlotMatch> matchedSlots = new HashMap<Slot, SlotMatch>();

    for (Slot slot : bestIntent.getSlots())
    {
      log.info("Looking for Slot {}", slot.getName());

      TokenNameFinderModel tnfModel = slotModels.get(slot.getName().toLowerCase());
      if (tnfModel == null)
      {
        log.warn("Could not find NER model for slot {}", slot.getName());
        continue;
      }

      NameFinderME nameFinder = new NameFinderME(tnfModel);
      Span[] spans = nameFinder.find(tokens);

      if (spans.length > 0)
      {
        String[] matches = Span.spansToStrings(spans, tokens);

        log.info("Matching for {} against {}", slot.getName(), matches);

        // TODO what to do with multi matches?
        SlotMatch match = slot.match(matches[0], context);
        if (match != null)
        {
          matchedSlots.put(slot, match);
          log.info("Match found {}", match);
        }
        else
        {
          log.info("No Match found slot: {} text: {} ", slot.getName(), matches);
        }
      }
      else
      {
        log.info("Did not find slot {} utterance {} ", slot.getName(), utterance);
      }
    }

    return new IntentMatch(bestIntent, matchedSlots, utterance);

  }

}
