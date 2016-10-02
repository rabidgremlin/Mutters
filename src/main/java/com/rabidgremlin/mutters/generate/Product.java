package com.rabidgremlin.mutters.generate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// TODO optimise and reduce memory usage
public class Product
{

  public static <T> List<List<T>> generate(List<T>... lists)
  {
    // TODO check for empty lists etc

    List<List<T>> result = new ArrayList<List<T>>();

    // copy first list as result
    for (T item : lists[0])
    {
      result.add(Arrays.asList(item));
    }

    for (int loop = 1; loop < lists.length; loop++)
    {
      result = multiply(result, lists[loop]);
    }

    return result;
  }

  private static <T> List<List<T>> multiply(List<List<T>> result, List<T> list)
  {
    List<List<T>> temp = new ArrayList<List<T>>();

    for (List<T> row : result)
    {
      for (T item : list)
      {
        List<T> tempRow = new ArrayList<T>(row);
        tempRow.add(item);
        temp.add(tempRow);
      }
    }

    return temp;
  }

}
