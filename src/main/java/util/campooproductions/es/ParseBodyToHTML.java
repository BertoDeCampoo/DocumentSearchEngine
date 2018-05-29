package util.campooproductions.es;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.ToXMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class ParseBodyToHTML 
{
	public static String parseBodyToHTML(String filePath) {
	    ContentHandler handler = new ToXMLContentHandler();
	 
	    AutoDetectParser parser = new AutoDetectParser();
	    Metadata metadata = new Metadata();
	    try {
	        parser.parse(new FileInputStream(filePath), handler, metadata);
	    } catch(IOException ioe)
		{
			ioe.printStackTrace();
		} catch (SAXException sax) {
			sax.printStackTrace();
		} catch (TikaException te) {
			te.printStackTrace();
		}
	    String contentToString = handler.toString();
	    String body = getBody(contentToString);
	    return body;
	}
	
	private static String getBody(String html)
	{
		String[] parts = html.split("<body>");
		String bodyToEnd = parts[1];
		parts = bodyToEnd.split("</body>");
		return parts[0];
	}
}
