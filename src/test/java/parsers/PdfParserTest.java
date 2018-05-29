package parsers;
import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import parsers.campooproductions.es.PdfParser;

public class PdfParserTest 
{
	private HashMap<Integer, String> result;
	private PdfParser pdfparser;
	private final static String PDF_FILE = "src\\test\\resources\\" + "solr-word.pdf";
	private final static String PDF_2_PAGES_FILE = "src\\test\\resources\\" + "pdf-2-paginas.pdf";
	
	@Before
	public void initialize()
	{
		pdfparser = new PdfParser();
	}
	
	@Test
	public void pdfOnePageTest() {
		result = pdfparser.parse(PDF_FILE);
		
		assertEquals(1, result.size());
		
		assertTrue(result.get(1).contains("This is a test of PDF and Word extraction in Solr, it is only a test.  Do not panic."));
	}
	
	@Test
	public void pdfTwoPagesTest() {
		result = pdfparser.parse(PDF_2_PAGES_FILE);
		
		assertEquals(3, result.size());
		
		assertTrue(result.get(1).contains("Esta es la página 1"));
		for(int i = 3; i < 50; i++)
		{
			assertTrue(result.get(1).contains("Línea " + i));
		}
		assertTrue(result.get(1).contains("Final de la página 1"));
		
		assertTrue(result.get(2).contains("Esta es la página 2"));
		for(int i = 2; i < 50; i++)
		{
			assertTrue(result.get(2).contains("Línea " + i));
		}
		assertTrue(result.get(2).contains("Final de la página 2"));
	}
}
