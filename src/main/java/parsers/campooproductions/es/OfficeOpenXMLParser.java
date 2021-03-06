package parsers.campooproductions.es;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

/**
 * Parses Office Open XML files (Office 2007 and newer): DOCX, XLSX, PPTX
 * @author Alberto Guti�rrez Arroyo
 *
 */
public class OfficeOpenXMLParser implements IParser 
{
	static Logger log = Logger.getLogger(OfficeOpenXMLParser.class.getName());
	
	@Override
	public HashMap<Integer, String> parse(String filePath) {
		HashMap<Integer, String> result = new HashMap<Integer, String>();
		try
		{
			log.info("Reading from file " + filePath);
			OOXMLParser wordparser = new OOXMLParser();
			BodyContentHandler handler = new BodyContentHandler(-1);
			log.info("Parsing Office Open XML File");
			wordparser.parse(new FileInputStream(filePath), handler, new Metadata(), new ParseContext());
			
			log.info("Retrieving content of the document");
			log.warn("Page system not supported for Office Open XML files, all the content will be appended on page 1");
			result.put(1, handler.toString());
		}
		catch(IOException ioe)
		{
			log.error(ioe.getMessage());
		} catch (SAXException sax) {
			log.error(sax.getMessage());
		} catch (TikaException te) {
			log.error(te.getMessage());
		}
		return result;
	}

}
