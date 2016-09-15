package com.rabidgremlin.mutters.bot.ink;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bladecoder.ink.runtime.Choice;
import com.bladecoder.ink.runtime.Story;
import com.rabidgremlin.mutters.bot.Bot;
import com.rabidgremlin.mutters.bot.BotResponse;
import com.rabidgremlin.mutters.bot.statemachine.AbstractStateMachineBot;
import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.session.Session;
import com.rabidgremlin.mutters.state.IntentResponse;
import com.rabidgremlin.mutters.state.StateMachine;
import com.rabidgremlin.mutters.util.SessionUtils;

public abstract class AbstractInkBot implements Bot
{
	private Logger log = LoggerFactory.getLogger(AbstractStateMachineBot.class);
	protected IntentMatcher matcher;
	protected String inkStoryJson;
	protected String defaultResponse = "Pardon?";

	public AbstractInkBot()
	{
		matcher = setUpIntents();
		inkStoryJson = getStoryJson();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rabidgremlin.mutters.bot.Bot#respond(com.rabidgremlin.mutters.session.Session, com.rabidgremlin.mutters.core.Context, java.lang.String)
	 */
	@Override
	public BotResponse respond(Session session, Context context, String messageText)
	{
		log.info("session: {} context: {} messageText: {}", new Object[] { session, context, messageText });

		// set up default response in case bot has issue processing input
		String responseText = SessionUtils.getReprompt(session);
		if (responseText == null)
		{
			responseText = defaultResponse;
		}

		// TODO put this back
		// default to reprompt hint if we have one
		// String hint = SessionUtils.getRepromptHint(session);
		String hint = null;

		try
		{
			// TODO is this efficent or do we need ThreadLocals ?
			Story story = new Story(inkStoryJson);
			SessionUtils.loadInkStoryState(session, story.getState());

			String reprompt = null;
			String reponseAction = null;
			Map<String, Object> responseActionParams = null;
			boolean askResponse = true;

			IntentMatch intentMatch = matcher.match(messageText, context);

			for (SlotMatch slotMatch : intentMatch.getSlotMatches().values())
			{
				// TODO do handle dates etc
				story.getVariablesState().set(slotMatch.getSlot().getName().toLowerCase(), slotMatch.getValue().toString());
			}

			if (intentMatch != null)
			{
				// get to write place in story
				while (story.canContinue())
				{
					log.info("Skipping {}", story.Continue());
				}

				if (story.getCurrentChoices().size() > 0)
				{
					int choiceIndex = 0;
					for (Choice c : story.getCurrentChoices())
					{
						if (StringUtils.equalsIgnoreCase(intentMatch.getIntent().getName(), c.getText()))
						{
							story.chooseChoiceIndex(choiceIndex);
							break;
						}
						choiceIndex++;
					}

					StringBuffer response = new StringBuffer();
					boolean first = true;
					while (story.canContinue())
					{
						String line = story.Continue();

						line = line.replaceAll("\n", "");

						log.info("Line {}", line);
						
						// HACK HACK TODO Fix
						if (line.trim().startsWith(":ORDER_TAXI"))
						{
							story.getVariablesState().set("taxiNo", Integer.toHexString(SessionUtils.getStringFromSlotOrSession(intentMatch, session, "address", "").hashCode()).substring(0,4));
						}
						

						if (!first && !line.trim().startsWith(":"))
						{
							response.append(line);
						}
						first = false;
						// System.out.print(line);
					}

					responseText = response.toString();

					SessionUtils.saveInkStoryState(session, story.getState());

					if (story.getCurrentChoices().size() == 0)
					{
						session.reset();
						askResponse = false;
					}
				}
				else
				{
					session.reset();
					askResponse = false;
				}

				if (reprompt != null)
				{
					SessionUtils.setReprompt(session, reprompt);
					SessionUtils.setRepromptHint(session, hint);
				}
				else
				{
					SessionUtils.setReprompt(session, defaultResponse + " " + responseText);
					SessionUtils.setRepromptHint(session, null);
				}

			}

			return new BotResponse(responseText, hint, askResponse, reponseAction, responseActionParams);
		}
		catch (Exception e)
		{
			log.warn("Unexpected error.", e);
			return new BotResponse(responseText, hint, true, null, null);
		}
	}

	public String getDefaultResponse()
	{
		return defaultResponse;
	}

	public void setDefaultResponse(String defaultResponse)
	{
		this.defaultResponse = defaultResponse;
	}

	public abstract IntentMatcher setUpIntents();

	public abstract String getStoryJson();

}
