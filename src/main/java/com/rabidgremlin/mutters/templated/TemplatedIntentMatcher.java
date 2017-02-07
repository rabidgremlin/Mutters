package com.rabidgremlin.mutters.templated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.rabidgremlin.mutters.core.CleanedInput;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.InputCleaner;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.IntentMatcher;

public class TemplatedIntentMatcher
    implements IntentMatcher
{

  private List<TemplatedIntent> intents = new ArrayList<TemplatedIntent>();

  public void addIntent(TemplatedIntent intent)
  {
    intents.add(intent);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.rabidgremlin.mutters.core.IntentMatcher#match(java.lang.String, com.rabidgremlin.mutters.core.Context,
   * Set<String> expectedIntents)
   */
  @Override
  public IntentMatch match(String utterance, Context context, Set<String> expectedIntents, HashMap<String, Object> debugValues)
  {
    // utterance is blank, nothing to match on
    if (StringUtils.isBlank(utterance))
    {
      return null;
    }
    
    CleanedInput cleanedUtterance = InputCleaner.cleanInput(utterance);

    for (TemplatedIntent intent : intents)
    {
      TemplatedUtteranceMatch utteranceMatch = intent.matches(cleanedUtterance, context);
      if (utteranceMatch.isMatched())
      {
        if (expectedIntents == null || (expectedIntents.contains(intent.getName())))
        {
          return new IntentMatch(intent, utteranceMatch.getSlotMatches(), utterance);
        }
      }
    }

    return null;
  }

  public List<TemplatedIntent> getIntents()
  {
    return Collections.unmodifiableList(intents);
  }

}
