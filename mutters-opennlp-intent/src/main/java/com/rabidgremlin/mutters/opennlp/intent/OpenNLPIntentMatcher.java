package com.rabidgremlin.mutters.opennlp.intent;

import java.net.URL;
import java.util.Set;
import java.util.SortedMap;

import com.rabidgremlin.mutters.core.SlotMatcher;
import com.rabidgremlin.mutters.core.Tokenizer;
import com.rabidgremlin.mutters.core.ml.AbstractMachineLearningIntentMatcher;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;

/**
 * Intent matcher that uses OpenNLP's document classifier.
 * 
 * 
 * @author rabidgremlin
 *
 */
public class OpenNLPIntentMatcher
    extends AbstractMachineLearningIntentMatcher
{
  /** The document categoriser for the intent matcher. */
  private DoccatModel model;

  /** Default minimum match score. */
  private static final float MIN_MATCH_SCORE = 0.75f;

  /**
   * Constructor. Sets up the matcher to use the specified model (on the classpath) and specified tokenizer. Defaults to
   * min score match of MIN_MATCH_SCORE and no maybe intent matching.
   * 
   * @param intentModel The name of the document categoriser model file to use. This file must be on the classpath.
   * @param tokenizer The tokenizer to use when tokenizing an utterance.
   * @param slotMatcher The slot matcher to use to extract slots from the utterance.
   */
  public OpenNLPIntentMatcher(String intentModel, Tokenizer tokenizer, SlotMatcher slotMatcher)
  {
    this(Thread.currentThread().getContextClassLoader().getResource(intentModel), tokenizer, slotMatcher, MIN_MATCH_SCORE, -1);
  }

  /**
   * Constructor. Sets up the matcher to use the specified model (on the classpath) and specifies the minimum and maybe
   * match scores.
   * 
   * @param intentModel The name of the document categoriser model file to use. This file must be on the classpath.
   * @param minMatchScore The minimum match score for an intent match to be considered good.
   * @param maybeMatchScore The maybe match score. Use -1 to disable maybe matching.
   * @param tokenizer The tokenizer to use when tokenizing an utterance.
   * @param slotMatcher The slot matcher to use to extract slots from the utterance.
   */
  public OpenNLPIntentMatcher(String intentModel, Tokenizer tokenizer, SlotMatcher slotMatcher, float minMatchScore, float maybeMatchScore)
  {
    this(Thread.currentThread().getContextClassLoader().getResource(intentModel), tokenizer, slotMatcher, minMatchScore, maybeMatchScore);
  }

  /**
   * Constructor. Sets up the matcher to use the specified model (via a URL) and specifies the minimum and maybe match
   * score.
   * 
   * @param intentModelUrl A URL pointing at the document categoriser model file to load.
   * @param minMatchScore The minimum match score for an intent match to be considered good.
   * @param maybeMatchScore The maybe match score. Use -1 to disable maybe matching.
   * @param tokenizer The tokenizer to use when tokenizing an utterance.
   * @param slotMatcher The slot matcher to use to extract slots from the utterance.
   */
  public OpenNLPIntentMatcher(URL intentModelUrl, Tokenizer tokenizer, SlotMatcher slotMatcher, float minMatchScore, float maybeMatchScore)
  {
    super(tokenizer, slotMatcher, minMatchScore, maybeMatchScore);

    try
    {
      model = new DoccatModel(intentModelUrl);
    }
    catch (Exception e)
    {
      throw new IllegalArgumentException("Unable to load intent model", e);
    }
  }

  @Override
  protected SortedMap<Double, Set<String>> generateSortedScoreMap(String[] utteranceTokens)
  {
    DocumentCategorizerME intentCategorizer = new DocumentCategorizerME(model);
    return intentCategorizer.sortedScoreMap(utteranceTokens);
  }

}
