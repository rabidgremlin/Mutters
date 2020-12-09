/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters;

import static com.google.common.truth.Truth.assertThat;

import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.templated.SimpleTokenizer;
import com.rabidgremlin.mutters.templated.TemplatedIntent;
import com.rabidgremlin.mutters.templated.TemplatedIntentMatcher;

class TestTemplatedIntentFiltering
{
  private final SimpleTokenizer tokenizer = new SimpleTokenizer();
  private TemplatedIntentMatcher matcher;

  @BeforeEach
  void setUpMatcher()
  {
    matcher = new TemplatedIntentMatcher(tokenizer);

    TemplatedIntent intent = matcher.addIntent("HelloIntent");
    intent.addUtterance("hello");
    intent.addUtterance("hi");
    intent.addUtterance("hiya");

    intent = matcher.addIntent("GoodbyeIntent");
    intent.addUtterance("goodbye");
    intent.addUtterance("bye");
  }

  @Test
  void testNoFiltering()
  {
    // should match on hello intent
    IntentMatch intentMatch = matcher.match("hello", new Context(), null);

    assertThat(intentMatch).isNotNull();
    assertThat(intentMatch.getIntent()).isNotNull();
    assertThat(intentMatch.getIntent().getName()).isEqualTo("HelloIntent");

    // should match on goodbye intent
    intentMatch = matcher.match("bye", new Context(), null);

    assertThat(intentMatch).isNotNull();
    assertThat(intentMatch.getIntent()).isNotNull();
    assertThat(intentMatch.getIntent().getName()).isEqualTo("GoodbyeIntent");
  }

  @Test
  void testFiltering()
  {
    HashSet<String> expectedIntents = new HashSet<>();
    expectedIntents.add("HelloIntent");

    // should match on hello intent
    IntentMatch intentMatch = matcher.match("hello", new Context(), expectedIntents);

    assertThat(intentMatch).isNotNull();
    assertThat(intentMatch.getIntent()).isNotNull();
    assertThat(intentMatch.getIntent().getName()).isEqualTo("HelloIntent");

    // should not match on goodbye intent (its not in expected intents)
    intentMatch = matcher.match("bye", new Context(), expectedIntents);

    assertThat(intentMatch).isNotNull();
    assertThat(intentMatch.matched()).isFalse();
  }
}
