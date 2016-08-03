package com.rabidgremlin.mutters.core;

import java.util.HashMap;

import org.apache.commons.codec.language.Metaphone;

public class CustomSlot implements Slot
{

	private String name;

	private HashMap<String, String> options = new HashMap<String, String>();

	public CustomSlot(String name, String[] options)
	{
		this.name = name;
		for (String option : options)
		{
			this.options.put(makeId(option), option);
		}
	}

	// metaphone ignore multi word strings like so need to treat each word as seperate token to make key
	private String makeId(String token)
	{
		String id = "";
		Metaphone metaphoner = new Metaphone();
		for (String part : token.split(" "))
		{
			id += metaphoner.metaphone(part);
		}

		return id;
	}

	@Override
	public SlotMatch match(String token, Context context)
	{
		String id = makeId(token);

		if (options.containsKey(id))
		{
			return new SlotMatch(this, token, options.get(id));
		}
		return null;
	}

	public String getName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return "CustomSlot [name=" + name + ", options=" + options + "]";
	}

}
