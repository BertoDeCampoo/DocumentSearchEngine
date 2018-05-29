package util.campooproductions.es;

import java.text.Normalizer;

public class StringCleaner 
{
	/**
	 * Removes all special characters and diacritical marks from a String, leaving only spaces between different words
	 * @param target	String to clean	
	 * @return			String without characters or diacritics
	 */
	public static String cleanString(String target)
	{
		String temp = target.toLowerCase();
		temp = Normalizer.normalize(temp, Normalizer.Form.NFD);
		temp = temp.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
	    temp = temp.replaceAll("[^a-zA-Z ]", " ");
	    temp = temp.replaceAll(" +", " ");
	    return temp.trim();
	}
}
