package parsers.campooproductions.es;

import java.io.FileInputStream;
import java.util.HashMap;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.ToXMLContentHandler;
import org.xml.sax.ContentHandler;

/**
 * Parses any file using a Tika AutoDetectParser
 * @author Alberto Gutiérrez Arroyo
 *
 */
public class UniversalParser implements IParser
{
	public String parseBodyToHTML(String filePath) {
	    ContentHandler handler = new ToXMLContentHandler();
	 
	    AutoDetectParser parser = new AutoDetectParser();
	    Metadata metadata = new Metadata();
	    try {
			parser.parse(new FileInputStream(filePath), handler, metadata);
		} catch (Exception e)
	    {
			System.err.println("Error parsing " + filePath);
			return "";
	    }
	    
	    String contentToString = handler.toString();
	    String body = getBody(contentToString);
	    return body;
	}
	
	private String getBody(String html)
	{
		String bodyToEnd = "";
		String[] parts = html.split("<body>");
		if(parts.length >= 2)
			bodyToEnd = parts[1];
		else 
			return null;
		parts = bodyToEnd.split("</body>");
		return parts[0];
	}
	
	private String[] splitBody(String html)
	{
		if (html != null)
			return html.split("<p />\n</div>\n<div class=\"page\"><p />");
		else
			return new String[0];
	}

	@Override
	public HashMap<Integer, String> parse(String filePath){
		String body = parseBodyToHTML(filePath);
		String[] parts = splitBody(body);
		String aux;
		HashMap<Integer, String> result = new HashMap<Integer, String>();
		for(int i = 0; i < parts.length; i++)
		{
			aux = parts[i].replaceAll("<div class=\"page\"><p />\n", "").replaceAll("</p>", "")
					.replaceAll("<p>", "").replaceAll("<p />", "").replaceAll("</div>", "");
			result.put(i + 1, aux);
		}
		return result;
	}
}
