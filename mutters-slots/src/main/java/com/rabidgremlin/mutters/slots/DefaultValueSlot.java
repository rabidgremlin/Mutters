/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.slots;

import java.time.LocalDate;
import java.time.LocalTime;

import com.rabidgremlin.mutters.core.Slot;

/**
 * An interface for a slot that has a default value, for cases when no slot
 * value is found.
 *
 * @author campbell18
 * @author wilmol
 */
public interface DefaultValueSlot<T> extends Slot<T>
{

  T getDefaultValue();

  /**
   * {@link DefaultValueSlot} which defaults to the empty String.
   */
  interface DefaultStringSlot extends DefaultValueSlot<String>
  {
    @Override
    default String getDefaultValue()
    {
      return "";
    }
  }

  /**
   * {@link DefaultValueSlot} which defaults to -1.
   */
  interface DefaultNumberSlot extends DefaultValueSlot<Number>
  {
    @Override
    default Number getDefaultValue()
    {
      return -1;
    }
  }

  /**
   * {@link DefaultValueSlot} which defaults to -1.
   */
  interface DefaultLongSlot extends DefaultValueSlot<Long>
  {
    @Override
    default Long getDefaultValue()
    {
      return -1L;
    }
  }

  /**
   * {@link DefaultValueSlot} which defaults to -1.
   */
  interface DefaultIntegerSlot extends DefaultValueSlot<Integer>
  {
    @Override
    default Integer getDefaultValue()
    {
      return -1;
    }
  }

  /**
   * {@link DefaultValueSlot} which defaults to -1.
   */
  interface DefaultDoubleSlot extends DefaultValueSlot<Double>
  {
    @Override
    default Double getDefaultValue()
    {
      return -1d;
    }
  }

  /**
   * {@link DefaultValueSlot} which defaults to {@link LocalDate#MIN}.
   */
  interface DefaultDateSlot extends DefaultValueSlot<LocalDate>
  {
    @Override
    default LocalDate getDefaultValue()
    {
      // "-999999999-01-01"
      return LocalDate.MIN;
    }
  }

  /**
   * {@link DefaultValueSlot} which defaults to {@link LocalTime#MIN}.
   */
  interface DefaultTimeSlot extends DefaultValueSlot<LocalTime>
  {
    @Override
    default LocalTime getDefaultValue()
    {
      // "00:00"
      return LocalTime.MIN;
    }
  }
}
