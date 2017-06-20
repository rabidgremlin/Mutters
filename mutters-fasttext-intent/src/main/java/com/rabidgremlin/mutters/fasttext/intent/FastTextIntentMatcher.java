package com.rabidgremlin.mutters.fasttext.intent;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jfasttext.JFastText;
import com.rabidgremlin.mutters.core.SlotMatcher;
import com.rabidgremlin.mutters.core.Tokenizer;
import com.rabidgremlin.mutters.core.ml.AbstractMachineLearningIntentMatcher;

/**
 * Intent matcher that uses Facebook's fastText document classifier.
 * 
 * It expects the training data to have the default __label__ prefix but strips this prefix off when returning the
 * matched intent name.
 * 
 * @author rabidgremlin
 *
 */
public class FastTextIntentMatcher
    extends AbstractMachineLearningIntentMatcher
{
  /** Logger. */
  private Logger log = LoggerFactory.getLogger(FastTextIntentMatcher.class);

  private JFastText jft = new JFastText();

  /** Default minimum match score. */
  private static final float MIN_MATCH_SCORE = 0.85f;

  /**
   * Constructor. Sets up the matcher to use the specified model (on the classpath) and specified tokenizer. Defaults to
   * min score match of MIN_MATCH_SCORE and no maybe intent matching.
   * 
   * @param intentModel The name of the document categoriser model file to use. This file must be on the classpath.
   * @param tokenizer The tokenizer to use when tokenizing an utterance.
   * @param slotMatcher The slot matcher to use to extract slots from the utterance.
   */
  public FastTextIntentMatcher(String intentModel, Tokenizer tokenizer, SlotMatcher slotMatcher)
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
  public FastTextIntentMatcher(String intentModel, Tokenizer tokenizer, SlotMatcher slotMatcher, float minMatchScore, float maybeMatchScore)
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
  public FastTextIntentMatcher(URL intentModelUrl, Tokenizer tokenizer, SlotMatcher slotMatcher, float minMatchScore, float maybeMatchScore)
  {
    super(tokenizer, slotMatcher, minMatchScore, maybeMatchScore);

    try
    {
      // jFastText can only load model from file so copy model to temp file
      File tempFile = File.createTempFile("jft", "bin");
      tempFile.deleteOnExit();

      BufferedOutputStream temp = new BufferedOutputStream(new FileOutputStream(tempFile));
      IOUtils.copy(intentModelUrl.openStream(), temp);
      temp.flush();
      temp.close();

      // load the model from the temp file
      jft.loadModel(tempFile.getAbsolutePath());

    }
    catch (Exception e)
    {
      throw new IllegalArgumentException("Unable to load fastText model", e);
    }
  }

  @Override
  protected SortedMap<Double, Set<String>> generateSortedScoreMap(String[] utteranceTokens)
  {
    // get the first 10 labels
    List<JFastText.ProbLabel> probLabels = jft.predictProba(StringUtils.join(utteranceTokens, " "), 10);

    SortedMap<Double, Set<String>> scoreMap = new TreeMap<Double, Set<String>>();

    // populate the scor map
    for (JFastText.ProbLabel probLabel : probLabels)
    {
      Double score = Math.exp(probLabel.logProb);

      // strip off the __label__
      String label = probLabel.label.substring("__label__".length());

      Set<String> labels = new HashSet<>();
      labels.add(label);
      scoreMap.put(score, labels);
    }

    return scoreMap;
  }

}
