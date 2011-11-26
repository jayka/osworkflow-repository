/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.util;

import com.opensymphony.util.XMLUtils;

import junit.framework.*;

import org.w3c.dom.*;

import org.xml.sax.SAXException;


/**
 * JUnit test case for XMLUtils
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @author <a href="mailto:joeo@enigmastation.com">Joseph B. Ottinger</a>
 * @version $Revision: 11 $
 */
public class XMLUtilsTest extends TestCase {
    //~ Instance fields ////////////////////////////////////////////////////////

    // @TODO: Test parsing XML from sources other than String (e.g. URL, InputStream, File)
    // @TODO: Test XSLT transformations
    String testDoc1 = "<html>" + "<head><title>My DOC</title></head>" + "<body>" + "<h1>My DOC</h1>" + "<p align=\"center\">Hello world</p>" + "<hr/>" + "<p>Goodbye moon</p>" + "</body>" + "</html>";
    String testDoc2 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" + "<document>" + "<foo>bar</foo>" + "</document>";
    String testDoc3 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" + "<document>" + "<baz>bar</baz>" + "</document>";
    String testXSL1 = "<?xml version=\"1.0\"?>" + "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" " + "version=\"1.0\" >" + "<xsl:template match=\"/document\">" + "<document>" + "<xsl:apply-templates />" + "</document>" + "</xsl:template>" + "<xsl:template match=\"foo\">" + "<baz>" + "<xsl:apply-templates />" + "</baz>" + "</xsl:template>" + "</xsl:stylesheet>";

    //~ Constructors ///////////////////////////////////////////////////////////

