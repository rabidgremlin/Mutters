/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.generate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class to generate the combinations of number of lists of type <T>.
 * 
 * For instance given 3 lists:
 * 
 * * "A", "B" * "1", "2", "3" * "X"
 * 
 * The class will generate 6 lists containing:
 * 
 * * "A", "1", "X" * "A", "2", "X" * "A", "3", "X" * "B", "1", "X" * "B", "2",
 * "X" * "B", "3", "X"
 * 
 * @see com.rabidgremlin.mutters.generate.TestProduct
 * 
 * @author rabidgremlin
 *
 */
public final class Product
{
  /**
   * Private constructor for utility class. *
   */
  private Product()
  {
    // private constructor for utility class
  }

  /**
   * Given a number of lists of type <T> this method will generate a list of lists
   * of type <T> containing all the combinations of the items in the input lists.
   * 
   * @param lists The lists to create combinations of.
   * @return A list of lists containing all the possible combinations.
   */
  public static <T> List<List<T>> generate(List<T>... lists)
  {
    // TODO check for empty lists etc
    // TODO optimise and reduce memory usage
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
