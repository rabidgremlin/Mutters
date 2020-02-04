/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.slots;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;

import com.rabidgremlin.mutters.core.SlotMatch;

/**
 * @author wilmol
 */
public class CustomSlotTest
{

  @Test
  public void testConstructWithVarargs()
  {
    CustomSlot customSlot = new CustomSlot("custom-slot", "optionA", "optionB");

    Optional<SlotMatch<String>> match = customSlot.match("optiona", null);

    assertTrue(match.isPresent());
    assertThat(match.get().getOriginalValue(), is("optiona"));
    assertThat(match.get().getValue(), is("optionA"));
    assertThat(match.get().getSlot(), is(customSlot));
  }

  @Test
  public void testConstructWithList()
  {
    CustomSlot customSlot = new CustomSlot("custom-slot", Arrays.asList("optionA", "optionB"));

    Optional<SlotMatch<String>> match = customSlot.match("optionb", null);

    assertTrue(match.isPresent());
    assertThat(match.get().getOriginalValue(), is("optionb"));
    assertThat(match.get().getValue(), is("optionB"));
    assertThat(match.get().getSlot(), is(customSlot));
  }

  @Test
  public void testConstructWithMap()
  {
    Map<String, String> map = new HashMap<>();
    map.put("keyA", "valueA");
    map.put("keyB", "valueB");
    CustomSlot customSlot = new CustomSlot("custom-slot", map);

    Optional<SlotMatch<String>> match = customSlot.match("keya", null);

    assertTrue(match.isPresent());
    assertThat(match.get().getOriginalValue(), is("keya"));
    assertThat(match.get().getValue(), is("valueA"));
    assertThat(match.get().getSlot(), is(customSlot));
  }

  @Test
  public void testToStringConstructedWithList()
  {
    CustomSlot customSlot = new CustomSlot("custom-slot", Arrays.asList("ABC", "xyz"));

    assertThat(customSlot.toString(), is("CustomSlot [name=custom-slot, options={abc=ABC, xyz=xyz}]"));
  }

  @Test
  public void testToStringConstructedWithMap()
  {
    Map<String, String> map = new HashMap<>();
    map.put("keyA", "valueA");
    map.put("keyB", "valueB");
    CustomSlot customSlot = new CustomSlot("custom-slot", map);

    assertThat(customSlot.toString(), is("CustomSlot [name=custom-slot, options={keya=valueA, keyb=valueB}]"));
  }
}
