/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.slots;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.rabidgremlin.mutters.core.SlotMatch;

/**
 * @author wilmol
 */
class CustomSlotWithDefaultValueTest
{

  @Test
  void testConstructWithArray()
  {
    String[] options = { "optionA", "optionB" };
    CustomSlotWithDefaultValue customSlot = new CustomSlotWithDefaultValue("custom-slot", options, "defaultValue");

    Optional<SlotMatch<String>> match = customSlot.match("optiona", null);

    assertThat(match).isPresent();
    assertThat(match.get().getOriginalValue()).isEqualTo("optiona");
    assertThat(match.get().getValue()).isEqualTo("optionA");
    assertThat(match.get().getSlot()).isEqualTo(customSlot);
    assertThat(customSlot.getDefaultValue()).isEqualTo("defaultValue");
  }

  @Test
  void testConstructWithList()
  {
    List<String> options = Arrays.asList("optionA", "optionB");
    CustomSlotWithDefaultValue customSlot = new CustomSlotWithDefaultValue("custom-slot", options, "defaultValue");

    Optional<SlotMatch<String>> match = customSlot.match("optiona", null);

    assertThat(match).isPresent();
    assertThat(match.get().getOriginalValue()).isEqualTo("optiona");
    assertThat(match.get().getValue()).isEqualTo("optionA");
    assertThat(match.get().getSlot()).isEqualTo(customSlot);
    assertThat(customSlot.getDefaultValue()).isEqualTo("defaultValue");
  }

  @Test
  void testConstructWithMap()
  {
    Map<String, String> options = new HashMap<>();
    options.put("keyA", "valueA");
    options.put("keyB", "valueB");
    CustomSlotWithDefaultValue customSlot = new CustomSlotWithDefaultValue("custom-slot", options, "defaultValue");

    Optional<SlotMatch<String>> match = customSlot.match("KEYA", null);

    assertThat(match).isPresent();
    assertThat(match.get().getOriginalValue()).isEqualTo("KEYA");
    assertThat(match.get().getValue()).isEqualTo("valueA");
    assertThat(match.get().getSlot()).isEqualTo(customSlot);
    assertThat(customSlot.getDefaultValue()).isEqualTo("defaultValue");
  }

  @Test
  void testToStringConstructedWithList()
  {
    List<String> options = Arrays.asList("optionA", "optionB");
    CustomSlotWithDefaultValue customSlot = new CustomSlotWithDefaultValue("custom-slot", options, "defaultValue");

    assertThat(customSlot.toString())
        .isEqualTo("CustomSlot [name=custom-slot, options={optiona=optionA, optionb=optionB}]");
  }

  @Test
  void testToStringConstructedWithMap()
  {
    Map<String, String> options = new HashMap<>();
    options.put("keyA", "valueA");
    options.put("keyB", "valueB");
    CustomSlotWithDefaultValue customSlot = new CustomSlotWithDefaultValue("custom-slot", options, "defaultOption");

    assertThat(customSlot.toString()).isEqualTo("CustomSlot [name=custom-slot, options={keya=valueA, keyb=valueB}]");
  }
}
