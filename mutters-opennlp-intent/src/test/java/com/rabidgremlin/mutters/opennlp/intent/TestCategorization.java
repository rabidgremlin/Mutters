/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.opennlp.intent;

import static com.google.common.truth.Truth.assertThat;

import java.net.URL;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;

import org.junit.jupiter.api.Test;

class TestCategorization
{

  @Test
  void testModelLoad() throws Exception
  {
    URL modelUrl = Thread.currentThread().getContextClassLoader().getResource("models/en-cat-taxi-intents.bin");
    assertThat(modelUrl).isNotNull();

    DoccatModel model = new DoccatModel(modelUrl);
    assertThat(model).isNotNull();
  }

  @Test
  void testCategorization() throws Exception
  {
    URL modelUrl = Thread.currentThread().getContextClassLoader().getResource("models/en-cat-taxi-intents.bin");
    assertThat(modelUrl).isNotNull();

    DoccatModel model = new DoccatModel(modelUrl);
    assertThat(model).isNotNull();

    DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);
    // model was built with OpenNLP whitespace tokenizer
    OpenNLPTokenizer tokenizer = new OpenNLPTokenizer(WhitespaceTokenizer.INSTANCE);

    String category = myCategorizer.getBestCategory(myCategorizer.categorize(tokenizer.tokenize("Order me a taxi")));
    assertThat(category).isNotNull();
    assertThat(category).isEqualTo("OrderTaxi");

    category = myCategorizer.getBestCategory(myCategorizer.categorize(tokenizer.tokenize("Send me a taxi")));
    assertThat(category).isNotNull();
    assertThat(category).isEqualTo("OrderTaxi");

    category = myCategorizer
        .getBestCategory(myCategorizer.categorize(tokenizer.tokenize("Send a taxi to 12 Pleasent Street")));
    assertThat(category).isNotNull();
    assertThat(category).isEqualTo("OrderTaxi");

    category = myCategorizer.getBestCategory(myCategorizer.categorize(tokenizer.tokenize("Cancel my cab")));
    assertThat(category).isNotNull();
    assertThat(category).isEqualTo("CancelTaxi");

    category = myCategorizer.getBestCategory(myCategorizer.categorize(tokenizer.tokenize("Where is my taxi ?")));
    assertThat(category).isNotNull();
    assertThat(category).isEqualTo("WhereTaxi");

    category = myCategorizer
        .getBestCategory(myCategorizer.categorize(tokenizer.tokenize("The address is 136 River Road")));
    assertThat(category).isNotNull();
    assertThat(category).isEqualTo("GaveAddress");
  }

}