    public XMLUtilsTest(String name) {
        super(name);
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public void testCloneExternal() throws Exception {
        Document doc1 = XMLUtils.parse(testDoc1);
        Document doc2 = XMLUtils.parse("<doc><chunk><text>hello</text><text>world</text></chunk></doc>");
        Element body = (Element) XMLUtils.xpath(doc1, "/html/body");
        Element chunk = (Element) XMLUtils.xpath(doc2, "/doc/chunk");
        Element chunkClone = (Element) XMLUtils.cloneNode(chunk, doc1, true);
        body.appendChild(chunkClone);
        assertEquals("world", ((Text) XMLUtils.xpath(doc1, "/html/body/chunk/text[2]/text()")).getData());
    }

    public void testCloneInternal() throws Exception {
        Document doc = XMLUtils.parse(testDoc1);
        Element body = (Element) XMLUtils.xpath(doc, "/html/body");
        Element p1 = (Element) XMLUtils.xpath(body, "p[1]");
        Element p3 = (Element) XMLUtils.cloneNode(p1, doc, true);
        p3.setAttribute("align", "right");
        body.appendChild(p3);
        assertEquals(3, XMLUtils.xpathList(doc, "/html/body/p").getLength());
        assertEquals("Hello world", ((Text) XMLUtils.xpath(body, "p[1]/text()")).getData());
        assertEquals("Goodbye moon", ((Text) XMLUtils.xpath(body, "p[2]/text()")).getData());
        assertEquals("Hello world", ((Text) XMLUtils.xpath(body, "p[3]/text()")).getData());
        assertEquals("center", ((Attr) XMLUtils.xpath(body, "p[1]/@align")).getValue());
        assertEquals("right", ((Attr) XMLUtils.xpath(body, "p[3]/@align")).getValue());

        // shallow clone
        Element p4 = (Element) XMLUtils.cloneNode(p1, doc, false);
        body.appendChild(p4);
        assertEquals("center", ((Attr) XMLUtils.xpath(body, "p[4]/@align")).getValue());
        assertNull(XMLUtils.xpath(body, "p[4]/text()"));
    }

    public void testElementText() throws Exception {
        Document doc = XMLUtils.parse(testDoc1);
        Element h1 = (Element) XMLUtils.xpath(doc, "/html/body/h1");
        Element p1 = (Element) XMLUtils.xpath(doc, "/html/body/p[1]");
        Element p2 = (Element) XMLUtils.xpath(doc, "/html/body/p[2]");
        Element head = (Element) XMLUtils.xpath(doc, "/html/head");
        assertEquals("My DOC", XMLUtils.getElementText(h1));
        assertEquals("Hello world", XMLUtils.getElementText(p1));
        assertEquals("Goodbye moon", XMLUtils.getElementText(p2));
        assertNull(XMLUtils.getElementText(head));
    }

    public void testInvalidXML() throws Exception {
        try {
            XMLUtils.parse("<html><a>hello <b>world</a></b></html>"); // nasty xml
            fail("Invalid XML did not cause error.");
        } catch (SAXException e) {
        }

        // good!
    }

    public void testNewDocument() throws Exception {
        Document doc = XMLUtils.newDocument("root");
        Element root = doc.getDocumentElement();
        Element title = doc.createElement("title");
        Text text = doc.createTextNode("here is some text");
        root.appendChild(title);
        title.appendChild(text);
        assertEquals("title", ((Element) XMLUtils.xpath(doc, "/root/title")).getTagName());
        assertEquals("here is some text", ((Text) XMLUtils.xpath(doc, "/root/title/text()")).getData());
        assertEquals(1, XMLUtils.xpathList(doc, "/root").getLength());
    }

    public void testPrint() throws Exception {
        Document doc = XMLUtils.parse(testDoc1);
        String str = XMLUtils.print(doc);
        assertTrue(str.indexOf("<title>My DOC</title>") > -1);
        assertTrue((str.indexOf("<hr/>") > -1) || (str.indexOf("<hr />") > -1));
        assertTrue(str.indexOf("<p align=\"center\">") > -1);

        Document doc2 = XMLUtils.parse(str);
        assertEquals("Goodbye moon", ((Text) XMLUtils.xpath(doc2, "/html/body/p[2]/text()")).getData());
    }

    public void testStringParse() throws Exception {
        Document doc1 = XMLUtils.parse(testDoc1);
        assertEquals("html", doc1.getDocumentElement().getTagName());

        Element body1 = (Element) doc1.getDocumentElement().getElementsByTagName("body").item(0);
        assertEquals("body", body1.getTagName());
        assertEquals(2, body1.getElementsByTagName("p").getLength());
    }

    /** We need to fix this. */
    public void testTransform() throws Exception {
        /*
        StringReader xmlr=new StringReader(testDoc2);
        StringReader xslr=new StringReader(testXSL1);
        StringWriter w=new StringWriter();
        XMLUtils.transform(xmlr, xslr, w, null);
        //assertEquals(w.getBuffer().toString(),testDoc3);
        w=new StringWriter();
        // This tests the cache. We need to reload the XML, though.
        // if you want to get around the cache, reinitialize xslr.
        xmlr=new StringReader(testDoc2);
        //xslr=new StringReader(testXSL1);
        XMLUtils.transform(xmlr, xslr, w, null);
        assertEquals(w.getBuffer().toString(),testDoc3);
        */
    }

    public void testXPathAttributes() throws Exception {
        Document doc = XMLUtils.parse(testDoc1);
        Attr align = (Attr) XMLUtils.xpath(doc, "/html/body/p[1]/@align");
        assertEquals("align", align.getName());
        assertEquals("center", align.getValue());
    }

    public void testXPathBase() throws Exception {
        Document doc = XMLUtils.parse(testDoc1);
        Element body = (Element) XMLUtils.xpath(doc, "/html/body");
        assertEquals("h1", ((Element) XMLUtils.xpath(body, "h1")).getTagName());
        assertEquals("p", ((Element) XMLUtils.xpath(body, "p")).getTagName());
        assertEquals("p", ((Element) XMLUtils.xpath(body, "p[1]")).getTagName());
        assertEquals("p", ((Element) XMLUtils.xpath(body, "p[2]")).getTagName());
        assertEquals("title", ((Element) XMLUtils.xpath(body, "../head/title")).getTagName());
        assertNull(XMLUtils.xpath(body, "../head/goat"));
    }

    public void testXPathElements() throws Exception {
        Document doc = XMLUtils.parse(testDoc1);
        assertEquals("h1", ((Element) XMLUtils.xpath(doc, "/html/body/h1")).getTagName());
        assertEquals("p", ((Element) XMLUtils.xpath(doc, "/html/body/p")).getTagName());
        assertEquals("p", ((Element) XMLUtils.xpath(doc, "/html/body/p[1]")).getTagName());
        assertEquals("p", ((Element) XMLUtils.xpath(doc, "/html/body/p[2]")).getTagName());
        assertEquals("p", ((Element) XMLUtils.xpath(doc, "//p")).getTagName());
        assertEquals("p", ((Element) XMLUtils.xpath(doc, "//body/p")).getTagName());
        assertNull(XMLUtils.xpath(doc, "//head/p"));
    }

    public void testXPathEmpties() throws Exception {
        Document doc = XMLUtils.parse(testDoc1);
        assertNull(XMLUtils.xpath(doc, "/html/body/p[3]"));
        assertNull(XMLUtils.xpath(doc, "/html/body/cheese"));
        assertNull(XMLUtils.xpath(doc, "/html/body/p[1]/@chicken"));
    }

    public void testXPathFilter() throws Exception {
        Document doc = XMLUtils.parse(testDoc1);
        assertEquals(2, XMLUtils.xpathList(doc, "/html/body/p").getLength());
        assertEquals(1, XMLUtils.xpathList(doc, "/html/body/p[@align='center']").getLength());
        assertEquals(0, XMLUtils.xpathList(doc, "/html/body/p[@align='left']").getLength());
    }

    public void testXPathList() throws Exception {
        Document doc = XMLUtils.parse(testDoc1);
        assertEquals(2, XMLUtils.xpathList(doc, "/html/body/p").getLength());
        assertEquals(1, XMLUtils.xpathList(doc, "/html/body/h1").getLength());
        assertEquals(0, XMLUtils.xpathList(doc, "/html/body/cheese").getLength());

        NodeList ps = XMLUtils.xpathList(doc, "/html/body/p");
        assertEquals("Hello world", ((Text) XMLUtils.xpath(ps.item(0), "text()")).getData());
        assertEquals("Goodbye moon", ((Text) XMLUtils.xpath(ps.item(1), "text()")).getData());
    }

    public void testXPathText() throws Exception {
        Document doc = XMLUtils.parse(testDoc1);
        Text t1 = (Text) XMLUtils.xpath(doc, "/html/body/p[1]/text()");
        assertEquals("Hello world", t1.getData());

        Text t2 = (Text) XMLUtils.xpath(doc, "/html/body/p[2]/text()");
        assertEquals("Goodbye moon", t2.getData());
    }
}
