# Mutters templated intent
This is an IntentMatcher implementation that uses templates to match against a user's input.

A ``TemplatedIntentMatcher`` often works better then machine learning based matchers for very short strings (3 words or less) or when you want an exact phrase to match. 

It is often effective to use a ``CompoundIntentMatcher`` to first try match against a ``TemplatedIntentMatcher`` before trying to match against a machine learning based IntentMatcher.

This package also provides an ``UtteranceGenerator`` which makes it easy to generate a large number of templates from a simple definition.

For example:
```
~what|what's|what is~ ~the|~ time ~in|at~ {Place}
```

Will generate generate 12 different template strings including:
  
```
what the time in {Place}
what's the time in {Place}
what is the time in {Place}
what is the time at {Place}
what is time at {Place}
etc
```


