package util;

import static org.junit.Assert.*;

import org.junit.Test;

import util.campooproductions.es.StringCleaner;

public class StringCleanerTest {

	@Test
	public void testCleanSpecialChars()
	{
		String target = "-Pru�ba\t b�s�ca.prueba,prueba\nB�Sica...�bAsic�, basica";
		String expected = "prueba basica prueba prueba basica basica basica";
		
		assertEquals(expected, StringCleaner.cleanString(target));
	}
	

}
