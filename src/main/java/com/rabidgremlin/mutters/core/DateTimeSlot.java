package com.rabidgremlin.mutters.core;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class DateTimeSlot implements Slot {

	private String name;

	public DateTimeSlot(String name) {
		this.name = name;
	}

	@Override
	public SlotMatch match(String token, Context context) {

		Parser parser = new Parser(context.getTimeZone());

		List<DateGroup> groups = parser.parse(token);
		for (DateGroup group : groups) {
			List<Date> dates = group.getDates();
			if (!dates.isEmpty()) {
				return new SlotMatch(this, token, new DateTime(dates.get(0),DateTimeZone.forTimeZone(context.getTimeZone())));
			}
		}

		return null;
	}

	@Override
	public String getName() {
		return name;
	}

}
