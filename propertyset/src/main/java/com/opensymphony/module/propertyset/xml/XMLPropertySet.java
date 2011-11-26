/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.xml;

import com.opensymphony.module.propertyset.PropertyImplementationException;
import com.opensymphony.module.propertyset.PropertySet;

/* ====================================================================
 * The OpenSymphony Software License, Version 1.1
 *
 * (this license is derived and fully compatible with the Apache Software
 * License - see http://www.apache.org/LICENSE.txt)
 *
 * Copyright (c) 2001 The OpenSymphony Group. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        OpenSymphony Group (http://www.opensymphony.com/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "OpenSymphony" and "The OpenSymphony Group"
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact license@opensymphony.com .
 *
 * 5. Products derived from this software may not be called "OpenSymphony"
 *    or "OSCore", nor may "OpenSymphony" or "OSCore" appear in their
 *    name, without prior written permission of the OpenSymphony Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 */
import com.opensymphony.module.propertyset.memory.SerializablePropertySet;

import com.opensymphony.util.Data;
import com.opensymphony.util.TextUtils;
import com.opensymphony.util.XMLUtils;

import org.w3c.dom.*;

import org.xml.sax.SAXException;

import java.io.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


/**
 * The XMLPropertySet behaves as an in-memory typed PropertySet, with the ability to
 * load and save all the properties to/from an XML document.
 *
 * <ul>
 * <li>boolean, int, long, double and String properties are saved as simple Text nodes.</li>
 * <li>text and XML properties are stored as CDATA blocks.</li>
 * <li>java.util.Date properties are stored in <code>yyyy-mm-dd HH:mm:ss</code> format.</li>
 * <li>java.util.Properties properties are stored in child elements.</li>
 * <li>java.lang.Object and byte[] data properties are encoded using base64 into text and stored as CDATA blocks.</li>
 * </ul>
 *
 * <h3>Example:</h3>
 * <blockquote><code>
 * XMLPropertySet p = new XMLPropertySet(); // create blank property-set<br>
 * p.load( new FileReader("my-properties.xml") ); // load properties from XML.<br>
 * System.out.println( p.getString("name") );<br>
 * p.setString("name","blah blah");<br>
 * p.save( new FileWriter("my-properties.xml") ); // save properties back to XML.<br>
 * </code></blockquote>
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 169 $
 *
 * @see com.opensymphony.module.propertyset.PropertySet
 * @see com.opensymphony.module.propertyset.memory.SerializablePropertySet
 */
public class XMLPropertySet extends SerializablePropertySet {
    //~ Instance fields ////////////////////////////////////////////////////////

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
    * Load properties from XML input.
    */
    public void load(Reader in) throws ParserConfigurationException, IOException, SAXException {
        loadFromDocument(XMLUtils.parse(in));
    }

    /**
    * Load properties from XML input.
    */
    public void load(InputStream in) throws ParserConfigurationException, IOException, SAXException {
        loadFromDocument(XMLUtils.parse(in));
    }

