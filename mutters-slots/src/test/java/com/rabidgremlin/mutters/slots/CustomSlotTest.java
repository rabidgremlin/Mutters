/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.slots;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.rabidgremlin.mutters.core.SlotMatch;

/**
 * @author wilmol
 */
class CustomSlotTest
{

  @Test
  void testConstructWithVarargs()
  {
    CustomSlot customSlot = new CustomSlot("custom-slot", "optionA", "optionB");

    Optional<SlotMatch<String>> match = customSlot.match("optiona", null);

    assertThat(match).isPresent();
    assertThat(match.get().getOriginalValue()).isEqualTo("optiona");
    assertThat(match.get().getValue()).isEqualTo("optionA");
    assertThat(match.get().getSlot()).isEqualTo(customSlot);
  }

  @Test
  void testConstructWithList()
  {
    CustomSlot customSlot = new CustomSlot("custom-slot", Arrays.asList("optionA", "optionB"));

    Optional<SlotMatch<String>> match = customSlot.match("optionb", null);

    assertThat(match).isPresent();
    assertThat(match.get().getOriginalValue()).isEqualTo("optionb");
    assertThat(match.get().getValue()).isEqualTo("optionB");
    assertThat(match.get().getSlot()).isEqualTo(customSlot);
  }

  @Test
  void testConstructWithMap()
  {
    Map<String, String> map = new HashMap<>();
    map.put("keyA", "valueA");
    map.put("keyB", "valueB");
    CustomSlot customSlot = new CustomSlot("custom-slot", map);

    Optional<SlotMatch<String>> match = customSlot.match("keya", null);

    assertThat(match).isPresent();
    assertThat(match.get().getOriginalValue()).isEqualTo("keya");
    assertThat(match.get().getValue()).isEqualTo("valueA");
    assertThat(match.get().getSlot()).isEqualTo(customSlot);
  }

  @Test
  void testToStringConstructedWithList()
  {
    CustomSlot customSlot = new CustomSlot("custom-slot", Arrays.asList("ABC", "xyz"));

    assertThat(customSlot.toString()).isEqualTo("CustomSlot [name=custom-slot, options={abc=ABC, xyz=xyz}]");
  }

  @Test
  void testToStringConstructedWithMap()
  {
    Map<String, String> map = new HashMap<>();
    map.put("keyA", "valueA");
    map.put("keyB", "valueB");
    CustomSlot customSlot = new CustomSlot("custom-slot", map);

    assertThat(customSlot.toString()).isEqualTo("CustomSlot [name=custom-slot, options={keya=valueA, keyb=valueB}]");
  }
}
