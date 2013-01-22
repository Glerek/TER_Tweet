/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Program;

import com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl;
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
public class XmlAction {
    
    private Document dom;
    public String url;
    public String user;
    public String password;

    public XmlAction() {
        dom = new Document() {

            @Override
            public DocumentType getDoctype() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public DOMImplementation getImplementation() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Element getDocumentElement() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Element createElement(String tagName) throws DOMException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public DocumentFragment createDocumentFragment() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Text createTextNode(String data) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Comment createComment(String data) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public CDATASection createCDATASection(String data) throws DOMException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Attr createAttribute(String name) throws DOMException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public EntityReference createEntityReference(String name) throws DOMException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public NodeList getElementsByTagName(String tagname) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Node importNode(Node importedNode, boolean deep) throws DOMException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Element getElementById(String elementId) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String getInputEncoding() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String getXmlEncoding() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean getXmlStandalone() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String getXmlVersion() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void setXmlVersion(String xmlVersion) throws DOMException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean getStrictErrorChecking() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void setStrictErrorChecking(boolean strictErrorChecking) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String getDocumentURI() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void setDocumentURI(String documentURI) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Node adoptNode(Node source) throws DOMException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public DOMConfiguration getDomConfig() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void normalizeDocument() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Node renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String getNodeName() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String getNodeValue() throws DOMException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void setNodeValue(String nodeValue) throws DOMException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public short getNodeType() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Node getParentNode() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public NodeList getChildNodes() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Node getFirstChild() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Node getLastChild() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Node getPreviousSibling() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Node getNextSibling() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public NamedNodeMap getAttributes() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Document getOwnerDocument() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Node insertBefore(Node newChild, Node refChild) throws DOMException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Node removeChild(Node oldChild) throws DOMException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Node appendChild(Node newChild) throws DOMException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean hasChildNodes() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Node cloneNode(boolean deep) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void normalize() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean isSupported(String feature, String version) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String getNamespaceURI() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String getPrefix() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void setPrefix(String prefix) throws DOMException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String getLocalName() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean hasAttributes() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String getBaseURI() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public short compareDocumentPosition(Node other) throws DOMException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String getTextContent() throws DOMException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void setTextContent(String textContent) throws DOMException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean isSameNode(Node other) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String lookupPrefix(String namespaceURI) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean isDefaultNamespace(String namespaceURI) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String lookupNamespaceURI(String prefix) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean isEqualNode(Node arg) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Object getFeature(String feature, String version) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Object setUserData(String key, Object data, UserDataHandler handler) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Object getUserData(String key) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }
    
    
    
    public void parseXmlFile(){
        
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			//parse using builder to get DOM representation of the XML file
			dom = db.parse("src/Files/Base.xml");


		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
    
    
    public ArrayList<String> parseDocument(){
            
        ArrayList<String> listeRetour = new ArrayList<String>();
		//get the root element
		Element docEle = dom.getDocumentElement();

		//get a nodelist of elements
		NodeList nl = docEle.getElementsByTagName("StringUrl");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {

				//get the employee element
				Element el = (Element)nl.item(i);
                                
                                url = el.getAttribute("StringUrl");
                                listeRetour.add(url);
                                user = el.getAttribute("StringUSer");
                                listeRetour.add(user);
                                password = el.getAttribute("StringPassword");
                                listeRetour.add(password);
			}
		}
                return listeRetour;

	}
    
}
