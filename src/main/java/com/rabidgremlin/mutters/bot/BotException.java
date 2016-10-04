package com.rabidgremlin.mutters.bot;

/**
 * This exception is thrown by a Bot if it has a failure whilst processing.
 * 
 * @author rabidgremlin
 *
 */
public class BotException
    extends Exception
{
  /** Serialization id. */
  private static final long serialVersionUID = -7838088498844800195L;

  public BotException()
  {
    super();
  }

  public BotException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
  {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public BotException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public BotException(String message)
  {
    super(message);
  }

  public BotException(Throwable cause)
  {
    super(cause);
  }

}
