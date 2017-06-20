package com.rabidgremlin.mutters.templated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.Intent;
import com.rabidgremlin.mutters.core.Tokenizer;

/**
 * This is an intent that matches based on an utterance template.
 * 
 * One or more utterance templates can be added to the intent. An utterance template can include slot identifiers (in
 * curly braces) which are used to extract the slots added to the intent.
 * 
 * @author rabidgremlin
 *
 */
public class TemplatedIntent
    extends Intent
{
  private Logger log = LoggerFactory.getLogger(TemplatedIntent.class);

  private List<TemplatedUtterance> utterances = new ArrayList<TemplatedUtterance>();

  private Tokenizer tokenizer;

  /**
   * Constructor.
   * 
   * @param name The name of the intent.
   * @param tokenizer The tokenizer to use when parsing the utterance templates. Should be the same one used to parse
   *          the input passed to the matches method.
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
   * Processes the supplied input and attempts to match against the utterance templates for the intent.
   * 
   * @param input The user's input.
   * @param context The user's context.
   * @return The templated utterance match.
   */
  public TemplatedUtteranceMatch matches(String[] input, Context context)
  {
    log.debug("------------- Intent: {} Input: {}", name, input);
    for (TemplatedUtterance utterance : utterances)
    {
      log.debug("       Matching to {} ", utterance.getTemplate());
      TemplatedUtteranceMatch match = utterance.matches(input, slots, context);
      if (match.isMatched())
      {
        log.debug("------------ Matched to {} match: {} -------------", utterance.getTemplate(),
            match);
        return match;
      }
    }

    log.debug("------------ No Match to {} -------------", name);
    return new TemplatedUtteranceMatch(false);
  }

  public List<TemplatedUtterance> getUtterances()
  {
    return Collections.unmodifiableList(utterances);
  }
}
