package com.rabidgremlin.mutters.ml;

import java.net.URL;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedMap;

import org.apache.commons.lang3.StringUtils;
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
 * Maybe intent match: If a maybeMatchScore is specified then the intent matcher will generate a MaybeXXXXX intent match
 * where XXXXX is the best matched intent which does not meet the specified min match score. In this case the maybe
 * match will be returned if the score difference between the best match and the next best match is higher than the
 * specified maybeMatchScore. If maybeMatchScore score is set to -1 then maybe intent matching is disabled.
 * 
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

  /** The minimum match score. The match must have at least this probability to be considered good. */
  private float minMatchScore;

  /** Maybe match score. */
  private float maybeMatchScore = -1;

  private static final String MAYBE_INTENT_PREFIX = "Maybe";

  /**
   * Constructor. Sets up the matcher to use the specified model.
   * 
   * @param intentModel The name of the document categoriser model file to use. This file must be on the classpath.
   */
  public MLIntentMatcher(String intentModel)
  {
    this(intentModel, MIN_MATCH_SCORE, -1);
  }

  /**
   * Constructor. Sets up the matcher to use the specified model (on the classpath) and specifies the minimum and maybe match scores.
   * 
   * @param intentModel The name of the document categoriser model file to use. This file must be on the classpath.
   * @param minMatchScore The minimum match score for an intent match to be considered good.
   * @param maybeMatchScore The maybe match score. Use -1 to disable maybe matching.
   */
  public MLIntentMatcher(String intentModel, float minMatchScore, float maybeMatchScore)
  {
    this(Thread.currentThread().getContextClassLoader().getResource(intentModel), minMatchScore, maybeMatchScore);    
  }
  
  /**
   * Constructor. Sets up the matcher to use the specified model (via a URL) and specifies the minimum and maybe match score.
   * 
   * @param intentModelUrl A URL pointing at the document categoriser model file to load.
   * @param minMatchScore The minimum match score for an intent match to be considered good.
   * @param maybeMatchScore The maybe match score. Use -1 to disable maybe matching.
   */
  public MLIntentMatcher(URL intentModelUrl, float minMatchScore, float maybeMatchScore)
  {
    this.minMatchScore = minMatchScore;
    this.maybeMatchScore = maybeMatchScore;
    try
    {      
      model = new DoccatModel(intentModelUrl);
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
   * @see com.rabidgremlin.mutters.core.IntentMatcher#match(String utterance, Context context, Set<String>
   * expectedIntents)
   */
  @Override
  public IntentMatch match(String utterance, Context context, Set<String> expectedIntents)
  {
    // utterance is blank, nothing to match on
    if (StringUtils.isBlank(utterance))
    {
      return null;
    }
    
    DocumentCategorizerME intentCategorizer = new DocumentCategorizerME(model);

    SortedMap<Double, Set<String>> scoredCats = intentCategorizer.sortedScoreMap(utterance);
    log.info("Sorted scores were: {}", scoredCats);

    double bestScore = 0;
    String bestCategory = null;
    boolean hasMaybeIntent = false;

    // were we passed a set of expected intents ?
    if (expectedIntents == null)
    {
      // no, grab the first of the best matches
      bestScore = scoredCats.lastKey();
      bestCategory = (String) scoredCats.get(bestScore).toArray()[0];

      // if we don't have a list of expected intents but do have a maybeMatchScore then assume we can have a Maybe
      // intent
      if (maybeMatchScore != -1)
      {
        hasMaybeIntent = true;
      }
    }
    else
    {
      // yep, find the best match that is also in the set of expected intents
      while (!scoredCats.isEmpty())
      {
        // get score of best category
        bestScore = scoredCats.lastKey();

        // get the cats with the best score
        Set<String> cats = scoredCats.get(bestScore);
        for (String cat : cats)
        {
          // is the cat in the expected cat set ?
          if (expectedIntents.contains(cat))
          {
            // yep, found one
            bestCategory = cat;

            // if we have a maybeMatchScore then check we have a maybe intent in the expected intents list
            if (maybeMatchScore != -1 && expectedIntents.contains(MAYBE_INTENT_PREFIX + cat))
            {
              hasMaybeIntent = true;
            }

            break;
          }

          log.info("Dropping match for {} wasn't in expected intents {}", cat, expectedIntents);
        }

        // did we find cat ?
        if (bestCategory != null)
        {
          // yep break
          break;
        }

        // nope, try next in list
        scoredCats.remove(scoredCats.lastKey());
      }

      if (bestCategory == null)
      {
        log.info("No matches, matching expectedIntents.");
        return null;
      }
    }

    log.info("Best Match was:" + bestCategory);

    // find the intent
    MLIntent bestIntent = intents.get(bestCategory.toUpperCase());
    if (bestIntent == null)
    {
      log.warn("Missing MLIntent named {}", bestCategory);
      return null;
    }

    // are we below min score matching ?
    if (bestScore < minMatchScore)
    {
      // log failure
      log.info("Best score for {} lower then minMatchScore of {}. Failing match.", bestCategory, minMatchScore);

      // do we have a maybe intent ?
      if (hasMaybeIntent)
      {
        // yes, was the score difference between best and next best good enough
        // to meet maybeMatchScore ?
        Double scoreDiff = calcScoreDifference(scoredCats);
        log.info("Checking if difference between best and next best score of {} is better than maybeMatchScore of {}", scoreDiff, maybeMatchScore);
        if (scoreDiff != null && scoreDiff > maybeMatchScore)
        {
          // yes, so lets return maybe intent
          MLIntent maybeIntent = intents.get((MAYBE_INTENT_PREFIX + bestCategory).toUpperCase());
          // don't have a maybe intent so clone the best intent
          if (maybeIntent == null)
          {
            maybeIntent = new MLIntent(MAYBE_INTENT_PREFIX + bestCategory);

            // copy slots from best intent
            for (Slot slot : bestIntent.getSlots())
            {
              maybeIntent.addSlot(slot);
            }

            // store for future reuse
            this.addIntent(maybeIntent);
          }

          // return maybe intent instead of best intent
          bestIntent = maybeIntent;
          log.info("Matching to maybe intent: {}", bestIntent.getName());
        }
        else
        {
          log.info("Score difference between best and next best too low. Skipping maybe intent");
          return null;
        }
      }
      else
      {
        return null;
      }
    }
    

    // use tokenizer of model for parsing utterance
    String[] tokens = model.getFactory().getTokenizer().tokenize(utterance);

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

  private Double calcScoreDifference(SortedMap<Double, Set<String>> scoredCats)
  {
    if (scoredCats.size() < 2)
    {
      return null;
    }

    Double score1 = scoredCats.lastKey();
    Double score2 = scoredCats.headMap(score1).lastKey();

    return new Double(score1 - score2);
  }

}
