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

public class DateSlot
    extends Slot
{

  private String name;

  public DateSlot(String name)
  {
    this.name = name;
  }

  @Override
  public SlotMatch match(String token, Context context)
  {
    // first try parse as NZ date as natty doesn't support NZ date formats
    // TODO: use locale on context to choose correct "full" date format to try parse first
    try
    {
      DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
      LocalDate nzDate = fmt.parseLocalDate(token);
      return new SlotMatch(this, token, nzDate);
    }
    catch (IllegalArgumentException e)
    {
      // do nothing
    }

    try
    {
      DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM");
      LocalDate nzDate = fmt.parseLocalDate(token);
      return new SlotMatch(this, token, nzDate);
    }
    catch (IllegalArgumentException e)
    {
      // do nothing
    }

    // try parse with natty
    Parser parser = new Parser(context.getTimeZone());

    List<DateGroup> groups = parser.parse(token);
    for (DateGroup group : groups)
    {
      if (!group.isDateInferred())
      {
        List<Date> dates = group.getDates();

        // natty is very aggressive so will match date on text that is largely not a date, which is not what we want
        String matchText = group.getText();
        float percMatch = (float) matchText.length() / (float) token.length();

        if (!dates.isEmpty() && percMatch > 0.75)
        {
          DateTime theDateTime = new DateTime(dates.get(0), DateTimeZone.forTimeZone(context.getTimeZone()));
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
