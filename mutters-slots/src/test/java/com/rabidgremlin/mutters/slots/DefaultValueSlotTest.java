/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.slots;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.SlotMatch;

/**
 * @author wilmol
 */
public class DefaultValueSlotTest
{

  @Test
  public void testDefaultStringSlot()
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

    assertThat(defaultValueSlot.getDefaultValue(), is(""));
  }

  @Test
  public void testDefaultNumberSlot()
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

    assertThat(defaultValueSlot.getDefaultValue(), is(-1));
  }

  @Test
  public void testDefaultLongSlot()
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

    assertThat(defaultValueSlot.getDefaultValue(), is(-1L));
  }

  @Test
  public void testDefaultIntegerSlot()
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

    assertThat(defaultValueSlot.getDefaultValue(), is(-1));
  }

  @Test
  public void testDefaultDoubleSlot()
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

    assertThat(defaultValueSlot.getDefaultValue(), is(-1d));
  }

  @Test
  public void testDefaultDateSlot()
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

    assertThat(defaultValueSlot.getDefaultValue(), is(LocalDate.MIN));
  }

  @Test
  public void testDefaultTimeSlot()
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

    assertThat(defaultValueSlot.getDefaultValue(), is(LocalTime.MIN));
  }
}
