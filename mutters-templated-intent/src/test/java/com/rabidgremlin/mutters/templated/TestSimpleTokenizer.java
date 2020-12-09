/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.templated;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

class TestSimpleTokenizer
{
  private final SimpleTokenizer tokenizer = new SimpleTokenizer();

  private final SimpleTokenizer tokenizerLower = new SimpleTokenizer(true);

  private void checkResult(String inputText, String... expectedTokens)
  {
    String[] tokenizedInput = tokenizer.tokenize(inputText);
    assertThat(tokenizedInput).isNotNull();

    assertThat(tokenizedInput).hasLength(expectedTokens.length);
    assertThat(tokenizedInput).isEqualTo(expectedTokens);
  }

  private void checkResultWithLower(String inputText, String... expectedTokens)
  {
    String[] tokenizedInput = tokenizerLower.tokenize(inputText);
    assertThat(tokenizedInput).isNotNull();

    assertThat(tokenizedInput).hasLength(expectedTokens.length);
    assertThat(tokenizedInput).isEqualTo(expectedTokens);
  }

  @Test
  void testRemoveQuestionMarks()
  {
    checkResult("Whats the time?", "Whats", "the", "time");
    checkResult("Whats the time ?", "Whats", "the", "time");
    checkResult("Whats the time ????", "Whats", "the", "time");
    checkResult("Whats the time????", "Whats", "the", "time");
    checkResult("Is this a test? Im not sure", "Is", "this", "a", "test", "Im", "not", "sure");
  }

  @Test
  void testRemoveSingleQuotes()
  {
    checkResult("What's the time", "Whats", "the", "time");
    checkResult("What's the time", "Whats", "the", "time");
    checkResult("What's the time", "Whats", "the", "time");
    checkResult("What's the time", "Whats", "the", "time");
    checkResult("Is this a test? Im not sure", "Is", "this", "a", "test", "Im", "not", "sure");
  }

  @Test
  void testRemoveExclamationMarks()
  {
    checkResult("Hello there!", "Hello", "there");
    checkResult("Hello there !", "Hello", "there");
    checkResult("Hello there !!!!", "Hello", "there");
    checkResult("Hello there!!!!", "Hello", "there");
    checkResult("Hello! there!", "Hello", "there");
  }

  @Test
  void testRemoveCommaMarks()
  {
    checkResult("Hello, there", "Hello", "there");
    checkResult("Hello there,", "Hello", "there");
    checkResult("Hello,, there", "Hello", "there");
    checkResult("Hello ,, there", "Hello", "there");
    checkResult("Hello, there,", "Hello", "there");
  }

  @Test
  void testRemoveFullStops()
  {
    checkResult("Hello.", "Hello");
    checkResult("Hello there.", "Hello", "there");
    checkResult("Hello.. there", "Hello", "there");
    checkResult(".Hello . there", "Hello", "there");
    checkResult("Hello. there,", "Hello", "there");
  }

  @Test
  void testPreserveNumbers()
  {
    checkResult("1.7889", "1.7889");
    checkResult("1.", "1.");
    checkResult("200.12", "200.12");
    checkResult(".57", ".57");
    checkResult("-100", "-100");
    checkResult("-1.45", "-1.45");
  }

  @Test
  void testPreserveDollars()
  {
    checkResult("$23.89", "$23.89");
    checkResult("$12", "$12");
    checkResult("12$", "12");
  }

  @Test
  void testPreserveDateAndTimes()
  {
    checkResult("30/05/1974", "30/05/1974");
    checkResult("23:13", "23:13");
  }

  @Test
  void testPreserveEmailAddress()
  {
    checkResult("bob@test.com", "bob@test.com");
    checkResult("bob@test.com.", "bob@test.com");
    checkResult("bob@test.com,", "bob@test.com");
    checkResult("bob.o'reily@test.com", "bob.o'reily@test.com");
    checkResult("@home", "home");
  }

  @Test
  void testTrimming()
  {
    checkResult(" Hi  there ", "Hi", "there");
    checkResult(" Hi", "Hi");
  }

  @Test
  void testSlots()
  {
    checkResult("I like {Color} and {Food}", "I", "like", "{Color}", "and", "{Food}");
  }

  @Test
  void testLowerCasing()
  {
    checkResultWithLower("I like {Color} and {Food}", "i", "like", "{Color}", "and", "{Food}");
    checkResultWithLower(".Hello . there", "hello", "there");
    checkResultWithLower("Is this a test? I'm not sure", "is", "this", "a", "test", "im", "not", "sure");
  }

  @Test
  void testOrdinalsParseOk()
  {
    checkResult("the 3rd of November", "the", "3rd", "of", "November");
    checkResult("I came 5th", "I", "came", "5th");
    checkResult("We are in 1st place", "We", "are", "in", "1st", "place");
  }

}
