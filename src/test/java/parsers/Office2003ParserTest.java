package parsers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import parsers.campooproductions.es.OLEParser;

public class Office2003ParserTest 
{
	private final static String DOC_FILE = "src\\test\\resources\\" + "word2003-2-paginas.doc";
	private OLEParser docparser;
	private HashMap<Integer, String> result;
	
	@Before
	public void initialize()
	{
		docparser = new OLEParser();
	}
	
	@Test
	public void docTest() {
		result = docparser.parse(DOC_FILE);
		System.out.println(result.toString());
		assertEquals(1, result.size());
		
		assertTrue(result.get(1).contains("Esta es la p�gina 1"));
		assertTrue(result.get(1).contains("Final de la p�gina 1"));
		
		for(int i = 3; i < 50; i++)
		{
			assertTrue(result.get(1).contains("L�nea " + i));
		}
		assertTrue(result.get(1).contains("Esta es la p�gina 2"));
		assertTrue(result.get(1).contains("Final de la p�gina 2"));
	}
}
