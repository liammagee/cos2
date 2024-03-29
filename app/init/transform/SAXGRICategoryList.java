package init.transform;

import java.io.*;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import java.util.HashMap;

/**
 * 
 * @author lida
 * 
 */

public class SAXGRICategoryList extends DefaultHandler 
{

	
	private static int index = 0;
	public static String baseDir = "conf/";
	private HashMap categoryList = new HashMap();
	

	
	public void start() throws ParserConfigurationException,SAXException, IOException 
	{
	
		DefaultHandler handler = new SAXGRIReferenceHandler();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		saxParser.parse(new File(baseDir + "GRI-XBRL/GRICategorise.xml"),this);

	}

	public void startElement(String namespaceURI, String localName,String qName, Attributes atts) throws SAXException 
	{
		
		if(qName.equalsIgnoreCase("loc"))
		{ 							
			if(atts.getValue(1).length()>26)				
				catgoryList.put(index++, atts.getValue(2).toString());
			}
		}
	

	

	public void endElement(String namespaceURI, String localName, String qName)throws SAXException 
	{
			

	}
	

	
	
	private HashMap catgoryList = new HashMap();
	public HashMap getCatgoryList() {
		return catgoryList;
	}

	public void setCatgoryList(HashMap catgoryList) {
		this.catgoryList = catgoryList;
	}
}

