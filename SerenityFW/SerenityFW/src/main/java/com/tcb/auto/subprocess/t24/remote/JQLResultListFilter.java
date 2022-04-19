package com.tcb.auto.subprocess.t24.remote;

import com.tcb.auto.utils.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by nhanha on 3/21/2019.
 */
public class JQLResultListFilter {
    private List<Map<String, String>> jqlList;
    private String filterStr;
    private String sepValue;

    public JQLResultListFilter(List<Map<String, String>> jqlList, String filterStr) {
        this.jqlList = jqlList;
        this.filterStr = filterStr;
        this.sepValue = Constants.SEP_VALUE;
    }

    public JQLResultListFilter(List<Map<String, String>> jqlList, String filterStr, String sepValue) {
        this.jqlList = jqlList;
        this.filterStr = filterStr;
        this.sepValue = sepValue;
    }

    /**
     * Get filter list form jql list by filter string
     * jQL list: [{ID=204, PROVINCE=HO-CHI-MINH*HA-NOI}]
     * filterStr: //PROVINCE[text()='HA-NOI']
     * @return filter List: [{ID=204, PROVINCE=HA-NOI}]
     * @throws XPathExpressionException
     */
    public List<Map<String, String>> getFilterList() throws XPathExpressionException {
        if(filterStr.isEmpty() || filterStr.equals(Constants.NULL_TEXT_INDICATOR)){
            //return org jqlList
            return jqlList;
        }
        //get xml document
        Document doc = getXmlFormJqlList();
        //Create XPath Query
        XPathFactory xpathfactory = XPathFactory.newInstance();
        XPath xpath = xpathfactory.newXPath();
        XPathExpression expr = xpath.compile(filterStr);
        Object result = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
        for (int i = 0; i < nodes.getLength(); i++) {
            //mark parent attributes as filtered
            Element parentElm = (Element) nodes.item(i).getParentNode();
            parentElm.setAttribute(Constants.XML_ATTR_FILTERED, "1");
            //System.out.println(nodes.item(i).getTextContent());
        }
        //remove attributes node without filtered
        NodeList attrNodeList = doc.getElementsByTagName(Constants.XML_ATTRIBUTES_TAG);
        for(int atIdx = 0; atIdx < attrNodeList.getLength(); atIdx++){
            if(!attrNodeList.item(atIdx).hasAttributes() || attrNodeList.item(atIdx).getAttributes().getNamedItem(Constants.XML_ATTR_FILTERED) == null){
                attrNodeList.item(atIdx).getParentNode().removeChild(attrNodeList.item(atIdx));
                atIdx--;
            }
        }
        return getListFromXml(doc);
    }

    /**
     * Check a column is multivalued
     * @param colValue
     * @return
     */
    private boolean isMultivalued(String colValue){
        return colValue.contains(sepValue);
    }

    /**
     * Get value of multivalued by index
     * @param colVal
     * @param index
     * @return
     */
    private String getValByIndex(String colVal, int index){
        String[] attrArray = colVal.split(Pattern.quote(sepValue));
        if(attrArray.length <= index) return Constants.NULL_TEXT_INDICATOR;
        return attrArray[index];
    }

    /**
     * Get max multivalued count of all col in a row
     * @param rowIdx
     * @return
     */
    private int getMaxValCount(int rowIdx){
        Map<String, String> mapRow = jqlList.get(rowIdx);
        int maxValCol = 0;
        for(Map.Entry<String, String> entryCol: mapRow.entrySet()){
            String colVal = entryCol.getValue();
            maxValCol = Math.max(maxValCol, colVal.split(Pattern.quote(sepValue)).length);
        }
        return maxValCol;
    }

    /**
     * Get list column has multivalued
     * @param rowIdx
     * @return
     */
    private List<String> getMultivaluedCol(int rowIdx){
        List<String> multivaluedColList = new ArrayList<String>();
        Map<String, String> mapRow = jqlList.get(rowIdx);
        for(Map.Entry<String, String> entryCol: mapRow.entrySet()){
            String colVal = entryCol.getValue();
            if(isMultivalued(colVal)) multivaluedColList.add(entryCol.getKey());
        }
        return multivaluedColList;
    }

