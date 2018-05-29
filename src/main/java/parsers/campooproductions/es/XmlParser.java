package parsers.campooproductions.es;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.xml.XMLParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

/**
 * Parses eXtensible Markup Language files (XML): XML
 * @author Alberto Gutiérrez Arroyo
 *
 */
public class XmlParser implements IParser
{
	static Logger log = Logger.getLogger(XmlParser.class.getName());

	@Override
	public HashMap<Integer, String> parse(String filePath) {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		log.info("Reading from file " + filePath);
		XMLParser xmlparser = new XMLParser();
		BodyContentHandler handler = new BodyContentHandler(-1);
		log.info("Parsing XML");
		try {
			xmlparser.parse(new FileInputStream(new File(filePath)), handler, new Metadata(), new ParseContext());
			map.put(1, handler.toString());
			
		} catch (IOException | SAXException | TikaException e) {
			log.error(e.getMessage());
		}
		return map;
	}

}
