package com.rabidgremlin.mutters.ml;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.IsCloseTo.*;
import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Test;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;

public class TestCategorization
{

	@Test
	public void testModelLoad() throws Exception
	{
		URL modelUrl = Thread.currentThread().getContextClassLoader().getResource("models/en-cat-taxi-intents.bin");		
		assertThat(modelUrl,is(notNullValue()));
		
		DoccatModel model = new DoccatModel(modelUrl);
		assertThat(model,is(notNullValue()));
	}
	
	
	
	@Test
	public void testCategorization() throws Exception
	{
		URL modelUrl = Thread.currentThread().getContextClassLoader().getResource("models/en-cat-taxi-intents.bin");		
		assertThat(modelUrl,is(notNullValue()));
		
		DoccatModel model = new DoccatModel(modelUrl);
		assertThat(model,is(notNullValue()));
		
		
		DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);
				 
		String category = myCategorizer.getBestCategory(myCategorizer.categorize("Order me a taxi"));		
		assertThat(category,is(notNullValue()));
		assertThat(category,is("OrderTaxi"));
		
		category = myCategorizer.getBestCategory(myCategorizer.categorize("Send me a taxi"));		
		assertThat(category,is(notNullValue()));
		assertThat(category,is("OrderTaxi"));		
		
		category = myCategorizer.getBestCategory(myCategorizer.categorize("Send a taxi to 12 Pleasent Street"));		
		assertThat(category,is(notNullValue()));
		assertThat(category,is("OrderTaxi"));
		
		category = myCategorizer.getBestCategory(myCategorizer.categorize("Cancel my cab"));		
		assertThat(category,is(notNullValue()));
		assertThat(category,is("CancelTaxi"));
		
		category = myCategorizer.getBestCategory(myCategorizer.categorize("Where is my taxi ?"));		
		assertThat(category,is(notNullValue()));
		assertThat(category,is("WhereTaxi"));
		
		category = myCategorizer.getBestCategory(myCategorizer.categorize("The address is 136 River Road"));		
		assertThat(category,is(notNullValue()));
		assertThat(category,is("GaveAddress"));
	}
	
	
}
