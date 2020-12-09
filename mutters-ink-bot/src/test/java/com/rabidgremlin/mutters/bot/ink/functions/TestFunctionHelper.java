/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.bot.ink.functions;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Test for parsing of function strings.
 * 
 * @author rabidgremlin
 *
 */
class TestFunctionHelper
{

  @Test
  void testParameterParsing()
  {
    FunctionDetails details = FunctionHelper
        .parseFunctionString("type::link url::https:\\\\/\\\\/en.wikipedia.org/wiki/Chatbot title::Here is the link");

    assertThat(details).isNotNull();

    assertThat(details.getFunctionData())
        .isEqualTo("type::link url::https:\\\\/\\\\/en.wikipedia.org/wiki/Chatbot title::Here is the link");

    assertThat(details.getFunctionParams()).isNotNull();
    assertThat(details.getFunctionParams().get("type")).isEqualTo("link");
    assertThat(details.getFunctionParams().get("url")).isEqualTo("https:\\\\/\\\\/en.wikipedia.org/wiki/Chatbot");
    assertThat(details.getFunctionParams().get("title")).isEqualTo("Here is the link");
  }

  @Test
  void testParameterParsingValueWithSpaces()
  {
    FunctionDetails details = FunctionHelper
        .parseFunctionString("title::Here is the link type::link url::https:\\\\/\\\\/en.wikipedia.org/wiki/Chatbot ");

    assertThat(details).isNotNull();

    assertThat(details.getFunctionData())
        .isEqualTo("title::Here is the link type::link url::https:\\\\/\\\\/en.wikipedia.org/wiki/Chatbot");

    assertThat(details.getFunctionParams()).isNotNull();
    assertThat(details.getFunctionParams().get("type")).isEqualTo("link");
    assertThat(details.getFunctionParams().get("url")).isEqualTo("https:\\\\/\\\\/en.wikipedia.org/wiki/Chatbot");
    assertThat(details.getFunctionParams().get("title")).isEqualTo("Here is the link");
  }

  @Test
  void testParameterNoParams()
  {
    FunctionDetails details = FunctionHelper.parseFunctionString(" dd/mm or next friday ");

    assertThat(details).isNotNull();

    assertThat(details.getFunctionData()).isEqualTo("dd/mm or next friday");

    assertThat(details.getFunctionParams()).isNull();
  }

  @Test
  void testBadParams()
  {
    assertThrows(IllegalArgumentException.class, () -> FunctionHelper.parseFunctionString("junk::ob:::"));
  }

}
