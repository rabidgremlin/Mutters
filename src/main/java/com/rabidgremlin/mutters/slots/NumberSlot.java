package com.rabidgremlin.mutters.slots;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.Slot;
import com.rabidgremlin.mutters.core.SlotMatch;

// very rough code
// TODO clean up
public class NumberSlot
    extends Slot
{

  private String name;

  public NumberSlot(String name)
  {
    super();
    this.name = name;
  }

  @Override
  public SlotMatch match(String token, Context context)
  {

    // try parse as integer
    try
    {
      Long value = Long.parseLong(token);

      return new SlotMatch(this, token, value);
    }
    catch (NumberFormatException de)
    {
      // try parse as decimal
      try
      {
        Double value = Double.parseDouble(token);

        return new SlotMatch(this, token, value);
      }
      catch (NumberFormatException le)
      {
        // try parse as word String

        Number value = wordStringToNumber(token);
        if (value != null)
        {
          return new SlotMatch(this, token, value);
        }
        else
        {

          return null;
        }
      }
    }
  }

  @Override
  public String getName()
  {
    return name;
  }

  // based on
  // http://stackoverflow.com/questions/26948858/converting-words-to-numbers-in-java
  public Number wordStringToNumber(String wordString)
  {
    if (wordString == null || wordString.length() < 1)
    {
      return null;
    }

    wordString = wordString.replaceAll("-", " ");
    wordString = wordString.replaceAll(",", " ");
    wordString = wordString.toLowerCase().replaceAll(" and", " ");
    String[] splittedParts = wordString.trim().split("\\s+");

    long finalResult = 0;
    long result = 0;

    for (String str : splittedParts)
    {
      if (str.equalsIgnoreCase("zero"))
      {
        result += 0;
      }
      else if (str.equalsIgnoreCase("one"))
      {
        result += 1;
      }
      else if (str.equalsIgnoreCase("two"))
      {
        result += 2;
      }
      else if (str.equalsIgnoreCase("three"))
      {
        result += 3;
      }
      else if (str.equalsIgnoreCase("four"))
      {
        result += 4;
      }
      else if (str.equalsIgnoreCase("five"))
      {
        result += 5;
      }
      else if (str.equalsIgnoreCase("six"))
      {
        result += 6;
      }
      else if (str.equalsIgnoreCase("seven"))
      {
        result += 7;
      }
      else if (str.equalsIgnoreCase("eight"))
      {
        result += 8;
      }
      else if (str.equalsIgnoreCase("nine"))
      {
        result += 9;
      }
      else if (str.equalsIgnoreCase("ten"))
      {
        result += 10;
      }
      else if (str.equalsIgnoreCase("eleven"))
      {
        result += 11;
      }
      else if (str.equalsIgnoreCase("twelve"))
      {
        result += 12;
      }
      else if (str.equalsIgnoreCase("thirteen"))
      {
        result += 13;
      }
      else if (str.equalsIgnoreCase("fourteen"))
      {
        result += 14;
      }
      else if (str.equalsIgnoreCase("fifteen"))
      {
        result += 15;
      }
      else if (str.equalsIgnoreCase("sixteen"))
      {
        result += 16;
      }
      else if (str.equalsIgnoreCase("seventeen"))
      {
        result += 17;
      }
      else if (str.equalsIgnoreCase("eighteen"))
      {
        result += 18;
      }
      else if (str.equalsIgnoreCase("nineteen"))
      {
        result += 19;
      }
      else if (str.equalsIgnoreCase("twenty"))
      {
        result += 20;
      }
      else if (str.equalsIgnoreCase("thirty"))
      {
        result += 30;
      }
      else if (str.equalsIgnoreCase("forty"))
      {
        result += 40;
      }
      else if (str.equalsIgnoreCase("fifty"))
      {
        result += 50;
      }
      else if (str.equalsIgnoreCase("sixty"))
      {
        result += 60;
      }
      else if (str.equalsIgnoreCase("seventy"))
      {
        result += 70;
      }
      else if (str.equalsIgnoreCase("eighty"))
      {
        result += 80;
      }
      else if (str.equalsIgnoreCase("ninety"))
      {
        result += 90;
      }
      else if (str.equalsIgnoreCase("hundred"))
      {
        result *= 100;
      }
      else if (str.equalsIgnoreCase("thousand"))
      {
        result *= 1000;
        finalResult += result;
        result = 0;
      }
      else if (str.equalsIgnoreCase("million"))
      {
        result *= 1000000;
        finalResult += result;
        result = 0;
      }
      else if (str.equalsIgnoreCase("billion"))
      {
        result *= 1000000000;
        finalResult += result;
        result = 0;
      }
      else if (str.equalsIgnoreCase("trillion"))
      {
        result *= 1000000000000L;
        finalResult += result;
        result = 0;
      }
      else
      {
        // unknown word
        return null;
      }
    }

    finalResult += result;
    result = 0;
    return finalResult;
  }

}
