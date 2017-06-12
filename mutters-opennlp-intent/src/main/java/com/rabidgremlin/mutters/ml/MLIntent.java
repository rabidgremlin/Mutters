package com.rabidgremlin.mutters.ml;

import com.rabidgremlin.mutters.core.Intent;

/**
 * This is the class for machine learning (ML) based intents. The named entity recognition (NER) models added to the
 * MLIntentMatcher with addSlotModel are used to populate the slots attached to the MLIntent.
 * 
 * 
 * @see com.rabidgremlin.mutters.ml.MLIntentMatcher
 * 
 * @author rabidgremlin
 *
 */
public class MLIntent
    extends Intent
{

  /**
   * Constructor.
   * 
   * @param name The name of the intent.
   */
  public MLIntent(String name)
  {
    super(name);
  }

}
