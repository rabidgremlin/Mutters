package com.rabidgremlin.mutters.core;

public class CompoundInputMatcher
    implements IntentMatcher
{
  private IntentMatcher firstMatcher;

  private IntentMatcher secondMatcher;

  public CompoundInputMatcher(IntentMatcher firstSlot, IntentMatcher secondSlot)
  {
    this.firstMatcher = firstSlot;
    this.secondMatcher = secondSlot;
  }

  @Override
  public IntentMatch match(String utterance, Context context)
  {
    IntentMatch match = firstMatcher.match(utterance, context);
    if (match == null)
    {
      match = secondMatcher.match(utterance, context);
    }

    return match;
  }
}
