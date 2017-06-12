package com.rabidgremlin.mutters.bot.ink.functions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/*
 * Utility class to hold parsed ink function data.
 * 
 */
public class FunctionDetails
{
  /** The data string. */
  private String functionData;

  /** The data string parsed into params. */
  private Map<String, String> functionParams;

  public FunctionDetails(String functionData)
  {  
    this.functionData = functionData;
  }

  public String getFunctionData()
  {
    return functionData;
  }

  public Map<String, String> getFunctionParams()
  {
    if (functionParams == null)
    {
      return null;
    }

    return Collections.unmodifiableMap(functionParams);
  }

  public void addFunctionParams(String name, String value)
  {
    if (functionParams == null)
    {
      functionParams = new HashMap<String, String>();
    }

    functionParams.put(name, value);
  }

}
