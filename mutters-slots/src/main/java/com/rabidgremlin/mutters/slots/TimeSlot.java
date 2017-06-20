package com.rabidgremlin.mutters.slots;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.Slot;
import com.rabidgremlin.mutters.core.SlotMatch;

/**
 * Slot that matches on times. Uses natty to handle 'times' such as '8pm'.
 * 
 * @author rabidgremlin
 *
 */
public class TimeSlot
    extends Slot
{

  private String name;

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

        // natty is very aggressive so will match date on text that is largely not a date, which is
        // not what we want
        String matchText = group.getText();
        float percMatch = (float) matchText.length() / (float) token.length();

        if (!dates.isEmpty() && percMatch > 0.75)
        {
          DateTime theDateTime = new DateTime(dates.get(0),
              DateTimeZone.forTimeZone(context.getTimeZone()));
          return new SlotMatch(this, token, theDateTime.toLocalTime());
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
