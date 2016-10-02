package com.rabidgremlin.mutters.core;

public interface IntentMatcher
{

  IntentMatch match(String utterance, Context context);

}
