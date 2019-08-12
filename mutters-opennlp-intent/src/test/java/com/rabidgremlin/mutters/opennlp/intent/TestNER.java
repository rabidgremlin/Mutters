package com.rabidgremlin.mutters.opennlp.intent;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.net.URL;

import org.junit.Test;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;

public class TestNER
{

  @Test
  public void testModelLoad()
    throws Exception
  {
    URL modelUrl = Thread.currentThread().getContextClassLoader()
        .getResource("models/en-ner-persons.bin");
    assertThat(modelUrl, is(notNullValue()));

    TokenNameFinderModel model = new TokenNameFinderModel(modelUrl);
    assertThat(model, is(notNullValue()));
  }

  @Test
  public void testPersonNER()
    throws Exception
  {
    URL modelUrl = Thread.currentThread().getContextClassLoader()
        .getResource("models/en-ner-persons.bin");
    assertThat(modelUrl, is(notNullValue()));

    TokenNameFinderModel model = new TokenNameFinderModel(modelUrl);
    assertThat(model, is(notNullValue()));

    NameFinderME nameFinder = new NameFinderME(model);
    String[] tokens = SimpleTokenizer.INSTANCE
        .tokenize("Mr. John Smith of New York, married Anne Green of London today.");
    assertThat(tokens.length, is(15));

    Span[] spans = nameFinder.find(tokens);
    assertThat(spans.length, is(2));

    String[] names = Span.spansToStrings(spans, tokens);
    assertThat(names.length, is(2));
    assertThat(names[0], is("John Smith"));
    assertThat(names[1], is("Anne Green"));
  }

  @Test
  public void testLocationNER()
    throws Exception
  {
    URL modelUrl = Thread.currentThread().getContextClassLoader()
        .getResource("models/en-ner-locations.bin");
    assertThat(modelUrl, is(notNullValue()));

    TokenNameFinderModel model = new TokenNameFinderModel(modelUrl);
    assertThat(model, is(notNullValue()));

    NameFinderME nameFinder = new NameFinderME(model);
    String[] tokens = SimpleTokenizer.INSTANCE
        .tokenize("Mr. John Smith of New York, married Anne Green of London today.");
    assertThat(tokens.length, is(15));

    Span[] spans = nameFinder.find(tokens);
    assertThat(spans.length, is(2));

    String[] locations = Span.spansToStrings(spans, tokens);
    assertThat(locations.length, is(2));
    assertThat(locations[0], is("New York"));
    assertThat(locations[1], is("London"));
  }

  @Test
  public void testDateNER()
    throws Exception
  {
    URL modelUrl = Thread.currentThread().getContextClassLoader()
        .getResource("models/en-ner-dates.bin");
    assertThat(modelUrl, is(notNullValue()));

    TokenNameFinderModel model = new TokenNameFinderModel(modelUrl);
    assertThat(model, is(notNullValue()));

    NameFinderME nameFinder = new NameFinderME(model);
    String[] tokens = SimpleTokenizer.INSTANCE
        .tokenize("Mr. John Smith of New York, married Anne Green of London today.");
    assertThat(tokens.length, is(15));

    Span[] spans = nameFinder.find(tokens);
    assertThat(spans.length, is(1));

    String[] locations = Span.spansToStrings(spans, tokens);
    assertThat(locations.length, is(1));
    assertThat(locations[0], is("today"));
  }

  @Test
  public void testAddressNER()
    throws Exception
  {
    URL modelUrl = Thread.currentThread().getContextClassLoader()
        .getResource("models/en-ner-address.bin");
    assertThat(modelUrl, is(notNullValue()));

    TokenNameFinderModel model = new TokenNameFinderModel(modelUrl);
    assertThat(model, is(notNullValue()));

    NameFinderME nameFinder = new NameFinderME(model);
    String[] tokens = SimpleTokenizer.INSTANCE.tokenize("Send a taxi to 12 Pleasent Street");
    Span[] spans = nameFinder.find(tokens);
    assertThat(spans.length, is(1));

    String[] locations = Span.spansToStrings(spans, tokens);
    assertThat(locations.length, is(1));
    assertThat(locations[0], is("12 Pleasent Street"));
  }
}
