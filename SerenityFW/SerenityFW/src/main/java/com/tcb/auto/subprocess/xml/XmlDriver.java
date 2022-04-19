package com.tcb.auto.subprocess.xml;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class provides methods to extract data from excel file.
 * 
 * @author bachtx2
 * @since JDK1.8
 * @version 1.0.170418
 */
public class XmlDriver {

	/**
	 * Parses a Xml String into an <b>org.w3c.dom.Document</b> object
	 * 
	 * @param xmlString
	 * @return <b>org.w3c.dom.Document</b> object
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public Document parseXmlString(String xmlString) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = domFactory.newDocumentBuilder();

		Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
		return doc;
	}

	/**
	 * Evaluate an xpath against a <b>org.w3c.dom.Document</b> object. There are
	 * currently two return types while evaluating xpath are accepted: STRING and
	 * NODESET.
	 * <p>
	 * <ul>
	 * <li>STRING is applied when a xpath function is called (see
	 * {@link https://www.w3schools.com/xml/xsltions.asp#string}). The Xpath
	 * function always return a String as the result of evaluation.</li>
	 * <li>NODESET is used more generally. If a xpath is evaluated as NODESET, an
	 * object of <b>org.w3c.dom.NodeList</b> is pointed, including all nodes which
	 * are referred in the xpath. In case of xpath refers to multiple nodes, all
	 * their text contents will be returned, separated by specific separators.</li>
	 * </ul>
	 * <p>
	 * There are three type of node are considered: TEXT_NODE, ATTRIBUTE_NODE and
	 * ELEMENT_NODE. The term ELEMENT_NODE is used to call all nodes that appear in
	 * a pair of <b>&lt;</b> and <b>&gt;</b> marks, and have corresponding close
	 * node in a pair of <b>&lt;/</b> and <b>&gt;</b> marks.
	 * <p>
	 * A ELEMENT_NODE can have only one direct child TEXT_NODE and no ELEMENT_NODE
	 * child node, or have multiple ELEMENT_NODE child nodes.
	 * <p>
	 * <ul>
	 * <li>In case there is only one TEXT_NODE child node, the text content is
	 * returned.</li>
	 * <li>In case there are at least one ELEMENT_NODE child node, the text content
	 * of this child node is returned as output string of the whole method. If there
	 * are multiple ELEMENT_NODE child nodes, all of their text content is returned
	 * in a string, separated by separator</li>
	 * </ul>
	 * 
	 * @param doc        the object to evaluate xpath
	 * @param xpath      the xpath to evaluate
	 * @param sep1       the separator to separate text value between each node
	 * @param sep2       the separator to separate text value between each child
	 *                   node
	 * @param returnType the type to determine which way to evaluate xpath
	 * @param asNull     NULL_TEXT_INDICATOR
	 * @return a string value after evaluating xpath
	 */
	public String solveXPath(Document doc, String xpath, String sep1, String sep2, String returnType, String asNull) {
		try {

			String xmlObject = ""; /* the string to return */
			XPath xPathSolver = XPathFactory.newInstance().newXPath();

			/**
			 * if returnType is STRING
			 */
			if (returnType.equalsIgnoreCase("STRING")) {
				xmlObject = (String) xPathSolver.compile(xpath).evaluate(doc, XPathConstants.STRING);
				return xmlObject;
			}

			/**
			 * if returnType is NODESET
			 */
			if (returnType.equalsIgnoreCase("NODESET")) {

				// get the list of node
				NodeList nodeList = (NodeList) xPathSolver.compile(xpath).evaluate(doc, XPathConstants.NODESET);

				// if node list contains no node, return NULL_TEXT_INDICATOR
				if (nodeList.getLength() == 0) {
					return asNull;
				}

				// if node list contains at least one node, loop through each
				// node
				for (int i = 0; i < nodeList.getLength(); i++) {
					// get current node
					Node node = nodeList.item(i);

					if (!xmlObject.equals(""))
						xmlObject += sep1;

					// TEXT_NODE and ATTRIBUTE_NODE can get text content
					// directly
					if (node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.ATTRIBUTE_NODE) {
						xmlObject += (node.getNodeValue().trim().isEmpty()) ? asNull : node.getNodeValue().trim();
					}

					// for ELEMENT_NODE
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						// if current node has no child node as ELEMENT_NODE,
						// add text content of this node to the returned string
						if (!hasChildElementNodes(node)) {
							xmlObject += (node.getTextContent().trim().isEmpty()) ? asNull
									: node.getTextContent().trim();
						} else {
							// or else, loop in each child node and get their
							// text contents
							Node childNode = node.getFirstChild();
							String properties = "";
							while (childNode.getNextSibling() != null) {
								if (childNode.getNodeType() == Node.ELEMENT_NODE) {
									if (!properties.isEmpty()) {
										properties += sep2;
									}
									properties += (childNode.getTextContent().trim().isEmpty()) ? asNull
											: childNode.getTextContent().trim();
								}
								childNode = childNode.getNextSibling();
							}
							xmlObject += properties;
						}
					}
				}
				return xmlObject;
			}
		} catch (XPathExpressionException e) {
			return e.getMessage();
		}
		return "Cannot Evaluate: Unexpected Error";
	}

	/**
	 * Check if a ELEMENT_NODE has child nodes
	 * 
	 * @param node current node to check
	 * @return
	 *         <ul>
	 *         <li><code>true</code> if the current node has at least one child
	 *         node. This child node must be ELEMENT_NODE</li>
	 *         <li><code>false</code> if this current node has no child node as
	 *         ELEMENT_NODE</li>
	 */
	public boolean hasChildElementNodes(Node node) {
		if (!node.hasChildNodes()) {
			return false;
		} else {
			int numOfChild = 0;
			Node childNode = node.getFirstChild();
			while (childNode.getNextSibling() != null) {
				if (childNode.getNodeType() == Node.ELEMENT_NODE) {
					numOfChild += 1;
				}
				childNode = childNode.getNextSibling();
			}
			return numOfChild > 0;
		}
	}

	/**
	 * get Value of Node XML via nodeName
	 * 
	 * @param XMLMessage
	 * @param nodeNames
	 * @return Map containt key and value. If many node the same name ==> return
	 *         list value with ;
	 */
	public Map<String, String> getNodeValues(String XMLMessage, List<String> nodeNames) throws Exception {
		/* Fomat xml */
		Map<String, String> nodeXML = new HashMap<String, String>();
		XMLMessage = XMLMessage.replaceAll("&amp;", "&");
		XMLMessage = XMLMessage.replaceAll("&gt;", ">");
		XMLMessage = XMLMessage.replaceAll("&lt;", "<");

		String result = "";
		Map<String, String> listNode = new LinkedHashMap<String, String>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		InputSource is;
		builder = factory.newDocumentBuilder();
		is = new InputSource(new StringReader(XMLMessage));
		Document doc = builder.parse(is);
		NodeList list = doc.getElementsByTagName("*");
		for (int i = 0; i < list.getLength(); i++) {
			Element element = (Element) list.item(i);
			String nodeName = element.getNodeName();
			String nodeValue = element.getTextContent();
			if (listNode.containsKey(nodeName)) {
				String currentValue = listNode.get(nodeName);
				listNode.put(nodeName, currentValue + ";" + nodeValue);
			} else {
				listNode.put(nodeName, nodeValue);
			}

		}
		String getNodeValues = "";

		for (int i = 0; i < nodeNames.size(); i++) {
			String nodeName = nodeNames.get(i).trim();
			if (listNode.get(nodeName) != null)
				nodeXML.put(nodeName, listNode.get(nodeName));

			else
				nodeXML.put(nodeName, listNode.get(""));
		}

		return nodeXML;
	}

	/**
	 * Get node by key
	 * 
	 * @param nodeList
	 * @param nodeKey
	 * @param nodeValue
	 * @param XMLMessage
	 * @param nodeNames
	 * @param selCol
	 * @return
	 * @throws Exception
	 */
	public String getNodeByKey(String nodeList, String nodeKey, String nodeValue, String XMLMessage, String nodeNames,
			String selCol) throws Exception {
		/* TH xml */
		XMLMessage = XMLMessage.replaceAll("&amp;", "&");
		XMLMessage = XMLMessage.replaceAll("&gt;", ">");
		XMLMessage = XMLMessage.replaceAll("&lt;", "<");

		String arrNodeName[] = nodeNames.split(",");
		String result = "";
		Map<String, String> listNode = new LinkedHashMap<String, String>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		InputSource is;
		builder = factory.newDocumentBuilder();
		is = new InputSource(new StringReader(XMLMessage));
		Document document = builder.parse(is);

		// Get all employees
		NodeList nList = document.getElementsByTagName(nodeList);
		System.out.println("============================");
		String getNodeValues = "";
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node node = nList.item(temp);
			System.out.println(""); // Just a separator
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				// Print each employee's detail
				Element eElement = (Element) node;
				if (eElement.getElementsByTagName(nodeKey).item(0).getTextContent().equalsIgnoreCase(nodeValue)) {
					for (int i = 0; i < arrNodeName.length - 1; i++) {
						getNodeValues = getNodeValues
								+ eElement.getElementsByTagName(arrNodeName[i]).item(0).getTextContent().trim()
								+ selCol;
					}
				}
			}
		}
		return getNodeValues;
	}

	/**
	 * get Value of Node XML via nodeName
	 * 
	 * @param XMLMessage
	 * @param nodeName
	 * @param childList
	 * @return Map contains key and value. If many node the same name ==> return
	 *         list value with ;
	 */
	public List<Map<String, String>> getListNodeValues(String XMLMessage, String nodeName, List<String> childList)
			throws Exception {
		/* Format xml */
		Map<String, String> nodeXML = new HashMap<String, String>();
		XMLMessage = XMLMessage.replaceAll("&amp;", "&");
		XMLMessage = XMLMessage.replaceAll("&gt;", ">");
		XMLMessage = XMLMessage.replaceAll("&lt;", "<");

		String result = "";
		Map<String, String> listNode = new LinkedHashMap<String, String>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		InputSource is;
		builder = factory.newDocumentBuilder();
		is = new InputSource(new StringReader(XMLMessage));
		Document doc = builder.parse(is);
		NodeList list = doc.getElementsByTagName(nodeName);

		// init list result size
		int size = list.getLength();
		List<Map<String, String>> resultList = new LinkedList<>();
		for (int idx = 0; idx < size; idx++) {
			// init blank Map
			Map<String, String> resultMap = new CaseInsensitiveMap<>();
			resultList.add(resultMap);
		}
		// for each all parent node
		for (int i = 0; i < list.getLength(); i++) {
			Element element = (Element) list.item(i);
			// get child node
			NodeList childNodeList = element.getElementsByTagName("*");

			// get map in result
			Map<String, String> resultMap = resultList.get(i);

			// for each all child node of parent node above
			for (int j = 0; j < childNodeList.getLength(); j++) {
				Element childElement = (Element) childNodeList.item(j);
				// get child node name and value
				String childName = childElement.getNodeName();
				String childValue = childElement.getTextContent();

				// check node name in input list child node?
				for (String inputNodeName : childList) {
					if (inputNodeName.contains("/")) {
						String parent = inputNodeName.split("/")[0];
						String child = inputNodeName.split("/")[1];
						if (childElement.getParentNode().getNodeName().equalsIgnoreCase(parent)
								&& childName.equalsIgnoreCase(child)) {
							resultMap.put(inputNodeName, childValue);
						}
					} else if (inputNodeName.equalsIgnoreCase(childName)) {
						// save node to results map
						resultMap.put(childName, childValue);
					}
				}
			}
		}

		return resultList;
	}

	// Get node via xpath
	private static List<String> evaluateXPath(Document document, String xpathExpression) throws Exception {
		// Create XPathFactory object
		XPathFactory xpathFactory = XPathFactory.newInstance();

		// Create XPath object
		XPath xpath = xpathFactory.newXPath();

		List<String> values = new ArrayList<>();
		try {
			// Create XPathExpression object
			XPathExpression expr = xpath.compile(xpathExpression);

			// Evaluate expression result on XML document
			NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

			for (int i = 0; i < nodes.getLength(); i++) {
				values.add(nodes.item(i).getNodeValue());
			}

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return values;
	}

	/**
	 * Return the Document object from an XML file
	 * 
	 * @param filepath: absolutely path to the file
	 * @return Document object
	 * @author anhptn14
	 */
	public Document parseXMLFromFile(String filepath) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		Document doc = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.parse(filepath);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			System.err.println("parseXML: Cannot create Document object from " + filepath);
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * Update the attribute value of a node: ex <staff id="1">; need to update id
	 * value of staff
	 * 
	 * @param doc: Document object
	 * @param tagName: like staff
	 * @param attributeName: like id
	 * @param newVal: new value for id
	 * @author anhptn14
	 * @return: Document object after changed
	 */
	public Document editNodeAttribute(Document doc, String tagName, String attributeName, String newVal) {
		Node staff = null;
		Node nodeAttr = null;
		try {
			// Get the node by tag name directly
			staff = doc.getElementsByTagName(tagName).item(0);
			NamedNodeMap attr = staff.getAttributes();
			nodeAttr = attr.getNamedItem(attributeName);
			// update attribute
			nodeAttr.setTextContent(newVal);
		} catch (Exception e) {
			if (staff == null)
				System.err.println(
						String.format("editNodeAttribute: failed. Cannot find a node with name \"%s\"", tagName));
			if (nodeAttr == null)
				System.err.println(
						String.format("editNodeAttribute: failed. Cannot find a attribute \"%s\" in node named \"%s\" ",
								attributeName, tagName));
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * Add a child Node to a Parent Node
	 * 
	 * @param doc: Document object
	 * @param parentTag: tagname of the parent Node
	 * @param newTag: tagname of the new node with want to add
	 * @param newVal: value of the node
	 * @return Document object after changed
	 */
	public Document addChildNodeToParentNode(Document doc, String parentTag, String newTag, String newVal) {
		Node parentNode = null;
		try {
			parentNode = doc.getElementsByTagName(parentTag).item(0);
		} catch (Exception e) {
			if (parentNode == null)
				System.err.println(String.format("addChildNodeToParentNode: failed. Cannot find Node %s", parentTag));
			e.printStackTrace();
			return doc;
		}
		// Check if there is no newTag node in parentTag
		NodeList list = parentNode.getChildNodes();

		// if newTag is existed --> Modify the newTag node with newVal
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (newTag.equals(node.getNodeName())) {
				node.setTextContent(newVal);
				System.out.println(
						String.format("addChildNodeToParentNode: warning. The node \"%s\" is existed", newTag));
				return doc;
			}
		}

		// else, append a new node to parent node
		Element newElementNode = doc.createElement(newTag);
		newElementNode.appendChild(doc.createTextNode(newVal));
		parentNode.appendChild(newElementNode);
		return doc;
	}

	/**
	 * Edit node
	 * 
	 * @param doc
	 * @param key    can be nameTag or xpath
	 * @param newVal
	 * @return
	 */
	public Document editNodeValue(Document doc, String key, String newVal) {
		if (key.contains("//") || key.contains("/"))
			editNodeValueByXpath(doc, key, newVal);
		else
			editNodeValueByTagName(doc, key, newVal);
		return doc;
	}

	/**
	 * Edit the value of the node via its tagname
	 * 
	 * @author anhptn14
	 * @param doc: Document object
	 * @param tagName: name of the node need to be changed
	 * @param newVal: New value for the node
	 * @return: Document object after changed
	 */
	public Document editNodeValueByTagName(Document doc, String tagName, String newVal) {
		Node node = null;
		try {
			node = doc.getElementsByTagName(tagName).item(0);
			node.setTextContent(newVal);
		} catch (Exception e) {
			if (node == null)
				System.err.println(String.format("editNodeValue: failed. There is no node \"%s\"", tagName));
			e.printStackTrace();
		}

		return doc;
	}

	/**
	 * Edit note content via its xpath
	 * 
	 * @param doc
	 * @param sXpath ex: //pa/ch; hoac /root/pa/ch; note, xpath cannot contain
	 *               special charactor, so if the tag like <ns2:Id> just use id for
	 *               the path for ex: //DbtrAcct//Othr/Id NOT
	 *               //ns2:DbtrAcct//ns2:Othr/ns2:Id
	 * @param newVal
	 * @return
	 */
	public Document editNodeValueByXpath(Document doc, String sXpath, String newVal) {
		Node node = null;
		try {
			XPathFactory xpf = XPathFactory.newInstance();
			XPath xpath = xpf.newXPath();
			XPathExpression expression = xpath.compile(sXpath);

			node = (Node) expression.evaluate(doc, XPathConstants.NODE);
			node.setTextContent(newVal);
		} catch (Exception e) {
			if (node == null)
				System.err.println(String.format("editNodeValueByXpath: failed. There is no node \"%s\"", sXpath));
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * Remove a node with a path to the node <root> <pa> <ch>aaa</ch> </pa> </root>
	 * 
	 * @param doc
	 * @param sXpath ex: //pa/ch; hoac /root/pa/ch
	 * @author anhptn14
	 * @return
	 */
	public Document removeNode(Document doc, String sXpath) {
		Node node = null;
		try {
			XPathFactory xpf = XPathFactory.newInstance();
			XPath xpath = xpf.newXPath();
			XPathExpression expression = xpath.compile(sXpath);

			node = (Node) expression.evaluate(doc, XPathConstants.NODE);
			node.getParentNode().removeChild(node);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(String.format("removeNode: failed. Cannot find node with address '%s'", sXpath));
		}

		return doc;
	}

	/**
	 * Remove a Node with tagName form a Document object
	 * 
	 * @param doc             Document object
	 * @param parentTagName   tag name of the parent node
	 * @param tagNameToRemove tag name of the Node needed to remove
	 * @return: Document object after changed
	 * @author anhptn14
	 */
	public Document removeNode(Document doc, String parentTagName, String tagNameToRemove) {
		Node parentNode = null;
		boolean found = false;
		try {
			parentNode = doc.getElementsByTagName(parentTagName).item(0);
			NodeList list = parentNode.getChildNodes();

			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				// remove tagNameToRemove
				if (tagNameToRemove.equals(node.getNodeName())) {
					parentNode.removeChild(node);
					found = true;
					break;
				}
			}
		} catch (Exception e) {
			if (parentNode == null)
				System.err.println(String.format("removeNode: Failed. Cannot find Node \"%s\"", parentTagName));
			e.printStackTrace();
		}
		if (!found)
			System.err.println(String.format("removeNode: Failed. Cannot find Node %s to remove", tagNameToRemove));
		return doc;
	}

	/**
	 * Write the Document object to a file
	 * 
	 * @param doc
	 * @param filePath
	 * @author anhptn14
	 */
	public void writeXMLDocumentToFile(Document doc, String filePath) {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filePath));
			transformer.transform(source, result);
			System.out.println("writeXMLDocumentToFile: Done");
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Convert Document XML object to String
	 * 
	 * @param doc
	 * @return
	 * @author anhptn14
	 */
	public String xmlDocumentToString(Document doc) {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		StringWriter writer;
		String xmlString = null;
		try {
			transformer = tf.newTransformer();
			writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			xmlString = writer.getBuffer().toString();
			System.out.println(xmlString);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return xmlString;
	}

	/**
	 * get Node value via its Xpath
	 * 
	 * @param message xml message
	 * @param sxpath  String xpath
	 * @return
	 */
	public String getNodeValueByXpath(String message, String sxpath) {
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();
		String text = null;
		try {
			text = xpath.evaluate(sxpath, parseXmlString(message).getDocumentElement());
		} catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return text;
	}
}
