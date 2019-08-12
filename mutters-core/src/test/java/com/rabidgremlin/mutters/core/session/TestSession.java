package com.rabidgremlin.mutters.core.session;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

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
}
