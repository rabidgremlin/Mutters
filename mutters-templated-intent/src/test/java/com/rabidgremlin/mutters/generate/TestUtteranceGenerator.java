package com.rabidgremlin.mutters.generate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

public class TestUtteranceGenerator
{
  @Test
  public void testGenerator()
  {
    UtteranceGenerator generator = new UtteranceGenerator();

    List<String> utterances = generator
        .generate("~what|what's|what is~ ~the|~ time ~in|at~ {Place}");

    System.out.println(utterances);

    assertThat(utterances, is(notNullValue()));
    assertThat(utterances.size(), is(3 * 2 * 1 * 2 * 1));

    assertThat(utterances.get(0), is("what the time in {Place}"));
    assertThat(utterances.get(11), is("what is time at {Place}"));
  }
}
