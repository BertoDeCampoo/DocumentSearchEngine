package util;

import static org.junit.Assert.*;

import org.junit.Test;

import util.campooproductions.es.ArrayEmptyElementsRemover;

public class ArrayEmptyElementsRemoverTest {

	@Test
	public void test() {
		String[] original = {"Hola", "esto", null, "es", null, "una", "prueba"};
		String[] expected = {"Hola", "esto", "es", "una", "prueba"};
		assertArrayEquals(expected, ArrayEmptyElementsRemover.removeEmptyStrings(original));
	}

}