    /**
    * Load properties from XML document.
    */
    public void loadFromDocument(Document doc) throws PropertyImplementationException {
        try {
            NodeList nodeList = XMLUtils.xpathList(doc, "/property-set/property");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element e = (Element) nodeList.item(i);
                String key = e.getAttribute("key");
                int type = type(e.getAttribute("type"));
                Object value = loadValue(e, key, type);

                if (value != null) {
                    setImpl(type, key, value);
                }
            }
        } catch (TransformerException e) {
            throw new PropertyImplementationException(e);
        }
    }

    /**
    * Save properties to XML output.
    */
    public void save(Writer out) throws ParserConfigurationException, IOException {
        XMLUtils.print(saveToDocument(), out);
    }

    /**
    * Save properties to XML output.
    */
    public void save(OutputStream out) throws ParserConfigurationException, IOException {
        XMLUtils.print(saveToDocument(), out);
    }

    /**
    * Save properties to XML Document.
    */
    public Document saveToDocument() throws ParserConfigurationException {
        Document doc = XMLUtils.newDocument("property-set");
        Iterator keys = getKeys().iterator();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            int type = getType(key);
            Object value = get(type, key);
            saveValue(doc, key, type, value);
        }

        return doc;
    }

    /**
    * Load value from <property>...</property> tag. Null returned if value cannot be determined.
    */
    private Object loadValue(Element e, String key, int type) {
        String text = XMLUtils.getElementText(e);

        switch (type) {
        case PropertySet.BOOLEAN:
            return new Boolean(TextUtils.parseBoolean(text));

        case PropertySet.INT:
            return new Integer(TextUtils.parseInt(text));

        case PropertySet.LONG:
            return new Long(TextUtils.parseLong(text));

        case PropertySet.DOUBLE:
            return new Double(TextUtils.parseDouble(text));

        case PropertySet.STRING:
        case PropertySet.TEXT:
            return text;

        case PropertySet.DATE:

            try {
                return dateFormat.parse(text);
            } catch (ParseException pe) {
                return null; // if the date cannot be parsed, ignore it.
            }

        case PropertySet.OBJECT:

            try {
                return TextUtils.decodeObject(text);
            } catch (Exception ex) {
                return null; // if Object cannot be decoded, ignore it.
            }

        case PropertySet.XML:

            try {
                return XMLUtils.parse(text);
            } catch (Exception ex) {
                return null; // if XML cannot be parsed, ignore it.
            }

        case PropertySet.DATA:

            try {
                return new Data(TextUtils.decodeBytes(text));
            } catch (IOException ioe) {
                return null; // if data cannot be decoded, ignore it.
            }

        case PropertySet.PROPERTIES:

            try {
                Properties props = new Properties();
                NodeList pElements = XMLUtils.xpathList(e, "properties/property");

                for (int i = 0; i < pElements.getLength(); i++) {
                    Element pElement = (Element) pElements.item(i);
                    props.put(pElement.getAttribute("key"), XMLUtils.getElementText(pElement));
                }

                return props;
            } catch (TransformerException te) {
                return null; // could not get nodes via x-path
            }

        default:
            return null;
        }
    }

    /**
    * Append <property key="..." type="...">...</property> tag to document.
    */
    private void saveValue(Document doc, String key, int type, Object value) {
        Element element = doc.createElement("property");
        element.setAttribute("key", key);
        element.setAttribute("type", type(type));

        Node valueNode;

        switch (type) {
        case PropertySet.BOOLEAN:
        case PropertySet.INT:
        case PropertySet.LONG:
        case PropertySet.DOUBLE:
        case PropertySet.STRING:
            valueNode = doc.createTextNode(value.toString());

            break;

        case PropertySet.TEXT:
            valueNode = doc.createCDATASection(value.toString());

            break;

        case PropertySet.DATE:
            valueNode = doc.createTextNode(dateFormat.format((Date) value));

            break;

        case PropertySet.OBJECT:

            try {
                valueNode = doc.createCDATASection(TextUtils.encodeObject(value));
            } catch (IOException ioe) {
                return; // cannot save Object - carry on with rest of properties.
            }

            break;

        case PropertySet.XML:

            try {
                valueNode = doc.createCDATASection(XMLUtils.print((Document) value));
            } catch (IOException ioe) {
                return; // cannot serialize XML - carry on with rest of properties.
            }

            break;

        case PropertySet.DATA:

            try {
                valueNode = doc.createCDATASection(TextUtils.encodeBytes(((Data) value).getBytes()));
            } catch (IOException ioe) {
                return; // cannot save data - carry on with rest of properties.
            }

            break;

        case PropertySet.PROPERTIES: { // scoping block
            valueNode = doc.createElement("properties");

            Properties props = (Properties) value;
            Iterator pKeys = props.keySet().iterator();

            while (pKeys.hasNext()) {
                String pKey = (String) pKeys.next();
                Element pElement = doc.createElement("property");
                pElement.setAttribute("key", pKey);
                pElement.setAttribute("type", "string");
                pElement.appendChild(doc.createTextNode(props.getProperty(pKey)));
                valueNode.appendChild(pElement);
            }
        }

        break;

        default:
            return; // if type not recognised, stop now.
        }

        element.appendChild(valueNode);

        doc.getDocumentElement().appendChild(element);
    }
}
