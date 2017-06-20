package com.rabidgremlin.mutters.bot.ink;

import java.util.ArrayList;
import java.util.List;

import com.bladecoder.ink.runtime.Story;
import com.rabidgremlin.mutters.core.Intent;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.IntentMatcher;
import com.rabidgremlin.mutters.core.session.Session;
import com.rabidgremlin.mutters.core.util.SessionUtils;
import com.rabidgremlin.mutters.opennlp.intent.OpenNLPIntentMatcher;
import com.rabidgremlin.mutters.opennlp.intent.OpenNLPTokenizer;
import com.rabidgremlin.mutters.opennlp.ner.OpenNLPSlotMatcher;
import com.rabidgremlin.mutters.slots.LiteralSlot;

import opennlp.tools.tokenize.WhitespaceTokenizer;

public class TaxiInkBotConfiguration
    implements InkBotConfiguration
{

  @Override
  public IntentMatcher getIntentMatcher()
  {
 // model was built with OpenNLP whitespace tokenizer
    OpenNLPTokenizer tokenizer = new OpenNLPTokenizer(WhitespaceTokenizer.INSTANCE);
    
    // use Open NLP NER for slot matching
    OpenNLPSlotMatcher slotMatcher = new OpenNLPSlotMatcher(tokenizer);
    slotMatcher.addSlotModel("Address", "models/en-ner-address.bin");   

    // create intent matcher
    OpenNLPIntentMatcher matcher = new OpenNLPIntentMatcher("models/en-cat-taxi-intents.bin", tokenizer, slotMatcher);

    Intent intent = new Intent("OrderTaxi");
    intent.addSlot(new LiteralSlot("Address"));
    matcher.addIntent(intent);

    intent = new Intent("CancelTaxi");
    matcher.addIntent(intent);

    intent = new Intent("WhereTaxi");
    matcher.addIntent(intent);

    intent = new Intent("GaveAddress");
    intent.addSlot(new LiteralSlot("Address"));
    matcher.addIntent(intent);

    intent = new Intent("Stop");
    matcher.addIntent(intent);

    intent = new Intent("Help");
    matcher.addIntent(intent);

    intent = new Intent("FavColor");
    matcher.addIntent(intent);

    return matcher;
  }

  @Override
  public String getStoryJson()
  {
    return StoryUtils.loadStoryJsonFromClassPath("taxibot.ink.json");
  }

  @Override
  public List<InkBotFunction> getInkFunctions()
  {
    List<InkBotFunction> functions = new ArrayList<>();

    functions.add(new InkBotFunction()
    {
      @Override
      public void execute(CurrentResponse currentResponse, Session session, IntentMatch intentMatch,
        Story story, String param)
      {
        try
        {
          story.getVariablesState().set("taxiNo",
              Integer
                  .toHexString(SessionUtils
                      .getStringFromSlotOrSession(intentMatch, session, "address", "").hashCode())
                  .substring(0, 4));
        }
        catch (Exception e)
        {
          throw new RuntimeException("Unable to set taxi no", e);
        }
      }

      @Override
      public String getFunctionName()
      {
        return "ORDER_TAXI";
      }
    });

    return functions;
  }

  @Override
  public List<GlobalIntent> getGlobalIntents()
  {
    List<GlobalIntent> globalIntents = new ArrayList<GlobalIntent>();

    globalIntents.add(new GlobalIntent("Stop", "stop"));
    globalIntents.add(new GlobalIntent("Help", "help"));

    return globalIntents;
  }

  @Override
  public ConfusedKnot getConfusedKnot()
  {   
    return null;
  }

  @Override
  public List<String> getDefaultResponses()
  {   
    return null;
  }

}
