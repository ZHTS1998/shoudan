package util;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.util.*;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import java.io.StringWriter;
import java.io.Writer;
import com.fasterxml.jackson.databind.DatabindException;
import org.w3c.dom.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;



public class XMLTools {

    public Document initXmlDocument() throws Exception {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            document.setXmlStandalone(true);
            Element rootElement = document.createElement("stream");
            if(document.getDocumentElement()==null){
                document.appendChild(rootElement);
            }
            return document;
        } catch (Exception e) {

            throw new Exception("Error initializing XML document", e);
        }
    }

    public Document mapToXmlDocument(Document document,Map<String,String> reqValueMap) throws Exception {
        try{
            Set<?> keySet = reqValueMap.keySet();
            for(Iterator<?> itr = keySet.iterator(); itr.hasNext();){

                String req = itr.next().toString();
                Element childElement = document.createElement(req);
                String value = reqValueMap.get(req);

                childElement.appendChild(document.createTextNode(value));
                document.getDocumentElement().appendChild(childElement);
            }
            return document;
        }catch (Exception e){
            throw new Exception("Error initializing XML document", e);
        }
    }
    public String mapToXmlString(Document document,Map<String,String> reqValueMap) throws Exception {
        try{
            String returnStr = "";

            this.mapToXmlDocument(document, reqValueMap);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "GBK");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new StringWriter());

            transformer.transform(source, result);


            returnStr = result.getWriter().toString();

            System.out.println(returnStr);

        return returnStr;
        }
        catch(Exception e){
            throw new Exception("Error initializing XML document", e);
        }
    }
    public String mapToJson(Map<String,String> reqValueMap) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString= objectMapper.writeValueAsString(reqValueMap);
        System.out.println(jsonString);
        return jsonString;
    }
    public XmlMapper jsonToXML(String jsonString) throws Exception {

        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter());
        Object javaObject = xmlMapper.readValue(jsonString, Object.class);
        Writer writer = new StringWriter();
        try {
            xmlMapper.writer().withDefaultPrettyPrinter()
                    .writeValue(writer, javaObject);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        }
        // 将JSON字符串转换为XML
        String str = writer.toString();
            String xmlString = xmlMapper.writeValueAsString(jsonString);
            System.out.println(str);
            return xmlMapper;

        // 输出XML字符串
    }
    public static String sortByASCII(Map<String, String> map) {
        List<Map.Entry<String, String>> fields = new ArrayList<>(map.entrySet());

        // 按照key的ASCII码排序
        Collections.sort(fields, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return (o1.getKey().compareTo(o2.getKey()));
            }
        });

        // 将排序后的value值进行拼接
        StringBuilder plain = new StringBuilder();
        for (Map.Entry<String, String> field : fields) {
            String value = field.getValue();
            plain.append(value);
        }

        return plain.toString();
    }
    public Map<String, String> xmlStringToMap(String xml) throws Exception {
        if(xml == null || xml.equals("")){
            return null;
        }
        Map<String, String> resultMap = new HashMap<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xml)));

        Element root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nodeList.item(i);
                String tagName = elem.getTagName();
                String tagValue = elem.getTextContent();
                resultMap.put(tagName, tagValue);
            }
        }

        return resultMap;
    }
}


