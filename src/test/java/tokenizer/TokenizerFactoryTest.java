package tokenizer;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import tokenizer.campooproductions.es.TokenizerFactory;

public class TokenizerFactoryTest {

	private TokenizerFactory tf, tfByFile;
	private String sample;
	@Before
	public void initialize()
	{
		tf = new TokenizerFactory();
		tfByFile = new TokenizerFactory("src\\test\\resources\\tokenizers.xml");
		sample = "Caboverdiano/Kabuverdianu (Cape Verde): M' podê cumê vidru, ca ta maguâ-m'.\n"
				+ "Papiamentu: Ami por kome glas anto e no ta hasimi daño.";
	}
	
	@Test
	public void tokenizerFactoryTest() {
		String[] result = tf.tokenize(sample);
		
		assertFalse(result.length == 0);
		assertEquals(23, result.length);
		assertEquals("caboverdiano", result[0]);
		assertEquals("kabuverdianu", result[1]);
		assertEquals("cape", result[2]);
		assertEquals("verde", result[3]);
		assertEquals("m", result[4]);
		assertEquals("pode", result[5]);
		assertEquals("cume", result[6]);
		assertEquals("vidru", result[7]);
		assertEquals("ca", result[8]);
		assertEquals("ta", result[9]);
		assertEquals("magua", result[10]);
		assertEquals("m", result[11]);
		assertEquals("papiamentu", result[12]);
		assertEquals("ami", result[13]);
		assertEquals("por", result[14]);
		assertEquals("kome", result[15]);
		assertEquals("glas", result[16]);
		assertEquals("anto", result[17]);
		assertEquals("e", result[18]);
		assertEquals("no", result[19]);
		assertEquals("ta", result[20]);
		assertEquals("hasimi", result[21]);
		assertEquals("dano", result[22]);
	}
	
	@Test
	public void tokenizerFactoryTestByFile() {
		String[] result = tfByFile.tokenize(sample);
		
		assertFalse(result.length == 0);
		assertEquals(23, result.length);
		assertEquals("caboverdiano", result[0]);
		assertEquals("kabuverdianu", result[1]);
		assertEquals("cape", result[2]);
		assertEquals("verde", result[3]);
		assertEquals("m", result[4]);
		assertEquals("pode", result[5]);
		assertEquals("cume", result[6]);
		assertEquals("vidru", result[7]);
		assertEquals("ca", result[8]);
		assertEquals("ta", result[9]);
		assertEquals("magua", result[10]);
		assertEquals("m", result[11]);
		assertEquals("papiamentu", result[12]);
		assertEquals("ami", result[13]);
		assertEquals("por", result[14]);
		assertEquals("kome", result[15]);
		assertEquals("glas", result[16]);
		assertEquals("anto", result[17]);
		assertEquals("e", result[18]);
		assertEquals("no", result[19]);
		assertEquals("ta", result[20]);
		assertEquals("hasimi", result[21]);
		assertEquals("dano", result[22]);
	}

}
