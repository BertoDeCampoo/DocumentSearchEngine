package parsers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import parsers.campooproductions.es.OfficeOpenXMLParser;

public class DocxParserTest 
{
	private final static String DOCX_FILE = "src\\test\\resources\\" + "word2007-2-paginas.docx";
	private OfficeOpenXMLParser docxparser;
	private HashMap<Integer, String> result;
	
	@Before
	public void initialize()
	{
		docxparser = new OfficeOpenXMLParser();
	}
	
	@Test
	public void docxTest() {
		result = docxparser.parse(DOCX_FILE);
		
		assertEquals(1, result.size());
		
		assertTrue(result.get(1).contains("Esta es la página 1"));
		assertTrue(result.get(1).contains("Final de la página 1"));
		
		for(int i = 3; i < 50; i++)
		{
			assertTrue(result.get(1).contains("Línea " + i));
		}
		assertTrue(result.get(1).contains("Esta es la página 2"));
		assertTrue(result.get(1).contains("Final de la página 2"));
	}
}
