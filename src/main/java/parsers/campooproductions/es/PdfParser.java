package parsers.campooproductions.es;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * Parses Portable Document Format files (PDF): PDF
 * @author Alberto Gutiérrez Arroyo
 *
 */
public class PdfParser implements IParser
{
	static Logger log = Logger.getLogger(PdfParser.class.getName());
	

	/**
	 * Tika does not deal with pages, so PDFBox is used
	 */
	@Override
	public HashMap<Integer, String> parse(String filePath) {
		HashMap<Integer, String> result = new HashMap<Integer, String>();
	    File file = new File(filePath);
	    PDDocument document;
	    PDFTextStripper pdfStripper;
		try {
			log.info("Loading PDF file " + filePath);
			document = PDDocument.load(file);
			pdfStripper = new PDFTextStripper();
			log.info("Retrieving number of pages of " + filePath);
			int numberOfPages = document.getNumberOfPages();
			String pageText;
			log.info("Retrieving content of " + numberOfPages + " pages");
			for(int i = 0; i < numberOfPages; i++)
			{
				pdfStripper = new PDFTextStripper();
				pdfStripper.setStartPage(i + 1);
				pdfStripper.setEndPage(i+1);
				pageText = pdfStripper.getText(document);
				result.put(i + 1, pageText);
			}
			log.info("Closing file");
		    document.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return result;
	}
}
