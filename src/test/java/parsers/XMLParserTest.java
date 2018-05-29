package parsers;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import parsers.campooproductions.es.XmlParser;

public class XMLParserTest 
{
	static Logger log = Logger.getLogger(PdfParserTest.class.getName());
	private XmlParser xmlparser;
	
	@Before
	public void initialize()
	{
		xmlparser = new XmlParser();
	}
	
	@Test
	public void testIndex() {
		
		String filename = "src\\test\\resources\\" + "version_control.xml";
		HashMap<Integer, String> map = xmlparser.parse(filename);
		System.out.println(map.toString());
	}
}
