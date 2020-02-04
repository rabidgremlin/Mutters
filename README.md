[![Build Status](https://travis-ci.com/rabidgremlin/mutters.svg?branch=master)](https://travis-ci.com/rabidgremlin/mutters)
[![codecov](https://codecov.io/gh/rabidgremlin/mutters/branch/master/graph/badge.svg)](https://codecov.io/gh/rabidgremlin/mutters)
[![GitHub license](https://img.shields.io/github/license/rabidgremlin/mutters.svg)](https://github.com/rabidgremlin/mutters/blob/master/LICENSE)

# Mutters
A Java framework for building bot brains. Heavily inspired by Amazon Echo's natural language understanding model.  

Implements:

* Templated and/or machine learning based identification of a user's intent based on their utterance. Support out of the box for OpenNLP or Facebook's fastText.
* Templated and/or machine learning based (NER) extraction of data from the user's utterance
* State management to support complex conversations, using either:
  * a state machine
  * Inkle's narrative scripting engine [Ink](http://www.inklestudios.com/ink/)
* Pluggable intent matching, named entity extraction and conversation state management so you can use our own implementations.  
  
## Example
The following is the code for a simple Taxi ordering bot. It uses OpenNLP machine learning to identify what the user is asking and to extract street addresses:

```
public class TaxiInkBot
    extends InkBot<TaxiInkBotConfiguration>
{

  public TaxiInkBot(TaxiInkBotConfiguration config)
  {
    super(config);    
  }

}
```

Which uses a configuration class to set up the NLP, Ink Story and functions that are used by the bot:

```
public class TaxiInkBotConfiguration
    implements InkBotConfiguration
{

  @Override
  public IntentMatcher getIntentMatcher()
  {
    // model was built with OpenNLP whitespace tokenizer
    OpenNLPTokenizer tokenizer = new OpenNLPTokenizer(WhitespaceTokenizer.INSTANCE);
    
    // use OpenNLP NER for slot matching
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
          // generate a fake order number based on address for demo
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

  // ...
}
```

Here is a snippet of the training data used to train the intents that the bot understands:

```
OrderTaxi Order me a cab
OrderTaxi Book a cab
OrderTaxi Order me a taxi
OrderTaxi Book a taxi
OrderTaxi Get me a cab
OrderTaxi Order a cab
OrderTaxi Get me a taxi
OrderTaxi Order a taxi
OrderTaxi Send a cab to 123 Mountain Drive
OrderTaxi Pick me up at 456 Queen Street
CancelTaxi Cancel my taxi
CancelTaxi Cancel my cab
CancelTaxi Cancel my order
WhereTaxi Where is my taxi ?
WhereTaxi Where is my cab ?
WhereTaxi How far away is my taxi ?
WhereTaxi How far away is my cab ?
WhereTaxi How long until my cab is here ?
WhereTaxi How long until my taxi is here ?
GaveAddress My address is 173 Essex Drive
GaveAddress The address is 1407 Graymalkin Lane , Salem Center
GaveAddress 52 Festive Road
GaveAddress it's 6151 Richmond Street
GaveAddress The pick up is at 740 Evergreen Terrace 
GaveAddress 890 Fifth Avenue
GaveAddress The address is 13 China Ave
GaveAddress 136 River Road
```

And some of the training data to teach the bot how to identify street addresses:

```
Send a cab to <START:address> 123 Mountain Drive <END>

The Whitehouse can be found at <START:address> 1600 Pennsylvania Avenue <END> . In the United Kingdom , the official residence of the Prime Minister can be found at <START:address> 10 Downing Street <END> .

In New Zealand , The Beehive can be found in <START:address> Molesworth St <END> .

Pick me up at <START:address> 456 Queen Street <END>

Some other famous addresses include : <START:address> 263 Prinsengracht , Amsterdam <END> , the  Home of Anne Frank in her diary and <START:address> 1060 West Addison Street , Chigaco <END> the location of Wrigley Field .

In Seinfield , Newman lives at <START:address> Apartment 5E , 129 West 81st Street <END> .
```

Lastly here are some snippets from the Ink file that is used to manage the conversation:

```
== start ==
+ [OrderTaxi] -> order_taxi
+ [CancelTaxi] -> cancel_taxi
+ [WhereTaxi] -> where_taxi

VAR address=""
VAR taxiNo = ""

== order_taxi ==
- (order_taxi_loop)
{
 - address == "": -> request_address 
 - else: -> order_the_taxi
}

= request_address
What is the pick up address ?
::SET_REPROMPT Where would you like to be picked up ?
::SET_HINT 123 Someplace Rd
+ [GaveAddress]
- -> order_taxi_loop

= order_the_taxi  
::ORDER_TAXI
Taxi {taxiNo} is on its way
::ADD_ATTACHMENT type::link url::http:\/\/trackcab.example.com/t/{taxiNo} title::Track your taxi here
::ADD_QUICK_REPLY Where is my taxi?
::ADD_QUICK_REPLY Cancel my taxi
-> END 
```

## Usage
If you are using Gradle you can pull (for the example above) the latest release with:

```
repositories {
    mavenCentral()    
}

dependencies {
        compile 'com.rabidgremlin:mutters-ink-bot:6.0.0'
        compile 'com.rabidgremlin:mutters-opennlp-intent:6.0.0'        
        compile 'com.rabidgremlin:mutters-opennlp-ner:6.0.0'
        compile 'com.rabidgremlin:mutters-slots:6.0.0'
}
```


Currently SNAPSHOT builds are available in the Sonatype OSSRH repository. 

Using Gradle you can pull the snapshot using:

```
repositories {
    mavenCentral()    
    maven {
        url "https://oss.sonatype.org/content/repositories/snapshots/"
    }
}

dependencies {
        compile 'com.rabidgremlin:mutters-ink-bot:6.0.0-SNAPSHOT'
        compile 'com.rabidgremlin:mutters-opennlp-intent:6.0.0-SNAPSHOT'
        compile 'com.rabidgremlin:mutters-opennlp-ner:6.0.0-SNAPSHOT'
        compile 'com.rabidgremlin:mutters-slots:6.0.0-SNAPSHOT'        
}        
```

## Packaging
Mutters is packaged into multiple jars to reduce dependencies and improve plugability.

| Package                  | Description                                                                      |
| ------------------------ | -------------------------------------------------------------------------------- |
| mutters-core             | Contains core classes, interfaces and utility classes                            |
| mutters-fasttext-intent  | Intent matcher that uses the fastText document classifier                        |
| mutters-ink-bot          | Implementation of a Bot that uses Inkle's Ink engine for conversation scripting  |
| mutters-opennlp-intent   | Intent matcher that uses OpenNLP's document classifier                           |
| mutters-opennlp-ner      | Slot matcher that uses OpenNLP's named entity recognition framework              |
| mutters-slots            | Implementation of a number of generic Slots                                      |
| mutters-statemachine-bot | Implementation of a Bot that uses a state machine for conversation flows         |
| mutters-templated-intent | Intent matcher that uses templates for matching                                  |
