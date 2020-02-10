package exchange.velox.commonutils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateSiteMap {


    private List<String> readPatterns() throws IOException {
        InputStream inclusions = getClass().getClassLoader().getResourceAsStream("sitemap-inclusions.txt");
        List<String> lines = IOUtils.readLines(inclusions, "UTF-8");
        List<String> results = new ArrayList<>(lines.size());
        for (String line : lines) {
            line = StringUtils.replace(line, ".", "\\.");
            line = StringUtils.replace(line, "/", "\\/");
            results.add(line);
        }
        return results;
    }

    @Test
    public void generateSiteMap() throws Exception {
        URL url = getClass().getClassLoader().getResource("input.xml");
        File fXmlFile = new File(url.toURI());
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("url");
        List<String> lines = readPatterns();
        List<Pattern> patterns = new ArrayList<>(lines.size());
        for (String line : lines) {
            patterns.add(Pattern.compile(line));
        }

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

        String resultFilePath = "src/test/resources/copyscape-sitemap.xml";
        StreamResult result = new StreamResult(new File(resultFilePath));
        transformer.transform(source, result);
        System.out.println("OUTPUT: " + resultFilePath);
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
