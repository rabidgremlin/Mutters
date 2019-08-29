package com.rabidgremlin.mutters.bot.ink;

import com.bladecoder.ink.runtime.Story;
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
   * @param story The story.
   * @param storyJson The JSON string for the story. Used to temporary story version check hack. See TestSessionRestore.
   */
  public static void saveInkStoryState(Session session, Story story, String storyJson)
  {  
    StoryState storyState = story.getState();  
	  
    if (storyState == null)
    {
    	throw new BadInkStoryState("storyState should not be null");
    }
	  
    try
    {
      session.setAttribute(SLOT_PREFIX + "0987654321STORYSTATE1234567890", storyState.toJson());
      // save length of story JSON and use as a crude version check
      session.setAttribute(SLOT_PREFIX + "0987654321STORYJSONLENGTH1234567890", Integer.valueOf(storyJson.length()));
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
   * @param story The story.
   * @param storyJson The JSON string for the story. Used to temporary story version check hack. See TestSessionRestore.
   */
  public static void loadInkStoryState(Session session, Story story, String storyJson)
  {
    StoryState storyState = story.getState();  
	  
    try
    {
      Integer storyJsonLength = (Integer) session
    	          .getAttribute(SLOT_PREFIX + "0987654321STORYJSONLENGTH1234567890");
      String stateJson = (String) session
              .getAttribute(SLOT_PREFIX + "0987654321STORYSTATE1234567890");            
                  
      if (stateJson != null)
      {
    	// have state to restore so check that we have a story length and then check for size mismatch  
        if (storyJsonLength == null || storyJsonLength != storyJson.length())
        {
          throw new Exception("Story size mismatch. Assume new story has been loaded. Cannot restore session.");
        }  
    	  
        storyState.loadJson(stateJson);
      }
    }
    catch (Exception e)
    {
      throw new BadInkStoryState("Unexpected error. Failed to load story state", e);
    }
  }

}
