/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.slots;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.util.Date;
import java.util.List;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.Slot;
import com.rabidgremlin.mutters.core.SlotMatch;

/**
 * Slot that matches on dates (into a {@link LocalDate}). Uses natty to handle
 * 'dates' such as 'next Friday'.
 * 
 * @author rabidgremlin
 *
 */
public class DateSlot extends Slot
{

  private final String name;

  public DateSlot(String name)
  {
    this.name = name;
  }

  @Override
  public SlotMatch match(String token, Context context)
  {
    // grab current year to use as default year
    int currentYear = LocalDate.now().getYear();

    // try parse in short format for locale
    LocalDate date = tryParse(token, currentYear,
        DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(context.getLocale()));
    if (date != null)
    {
      return new SlotMatch(this, token, date);
    }

    // try parse in medium format for locale
    date = tryParse(token, currentYear,
        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(context.getLocale()));
    if (date != null)
    {
      return new SlotMatch(this, token, date);
    }

    // try parse in long format for locale
    date = tryParse(token, currentYear,
        DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(context.getLocale()));
    if (date != null)
    {
      return new SlotMatch(this, token, date);
    }

    // try other formats
    date = tryParse(token, currentYear,
        DateTimeFormatter.ofPattern("d[/][-][.][ ]M[/][-][.][ ][yyyy][yy]").withLocale(context.getLocale()));
    if (date != null)
    {
      return new SlotMatch(this, token, date);
    }

    // try parse with natty
    Parser parser = new Parser(context.getTimeZone());

    List<DateGroup> groups = parser.parse(token);
    for (DateGroup group : groups)
    {
      if (!group.isDateInferred())
      {
        List<Date> dates = group.getDates();

        // natty is very aggressive so will match date on text that is largely not a
        // date, which is
        // not what we want
        String matchText = group.getText();
        float percMatch = (float) matchText.length() / (float) token.length();

        if (!dates.isEmpty() && percMatch > 0.75)
        {
          ZonedDateTime theDateTime = ZonedDateTime.ofInstant(dates.get(0).toInstant(),
              context.getTimeZone().toZoneId());
          LocalDate localDate = theDateTime.toLocalDate();
          return new SlotMatch(this, token, localDate);
        }
      }
    }
    return null;
  }

  private LocalDate tryParse(String token, int currentYear, DateTimeFormatter fmt)
  {
    try
    {
      // workaround for migrating jodatime 'withDefault...' see:
      // https://stackoverflow.com/a/49815584/6122976

      // init the defaults
      LocalDate defaults = LocalDate.of(currentYear, Month.JANUARY, 1);
      // parse string
      TemporalAccessor parsed = fmt.parse(token);
      // override defaults with parsed values
      ChronoField[] fieldsToOverride = { ChronoField.YEAR, ChronoField.MONTH_OF_YEAR, ChronoField.DAY_OF_MONTH };
      for (TemporalField fieldToOverride : fieldsToOverride)
      {
        if (parsed.isSupported(fieldToOverride))
        {
          defaults = defaults.with(fieldToOverride, parsed.getLong(fieldToOverride));
        }
      }

      return defaults;
    }
    catch (DateTimeParseException e)
    {
      return null;
    }
  }

  @Override
  public String getName()
  {
    return name;
  }
}
