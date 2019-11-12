/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.generate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TestProduct
{
  @Test
  public void testProduct()
  {
    List<String> list1 = Arrays.asList("A", "B");
    List<String> list2 = Arrays.asList("1", "2", "3");
    List<String> list3 = Arrays.asList("X");

    @SuppressWarnings("unchecked")
    List<List<String>> product = Product.generate(list1, list2, list3);

    assertThat(product, is(notNullValue()));
    assertThat(product.size(), is(list1.size() * list2.size() * list3.size()));

    assertThat(product.get(0), is(Arrays.asList("A", "1", "X")));
    assertThat(product.get(1), is(Arrays.asList("A", "2", "X")));
    assertThat(product.get(2), is(Arrays.asList("A", "3", "X")));
    assertThat(product.get(3), is(Arrays.asList("B", "1", "X")));
    assertThat(product.get(4), is(Arrays.asList("B", "2", "X")));
    assertThat(product.get(5), is(Arrays.asList("B", "3", "X")));

    System.out.println(product);
  }
}
