/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.core.session;

import static com.google.common.truth.Truth.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;

class TestSession
{
  @Test
  void testAttributeLifeCycle()
  {
    Session session = new Session();

    session.setAttribute("bob", "alice");
    assertThat(session.getAttribute("bob")).isEqualTo("alice");

    session.removeAttribute("bob");
    assertThat(session.getAttribute("bob")).isNull();
  }

  @Test
  void testLongTermAttributeLifeCycle()
  {
    Session session = new Session();

    session.setLongTermAttribute("bobLT", "aliceLT");
    assertThat(session.getLongTermAttribute("bobLT")).isEqualTo("aliceLT");

    session.removeLongTermAttribute("bobLT");
    assertThat(session.getLongTermAttribute("bobLT")).isNull();
  }

  @Test
  void testReset()
  {
    Session session = new Session();

    session.setAttribute("bob", "alice");
    session.setLongTermAttribute("bobLT", "aliceLT");

    session.reset();

    assertThat(session.getLongTermAttribute("bob")).isNull();
    assertThat(session.getLongTermAttribute("bobLT")).isEqualTo("aliceLT");
  }

  @Test
  void testResetAll()
  {
    Session session = new Session();

    session.setAttribute("bob", "alice");
    session.setLongTermAttribute("bobLT", "aliceLT");

    session.resetAll();

    assertThat(session.getLongTermAttribute("bob")).isNull();
    assertThat(session.getLongTermAttribute("bobLT")).isNull();
  }

  public void testGettingAttributeKeys()
  {
    Session session = new Session();
    session.setAttribute("bob", "alice");

    // check attribute keys are as expected
    Set<String> attributeKeys = session.getAttributeKeys();
    assertThat(attributeKeys).isNotNull();
    assertThat(attributeKeys.size()).isEqualTo(1);
    assertThat(attributeKeys.contains("bob")).isTrue();

    // make sure nothing has leaked into long term attributes
    assertThat(session.getAttributeKeys()).isNotNull();
    assertThat(session.getAttributeKeys()).isEqualTo(1);
  }

  public void testGettingLongTermAttributeKeys()
  {
    Session session = new Session();
    session.setLongTermAttribute("smith", "jones");

    // check long term attribute keys are as expected
    Set<String> attributeKeys = session.getLongTermAttributeKeys();
    assertThat(attributeKeys).isNotNull();
    assertThat(attributeKeys.size()).isEqualTo(1);
    assertThat(attributeKeys.contains("smith")).isTrue();

    // make sure nothing has leaked into long term attributes
    assertThat(session.getAttributeKeys()).isNotNull();
    assertThat(session.getAttributeKeys()).isEqualTo(1);
  }
}
