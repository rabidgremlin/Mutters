package com.rabidgremlin.mutters;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import com.rabidgremlin.mutters.input.CleanedInput;
import com.rabidgremlin.mutters.input.InputCleaner;

public class TestInputCleaning
{
  private void checkResult(String inputText, String[] expectedTokens)
  {
    CleanedInput cleanedInput = InputCleaner.cleanInput(inputText);
    assertThat(cleanedInput, is(notNullValue()));

    List<String> cleanedTokens = cleanedInput.getCleanedTokens();
    assertThat(cleanedTokens, is(notNullValue()));
    assertThat(cleanedTokens.size(), is(expectedTokens.length));

    for (int loop = 0; loop < expectedTokens.length; loop++)
    {
      assertThat(cleanedTokens.get(loop), is(expectedTokens[loop]));
    }
  }

  @Test
  public void testRemoveQuestionMarks()
  {
    checkResult("What's the time?", new String[]{ "What's", "the", "time" });
    checkResult("What's the time ?", new String[]{ "What's", "the", "time" });
    checkResult("What's the time ????", new String[]{ "What's", "the", "time" });
    checkResult("What's the time????", new String[]{ "What's", "the", "time" });
    checkResult("Is this a test? I'm not sure", new String[]{ "Is", "this", "a", "test", "I'm", "not", "sure" });
  }

  @Test
  public void testRemoveExclamationMarks()
  {
    checkResult("Hello there!", new String[]{ "Hello", "there" });
    checkResult("Hello there !", new String[]{ "Hello", "there" });
    checkResult("Hello there !!!!", new String[]{ "Hello", "there" });
    checkResult("Hello there!!!!", new String[]{ "Hello", "there" });
    checkResult("Hello! there!", new String[]{ "Hello", "there" });
  }

  @Test
  public void testRemoveCommaMarks()
  {
    checkResult("Hello, there", new String[]{ "Hello", "there" });
    checkResult("Hello there,", new String[]{ "Hello", "there" });
    checkResult("Hello,, there", new String[]{ "Hello", "there" });
    checkResult("Hello ,, there", new String[]{ "Hello", "there" });
    checkResult("Hello, there,", new String[]{ "Hello", "there" });
  }

  @Test
  public void testTrimming()
  {
    checkResult(" Hi  there ", new String[]{ "Hi", "there" });
    checkResult(" Hi", new String[]{ "Hi" });
  }

  @Test
  public void testSlots()
  {
    checkResult("I like {Color} and {Food}", new String[]{ "I", "like", "{Color}", "and", "{Food}" });
  }

}
