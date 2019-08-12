package com.rabidgremlin.mutters.core;

/**
 * This interface is implemented by all tokenizers.
 * 
 * @author rabidgremlin
 *
 */
public interface Tokenizer
{
  /**
   * Takes a piece of text and tokenizes it.
   * 
   * @param text The text to tokenize.
   * @return Array of tokens.
   */
  public String[] tokenize(String text);

}
