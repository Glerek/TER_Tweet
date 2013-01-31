/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ReadXml;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author kevinsancho
 */
public class XmlIncrement {
    
    public String increment;
        
    public String parseXmlFile()
    {
                
	try 
        {
	File fXmlFile = new File("src/Files/Indice.xml");
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);
 
	doc.getDocumentElement().normalize();
 
	NodeList nList = doc.getElementsByTagName("Save");
 
	for (int temp = 0; temp < nList.getLength(); temp++) 
        {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) 
            {
                    Element eElement = (Element) nNode;

                    this.increment = eElement.getElementsByTagName("intIncrement").item(0).getTextContent();
            }
	}
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return this.increment;
    } 
    
    public void saveinXmlFile(String intIncrement)
    {
        
    }
    
}
