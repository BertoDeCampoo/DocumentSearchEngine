package parsers.campooproductions.es;

import java.util.HashMap;

public interface IParser 
{
	
	/**
	 * Parses a given file and return its contents in a data structure
	 * @param filePath	Path to the file given
	 * @return			HashMap with the number of the page and its content
	 */
	public HashMap<Integer, String> parse(String filePath);
	
}
