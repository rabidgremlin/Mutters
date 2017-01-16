# Mutters
A Java framework for building bot brains. Heavily inspired by Amazon Echo's natural language understanding model.  

Implements:

* Templated and/or machine learning based identification of a user's intent based on their utterance
* Templated and/or machine learning based (NER) extraction of data from the user's utterance
* State management to support complex conversations, using either:
  * a state machine
  * Inkle's narrative scripting engine [Ink](http://www.inklestudios.com/ink/)
  
## Example
The following is the code for a simple Taxi ordering bot. It uses machine learning to identify what the user is asking and to extract street 
addresses:

```
public class TaxiInkBot
    extends AbstractInkBot
{

  @Override
  public IntentMatcher setUpIntents()
  {
    MLIntentMatcher matcher = new MLIntentMatcher("models/en-cat-taxi-intents.bin");
    matcher.addSlotModel("Address", "models/en-ner-address.bin");

    MLIntent intent = new MLIntent("OrderTaxi");
    intent.addSlot(new LiteralSlot("Address"));
    matcher.addIntent(intent);

    intent = new MLIntent("CancelTaxi");
    matcher.addIntent(intent);

    intent = new MLIntent("WhereTaxi");
    matcher.addIntent(intent);

    intent = new MLIntent("GaveAddress");
    intent.addSlot(new LiteralSlot("Address"));
    matcher.addIntent(intent);

    return matcher;
  }

  @Override
  public String getStoryJson()
  {
    return loadStoryJsonFromClassPath("taxibot.ink.json");
  }

  @Override
  public void setUpFunctions()
  {
    addFunction(new InkBotFunction()
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
  }

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
+ OrderTaxi -> order_taxi
+ CancelTaxi -> cancel_taxi
+ WhereTaxi -> where_taxi

VAR address=""
VAR taxiNo = ""

== order_taxi ==


- (order_taxi_loop)
{
  - address == "":
    -> request_address
  - else:
    -> order_the_taxi
}
-> END

= request_address
What is the pick up address ?
:SET_REPROMPT Where would you like to be picked up ?
:SET_HINT 123 Someplace Rd
+ GaveAddress
- -> order_taxi_loop
  
= order_the_taxi  
:ORDER_TAXI
Taxi {taxiNo} is on its way
:SET_ACTION OPEN_URL url:http:\/\/trackcab.example.com/t/{taxiNo} 
-> END 
```

## Usage
If you are using Gradle you can pull the latest release with:

```
repositories {
    mavenCentral()    
}

dependencies {
        compile 'com.rabidgremlin:mutters:1.1.0'
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
        compile 'com.rabidgremlin:mutters:1.1.0-SNAPSHOT'
}        
```

