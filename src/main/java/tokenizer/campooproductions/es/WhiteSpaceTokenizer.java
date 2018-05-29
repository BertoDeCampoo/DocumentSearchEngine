package tokenizer.campooproductions.es;

public class WhiteSpaceTokenizer implements ITokenizer
{

	public String[] tokenize(String text) {
		return text.split("\\s+");
	}

}
