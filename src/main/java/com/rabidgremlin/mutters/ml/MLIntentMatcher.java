package com.rabidgremlin.mutters.ml;

import java.net.URL;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedMap;

import org.apache.commons.lang.NotImplementedException;
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

/**
 * This intent matcher uses machine learning (ML) in the form of a document categoriser to match a user's utterance to
 * an intent.
 * 
 * It also uses named entity recognition (NER) models to identify text to extract for an intent's slots.
 * 
 * See the <a href=
 * "https://github.com/rabidgremlin/Mutters/blob/master/src/test/java/com/rabidgremlin/mutters/bot/ink/TaxiInkBot.java"
 * target="_blank">TaxiInkBot</a> for an example of how a MLIntentMatcher is configured.
 * 
 * @author rabidgremlin
 *
 */
public class MLIntentMatcher
    implements IntentMatcher
{
  /** Logger. */
  private Logger log = LoggerFactory.getLogger(MLIntentMatcher.class);

  /** The document categoriser for the intent matcher. */
  private DoccatModel model;

  /** Map of intents to match on. */
  private HashMap<String, MLIntent> intents = new HashMap<String, MLIntent>();

  /** Map of NER models. */
  private HashMap<String, TokenNameFinderModel> nerModels = new HashMap<String, TokenNameFinderModel>();

  /** Map of slot models. These share the NER models. */
  private HashMap<String, TokenNameFinderModel> slotModels = new HashMap<String, TokenNameFinderModel>();

  /** Default minimum match score. */
  private static final float MIN_MATCH_SCORE = 0.75f;

  /** The minium match score. The match must have at least this probability to be considered good. */
  private float minMatchScore;

  /**
   * Constructor. Sets up the matcher to use the specified model.
   * 
   * @param intentModel The name of the document categoriser model file to use. This file must be on the classpath.
   */
  public MLIntentMatcher(String intentModel)
  {
    this(intentModel, MIN_MATCH_SCORE);
  }

  /**
   * Constructor. Sets up the matcher to use the specified model and specifies the minimum match score.
   * 
   * @param intentModel The name of the document categoriser model file to use. This file must be on the classpath.
   * @param minMatchScore The minimum match score for an intent match to be considered good.
   */
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

  /**
   * Adds an intent to the matcher.
   * 
   * @param intent The intent.
   */
  public void addIntent(MLIntent intent)
  {
    intents.put(intent.getName().toUpperCase(), intent);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.rabidgremlin.mutters.core.IntentMatcher#match(String utterance, Context context, Set<String> expectedIntents)
   */
  @Override
  public IntentMatch match(String utterance, Context context, Set<String> expectedIntents)
  {
    if (expectedIntents != null)
    {
      throw new NotImplementedException("expectedIntents not yet implemented for TemplatedIntentMatcher");
    }
    
    DocumentCategorizerME intentCategorizer = new DocumentCategorizerME(model);

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
