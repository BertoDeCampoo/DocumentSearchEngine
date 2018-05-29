package tokenizer.campooproductions.es;

import util.campooproductions.es.StringCleaner;

public class FullSymbolTokenizer implements ITokenizer
{
	public String[] tokenize(String text) {
		text = StringCleaner.cleanString(text);
		return text.split(" ");
	}

}
