/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.generate;

import static com.google.common.truth.Truth.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

class TestUtteranceGenerator
{
  @Test
  void testGenerator()
  {
    UtteranceGenerator generator = new UtteranceGenerator();

    List<String> utterances = generator.generate("~what|what's|what is~ ~the|~ time ~in|at~ {Place}");

    System.out.println(utterances);

    assertThat(utterances).isNotNull();
    assertThat(utterances).hasSize(3 * 2 * 1 * 2 * 1);

    assertThat(utterances.get(0)).isEqualTo("what the time in {Place}");
    assertThat(utterances.get(11)).isEqualTo("what is time at {Place}");
  }
}
