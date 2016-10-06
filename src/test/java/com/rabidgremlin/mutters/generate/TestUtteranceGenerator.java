package com.rabidgremlin.mutters.generate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import com.rabidgremlin.mutters.templated.TemplatedUtterance;

public class TestUtteranceGenerator
{
  @Test
  public void testGenerator()
  {
    UtteranceGenerator generator = new UtteranceGenerator();

    List<TemplatedUtterance> utterances = generator
        .generate("~what|what's|what is~ ~the|~ time ~in|at~ {Place}");

    System.out.println(utterances);

    assertThat(utterances, is(notNullValue()));
    assertThat(utterances.size(), is(3 * 2 * 1 * 2 * 1));

    assertThat(utterances.get(0).getTemplate(), is("what the time in {Place}"));
    assertThat(utterances.get(11).getTemplate(), is("what is time at {Place}"));
  }
}
