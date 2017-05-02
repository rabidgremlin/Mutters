package com.rabidgremlin.mutters.util;

/**
 * This run time exception is thrown if there is an issue loading an Ink story state from a Session.
 * 
 * @author rabidgremlin
 * 
 */
public class BadInkStoryState
    extends RuntimeException
{

  public BadInkStoryState()
  {
  }

  public BadInkStoryState(String message)
  {
    super(message);
  }

  public BadInkStoryState(Throwable cause)
  {
    super(cause);
  }

  public BadInkStoryState(String message, Throwable cause)
  {
    super(message, cause);
  }

  public BadInkStoryState(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
  {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
