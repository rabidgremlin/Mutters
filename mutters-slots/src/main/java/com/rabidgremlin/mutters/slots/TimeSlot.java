/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.slots;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.Slot;
import com.rabidgremlin.mutters.core.SlotMatch;

/**
 * Slot that matches on times (into a {@link java.time.LocalTime}). Uses natty
 * to handle 'times' such as '8pm'.
 * 
 * @author rabidgremlin
 *
 */
public class TimeSlot extends Slot
{

  private final String name;

  public TimeSlot(String name)
  {
    this.name = name;
  }

  @Override
  public SlotMatch match(String token, Context context)
  {

    Parser parser = new Parser(context.getTimeZone());

    List<DateGroup> groups = parser.parse(token);
    for (DateGroup group : groups)
    {
      if (!group.isTimeInferred())
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
          LocalTime localTime = theDateTime.toLocalTime();
          return new SlotMatch(this, token, localTime);
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
