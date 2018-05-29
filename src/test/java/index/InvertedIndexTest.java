package index;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import index.campooproductions.es.InvertedIndex;
import index.campooproductions.es.LegacyInvertedIndex;
import tokenizer.campooproductions.es.*;

public class InvertedIndexTest {

	private String texto1, texto2;
	private String doc1, doc2, doc3;
	private InvertedIndex index;
	
	@Before
	public void initialize()
	{
		texto1 = "-Pruèba\t básÏca.prueba,prueba\nBÁSica...´bAsicâ, basica";
		texto2 = ". Permite revisar, escribir y solicitar artículos.prueba";
		doc1 = "documento1";
		doc2 = "documento2";
		doc3 = "documento3";
		//ITokenizer it = new TokenizerFactory("tokenizers.xml");
		index = new InvertedIndex(new TokenizerFactory("tokenizers.xml"));
		index.addPageToIndex(1, texto1, doc1);
		index.addPageToIndex(2, texto2, doc1);
		index.addPageToIndex(1, "HOLA", doc2);
		index.addPageToIndex(1, "Prueba en el documento 3 basica", doc3);
	}

	@Test
	public void testAddPage() {
		
		assertEquals(2, index.getPageNumbers("prueba", doc1).size());
		assertEquals(3, index.getOccurrencesOnPage(1, "prueba", doc1));
		assertEquals(3, index.getOccurrencesOnPage(1, "PRÜÉBÂ", doc1));
		assertEquals(0, index.getOccurrencesOnPage(1, "permite", doc1));
		assertEquals(4, index.getOccurrencesOnPage(1, "basica", doc1));
		assertEquals(4, index.getOccurrencesOnPage(1, "BÁSICA", doc1));
		assertEquals(1, index.getOccurrencesOnPage(2, "pErmitê", doc1));
		assertEquals(1, index.getOccurrencesOnPage(2, "pErmitê", doc1));
		assertEquals(1, index.getOccurrencesOnPage(2, "Prueba", doc1));
		assertEquals(1, index.getOccurrencesOnPage(2, "permite", doc1));
	}
	
	@Test
	public void TestInvertedExpectedNumresults(){
	    LegacyInvertedIndex index = new LegacyInvertedIndex(new TokenizerFactory("tokenizers.xml"));
	    index.addPageToIndex(1, "ejemplo de la primera página con contenido 1. contenido", doc1);
	    index.addPageToIndex(2, "ejemplo de la segunda página con contenido 2", doc1);
	    index.addPageToIndex(3, "ejemplo de la tercera página con contenido 3 simple", doc1);
	    
	    assertEquals(3,index.getPageNumbers("ejemplo", doc1).size());
	    assertEquals(1,index.getPageNumbers("simple", doc1).size());
	    assertEquals(0,index.getPageNumbers("no", doc1).size());
	}
	
	@Test
	public void testNumberOfDocuments(){
		assertEquals(3, index.getNumberOfDocuments());
		index.addPageToIndex(1, "ja ja prueba cincomil", "documento3");
		assertEquals(3, index.getNumberOfDocuments());
		index.addPageToIndex(1, "ja ja prueba cincomil", "documento4");
		assertEquals(4, index.getNumberOfDocuments());
		index.addPageToIndex(2, "ja ja prueba cincomil", "documento4");
		index.addPageToIndex(3, "ja ja prueba cincomil", "documento4");
		assertEquals(4, index.getNumberOfDocuments());
	}
	
	@Test
	public void testGetOccurrencesOnDocuments(){
		HashMap<String, Integer> documentsOccurrences = new HashMap<String, Integer>();
		documentsOccurrences.put("documento1", 4);
		documentsOccurrences.put("documento3", 1);
		assertEquals(documentsOccurrences, index.getOccurrencesOnDocuments("prueba"));
		
		documentsOccurrences = new HashMap<String, Integer>();
		documentsOccurrences.put("documento1", 4);
		documentsOccurrences.put("documento3", 1);
		assertEquals(documentsOccurrences, index.getOccurrencesOnDocuments("basica"));
	}
	
	@Test
	public void searchOnDocumentsTest() {
		index.addPageToIndex(1, "teléfono", "doc3000");
		index.addPageToIndex(2, "esternocleidomastoideos prueba", "doc3000");
		index.addPageToIndex(2, "esternocleidomastoideos prueba", "doc30001");
	}
	
	@Test
	public void removeTest() {
		assertEquals(3, index.getNumberOfDocuments());
		index.remove(doc2);
		assertEquals(2, index.getNumberOfDocuments());
		index.remove(doc1);
		assertEquals(1, index.getNumberOfDocuments());
		assertEquals(doc3, index.getAllDocuments()[0]);
	}
	
	@Test
	public void getNumberOfPagesTest() {
		assertEquals(2, index.getNumberOfPages(doc1));
		index.addPageToIndex(3, "AÑADIDO", doc1);
		assertEquals(3, index.getNumberOfPages(doc1));
		index.addPageToIndex(1, "AÑADIDO", doc1);
		index.addPageToIndex(3000, "AÑADIDO", doc1);
		assertEquals(3000, index.getNumberOfPages(doc1));
		index.addPageToIndex(-1, "AÑADIDO", doc1);
		assertEquals(3000, index.getNumberOfPages(doc1));
	}

}
