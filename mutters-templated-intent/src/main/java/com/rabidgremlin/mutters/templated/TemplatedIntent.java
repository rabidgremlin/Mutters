package com.rabidgremlin.mutters.templated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabidgremlin.mutters.input.CleanedInput;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.Intent;

public class TemplatedIntent
    extends Intent
{
  private Logger log = LoggerFactory.getLogger(TemplatedIntent.class);

  private List<TemplatedUtterance> utterances = new ArrayList<TemplatedUtterance>();

  public TemplatedIntent(String name)
  {
    super(name);
  }

  public void addUtterance(TemplatedUtterance utterance)
  {
    utterances.add(utterance);
  }

  public void addUtterances(List<TemplatedUtterance> utterances)
  {
    this.utterances.addAll(utterances);
  }

  public TemplatedUtteranceMatch matches(CleanedInput input, Context context)
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
