/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.slots;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import com.rabidgremlin.mutters.core.AbstractSlot;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.SlotMatch;

/**
 * Slot that matches on {@link ZonedDateTime}. Uses natty to handle 'dates' such
 * as 'next Friday at 8pm'.
 * 
 * @author rabidgremlin
 *
 */
public class DateTimeSlot extends AbstractSlot<ZonedDateTime>
{

  public DateTimeSlot(String name)
  {
    super(name);
  }

  @Override
  public Optional<SlotMatch<ZonedDateTime>> match(String token, Context context)
  {

    Parser parser = new Parser(context.getTimeZone());

    List<DateGroup> groups = parser.parse(token);
    for (DateGroup group : groups)
    {
      if (!group.isDateInferred() && !group.isTimeInferred())
      {
        List<Date> dates = group.getDates();

        // natty is very aggressive so will match date on text that is largely not a
        // date, which is
        // not what we want
        String matchText = group.getText();
        float percMatch = (float) matchText.length() / (float) token.length();

        if (!dates.isEmpty() && percMatch > 0.75)
        {
          Date date = dates.get(0);
          TimeZone timeZone = context.getTimeZone();
          return Optional
              .of(new SlotMatch<>(this, token, ZonedDateTime.ofInstant(date.toInstant(), timeZone.toZoneId())));
        }
      }
    }

    return Optional.empty();
  }
}
