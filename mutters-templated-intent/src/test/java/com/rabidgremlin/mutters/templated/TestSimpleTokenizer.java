/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.templated;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TestSimpleTokenizer
{
  private final SimpleTokenizer tokenizer = new SimpleTokenizer();

  private final SimpleTokenizer tokenizerLower = new SimpleTokenizer(true);

  private void checkResult(String inputText, String[] expectedTokens)
  {
    String[] tokenizedInput = tokenizer.tokenize(inputText);
    assertThat(tokenizedInput, is(notNullValue()));

    assertThat(tokenizedInput.length, is(expectedTokens.length));
    assertThat(tokenizedInput, is(expectedTokens));
  }

  private void checkResultWithLower(String inputText, String[] expectedTokens)
  {
    String[] tokenizedInput = tokenizerLower.tokenize(inputText);
    assertThat(tokenizedInput, is(notNullValue()));

    assertThat(tokenizedInput.length, is(expectedTokens.length));
    assertThat(tokenizedInput, is(expectedTokens));
  }

  @Test
  public void testRemoveQuestionMarks()
  {
    checkResult("Whats the time?", new String[] { "Whats", "the", "time" });
    checkResult("Whats the time ?", new String[] { "Whats", "the", "time" });
    checkResult("Whats the time ????", new String[] { "Whats", "the", "time" });
    checkResult("Whats the time????", new String[] { "Whats", "the", "time" });
    checkResult("Is this a test? Im not sure", new String[] { "Is", "this", "a", "test", "Im", "not", "sure" });
  }

  @Test
  public void testRemoveSingleQuotes()
  {
    checkResult("What's the time", new String[] { "Whats", "the", "time" });
    checkResult("What's the time", new String[] { "Whats", "the", "time" });
    checkResult("What's the time", new String[] { "Whats", "the", "time" });
    checkResult("What's the time", new String[] { "Whats", "the", "time" });
    checkResult("Is this a test? Im not sure", new String[] { "Is", "this", "a", "test", "Im", "not", "sure" });
  }

  @Test
  public void testRemoveExclamationMarks()
  {
    checkResult("Hello there!", new String[] { "Hello", "there" });
    checkResult("Hello there !", new String[] { "Hello", "there" });
    checkResult("Hello there !!!!", new String[] { "Hello", "there" });
    checkResult("Hello there!!!!", new String[] { "Hello", "there" });
    checkResult("Hello! there!", new String[] { "Hello", "there" });
  }

  @Test
  public void testRemoveCommaMarks()
  {
    checkResult("Hello, there", new String[] { "Hello", "there" });
    checkResult("Hello there,", new String[] { "Hello", "there" });
    checkResult("Hello,, there", new String[] { "Hello", "there" });
    checkResult("Hello ,, there", new String[] { "Hello", "there" });
    checkResult("Hello, there,", new String[] { "Hello", "there" });
  }

  @Test
  public void testRemoveFullStops()
  {
    checkResult("Hello.", new String[] { "Hello" });
    checkResult("Hello there.", new String[] { "Hello", "there" });
    checkResult("Hello.. there", new String[] { "Hello", "there" });
    checkResult(".Hello . there", new String[] { "Hello", "there" });
    checkResult("Hello. there,", new String[] { "Hello", "there" });
  }

  @Test
  public void testPreserveNumbers()
  {
    checkResult("1.7889", new String[] { "1.7889" });
    checkResult("1.", new String[] { "1." });
    checkResult("200.12", new String[] { "200.12" });
    checkResult(".57", new String[] { ".57" });
    checkResult("-100", new String[] { "-100" });
    checkResult("-1.45", new String[] { "-1.45" });
  }

  @Test
  public void testPreserveDollars()
  {
    checkResult("$23.89", new String[] { "$23.89" });
    checkResult("$12", new String[] { "$12" });
    checkResult("12$", new String[] { "12" });
  }

  @Test
  public void testPreserveDateAndTimes()
  {
    checkResult("30/05/1974", new String[] { "30/05/1974" });
    checkResult("23:13", new String[] { "23:13" });
  }

  @Test
  public void testPreserveEmailAddress()
  {
    checkResult("bob@test.com", new String[] { "bob@test.com" });
    checkResult("bob@test.com.", new String[] { "bob@test.com" });
    checkResult("bob@test.com,", new String[] { "bob@test.com" });
    checkResult("bob.o'reily@test.com", new String[] { "bob.o'reily@test.com" });
    checkResult("@home", new String[] { "home" });
  }

  @Test
  public void testTrimming()
  {
    checkResult(" Hi  there ", new String[] { "Hi", "there" });
    checkResult(" Hi", new String[] { "Hi" });
  }

  @Test
  public void testSlots()
  {
    checkResult("I like {Color} and {Food}", new String[] { "I", "like", "{Color}", "and", "{Food}" });
  }

  @Test
  public void testLowerCasing()
  {
    checkResultWithLower("I like {Color} and {Food}", new String[] { "i", "like", "{Color}", "and", "{Food}" });
    checkResultWithLower(".Hello . there", new String[] { "hello", "there" });
    checkResultWithLower("Is this a test? I'm not sure",
        new String[] { "is", "this", "a", "test", "im", "not", "sure" });
  }

  @Test
  public void testOrdinalsParseOk()
  {
    checkResult("the 3rd of November", new String[] { "the", "3rd", "of", "November" });
    checkResult("I came 5th", new String[] { "I", "came", "5th" });
    checkResult("We are in 1st place", new String[] { "We", "are", "in", "1st", "place" });
  }

}
