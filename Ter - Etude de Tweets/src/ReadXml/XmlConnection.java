/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ReadXml;

import com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 *
 * @author kevinsancho
 */
public class XmlConnection {
    
    public String url;
    public String user;
    public String password;

 
    public ArrayList<String> parseXmlFile()
    {
        ArrayList<String> returnString = new ArrayList<String>();
	try 
        {
	File fXmlFile = new File("src/Files/Connection.xml");
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);
 
	doc.getDocumentElement().normalize();
 
	NodeList nList = doc.getElementsByTagName("Connection");
 
	for (int temp = 0; temp < nList.getLength(); temp++) 
        {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) 
            {
                    Element eElement = (Element) nNode;

                    this.user = eElement.getElementsByTagName("StringUser").item(0).getTextContent();
                    this.password = eElement.getElementsByTagName("StringPassword").item(0).getTextContent();
                    this.url =  eElement.getElementsByTagName("StringUrl").item(0).getTextContent();
            }
	}
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        returnString.add(this.url);
        returnString.add(this.user);
        returnString.add(this.password);
        return returnString;
    }  
}
