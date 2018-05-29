package index.campooproductions.es;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import tokenizer.campooproductions.es.TokenizerFactory;
import util.campooproductions.es.StringCleaner;

/**
 * Creates the inverted index using page content and page number
 * @author Alberto Gutiérrez
 *
 */
public class InvertedIndex implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1915693828589918752L;
	/**
	 * Main index: Word, Document, Page, Occurrences
	 */
	private HashMap<String, HashMap<String, HashMap<Integer, Integer>>> index;
	/**
	 * Term-Frequency index: Stores the word and a HashMap with the document in which it
	 * appears and the term-frequency value for that document
	 */
	private HashMap<String, HashMap<String, Double>> tf;
	/**
	 * Inverse Term-Frequency index: Stores the word and his idf value
	 */
	private HashMap<String, Double> idf;
	/**
	 * Tokenizer to use in the processing of pages for the index
	 */
	private TokenizerFactory tokenizer;
	//Inicializar logger:
	//BasicConfigurator.configure();
	//log.info("Logger initialized");
	static Logger log = Logger.getLogger(InvertedIndex.class.getName());
	
	/**
	 * Creates a new instance of InvertedIndex with the default TokenizerFactory
	 */
	public InvertedIndex()
	{
		log.setLevel(Level.WARN);
		index = new HashMap<String, HashMap<String, HashMap<Integer, Integer>>>();
		tf = new HashMap<String, HashMap<String, Double>>();
		idf = new HashMap<String, Double>();
		this.tokenizer = new TokenizerFactory();
		log.info("Index initialized succesfully.");
	}
	
	/**
	 * Creates a new instance of InvertedIndex
	 * @param tokenizer	TokenizerFactory to use to build the index
	 */
	public InvertedIndex(TokenizerFactory tokenizer)
	{
		index = new HashMap<String, HashMap<String, HashMap<Integer, Integer>>>();
		tf = new HashMap<String, HashMap<String, Double>>();
		idf = new HashMap<String, Double>();
		this.tokenizer = tokenizer;
		log.info("Index initialized succesfully.");
	}
	
	/**
	 * Adds a page to the index.	
	 * @param pageNumber	The number of the page
	 * @param pageContent	The content of the page
	 * @param document		The document in which the page is located
	 */
	public void addPageToIndex(int pageNumber, String pageContent, String document)
	{
		if(!(pageNumber > 0 && pageContent != null && document != null))
		{
			System.err.println("Invalid page, discarded");
			return;
		}
		String[] words = tokenizer.tokenize(pageContent);
		HashMap<String, HashMap<Integer, Integer>> docHashMap = null;
		HashMap<Integer, Integer> pageHashMap = null;
		HashMap<String, Double> termFrecuency = null;
				
		for(String word : words)
		{
			//Si no tenemos esa palabra en el índice
			if(!(index.containsKey(word)))
			{
				docHashMap = new HashMap<String, HashMap<Integer, Integer>>();
				pageHashMap = new HashMap<Integer, Integer>();
				pageHashMap.put(pageNumber, 1);
				docHashMap.put(document, pageHashMap);
				index.put(word, docHashMap);
				
				termFrecuency = new HashMap<String, Double>();
				termFrecuency.put(document, tf(word, document));
				tf.put(word, termFrecuency);
				idf.put(word, idf(word));
				log.info(word + " added to the index within document " + document + " on page " + pageNumber);
			}
			//Si ya existe esa palabra en el índice
			else
			{
				//Si esta palabra no ha aparecido en el documento
				if(!(index.get(word).containsKey(document)))
				{
					docHashMap = index.get(word);
					pageHashMap = new HashMap<Integer, Integer>();
					pageHashMap.put(pageNumber, 1);
					index.get(word).put(document, pageHashMap);
					log.info(word + " added to document " + document + " on page " + pageNumber);
				}
				//Si esa palabra ya aparece en ese documento, se incrementa el contador
				else
				{
					pageHashMap = index.get(word).get(document);
					if(pageHashMap.get(pageNumber) == null)
						pageHashMap.put(pageNumber, 1);
					else
						pageHashMap.replace(pageNumber,  pageHashMap.get(pageNumber) + 1);
					log.info(word + " added to page " + pageNumber + " of document " + document);
				}		
				termFrecuency = tf.get(word);
				termFrecuency.put(document, tf(word, document));
				tf.put(word, termFrecuency);
				idf.put(word,  idf(word));
			}
		}
	}	
	
	/**
	 * Gets the pages in which the given word appears (And the times in each page)
	 * @param word		String to search on the index
	 * @param document	Document in which to search
	 * @return			List with the pages in which the word appears and the number of times for each page
	 */
	public List<Integer> getPageNumbers(String word, String document)
	{
		List<Integer> result = new ArrayList<Integer>();
		HashMap<String, HashMap<Integer, Integer>> documents = index.get(word);
		HashMap<Integer, Integer> pages;
		if(documents != null)
		{
			pages = documents.get(document);
			if(pages != null)
				for(HashMap.Entry<Integer, Integer> num : pages.entrySet())
					result.add(num.getKey());
		}
		Collections.sort(result);
		return result;
	}
	
	/**
	 * Returns the number of times a given word appears on a given page
	 * @param pageNumber	the page in which to search the given word
	 * @param word			String to search on the given page
	 * @param document		Document in which to check occurrences
	 * @return				the number of times that word appears on the page
	 */
	public int getOccurrencesOnPage(int pageNumber, String word, String document)
	{
		word = StringCleaner.cleanString(word);
		HashMap<String, HashMap<Integer, Integer>> documents = index.get(word);
		HashMap<Integer, Integer> pages;
		if(documents != null)
		{
			pages = documents.get(document);
			if(pages != null)
			{
				Integer occurrences = pages.get(pageNumber);
				if(occurrences != null)
					return occurrences;
			}
		}
		return 0;
	}
	
	/**
	 * Generates a HashMap containing the documents in which a given word appears and the times for each document
	 * @param word	the word to search
	 * @return		HashMap<String, Integer>: Document and occurrences
	 */
	public HashMap<String, Integer> getOccurrencesOnDocuments(String word)
	{
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		
		HashMap<String, HashMap<Integer, Integer>> aux = index.get(word);
		if(aux != null)
			//Por cada documento
			for(Entry<String, HashMap<Integer, Integer>> currentDocument : aux.entrySet())
			{
				//Por cada página en la que aparece la palabra
				HashMap<Integer, Integer> occurrences = currentDocument.getValue();
				int total = 0;
				for(Entry<Integer, Integer>  occurrencesOnCurrentPage : occurrences.entrySet())
				{
					total += occurrencesOnCurrentPage.getValue();
				}
				result.put(currentDocument.getKey(), total);
			}
		return result;
	}
	
	/**
	 * Calculates the term-frequency value for a given word on a given document
	 * @param word		Word to get the tf
	 * @param document	Document in which the word appears
	 * @return			Term-Frequency value
	 */
	private double tf(String word, String document)
	{
		double total = 0;
		HashMap<Integer, Integer> map = index.get(word).get(document);
		if(map != null)
			for(HashMap.Entry<Integer, Integer> num : map.entrySet())
				total += num.getValue();
		return total;
	}
	
	/**
	 * Calculates the inverse idf value for a given word
	 * @param word		Word to get idf
	 * @return			Inverse Term-Frequency value
	 */
	private double idf(String word)
	{
		double numDocsContainingWord = index.get(word).keySet().size();
		double numDocuments = getNumberOfDocuments();
		
		return Math.log(((numDocuments*1.0)/numDocsContainingWord) + 1);
	}
	
	/**
	 * Calculates the TF-IDF for a given word. Returns it in a HashMap
	 * @param word	String the document must contain
	 * @return		<CODE>HashMap</CODE> with the word and it's TF-IDF value
	 */
	public HashMap<String, Double> getTfIdfIndex(String word)
	{
		HashMap<String, Double> result = new HashMap<String, Double>();
		
		for(String document : getDocumentsContaining(word))
		{
			result.put(document, getTfIdf(word, document));
		}
		return result;
	}
	/**
	 * Calculates the TF-IDF value of a given word for a document
	 * @param word		Word to look for
	 * @param document	Document to get TF-IDF 
	 * @return			Value of TF-IDF of a given word for a document
	 */
	public Double getTfIdf(String word, String document)
	{
		return getTf(word, document) * getIdf(word);
	}
	
	/**
	 * Calculates the TF value of a given word
	 * @param word	Word to look for
	 * @return		Value of TF for that word
	 */
	public HashMap<String, Double> getTfIndex(String word)
	{
		HashMap<String, Double> result = tf.get(word);
		if(result != null)
		{
			return result;
		}
		else
			return new HashMap<String, Double>();
	}
	
	/**
	 * Gets the Term-Frequency value of a given word (Retrieves it from an internal HashMap)
	 * @param word		Word to get the Term-Frequency
	 * @param document	Document in which we look for the word
	 * @return			Term Frequency of the word in the document
	 */
	public Double getTf(String word, String document)
	{
		HashMap<String, Double> documents = tf.get(word);
		
		if(documents != null)
		{
			Double result = documents.get(document);
			if(result != null)
				return result;
		}
		return new Double(0);
	}
	
	/**
	 * Gets the Inverse Term-Frequency value of a given word (Retrieves it from an internal HashMap)
	 * @param word		Word to get the Inverse Term-Frequency
	 * @param document	Document in which we look for the word
	 * @return			Inverse Term Frequency of the word in the document
	 */
	public Double getIdf(String word)
	{
		return idf.get(word);
	}
	
	/**
	 * Gets the number of documents stored in the index
	 * @return			Number of documents stored in the index
	 */
	public int getNumberOfDocuments()
	{
		Collection<HashMap<String, HashMap<Integer, Integer>>> collection = index.values();
		
		HashMap<String, HashMap<Integer, Integer>> documents = new HashMap<String, HashMap<Integer, Integer>>(); 
		
		for(HashMap<String, HashMap<Integer, Integer>> document : collection)
		{
			for(String key : document.keySet())
				documents.putIfAbsent(key, document.get(key));
		}
		return documents.size();
	}
	
	/**
	 * Returns the list of all the documents on the index
	 * @return	String[] with the name of the documents
	 */
	public String[] getAllDocuments()
	{
		HashSet<String> result = new HashSet<String>();
		
		for (String word : index.keySet()) {
			for(String document : index.get(word).keySet())
			{
				result.add(document);
			}
		}
		List<String> keys = new LinkedList<String>(result);
		keys.sort(String::compareToIgnoreCase);
		Object[] aux = keys.toArray();
		String[] aux2 = new String[aux.length];
		for(int i = 0; i < aux2.length; i++)
		{
			aux2[i] = aux[i].toString();
		}
		return aux2;
	}
	
	/**
	 * Returns an array of Strings with the name of the documents containing a given word
	 * @param word	String the documents must contain
	 * @return		String[] with the name of the documents
	 */
	public String[] getDocumentsContaining(String word)
	{
		ArrayList<String> result = new ArrayList<String>();
		
		HashMap<String, HashMap<Integer, Integer>> aux = index.get(word);
		if(aux != null)
			//For each document
			for(Entry<String, HashMap<Integer, Integer>> currentDocument : aux.entrySet())
			{
				result.add(currentDocument.getKey());
			}
		String[] documents = new String[result.size()];
		for(int i = 0; i < documents.length; i++)
			documents[i] = result.get(i);
		Arrays.sort(documents);
		return documents;
	}
	
	/**
	 * Returns a list containing the documents in which the given word appears with TF-IDF value
	 * @param word	String to search on the documents
	 * @return		LinkedList <CODE>String</CODE> Documents containing the word
	 */
	public List<String> searchOnDocuments(String word)
	{
		TreeMap<Double, String> documents = new TreeMap<Double, String>(Collections.reverseOrder());
		
		HashMap<String, Double> docs = tf.get(word);
		List<String> docNames = new LinkedList<String>();
		
		if(docs != null)
		{
			for(Entry<String, Double> document : docs.entrySet())
			{
				Double tfidf = getTfIdf(word, document.getKey());
				
				
				while(documents.get(tfidf) != null)
					tfidf += 0.0000000001;
				//If another document already has the same word with the same tf-idf
				documents.put(tfidf, document.getKey());
			}
			docNames = new LinkedList<String>(documents.values());
		}
		return docNames;
	}
	
	/**
	 * Removes a document from the index
	 * @param document	Document to remove
	 */
	public void remove(String document)
	{
		for(Entry<String, HashMap<String, HashMap<Integer, Integer>>> word : index.entrySet())
		{
			String currentWord = word.getKey();
			if(index.get(currentWord).containsKey(document))
			{
				index.get(currentWord).remove(document);
				tf.get(currentWord).remove(document);
				idf.put(currentWord, idf(currentWord));
				log.info(word + " removed from document " + document);
			}
		}
		//Clear empty words (They only appeared on the removed document
		Iterator<Entry<String, HashMap<String, HashMap<Integer, Integer>>>> wordIterator = index.entrySet().iterator();
		while (wordIterator.hasNext()) {
		    Entry<String, HashMap<String, HashMap<Integer, Integer>>> e = wordIterator.next();
		    HashMap<String, HashMap<Integer, Integer>> value = e.getValue();
		    if (value.isEmpty()) {
		        wordIterator.remove();
		    }
		}
		log.warn(document + " removed from index");
	}
	
	/**
	 * Returns the number of pages for a given document
	 * @param document	<CODE>String</CODE> document to get page number
	 * @return			<CODE>int</CODE> number of pages of the document
	 */
	public int getNumberOfPages(String document)
	{
		int topPage = 0;
		for(Entry<String, HashMap<String, HashMap<Integer, Integer>>> word : index.entrySet())
		{
			String currentWord = word.getKey();
			
			if(index.get(currentWord).containsKey(document))
			{
				HashMap<Integer, Integer> map = index.get(currentWord).get(document);
				int currentMaxPage = Collections.max(map.keySet());
				if(topPage < currentMaxPage)
					topPage = currentMaxPage;
			}
		}
		return topPage;
	}
	@Override
	public String toString()
	{
		return index.toString();
	}
	
	/**
	 * Serializes the index, tf and idf HashMaps to a given file
	 * <BR><B>IMPORTANT: The file name must not contain extension</B>
	 * @param absolutePath	Path in which to serialize the index (without extension)
	 */
	public void exportIndex(String absolutePath)
	{
		if(!(absolutePath.endsWith(".ser")))
			absolutePath += ".ser";
		try {
	         FileOutputStream fileOut =
	         new FileOutputStream(absolutePath);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(index);
	         out.writeObject(tf);
	         out.writeObject(idf);
	         out.close();
	         fileOut.close();
	         log.info("Serialized index is saved in " + absolutePath);
	      }catch(IOException i) {
	         log.error("Error serializing index on " + absolutePath);
	      }
	}
	
	/**
	 * Deserializes the InvertedIndex from a given file. 
	 * @param absolutepath	Location of the file. Needs the extension
	 * @throws FileNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public void importIndex(String absolutepath) throws FileNotFoundException
	{
		HashMap<String, HashMap<String, HashMap<Integer, Integer>>> deserializedIndex = null;
		HashMap<String, HashMap<String, Double>> deserializedTf = null;
		HashMap<String, Double> deserializedIdf = null;
		try {
			FileInputStream fileIn = new FileInputStream(absolutepath);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			deserializedIndex = (HashMap<String, HashMap<String, HashMap<Integer, Integer>>>) in.readObject();
			deserializedTf = (HashMap<String, HashMap<String, Double>>) in.readObject();
			deserializedIdf = (HashMap<String, Double>) in.readObject();
			if(deserializedIndex != null && deserializedTf != null && deserializedIdf != null)
			{
				this.index = deserializedIndex;
				this.tf = deserializedTf;
				this.idf = deserializedIdf;
				log.info("Serialized index imported from " + absolutepath);
			}
			in.close();
			fileIn.close();
		}catch(ClassNotFoundException c) {
			log.error("SerializableInvertedIndex class not found on " + absolutepath);
		} catch (IOException e) {
			log.error("I/O error deserializing from " + absolutepath);
		}
	}
}
