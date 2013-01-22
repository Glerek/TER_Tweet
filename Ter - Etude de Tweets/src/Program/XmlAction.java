/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Program;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author kevinsancho
 */
public class XmlAction {
    
    private Document dom;
    public String url;
    public String user;
    public String password;
    
    private void parseXmlFile(){
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			//parse using builder to get DOM representation of the XML file
			dom = db.parse("Files/Base.xml");


		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
    
    
    private void parseDocument(){
		//get the root element
		Element docEle = dom.getDocumentElement();

		//get a nodelist of elements
		NodeList nl = docEle.getElementsByTagName("Connection");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {

				//get the employee element
				Element el = (Element)nl.item(i);
                                
                                url = el.getAttribute("StringUrl");
                                user = el.getAttribute("StringUSer");
                                password = el.getAttribute("StringPassword");
			}
		}
	}
    
}
