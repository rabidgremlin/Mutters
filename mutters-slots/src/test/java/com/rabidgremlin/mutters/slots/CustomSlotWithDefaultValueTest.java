/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.slots;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;

import com.rabidgremlin.mutters.core.SlotMatch;

/**
 * @author wilmol
 */
public class CustomSlotWithDefaultValueTest
{

  @Test
  public void testConstructWithArray()
  {
    String[] options = { "optionA", "optionB" };
    CustomSlotWithDefaultValue customSlot = new CustomSlotWithDefaultValue("custom-slot", options, "defaultValue");

    Optional<SlotMatch<String>> match = customSlot.match("optiona", null);

    assertTrue(match.isPresent());
    assertThat(match.get().getOriginalValue(), is("optiona"));
    assertThat(match.get().getValue(), is("optionA"));
    assertThat(match.get().getSlot(), is(customSlot));
    assertThat(customSlot.getDefaultValue(), is("defaultValue"));
  }

  @Test
  public void testConstructWithList()
  {
    List<String> options = Arrays.asList("optionA", "optionB");
    CustomSlotWithDefaultValue customSlot = new CustomSlotWithDefaultValue("custom-slot", options, "defaultValue");

    Optional<SlotMatch<String>> match = customSlot.match("optiona", null);

    assertTrue(match.isPresent());
    assertThat(match.get().getOriginalValue(), is("optiona"));
    assertThat(match.get().getValue(), is("optionA"));
    assertThat(match.get().getSlot(), is(customSlot));
    assertThat(customSlot.getDefaultValue(), is("defaultValue"));
  }

  @Test
  public void testConstructWithMap()
  {
    Map<String, String> options = new HashMap<>();
    options.put("keyA", "valueA");
    options.put("keyB", "valueB");
    CustomSlotWithDefaultValue customSlot = new CustomSlotWithDefaultValue("custom-slot", options, "defaultValue");

    Optional<SlotMatch<String>> match = customSlot.match("KEYA", null);

    assertTrue(match.isPresent());
    assertThat(match.get().getOriginalValue(), is("KEYA"));
    assertThat(match.get().getValue(), is("valueA"));
    assertThat(match.get().getSlot(), is(customSlot));
    assertThat(customSlot.getDefaultValue(), is("defaultValue"));
  }

  @Test
  public void testToStringConstructedWithList()
  {
    List<String> options = Arrays.asList("optionA", "optionB");
    CustomSlotWithDefaultValue customSlot = new CustomSlotWithDefaultValue("custom-slot", options, "defaultValue");

    assertThat(customSlot.toString(), is("CustomSlot [name=custom-slot, options={optiona=optionA, optionb=optionB}]"));
  }

  @Test
  public void testToStringConstructedWithMap()
  {
    Map<String, String> options = new HashMap<>();
    options.put("keyA", "valueA");
    options.put("keyB", "valueB");
    CustomSlotWithDefaultValue customSlot = new CustomSlotWithDefaultValue("custom-slot", options, "defaultOption");

    assertThat(customSlot.toString(), is("CustomSlot [name=custom-slot, options={keya=valueA, keyb=valueB}]"));
  }
}
