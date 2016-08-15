package com.rabidgremlin.mutters.core;

import java.util.HashMap;

public class CustomSlot implements Slot
{

	private String name;

	private HashMap<String, String> options = new HashMap<String, String>();

	public CustomSlot(String name, String[] options)
	{
		this.name = name;
		for (String option : options)
		{
			this.options.put(option.toLowerCase(), option);
		}
	}
	
	public CustomSlot(String name, HashMap<String, String> optionValueMap)
	{
		this.name = name;
		for (String key : optionValueMap.keySet())
		{
			this.options.put(key.toLowerCase(), optionValueMap.get(key));
		}
	}
	

	@Override
	public SlotMatch match(String token, Context context)
	{
		String id = token.toLowerCase();

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
