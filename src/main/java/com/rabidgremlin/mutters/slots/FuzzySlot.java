package com.rabidgremlin.mutters.slots;

import java.util.HashMap;

import org.apache.commons.codec.language.Soundex;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.Slot;
import com.rabidgremlin.mutters.core.SlotMatch;

public class FuzzySlot implements Slot
{

	private String name;

	private HashMap<String, String> options = new HashMap<String, String>();

	public FuzzySlot(String name, String[] options)
	{
		this.name = name;
		for (String option : options)
		{
			this.options.put(makeId(option), option);
		}
	}
	
	public FuzzySlot(String name, HashMap<String, String> optionValueMap)
	{
		this.name = name;
		for (String key : optionValueMap.keySet())
		{
			this.options.put(makeId(key), optionValueMap.get(key));
		}
	}
	

	// metaphone ignore multi word strings like so need to treat each word as seperate token to make key
	private String makeId(String token)
	{
		String id = "";
		Soundex soundexr = new Soundex();
		for (String part : token.split(" "))
		{
			id += soundexr.soundex(part);
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
		return "FuzzySlot [name=" + name + ", options=" + options + "]";
	}

}
