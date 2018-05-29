package parsers;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import parsers.campooproductions.es.UniversalParser;

public class UniversalParserTest {

	private UniversalParser uparser;
	private HashMap<Integer, String> result;
	private final static String DOC_FILE = "src\\test\\resources\\" + "word2003-2-paginas.doc";
	private final static String DOCX_FILE = "src\\test\\resources\\" + "word2007-2-paginas.docx";
	private final static String PDF_FILE = "src\\test\\resources\\" + "pdf-2-paginas.pdf";
	private final static String XML_FILE = "src\\test\\resources\\" + "version_control.xml";
	private final static String TXT_FILE = "src\\test\\resources\\" + "version_control.txt";
	
	@Before
	public void initialize()
	{
		uparser = new UniversalParser();
	}
	
	@Test
	public void docTest() {
		result = uparser.parse(DOC_FILE);
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

	@Test
	public void docxTest() {
		result = uparser.parse(DOCX_FILE);
		
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
	
	@Test
	public void pdfTest() {
		result = uparser.parse(PDF_FILE);
		
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
	
	@Test
	public void xmlTest()
	{
		result = uparser.parse(XML_FILE);
		
		assertEquals(1, result.size());
		
		assertTrue(result.get(1).contains("Solr Version Control System"));
		assertTrue(result.get(1).contains("Overview"));
		assertTrue(result.get(1).contains("The Solr source code resides in the Apache  Subversion (SVN) repository."));
		assertTrue(result.get(1).contains("The command-line SVN client can be obtained  here or as an optional package for  cygwin."));
		assertTrue(result.get(1).contains("The TortoiseSVN GUI client for Windows can be obtained  here. There"));
		assertTrue(result.get(1).contains("are also SVN plugins available for older versions of  Eclipse and"));
		assertTrue(result.get(1).contains("IntelliJ IDEA that don't have subversion support already included."));
		assertTrue(result.get(1).contains("Here is some more text.  It contains  a link."));
		assertTrue(result.get(1).contains("Text Here"));
	}
	
	@Test
	public void txtTest()
	{
		result = uparser.parse(TXT_FILE);
		
		assertEquals(1, result.size());
		
		assertTrue(result.get(1).contains("Solr Version Control System"));
		assertTrue(result.get(1).contains("Overview"));
		assertTrue(result.get(1).contains("The Solr source code resides in the Apache Subversion (SVN) repository."));
		assertTrue(result.get(1).contains("The command-line SVN client can be obtained here or as an optional package"));
		assertTrue(result.get(1).contains("for cygwin."));
		assertTrue(result.get(1).contains("The TortoiseSVN GUI client for Windows can be obtained here. There"));
		assertTrue(result.get(1).contains("are also SVN plugins available for older versions of Eclipse and "));
		assertTrue(result.get(1).contains("IntelliJ IDEA that don't have subversion support already included."));
		assertTrue(result.get(1).contains("Note: This document is an excerpt from a document Licensed to the"));
		assertTrue(result.get(1).contains("Apache Software Foundation (ASF) under one or more contributor"));
		assertTrue(result.get(1).contains("license agreements. See the XML version (version_control.xml) for"));
		assertTrue(result.get(1).contains("more details."));
	}
}
