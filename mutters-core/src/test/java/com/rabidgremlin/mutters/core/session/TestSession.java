/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core.session;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.junit.Test;

public class TestSession
{
  @Test
  public void testAttributeLifeCycle()
  {
    Session session = new Session();

    session.setAttribute("bob", "alice");
    assertThat(session.getAttribute("bob"), is("alice"));

    session.removeAttribute("bob");
    assertThat(session.getAttribute("bob"), is(nullValue()));
  }

  @Test
  public void testLongTermAttributeLifeCycle()
  {
    Session session = new Session();

    session.setLongTermAttribute("bobLT", "aliceLT");
    assertThat(session.getLongTermAttribute("bobLT"), is("aliceLT"));

    session.removeLongTermAttribute("bobLT");
    assertThat(session.getLongTermAttribute("bobLT"), is(nullValue()));
  }

  @Test
  public void testReset()
  {
    Session session = new Session();

    session.setAttribute("bob", "alice");
    session.setLongTermAttribute("bobLT", "aliceLT");

    session.reset();

    assertThat(session.getLongTermAttribute("bob"), is(nullValue()));
    assertThat(session.getLongTermAttribute("bobLT"), is("aliceLT"));
  }

  @Test
  public void testResetAll()
  {
    Session session = new Session();

    session.setAttribute("bob", "alice");
    session.setLongTermAttribute("bobLT", "aliceLT");

    session.resetAll();

    assertThat(session.getLongTermAttribute("bob"), is(nullValue()));
    assertThat(session.getLongTermAttribute("bobLT"), is(nullValue()));
  }

  public void testGettingAttributeKeys()
  {
    Session session = new Session();
    session.setAttribute("bob", "alice");

    // check attribute keys are as expected
    Set<String> attributeKeys = session.getAttributeKeys();
    assertThat(attributeKeys, is(not(nullValue())));
    assertThat(attributeKeys.size(), is(1));
    assertThat(attributeKeys.contains("bob"), is(true));

    // make sure nothing has leaked into long term attributes
    assertThat(session.getLongTermAttributeKeys(), is(not(nullValue())));
    assertThat(session.getLongTermAttributeKeys().size(), is(1));
  }

  public void testGettingLongTermAttributeKeys()
  {
    Session session = new Session();
    session.setLongTermAttribute("smith", "jones");

    // check long term attribute keys are as expected
    Set<String> attributeKeys = session.getLongTermAttributeKeys();
    assertThat(attributeKeys, is(not(nullValue())));
    assertThat(attributeKeys.size(), is(1));
    assertThat(attributeKeys.contains("smith"), is(true));

    // make sure nothing has leaked into long term attributes
    assertThat(session.getAttributeKeys(), is(not(nullValue())));
    assertThat(session.getAttributeKeys().size(), is(1));
  }
}
