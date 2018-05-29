package tokenizer.campooproductions.es;

public class PunctuationMarksTokenizer implements ITokenizer {

	private final static String punctuationMarks = "[()/',;:¿¡.!?\\-]";
	
	public String[] tokenize(String text) {
		return text.split(punctuationMarks);
	}

}
