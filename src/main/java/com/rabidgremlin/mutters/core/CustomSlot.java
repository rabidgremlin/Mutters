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
		Metaphone metaphoner = new Metaphone();
		for (String option : options)
		{
			this.options.put(metaphoner.metaphone(option), option);
		}
	}

	@Override
	public SlotMatch match(String token, Context context)
	{
		Metaphone metaphoner = new Metaphone();
		String id = metaphoner.metaphone(token);
		
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
