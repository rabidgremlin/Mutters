package com.rabidgremlin.mutters.slots;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.Slot;
import com.rabidgremlin.mutters.core.SlotMatch;

public class FuzzySlot implements Slot
{
    
	private String name;
	private double tolerance;
	private List<String> options = new ArrayList<String>();

	public FuzzySlot(String name, String[] options, double tolerance)
	{
		this.name = name;
		this.options = Arrays.asList(options);
		this.tolerance = tolerance;
	}
	
	public FuzzySlot(String name, String[] options)
	{
		this(name,options,0.99);
	}
	

	@Override
	public SlotMatch match(String token, Context context)
	{
		double bestMatchScore = 0;
		String bestMatch = null;
		
		for(String option:options)
		{
			double score = StringUtils.getJaroWinklerDistance(token,option);
			if (score > bestMatchScore)
			{
				bestMatchScore = score;
				bestMatch = option;
			}			
		}
		
		if (bestMatchScore > tolerance && bestMatch != null)
		{
			return new SlotMatch(this, token, bestMatch);
		}
		else
		{
			return null;	
		}
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
