/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.slots;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.rabidgremlin.mutters.core.SlotMatch;

/**
 * @author wilmol
 */
class FuzzySlotTest
{

  @Test
  void testConstructWithArrayDefaultTolerance()
  {
    FuzzySlot fuzzySlot = new FuzzySlot("fuzzy-slot", "optionA", "optionB");

    Optional<SlotMatch<String>> match = fuzzySlot.match("optiona", null);

    assertThat(match).isPresent();
    assertThat(match.get().getOriginalValue()).isEqualTo("optiona");
    assertThat(match.get().getValue()).isEqualTo("optionA");
    assertThat(match.get().getSlot()).isEqualTo(fuzzySlot);
  }

  @Test
  void testConstructWithListDefaultTolerance()
  {
    List<String> options = Arrays.asList("optionA", "optionB");
    FuzzySlot fuzzySlot = new FuzzySlot("fuzzy-slot", options);

    Optional<SlotMatch<String>> match = fuzzySlot.match("optiona", null);

    assertThat(match).isPresent();
    assertThat(match.get().getOriginalValue()).isEqualTo("optiona");
    assertThat(match.get().getValue()).isEqualTo("optionA");
    assertThat(match.get().getSlot()).isEqualTo(fuzzySlot);
  }

  @Test
  void testConstructWithArrayCustomTolerance()
  {
    String[] options = { "optionA", "optionB" };
    FuzzySlot fuzzySlot = new FuzzySlot("fuzzy-slot", options, 0.99);

    Optional<SlotMatch<String>> match = fuzzySlot.match("optiona", null);

    assertThat(match).isPresent();
    assertThat(match.get().getOriginalValue()).isEqualTo("optiona");
    assertThat(match.get().getValue()).isEqualTo("optionA");
    assertThat(match.get().getSlot()).isEqualTo(fuzzySlot);
  }

  @Test
  void testConstructWithListCustomTolerance()
  {
    List<String> options = Arrays.asList("optionA", "optionB");
    FuzzySlot fuzzySlot = new FuzzySlot("fuzzy-slot", options, 0.99);

    Optional<SlotMatch<String>> match = fuzzySlot.match("optiona", null);

    assertThat(match).isPresent();
    assertThat(match.get().getOriginalValue()).isEqualTo("optiona");
    assertThat(match.get().getValue()).isEqualTo("optionA");
    assertThat(match.get().getSlot()).isEqualTo(fuzzySlot);
  }

  @Test
  void testConstructWithArrayRejectsToleranceBelow10Percent()
  {
    String[] options = { "optionA", "optionB" };
    IllegalArgumentException expected = assertThrows(IllegalArgumentException.class,
        () -> new FuzzySlot("fuzzy-slot", options, 0.09));
    assertThat(expected).hasMessageThat().isEqualTo("Invalid tolerance: 0.09");
  }

  @Test
  void testConstructWithListRejectsToleranceBelow10Percent()
  {
    List<String> options = Arrays.asList("optionA", "optionB");
    IllegalArgumentException expected = assertThrows(IllegalArgumentException.class,
        () -> new FuzzySlot("fuzzy-slot", options, 0.09));
    assertThat(expected).hasMessageThat().isEqualTo("Invalid tolerance: 0.09");
  }

  @Test
  void testConstructWithArrayRejectsToleranceAbove100Percent()
  {
    String[] options = { "optionA", "optionB" };
    IllegalArgumentException expected = assertThrows(IllegalArgumentException.class,
        () -> new FuzzySlot("fuzzy-slot", options, 1.01));
    assertThat(expected).hasMessageThat().isEqualTo("Invalid tolerance: 1.01");
  }

  @Test
  void testConstructWithListRejectsToleranceAbove100Percent()
  {
    List<String> options = Arrays.asList("optionA", "optionB");
    IllegalArgumentException expected = assertThrows(IllegalArgumentException.class,
        () -> new FuzzySlot("fuzzy-slot", options, 1.01));
    assertThat(expected).hasMessageThat().isEqualTo("Invalid tolerance: 1.01");
  }

  @Test
  void testToString()
  {
    List<String> options = Arrays.asList("optionA", "optionB");
    FuzzySlot fuzzySlot = new FuzzySlot("fuzzy-slot", options);

    assertThat(fuzzySlot.toString())
        .isEqualTo("FuzzySlot [name=fuzzy-slot, options=[optionA, optionB], tolerance=0.95]");
  }
}