    /**
     * Get XML Document from list of map jQL
     * jQL list: [{ID=204, PROVINCE=HO-CHI-MINH*HA-NOI}]
     * XML Results: <data> <row> <attributes> <ID>204</ID> <PROVINCE multivalued="1">HO-CHI-MINH</PROVINCE> </attributes> <attributes> <ID>204</ID> <PROVINCE multivalued="1">HA-NOI</PROVINCE> </attributes> </row> </data>
     * @return XML Document
     */
    private Document getXmlFormJqlList(){
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            int rowIdx = 0;
            Node dataRootTag =doc.createElement(Constants.XML_DATA_TAG);
            // ----- ROW -----
            for(Map<String, String> mapRow: jqlList){
                //init XML_ROW_TAG
                Element rowTag = doc.createElement(Constants.XML_ROW_TAG);

                //get max length of multivalued attribute
                int maxValCol = getMaxValCount(rowIdx);

                if(maxValCol == 1){
                    //no multivalued => add single node to row
                    //create XML_ATTRIBUTES_TAG tag
                    Element attrTag = doc.createElement(Constants.XML_ATTRIBUTES_TAG);
                    //add data for attributes tag

                    for(Map.Entry<String, String> entryCol: mapRow.entrySet()){
                        String colVal = entryCol.getValue();
                        Element prop = doc.createElement(entryCol.getKey());
                        prop.setTextContent(entryCol.getValue());
                        //add node to attributes tag
                        attrTag.appendChild(prop);
                    }
                    //add attributes to row
                    rowTag.appendChild(attrTag);
                }else{
                    //has multivalued => create multi attributes node
                    List<String> multivaluedColList = getMultivaluedCol(rowIdx);

                    for(int j = 0; j < maxValCol; j++){
                        //create XML_ATTRIBUTES_TAG tag
                        Element attrTag = doc.createElement(Constants.XML_ATTRIBUTES_TAG);

                        for(Map.Entry<String, String> entryCol: mapRow.entrySet()){
                            String colVal = entryCol.getValue();
                            String key = entryCol.getKey();
                            Element prop = doc.createElement(key);
                            if(multivaluedColList.contains(key)){
                                //multivalued prop => get a part value of prop
                                prop.setTextContent(getValByIndex(colVal, j));
                                prop.setAttribute(Constants.XML_ATTR_MULTIVALUED, "1");
                            }else{
                                //get whole value of prop
                                prop.setTextContent(entryCol.getValue());
                            }

                            //add node to attributes tag
                            attrTag.appendChild(prop);
                        }
                        //add attributes to row
                        rowTag.appendChild(attrTag);
                    }
                }

                dataRootTag.appendChild(rowTag);
                rowIdx++;
            }
            doc.appendChild(dataRootTag);

            return doc;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    private String getXmlContent(Document doc){
        if(doc == null) {
            return "";
        }
        try {
            //get XML result
            StringWriter writer = new StringWriter();
            //transform document to string
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String xmlString = writer.getBuffer().toString();
            return xmlString;
        } catch (TransformerException e) {
            return "";
        }
    }

    /**
     * Convert XML Document to list of map jQL
     * XML Example: <data> <row> <attributes> <ID>204</ID> <PROVINCE multivalued="1">HO-CHI-MINH</PROVINCE> </attributes> <attributes> <ID>204</ID> <PROVINCE multivalued="1">HA-NOI</PROVINCE> </attributes> </row> </data>
     * Converted list: [{ID=204, PROVINCE=HO-CHI-MINH*HA-NOI}]
     * @param doc
     * @return
     */
    private List<Map<String, String>> getListFromXml(Document doc){
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        //get row node list
        NodeList rowNodeLst = doc.getElementsByTagName(Constants.XML_ROW_TAG);
        for(int rowIdx = 0; rowIdx < rowNodeLst.getLength(); rowIdx++){
            Node rowNode = rowNodeLst.item(rowIdx);
            if(!rowNode.hasChildNodes()) continue;
            //get Attributes Node list
            NodeList attrNodeLst = rowNode.getChildNodes();
            //create Map data
            Map<String, String> mapRow = new LinkedHashMap<String, String>();

            //get node 0
            Node attrNode = attrNodeLst.item(0);
            //get all prop in Attributes Node
            NodeList propNodeList = attrNode.getChildNodes();
            for (int pIdx = 0; pIdx < propNodeList.getLength(); pIdx++){
                //get Property node
                Node propNode = propNodeList.item(pIdx);
                mapRow.put(propNode.getNodeName(), propNode.getTextContent());
            }

            //check is single or multivalued?
            if(attrNodeLst.getLength() > 1){
                //has multivalued
                for(int atIdx = 1; atIdx < attrNodeLst.getLength(); atIdx++){   //start index 1 because index 0 already process
                    attrNode = attrNodeLst.item(atIdx);
                    //get all prop in Attributes Node
                    propNodeList = attrNode.getChildNodes();
                    for (int pIdx = 0; pIdx < propNodeList.getLength(); pIdx++){
                        //get Property node
                        Node propNode = propNodeList.item(pIdx);
                        if(propNode.hasAttributes() && propNode.getAttributes().getNamedItem(Constants.XML_ATTR_MULTIVALUED) != null){
                            //is multivalued => join string
                            String propVal = mapRow.get(propNode.getNodeName());
                            mapRow.put(propNode.getNodeName(), propVal + sepValue + propNode.getTextContent());
                        }
                    }
                }
            }

            //add to list
            resultList.add(mapRow);
        }
        return resultList;
    }
}
