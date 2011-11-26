/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.util;

import com.opensymphony.provider.ProviderFactory;
import com.opensymphony.provider.ProviderInvocationException;
import com.opensymphony.provider.XMLPrinterProvider;
import com.opensymphony.provider.XPathProvider;

import org.w3c.dom.*;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.*;

import java.net.URL;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


/**
 * XMLUtils is a bunch of quick access utility methods to common XML operations.
 *
 * <p>These include:</p>
 *
 * <ul>
 * <li>Parsing XML stream into org.w3c.dom.Document.
 * <li>Creating blank Documents.
 * <li>Serializing (pretty-printing) Document back to XML stream.
 * <li>Extracting nodes using X-Path expressions.
 * <li>Cloning nodes.
 * <li>Performing XSL transformations.
 * </ul>
 *
 * <p>This class contains static methods only and is not meant to be instantiated. It also
 * contains only basic (common) functions - for more control access appropriate API directly.</p>
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @author <a href="mailto:hani@fate.demon.co.uk">Hani Suleiman</a>
 * @version $Revision: 125 $
 */
public class XMLUtils {
    //~ Static fields/initializers /////////////////////////////////////////////

    private static final XPathProvider xPathProvider;
    private static final XMLPrinterProvider xmlPrinterProvider;

    static {
        ProviderFactory factory = ProviderFactory.getInstance();
        xPathProvider = (XPathProvider) factory.getProvider("xpath.provider", com.opensymphony.provider.xpath.XalanXPathProvider.class.getName());
        xmlPrinterProvider = (XMLPrinterProvider) factory.getProvider("xmlprinter.provider", 
            //			com.opensymphony.provider.xmlprinter.XalanXMLPrinterProvider.class.getName()
            com.opensymphony.provider.xmlprinter.DefaultXMLPrinterProvider.class.getName());
    }

    /**
     * The cache size for the XSL transforms
     */
    private static int cacheSize = 10;
    private static HashMap xslCache = new HashMap();
    private static LinkedList xslKeyList = new LinkedList();

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Return the contained text within an Element. Returns null if no text found.
     */
    public final static String getElementText(Element element) {
        NodeList nl = element.getChildNodes();

        for (int i = 0; i < nl.getLength(); i++) {
            Node c = nl.item(i);

            if (c instanceof Text) {
                return ((Text) c).getData();
            }
        }

        return null;
    }

    /**
     * Clone given Node into target Document. If targe is null, same Document will be used.
     * If deep is specified, all children below will also be cloned.
     */
    public final static Node cloneNode(Node node, Document target, boolean deep) throws DOMException {
        if ((target == null) || (node.getOwnerDocument() == target)) {
            // same Document
            return node.cloneNode(deep);
        } else {
            //DOM level 2 provides this in Document, so once xalan switches to that,
            //we can take out all the below and just call target.importNode(node, deep);
            //For now, we implement based on the javadocs for importNode
            Node newNode;
            int nodeType = node.getNodeType();

            switch (nodeType) {
            case Node.ATTRIBUTE_NODE:
                newNode = target.createAttribute(node.getNodeName());

                break;

            case Node.DOCUMENT_FRAGMENT_NODE:
                newNode = target.createDocumentFragment();

                break;

            case Node.ELEMENT_NODE:

                Element newElement = target.createElement(node.getNodeName());
                NamedNodeMap nodeAttr = node.getAttributes();

                if (nodeAttr != null) {
                    for (int i = 0; i < nodeAttr.getLength(); i++) {
                        Attr attr = (Attr) nodeAttr.item(i);

                        if (attr.getSpecified()) {
                            Attr newAttr = (Attr) cloneNode(attr, target, true);
                            newElement.setAttributeNode(newAttr);
                        }
                    }
                }

                newNode = newElement;

                break;

            case Node.ENTITY_REFERENCE_NODE:
                newNode = target.createEntityReference(node.getNodeName());

                break;

            case Node.PROCESSING_INSTRUCTION_NODE:
                newNode = target.createProcessingInstruction(node.getNodeName(), node.getNodeValue());

                break;

            case Node.TEXT_NODE:
                newNode = target.createTextNode(node.getNodeValue());

                break;

            case Node.CDATA_SECTION_NODE:
                newNode = target.createCDATASection(node.getNodeValue());

                break;

            case Node.COMMENT_NODE:
                newNode = target.createComment(node.getNodeValue());

                break;

            case Node.NOTATION_NODE:
            case Node.ENTITY_NODE:
            case Node.DOCUMENT_TYPE_NODE:
            case Node.DOCUMENT_NODE:default:
                throw new IllegalArgumentException("Importing of " + node + " not supported yet");
            }

            if (deep) {
                for (Node child = node.getFirstChild(); child != null;
                        child = child.getNextSibling()) {
                    newNode.appendChild(cloneNode(child, target, true));
                }
            }

            return newNode;
        }
    }

