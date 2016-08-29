package com.rabidgremlin.mutters.ml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.rabidgremlin.mutters.core.Intent;

public class MLIntent implements Intent
{
	private String name;
	private List<String> slotNames = new ArrayList<String>();
	
	public MLIntent(String name)
	{
		this.name = name;
	}

	@Override
	public String getName()
	{
		return name;
	}
	
	public void addSlot(String slotname)
	{
		slotNames.add(slotname);
	}
	
	public List<String> getSlotsNames()
	{
		return Collections.unmodifiableList(slotNames);
	}

}
