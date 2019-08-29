package com.rabidgremlin.mutters.bot.ink;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.bot.BotException;
import com.rabidgremlin.mutters.core.bot.BotResponse;
import com.rabidgremlin.mutters.core.session.Session;

/** Test session restores when story changes (which can happen if sessions are stored outside of the bot for instance in a external cache).
 * 
 * Ink tries to gracefully handle story state restores when a story structure has changed. It is supposed to raise warnings and errors that can
 * be checked when this occurs, but this is not happening. There is also a case where restore causes wrong path to be followed and incorrect output
 * to be generated. See testSessionRestoreWhenStoryOptionsChange()
 * 
 * Current 'hack' is treat any change of story as an incompatible state change and raise a BadInkStoryState. 
 *
 */
public class TestSessionRestore
{
	private static SessionRestoreTestBot storyThreeBot;
	private static SessionRestoreTestBot storyNewBot;

	@BeforeClass
	public static void setUpBot()
	{
		storyThreeBot = new SessionRestoreTestBot(new SessionRestoreTestBotConfiguration("story_three_options.ink.json"));
		storyNewBot = new SessionRestoreTestBot(new SessionRestoreTestBotConfiguration("story_new_option.ink.json"));
	}

	@Test
	public void testSessionRestoreWhenStoryOptionsChange() throws BotException
	{
		Session session = new Session();
		Context context = new Context();

		BotResponse response = storyThreeBot.respond(session, context, "Three");		
		assertThat(response.getResponse(), is("You chose option three"));
		
		response = storyThreeBot.respond(session, context, "Three");		
		assertThat(response.getResponse(), is("You chose option three"));
		
		response = storyThreeBot.respond(session, context, "Three");		
		assertThat(response.getResponse(), is("You chose option three"));
		
		response = storyThreeBot.respond(session, context, "Three");		
		assertThat(response.getResponse(), is("You chose option three"));
				
		// switch to new story with more options and change of options order
		// Response should be "You chose option three" but actually land up "You chose option two" due to bug in ink
		// so short term, we will just spit out a BadInkStoryState exception
		try
		{
		  response = storyNewBot.respond(session, context, "Three");
   		  //assertThat(response.getResponse(), is("You chose option three"));
		  fail("Code should not reach here. Expected exception to be thrown");
		}
		catch(Exception e)
		{
		   if (!e.getCause().getClass().equals(BadInkStoryState.class))
		   {
			 fail("Was expecting cause to be BadInkStoryState exception.");	 
		   }
		}
	}
}
