package parsers.campooproductions.es;

public class ParserFactory 
{
	public IParser getParser(String fileName)
	{
		String[] parts = fileName.toLowerCase().split("\\.");
		String extension = parts[parts.length - 1];
		
		if(extension.equals("pdf"))
			return new PdfParser();
		else if(extension.equals("doc") || extension.equals("ppt") || extension.equals("xls"))
			return new OLEParser();
		else if(extension.equals("docx") || extension.equals("pptx") || extension.equals("xlsx"))
			return new OfficeOpenXMLParser();
		else if(extension.equals("xml"))
			return new XmlParser();
		else
			return new UniversalParser();
	}
}
