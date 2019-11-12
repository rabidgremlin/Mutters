/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core.ml;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.Intent;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.core.MatcherScores;
import com.rabidgremlin.mutters.core.NoIntentMatch;
import com.rabidgremlin.mutters.core.Slot;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.SlotMatcher;
import com.rabidgremlin.mutters.core.Tokenizer;

/**
 * This is a helpful base class for intent matchers that use machine learning
 * (ML) to match a user's utterance to an intent.
 * 
 * It supports a pluggable SlotMatcher so that named entity recognition (NER)
 * can also be performed on the utterance.
 * 
 * Maybe intent match: If a maybeMatchScore is specified then the intent matcher
 * will generate a MaybeXXXXX intent match where XXXXX is the best matched
 * intent which does not meet the specified min match score. In this case the
 * maybe match will be returned if the score difference between the best match
 * and the next best match is higher than the specified maybeMatchScore. If
 * maybeMatchScore score is set to -1 then maybe intent matching is disabled.
 * 
 * 
 * @author rabidgremlin
 *
 */
public abstract class AbstractMachineLearningIntentMatcher implements IntentMatcher
{
  /** Logger. */
  private Logger log = LoggerFactory.getLogger(AbstractMachineLearningIntentMatcher.class);

  /** Map of intents to match on. */
  private HashMap<String, Intent> intents = new HashMap<String, Intent>();

  /** Default minimum match score. */
  public static final float MIN_MATCH_SCORE = 0.75f;

  /**
   * The minimum match score. The match must have at least this probability to be
   * considered good.
   */
  private float minMatchScore;

  /** Maybe match score. */
  private float maybeMatchScore = -1;

  public static final String MAYBE_INTENT_PREFIX = "Maybe";

  /** Debug value key for intent matching scores. */
  public final static String DEBUG_MATCHING_SCORES = "intentMatchingScores";

  /** The tokenizer to use for the intent matching. */
  private Tokenizer tokenizer;

  /** The slot matcher to use for NER. */
  private SlotMatcher slotMatcher;

  /**
   * Constructor.
   * 
   * @param tokenizer       The tokenizer to use when tokenizing an utterance.
   * @param slotMatcher     The slot matcher to use when extract slots from the
   *                        utterance.
   * @param minMatchScore   The minimum match score for an intent match to be
   *                        considered good.
   * @param maybeMatchScore The maybe match score. Use -1 to disable maybe
   *                        matching.
   */
  public AbstractMachineLearningIntentMatcher(Tokenizer tokenizer, SlotMatcher slotMatcher, float minMatchScore,
      float maybeMatchScore)
  {
    this.minMatchScore = minMatchScore;
    this.maybeMatchScore = maybeMatchScore;
    this.tokenizer = Objects.requireNonNull(tokenizer);
    this.slotMatcher = Objects.requireNonNull(slotMatcher);
  }

  /**
   * Adds an intent to the matcher.
   * 
   * @param intent The intent.
   */
  public void addIntent(Intent intent)
  {
    intents.put(intent.getName().toUpperCase(), intent);
  }

  /**
   * Returns the intents for this matcher.
   * 
   * @return The intents for this matcher
   */
  public Collection<Intent> getIntents()
  {
    return Collections.unmodifiableCollection(intents.values());
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.rabidgremlin.mutters.core.IntentMatcher#match(String utterance,
   * Context context, Set<String> expectedIntents)
   */
  @Override
  public IntentMatch match(String utterance, Context context, Set<String> expectedIntents)
  {
    // utterance is blank, nothing to match on
    if (StringUtils.isBlank(utterance))
    {
      return new NoIntentMatch(utterance);
    }

    String[] utteranceTokens = tokenizer.tokenize(utterance);

    SortedMap<Double, SortedSet<String>> scoredCats = generateSortedScoreMap(utteranceTokens);
    log.debug("Sorted scores were: {}", scoredCats);

    double bestScore = 0;
    String bestCategory = null;
    boolean hasMaybeIntent = false;

    // were we passed a set of expected intents ?
    if (expectedIntents == null)
    {
      // no, grab the first of the best matches
      bestScore = scoredCats.lastKey();
      bestCategory = (String) scoredCats.get(bestScore).toArray()[0];

      // if we don't have a list of expected intents but do have a maybeMatchScore
      // then assume we can have a Maybe
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

            // if we have a maybeMatchScore then check we have a maybe intent in the
            // expected intents list
            if (maybeMatchScore != -1 && expectedIntents.contains(MAYBE_INTENT_PREFIX + cat))
            {
              hasMaybeIntent = true;
            }

            break;
          }

          log.debug("Dropping match for {} wasn't in expected intents {}", cat, expectedIntents);
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
        log.debug("No matches, matching expectedIntents.");
        return new NoIntentMatch(utterance, new MatcherScores(scoredCats));
      }
    }

    log.debug("Best Match was:" + bestCategory);

    // find the intent
    Intent bestIntent = intents.get(bestCategory.toUpperCase());
    if (bestIntent == null)
    {
      log.warn("Missing MLIntent named {}", bestCategory);
      return new NoIntentMatch(utterance, new MatcherScores(scoredCats));
    }

    // are we below min score matching ?
    if (bestScore < minMatchScore)
    {
      // log failure
      log.debug("Best score for {} lower then minMatchScore of {}. Failing match.", bestCategory, minMatchScore);

      // do we have a maybe intent ?
      if (hasMaybeIntent)
      {
        // yes, was the score difference between best and next best good enough
        // to meet maybeMatchScore ?
        Double scoreDiff = calcScoreDifference(scoredCats);
        log.debug("Checking if difference between best and next best score of {} is better than maybeMatchScore of {}",
            scoreDiff, maybeMatchScore);
        if (scoreDiff != null && scoreDiff > maybeMatchScore)
        {
          // yes, so lets return maybe intent
          Intent maybeIntent = intents.get((MAYBE_INTENT_PREFIX + bestCategory).toUpperCase());
          // don't have a maybe intent so clone the best intent
          if (maybeIntent == null)
          {
            maybeIntent = new Intent(MAYBE_INTENT_PREFIX + bestCategory);

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
          log.debug("Matching to maybe intent: {}", bestIntent.getName());
        }
        else
        {
          log.debug("Score difference between best and next best too low. Skipping maybe intent");
          return new NoIntentMatch(utterance, new MatcherScores(scoredCats));
        }
      }
      else
      {
        return new NoIntentMatch(utterance, new MatcherScores(scoredCats));
      }
    }

    // do NER
    HashMap<Slot, SlotMatch> matchedSlots = slotMatcher.match(context, bestIntent, utterance);

    // return best match
    return new IntentMatch(bestIntent, matchedSlots, utterance, new MatcherScores(scoredCats));

  }

  private Double calcScoreDifference(SortedMap<Double, SortedSet<String>> scoredCats)
  {
    if (scoredCats.size() < 2)
    {
      return null;
    }

    Double score1 = scoredCats.lastKey();
    Double score2 = scoredCats.headMap(score1).lastKey();

    return new Double(score1 - score2);
  }

  protected abstract SortedMap<Double, SortedSet<String>> generateSortedScoreMap(String[] utteranceTokens);
}
