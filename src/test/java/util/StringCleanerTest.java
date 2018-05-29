package util;

import static org.junit.Assert.*;

import org.junit.Test;

import util.campooproductions.es.StringCleaner;

public class StringCleanerTest {

	@Test
	public void testCleanSpecialChars()
	{
		String target = "-Pruèba\t básÏca.prueba,prueba\nBÁSica...´bAsicâ, basica";
		String expected = "prueba basica prueba prueba basica basica basica";
		
		assertEquals(expected, StringCleaner.cleanString(target));
	}
	

}