    /**
     * Create blank Document.
     */
    public final static Document newDocument() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        return builder.newDocument();
    }

    /**
     * Create blank Document, and insert root element with given name.
     */
    public final static Document newDocument(String rootElementName) throws ParserConfigurationException {
        Document doc = newDocument();
        doc.appendChild(doc.createElement(rootElementName));

        return doc;
    }

    /**
     * Parse an InputSource of XML into Document.
     */
    public final static Document parse(InputSource in) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        return builder.parse(in);
    }

    /**
     * Parse an InputStream of XML into Document.
     */
    public final static Document parse(InputStream in) throws ParserConfigurationException, IOException, SAXException {
        return parse(new InputSource(in));
    }

    /**
     * Parse a Reader of XML into Document.
     */
    public final static Document parse(Reader in) throws ParserConfigurationException, IOException, SAXException {
        return parse(new InputSource(in));
    }

    /**
     * Parse a File of XML into Document.
     */
    public final static Document parse(File file) throws ParserConfigurationException, IOException, SAXException {
        return parse(new InputSource(new FileInputStream(file)));
    }

    /**
     * Parse the contents of a URL's XML into Document.
     */
    public final static Document parse(URL url) throws ParserConfigurationException, IOException, SAXException {
        return parse(new InputSource(url.toString()));
    }

    /**
     * Parse a String containing XML data into a Document.
     * Note that String contains XML itself and is not URI.
     */
    public final static Document parse(String xml) throws ParserConfigurationException, IOException, SAXException {
        return parse(new InputSource(new StringReader(xml)));
    }

    /**
     * Pretty-print a Document to Writer.
     */
    public final static void print(Document document, Writer out) throws IOException {
        xmlPrinterProvider.print(document, out);
    }

    /**
     * Pretty-print a Document to OutputStream.
     */
    public final static void print(Document document, OutputStream out) throws IOException {
        print(document, new OutputStreamWriter(out));
    }

    /**
     * Pretty-print a Document to File.
     */
    public final static void print(Document document, File file) throws IOException {
        print(document, new FileWriter(file));
    }

    /**
     * Pretty-print a Document back to String of XML.
     */
    public final static String print(Document document) throws IOException {
        StringWriter result = new StringWriter();
        print(document, result);

        return result.toString();
    }

    /**
     * Perform XSL transformation.
     */
    public final static void transform(Reader xml, Reader xsl, Writer result) throws TransformerException {
        transform(xml, xsl, result, null);
    }

    /**
     * Return single Node from base Node using X-Path expression.
     */
    public final static Node xpath(Node base, String xpath) throws TransformerException {
        try {
            return xPathProvider.getNode(base, xpath);
        } catch (ProviderInvocationException e) {
            try {
                throw e.getCause();
            } catch (TransformerException te) {
                throw te;
            } catch (Throwable tw) {
                tw.printStackTrace();

                return null;
            }
        }
    }

    /**
     * Return multiple Nodes from base Node using X-Path expression.
     */
    public final static NodeList xpathList(Node base, String xpath) throws TransformerException {
        try {
            return xPathProvider.getNodes(base, xpath);
        } catch (ProviderInvocationException e) {
            try {
                throw e.getCause();
            } catch (TransformerException te) {
                throw te;
            } catch (Throwable tw) {
                tw.printStackTrace();

                return null;
            }
        }
    }

    /**
     * Sets the internal cache size for XSL sheets
     * @param newCacheSize
     */
    public static void setCacheSize(int newCacheSize) {
        cacheSize = newCacheSize;
    }

    /**
     * Accessor for the internal XSL transformer cache
     * @return the cache size
     */
    public static int getCacheSize() {
        return cacheSize;
    }

    /**
     * This method applies an XSL sheet to an XML document.
     * <p>2002/Apr/7, fixed bug 540875, first reported by Erik Weber, and
     * added configurable cache size.
     * @param xml the XML source
     * @param xsl the XSL source
     * @param result where to put the response
     * @param parameters a map consisting of params for the transformer
     * @param xslkey a key used to refer to the XSL
     * @throws TransformerException
     */
    public final static void transform(Reader xml, Reader xsl, Writer result, Map parameters, String xslkey) throws TransformerException {
        try {
            Transformer t;

            if ((null != xslkey) && (xslCache.containsKey(xslkey))) {
                t = (Transformer) xslCache.get(xslkey);

                synchronized (xslKeyList) {
                    xslKeyList.remove(xslkey);
                    xslKeyList.add(xslkey);
                }
            } else {
                TransformerFactory factory = TransformerFactory.newInstance();
                t = factory.newTransformer(new StreamSource(xsl));

                if (null != xslkey) {
                    xslCache.put(xslkey, t);
                    xslKeyList.add(xslkey);

                    synchronized (xslKeyList) {
                        int s = xslKeyList.size();
                        int cacheSize = getCacheSize();
                        int iterations = 1;

                        /* if the cache size was adjusted after the cache is initialized,
                         * we don't want to shrink TOO fast, just to be nice to runtime
                         * performance
                         *
                         * That's my story, and I'm stickin' to it
                         */
                        if (s > (cacheSize + 1)) {
                            iterations = 2;
                        }

                        while (iterations-- != 0) {
                            Object removalKey = xslKeyList.get(0);
                            xslKeyList.remove(0);
                            xslCache.remove(removalKey);
                        }
                    }
                }
            }

            if (parameters != null) {
                Iterator i = parameters.keySet().iterator();

                while (i.hasNext()) {
                    Object key = i.next();
                    Object value = parameters.get(key);
                    t.setParameter(key.toString(), value.toString());
                }
            }

            t.transform(new StreamSource(xml), new StreamResult(result));
        } catch (TransformerConfigurationException tce) {
            throw new TransformerException(tce);
        }
    }

    /**
     * Perform XSL transformation, with params.
     */
    public final static void transform(Reader xml, Reader xsl, Writer result, Map parameters) throws TransformerException {
        transform(xml, xsl, result, parameters, xsl.toString());
    }

    /**
     * Perform XSL transformation.
     */
    public final static void transform(InputStream xml, InputStream xsl, OutputStream result) throws TransformerException {
        transform(new InputStreamReader(xml), new InputStreamReader(xsl), new OutputStreamWriter(result));
    }

    /**
     * Perform XSL transformation. XML and XSL supplied in Strings and result returned as String.
     * Note that inputs are actual XML/XSL data, not URIs.
     */
    public final static String transform(String xml, String xsl) throws TransformerException {
        StringWriter result = new StringWriter();
        transform(new StringReader(xml), new StringReader(xsl), result);

        return result.toString();
    }

    /**
     * Perform XSL transformations using given Documents and return new Document.
     */
    public final static Document transform(Document xml, Document xsl) throws ParserConfigurationException, TransformerException {
        try {
            Document result = newDocument();
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer t = factory.newTransformer(new DOMSource(xsl));
            t.transform(new DOMSource(xml), new DOMResult(result));

            return result;
        } catch (TransformerConfigurationException tce) {
            throw new TransformerException(tce);
        }
    }
}
