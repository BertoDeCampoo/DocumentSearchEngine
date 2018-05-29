package tokenizer.campooproductions.es;

public interface ITokenizer 
{
	/**
	 * Tokenizes a given text following a pattern
	 * @param text	Text to split
	 * @return		Array of string containing the parts of the original text
	 */
	public String[] tokenize(String text);
}
