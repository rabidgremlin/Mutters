/* Licensed under Apache-2.0 */
package com.rabidgremlin.mutters.templated;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

import com.rabidgremlin.mutters.core.Tokenizer;

/**
 * This class implements a 'simple' tokenizer that splits on whitespace and
 * strips out punctuation.
 * 
 * It does attempt to preserve anything that looks like a decimal number, date,
 * time, email address or a templated utterance slot tag.
 * 
 * It also supports lower casing of tokens which can help with intent matching.
 * 
 * @author rabidgremlin
 *
 */
public class SimpleTokenizer implements Tokenizer
{
  /** Are we forcing tokens to lower case ? */
  private boolean forceLowerCase;

  /**
   * Constructor. Defaults to not lowercasing tokens.
   */
  public SimpleTokenizer()
  {
    this(false);
  }

  /**
   * Constructor.
   * 
   * @param forceLowerCase Set to true if tokens should be lower cased.
   */
  public SimpleTokenizer(boolean forceLowerCase)
  {
    this.forceLowerCase = forceLowerCase;
  }

  @Override
  public String[] tokenize(String text)
  {
    String[] tokens = text.trim().split("\\s+");
    List<String> finalTokens = new ArrayList<>();

    for (String token : tokens)
    {
      boolean applyStriping = true;
      boolean applylowercasing = true;

      // is it a decimal or negative number
      if ((token.indexOf('.') != -1 || token.startsWith("-")) && isNumeric(token))
      {
        applyStriping = false;
      }
      else
      {
        // is it a $ value ?
        if (token.startsWith("$") && isNumeric(token.substring(1)))
        {
          applyStriping = false;
        }
        else
        {
          // is it an email address ?
          if (token.indexOf('@') != -1)
          {
            if (token.endsWith(".") || token.endsWith(","))
            {
              token = token.substring(0, token.length() - 1);
            }
            if (EmailValidator.getInstance().isValid(token))
            {
              applyStriping = false;
            }
          }
          else
          {
            // is it a templated utterance tag ?
            if (token.startsWith("{") && token.endsWith("}"))
            {
              applyStriping = false;
              applylowercasing = false;
            }
          }
        }
      }

      // should stripping be applied ?
      if (applyStriping)
      {
        token = token.replaceAll("[^A-Za-z0-9:/]", "");
      }

      // are we lowercasing token ?
      if (forceLowerCase && applylowercasing)
      {
        token = token.toLowerCase();
      }

      // do we have anything left ?
      if (!StringUtils.isBlank(token))
      {
        finalTokens.add(token);
      }
    }

    return finalTokens.toArray(new String[finalTokens.size()]);
  }

  // yuck but does the job
  private boolean isNumeric(String str)
  {
    try
    {
      Double.parseDouble(str);
    }
    catch (NumberFormatException nfe)
    {
      return false;
    }
    return true;
  }
}
