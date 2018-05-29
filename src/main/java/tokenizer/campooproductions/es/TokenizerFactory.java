package tokenizer.campooproductions.es;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import util.campooproductions.es.ArrayEmptyElementsRemover;

/**
 * 
 * @author Alberto Gutiérrez
 *
 */
public class TokenizerFactory
{
	private List<ITokenizer> itokenizers;
	private final static String Default_Tokenizers_File = "src\\main\\resources\\tokenizers.xml";
	static Logger log = Logger.getLogger(TokenizerFactory.class.getName());
	
	/**
	 * Creates a new instance of TokenizerFactory. A new TokenizerFactory is created
	 * with the information provided on the default XML Tokenizers File
	 */
	public TokenizerFactory()
	{
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<XMLConfiguration> builder =
			    new FileBasedConfigurationBuilder<XMLConfiguration>(XMLConfiguration.class)
			    .configure(params.xml()
			        .setFileName(Default_Tokenizers_File));
			XMLConfiguration config = builder.getConfiguration();
			
			List<Object> listTokenizers = new ArrayList<Object>();
			listTokenizers = config.getList("tokenizers.class", listTokenizers);
			itokenizers = new ArrayList<ITokenizer>();
			for(Object obj : listTokenizers)
			{
				String classToInstantiate = "tokenizer.uneatlantico.es." + obj.toString();
				Object tok = Class.forName(classToInstantiate).newInstance();
				if(tok instanceof ITokenizer)
				{
					ITokenizer itokenizer = (ITokenizer) tok;
					itokenizers.add(itokenizer);
				}
			}
		}
		catch(ConfigurationException e)
		{
			log.error(e.getMessage());
		} catch (InstantiationException e) {
			log.error(e.getMessage());
		} catch (IllegalAccessException e) {
			log.error(e.getMessage());
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * Creates a new instance of TokenizerFactory. A new TokenizerFactory is created 
	 * with the information provided on the XML file specified
	 * @param filename	the XML file name.
	 */
	public TokenizerFactory(String filename)
	{
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<XMLConfiguration> builder =
			    new FileBasedConfigurationBuilder<XMLConfiguration>(XMLConfiguration.class)
			    .configure(params.xml()
			        .setFileName(filename));
			XMLConfiguration config = builder.getConfiguration();
			
			List<Object> listTokenizers = new ArrayList<Object>();
			listTokenizers = config.getList("tokenizers.class", listTokenizers);
			itokenizers = new ArrayList<ITokenizer>();
			for(Object obj : listTokenizers)
			{
				String classToInstantiate = "tokenizer.uneatlantico.es." + obj.toString();
				Object tok = Class.forName(classToInstantiate).newInstance();
				if(tok instanceof ITokenizer)
				{
					ITokenizer itokenizer = (ITokenizer) tok;
					itokenizers.add(itokenizer);
				}
			}
		}
		catch(ConfigurationException e)
		{
			log.error(e.getMessage());
		} catch (InstantiationException e) {
			log.error(e.getMessage());
		} catch (IllegalAccessException e) {
			log.error(e.getMessage());
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage());
		}
	}
	
	public String[] tokenize(String text)
	{
		String[] result = itokenizers.get(0).tokenize(text);
		
		for(ITokenizer itokenizer : itokenizers)
		{
			String[] aux = new String[0];
			for(String word : result)
			{
				String[] words = itokenizer.tokenize(word);
				aux = (String[]) ArrayUtils.addAll(aux, words);
			}
			result = aux;
		}
		return ArrayEmptyElementsRemover.removeEmptyStrings(result);
	}
	
	public List<ITokenizer> getTokenizers()
	{
		return this.itokenizers;
	}
}
