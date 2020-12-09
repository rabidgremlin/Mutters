/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.opennlp.intent;

import static com.google.common.truth.Truth.assertThat;

import java.net.URL;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;

import org.junit.jupiter.api.Test;

class TestNER
{

  @Test
  void testModelLoad() throws Exception
  {
    URL modelUrl = Thread.currentThread().getContextClassLoader().getResource("models/en-ner-persons.bin");
    assertThat(modelUrl).isNotNull();

    TokenNameFinderModel model = new TokenNameFinderModel(modelUrl);
    assertThat(model).isNotNull();
  }

  @Test
  void testPersonNER() throws Exception
  {
    URL modelUrl = Thread.currentThread().getContextClassLoader().getResource("models/en-ner-persons.bin");
    assertThat(modelUrl).isNotNull();

    TokenNameFinderModel model = new TokenNameFinderModel(modelUrl);
    assertThat(model).isNotNull();

    NameFinderME nameFinder = new NameFinderME(model);
    String[] tokens = SimpleTokenizer.INSTANCE
        .tokenize("Mr. John Smith of New York, married Anne Green of London today.");
    assertThat(tokens).hasLength(15);

    Span[] spans = nameFinder.find(tokens);
    assertThat(spans).hasLength(2);

    String[] names = Span.spansToStrings(spans, tokens);
    assertThat(names).hasLength(2);
    assertThat(names[0]).isEqualTo("John Smith");
    assertThat(names[1]).isEqualTo("Anne Green");
  }

  @Test
  void testLocationNER() throws Exception
  {
    URL modelUrl = Thread.currentThread().getContextClassLoader().getResource("models/en-ner-locations.bin");
    assertThat(modelUrl).isNotNull();

    TokenNameFinderModel model = new TokenNameFinderModel(modelUrl);
    assertThat(model).isNotNull();

    NameFinderME nameFinder = new NameFinderME(model);
    String[] tokens = SimpleTokenizer.INSTANCE
        .tokenize("Mr. John Smith of New York, married Anne Green of London today.");
    assertThat(tokens).hasLength(15);

    Span[] spans = nameFinder.find(tokens);
    assertThat(spans).hasLength(2);

    String[] locations = Span.spansToStrings(spans, tokens);
    assertThat(locations).hasLength(2);
    assertThat(locations[0]).isEqualTo("New York");
    assertThat(locations[1]).isEqualTo("London");
  }

  @Test
  void testDateNER() throws Exception
  {
    URL modelUrl = Thread.currentThread().getContextClassLoader().getResource("models/en-ner-dates.bin");
    assertThat(modelUrl).isNotNull();

    TokenNameFinderModel model = new TokenNameFinderModel(modelUrl);
    assertThat(model).isNotNull();

    NameFinderME nameFinder = new NameFinderME(model);
    String[] tokens = SimpleTokenizer.INSTANCE
        .tokenize("Mr. John Smith of New York, married Anne Green of London today.");
    assertThat(tokens).hasLength(15);

    Span[] spans = nameFinder.find(tokens);
    assertThat(spans).hasLength(1);

    String[] locations = Span.spansToStrings(spans, tokens);
    assertThat(locations).hasLength(1);
    assertThat(locations[0]).isEqualTo("today");
  }

  @Test
  void testAddressNER() throws Exception
  {
    URL modelUrl = Thread.currentThread().getContextClassLoader().getResource("models/en-ner-address.bin");
    assertThat(modelUrl).isNotNull();

    TokenNameFinderModel model = new TokenNameFinderModel(modelUrl);
    assertThat(model).isNotNull();

    NameFinderME nameFinder = new NameFinderME(model);
    String[] tokens = SimpleTokenizer.INSTANCE.tokenize("Send a taxi to 12 Pleasent Street");
    Span[] spans = nameFinder.find(tokens);
    assertThat(spans).hasLength(1);

    String[] locations = Span.spansToStrings(spans, tokens);
    assertThat(locations).hasLength(1);
    assertThat(locations[0]).isEqualTo("12 Pleasent Street");
  }
}
