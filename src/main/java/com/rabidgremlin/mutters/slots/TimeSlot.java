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

public class TimeSlot implements Slot
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
				if (!dates.isEmpty())
				{
					DateTime theDateTime = new DateTime(dates.get(0), DateTimeZone.forTimeZone(context.getTimeZone()));
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
