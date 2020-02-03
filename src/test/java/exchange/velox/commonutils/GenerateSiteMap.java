package exchange.velox.commonutils;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateSiteMap {

    @Test
    public void generateSiteMap() throws Exception {
        URL url = getClass().getClassLoader().getResource("example.xml");
        File fXmlFile = new File(url.toURI());
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("url");
        List<Pattern> patterns = new ArrayList<>();
        patterns.add(Pattern.compile("\\/global\\/"));
        patterns.add(Pattern.compile("\\/vn\\/"));

        Set<Element> removeNodes = new HashSet<>();
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                if (exclude(eElement.getElementsByTagName("loc").item(0).getTextContent(), patterns)) {
                    removeNodes.add(eElement);
                    nNode.setTextContent("");
                }
            }
        }
        for (Element e : removeNodes) {
            e.getParentNode().removeChild(e);
        }
        NodeList childNodes = nList.item(0).getParentNode().getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (childNode.getTextContent().trim().isEmpty()) {
                childNode.setTextContent("");
            }

        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
        transformer.setOutputProperty( "{http://xml.apache.org/xslt}indent-amount", "2" );
        DOMSource source = new DOMSource(doc);

        StreamResult result = new StreamResult(new File("src/test/resources/result.xml"));
        transformer.transform(source, result);
    }

    private boolean exclude(String url, List<Pattern> patterns) {
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }
}
