package com.rabidgremlin.mutters.bot.ink;

import com.bladecoder.ink.runtime.StoryState;
import com.rabidgremlin.mutters.core.session.Session;

/**
 * Utility class for InkBot's interactions with a Session.
 * 
 * @author rabidgremlin
 *
 */
public class SessionUtils
    extends com.rabidgremlin.mutters.core.util.SessionUtils
{

  protected SessionUtils()
  {
    // utility class
  }

  /**
   * Stores the failed to understand count for the bot.
   * 
   * @param session The session.
   * @param count The count.
   */
  public static void setFailedToUnderstandCount(Session session, int count)
  {
    session.setAttribute(SLOT_PREFIX + "0987654321FAILEDCOUNT1234567890", new Integer(count));
  }

  /**
   * Gets the failed to understand count for the bot.
   * 
   * @param session The session.
   * @return The count.
   */
  public static int getFailedToUnderstandCount(Session session)
  {
    Integer failedToUnderstandCount = (Integer) session.getAttribute(SLOT_PREFIX + "0987654321FAILEDCOUNT1234567890");

    if (failedToUnderstandCount == null)
    {
      return 0;
    }
    else
    {
      return failedToUnderstandCount;
    }
  }

  /**
   * Stores the current Ink story state for the user into the session.
   * 
   * @param session The session.
   * @param storyState The story state.
   */
  public static void saveInkStoryState(Session session, StoryState storyState)
  {
    try
    {
      session.setAttribute(SLOT_PREFIX + "0987654321STORYSTATE1234567890", storyState.toJson());
    }
    catch (Exception e)
    {
      throw new RuntimeException("Unexpected error. Failed to save story state", e);
    }
  }

  /**
   * Gets the user's current Ink story state from the session.
   * 
   * @param session The session.
   * @param storyState The story state.
   */
  public static void loadInkStoryState(Session session, StoryState storyState)
  {
    try
    {
      String stateJson = (String) session
          .getAttribute(SLOT_PREFIX + "0987654321STORYSTATE1234567890");
      if (stateJson != null)
      {
        storyState.loadJson(stateJson);
      }
    }
    catch (Exception e)
    {
      throw new BadInkStoryState("Unexpected error. Failed to load story state", e);
    }
  }

}
