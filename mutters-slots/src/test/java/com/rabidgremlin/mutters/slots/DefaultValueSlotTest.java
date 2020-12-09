/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.slots;

import static com.google.common.truth.Truth.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.SlotMatch;

/**
 * @author wilmol
 */
class DefaultValueSlotTest
{

  @Test
  void testDefaultStringSlot()
  {
    DefaultValueSlot<String> defaultValueSlot = new DefaultValueSlot.DefaultStringSlot()
    {
      @Override
      public Optional<SlotMatch<String>> match(String token, Context context)
      {
        return Optional.empty();
      }

      @Override
      public String getName()
      {
        return "slot";
      }
    };

    assertThat(defaultValueSlot.getDefaultValue()).isEmpty();
  }

  @Test
  void testDefaultNumberSlot()
  {
    DefaultValueSlot<Number> defaultValueSlot = new DefaultValueSlot.DefaultNumberSlot()
    {
      @Override
      public Optional<SlotMatch<Number>> match(String token, Context context)
      {
        return Optional.empty();
      }

      @Override
      public String getName()
      {
        return "slot";
      }
    };

    assertThat(defaultValueSlot.getDefaultValue()).isEqualTo(-1);
  }

  @Test
  void testDefaultLongSlot()
  {
    DefaultValueSlot<Long> defaultValueSlot = new DefaultValueSlot.DefaultLongSlot()
    {
      @Override
      public Optional<SlotMatch<Long>> match(String token, Context context)
      {
        return Optional.empty();
      }

      @Override
      public String getName()
      {
        return "slot";
      }
    };

    assertThat(defaultValueSlot.getDefaultValue()).isEqualTo(-1L);
  }

  @Test
  void testDefaultIntegerSlot()
  {
    DefaultValueSlot<Integer> defaultValueSlot = new DefaultValueSlot.DefaultIntegerSlot()
    {
      @Override
      public Optional<SlotMatch<Integer>> match(String token, Context context)
      {
        return Optional.empty();
      }

      @Override
      public String getName()
      {
        return "slot";
      }
    };

    assertThat(defaultValueSlot.getDefaultValue()).isEqualTo(-1);
  }

  @Test
  void testDefaultDoubleSlot()
  {
    DefaultValueSlot<Double> defaultValueSlot = new DefaultValueSlot.DefaultDoubleSlot()
    {
      @Override
      public Optional<SlotMatch<Double>> match(String token, Context context)
      {
        return Optional.empty();
      }

      @Override
      public String getName()
      {
        return "slot";
      }
    };

    assertThat(defaultValueSlot.getDefaultValue()).isEqualTo(-1d);
  }

  @Test
  void testDefaultDateSlot()
  {
    DefaultValueSlot<LocalDate> defaultValueSlot = new DefaultValueSlot.DefaultDateSlot()
    {
      @Override
      public Optional<SlotMatch<LocalDate>> match(String token, Context context)
      {
        return Optional.empty();
      }

      @Override
      public String getName()
      {
        return "slot";
      }
    };

    assertThat(defaultValueSlot.getDefaultValue()).isEqualTo(LocalDate.MIN);
  }

  @Test
  void testDefaultTimeSlot()
  {
    DefaultValueSlot<LocalTime> defaultValueSlot = new DefaultValueSlot.DefaultTimeSlot()
    {
      @Override
      public Optional<SlotMatch<LocalTime>> match(String token, Context context)
      {
        return Optional.empty();
      }

      @Override
      public String getName()
      {
        return "slot";
      }
    };

    assertThat(defaultValueSlot.getDefaultValue()).isEqualTo(LocalTime.MIN);
  }
}
