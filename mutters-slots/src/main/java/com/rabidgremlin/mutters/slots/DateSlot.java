package com.rabidgremlin.mutters.slots;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.Slot;
import com.rabidgremlin.mutters.core.SlotMatch;

/**
 * Slot that matches on dates. Uses natty to handle 'dates' such as 'next Friday'.
 * 
 * @author rabidgremlin
 *
 */
public class DateSlot
    extends Slot
{

  private String name;

  public DateSlot(String name)
  {
    this.name = name;
  }

  private LocalDate tryParse(String token, DateTimeFormatter fmt)
  {
    try
    {
      return fmt.parseLocalDate(token);
    }
    catch (IllegalArgumentException e)
    {
      return null;
    }
  }

  private DateTimeFormatter fixupFormatter(Context context, int currentYear, DateTimeFormatter fmt)
  {
    return fmt.withLocale(context.getLocale()).withDefaultYear(currentYear).withPivotYear(currentYear);
  }

  @Override
  public SlotMatch match(String token, Context context)
  {
    // grab current year to use as default and pivot year
    int currentYear = LocalDate.now().getYear();

    // try parse in short format for locale
    LocalDate date = tryParse(token, fixupFormatter(context, currentYear, DateTimeFormat.shortDate()));
    if (date != null)
    {
      return new SlotMatch(this, token, date);
    }

    // try parse in medium format for locale
    date = tryParse(token, fixupFormatter(context, currentYear, DateTimeFormat.mediumDate()));
    if (date != null)
    {
      return new SlotMatch(this, token, date);
    }

    // try parse in long format for locale
    date = tryParse(token, fixupFormatter(context, currentYear, DateTimeFormat.longDate()));
    if (date != null)
    {
      return new SlotMatch(this, token, date);
    }

    // HACK: try parse dd/MM format if we are in NZ
    if (context.getLocale().getCountry().equals("NZ"))
    {
      date = tryParse(token, fixupFormatter(context, currentYear, DateTimeFormat.forPattern("dd/MM")));
      if (date != null)
      {
        return new SlotMatch(this, token, date);
      }
    }

    // try parse with natty
    Parser parser = new Parser(context.getTimeZone());

    List<DateGroup> groups = parser.parse(token);
    for (DateGroup group : groups)
    {
      if (!group.isDateInferred())
      {
        List<Date> dates = group.getDates();

        // natty is very aggressive so will match date on text that is largely not a date, which is
        // not what we want
        String matchText = group.getText();
        float percMatch = (float) matchText.length() / (float) token.length();

        if (!dates.isEmpty() && percMatch > 0.75)
        {
          DateTime theDateTime = new DateTime(dates.get(0),
              DateTimeZone.forTimeZone(context.getTimeZone()));
          return new SlotMatch(this, token, theDateTime.toLocalDate());
        }
      }
    }

    return null;
  }

  @Override
  public String getName()
  {
    return name;
  }

}
