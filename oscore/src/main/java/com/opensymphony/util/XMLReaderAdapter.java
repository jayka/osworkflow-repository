/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.util;

import org.xml.sax.*;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import java.io.IOException;

import java.lang.reflect.Method;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;


/**
 * XMLReaderAdapter converts a java bean to its xml representation. It does
 * this by simulating a SAX XMLReader which an xslt processor can use as
 * a SAXSource.
 *
 * @author <a href="mailto:salaman@qoretech.com">Victor Salaman</a>
 * @version $Revision: 5 $
 */
public class XMLReaderAdapter implements Locator, XMLReader {
    //~ Static fields/initializers /////////////////////////////////////////////

    private static final Object[] NULLPARAMS = new Object[0];
    private static final String EMPTYSTRING = null; //new String();

    //~ Instance fields ////////////////////////////////////////////////////////

    private AttributesImpl atts = new AttributesImpl();
    private ContentHandler contentHandler = new DefaultHandler();
    private Object object;
    private String root = null;
    private String systemId;

    //~ Constructors ///////////////////////////////////////////////////////////

    public XMLReaderAdapter(Object object) {
        this(object, null);
    }

    public XMLReaderAdapter(Object object, String root) {
        this.object = object;
        this.root = root;
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public int getColumnNumber() {
        return -1;
    }

    public void setContentHandler(ContentHandler handler) {
        this.contentHandler = handler;
    }

    public ContentHandler getContentHandler() {
        return contentHandler;
    }

    public void setDTDHandler(DTDHandler handler) {
    }

    public DTDHandler getDTDHandler() {
        return null;
    }

    public void setEntityResolver(EntityResolver resolver) {
    }

    public EntityResolver getEntityResolver() {
        return null;
    }

    public void setErrorHandler(ErrorHandler handler) {
    }

    public ErrorHandler getErrorHandler() {
        return null;
    }

    public void setFeature(String featureId, boolean on) throws SAXNotRecognizedException, SAXNotSupportedException {
    }

    public boolean getFeature(String featureId) throws SAXNotRecognizedException {
        return false;
    }

    public int getLineNumber() {
        return -1;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void setProperty(String propertyId, Object property) throws SAXNotRecognizedException, SAXNotSupportedException {
    }

    public Object getProperty(String name) throws SAXNotRecognizedException {
        throw new SAXNotRecognizedException(name);
    }

    public String getPublicId() {
        return null;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getSystemId() {
        return systemId;
    }

    public void parse(InputSource source) throws IOException, SAXException {
        parse();
    }

    public void parse(String s) throws IOException, SAXException {
        parse();
    }

    public void parse() throws IOException, SAXException {
        if (object == null) {
            throw new SAXException("no object to reflected defined");
        }

        if (contentHandler == null) {
            throw new SAXException("no content handler defined");
        }

        contentHandler.setDocumentLocator(this);
        contentHandler.startDocument();
        walk(object, (root == null) ? "data" : root, false);
        contentHandler.endDocument();
    }

    private String getTagName(String tagName) {
        boolean lastWasLower = false;
        int bufIndex = 0;
        int oIndex = tagName.length();
        StringBuffer buff = new StringBuffer(tagName);

        for (int i = 0; i < oIndex; i++) {
            char c = tagName.charAt(i);

            if (i > 0) {
                if (lastWasLower && Character.isUpperCase(c)) {
                    buff.setCharAt(bufIndex, Character.toLowerCase(c));
                    buff.insert(bufIndex, '-');
                    bufIndex++;
                }
            } else {
                buff.setCharAt(0, Character.toLowerCase(c));
                lastWasLower = true;
            }

            bufIndex++;
        }

        return buff.toString();
    }

    private void walk(Object object, String name, boolean useKey) throws SAXException {
        boolean accessorsFound;

        if (!useKey) {
            contentHandler.startElement(EMPTYSTRING, name, EMPTYSTRING, atts);
        } else {
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(EMPTYSTRING, EMPTYSTRING, "key", "key", name);
            contentHandler.startElement(EMPTYSTRING, "item", EMPTYSTRING, atts);
        }

        try {
            if (object != null) {
                Class c = object.getClass();

                if (c.isAssignableFrom(String.class)) {
                    String value = (String) object;
                    contentHandler.characters(value.toCharArray(), 0, value.length());
                } else {
                    PropertyDescriptor[] props = Introspector.getBeanInfo(c, Object.class).getPropertyDescriptors();
                    accessorsFound = false;

                    for (int i = 0; i < props.length; i++) {
                        Method m = props[i].getReadMethod();
                        String s = m.getName();

                        if ((s.startsWith("get") && (s.length() > 3)) || (s.startsWith("is") && (s.length() > 2))) {
                            String attributeName = getTagName(s.substring(s.startsWith("is") ? 2 : 3));
                            Class rt = m.getReturnType();

                            if (attributeName.equals("class")) {
                                continue;
                            }

                            if (m.getParameterTypes().length == 0) {
                                Object value = m.invoke(object, NULLPARAMS);

                                if (value == null) {
                                    continue;
                                }

                                accessorsFound = true;

                                if (Collection.class.isAssignableFrom(value.getClass())) {
                                    Collection cole = (Collection) value;
                                    contentHandler.startElement(EMPTYSTRING, attributeName, EMPTYSTRING, atts);

                                    for (Iterator colIter = cole.iterator();
                                            colIter.hasNext();) {
                                        Object obj = (Object) colIter.next();
                                        walk(obj, "item", false);
                                    }

                                    contentHandler.endElement(EMPTYSTRING, attributeName, EMPTYSTRING);

                                    continue;
                                } else if (Enumeration.class.isAssignableFrom(value.getClass())) {
                                    Enumeration e = (Enumeration) value;
                                    contentHandler.startElement(EMPTYSTRING, attributeName, EMPTYSTRING, atts);

                                    while (e.hasMoreElements()) {
                                        Object obj = (Object) e.nextElement();
                                        walk(obj, "item", false);
                                    }

                                    contentHandler.endElement(EMPTYSTRING, attributeName, EMPTYSTRING);

                                    continue;
                                } else if (Vector.class.isAssignableFrom(value.getClass())) {
                                    Vector vec = (Vector) value;
                                    contentHandler.startElement(EMPTYSTRING, attributeName, EMPTYSTRING, atts);

                                    for (int ix = 0; ix < vec.size(); ix++) {
                                        Object obj = (Object) vec.elementAt(ix);
                                        walk(obj, "item", false);
                                    }

                                    contentHandler.endElement(EMPTYSTRING, attributeName, EMPTYSTRING);

                                    continue;
                                }

                                if (Map.class.isAssignableFrom(value.getClass())) {
                                    contentHandler.startElement(EMPTYSTRING, attributeName, EMPTYSTRING, atts);

                                    Map map = (Map) value;

                                    for (Iterator iter = map.entrySet().iterator();
                                            iter.hasNext();) {
                                        Map.Entry entry = (Map.Entry) iter.next();
                                        walk(entry.getValue(), (String) entry.getKey(), true);
                                    }

                                    contentHandler.endElement(EMPTYSTRING, attributeName, EMPTYSTRING);

                                    continue;
                                } else {
                                    walk(value, attributeName, false);
                                }
                            }
                        }
                    }

                    if (!accessorsFound) {
                        contentHandler.characters(object.toString().toCharArray(), 0, object.toString().length());
                    }
                }
            }
        } catch (Exception e) {
            throw new SAXException(e);
        }

        if (!useKey) {
            contentHandler.endElement(EMPTYSTRING, name, EMPTYSTRING);
        } else {
            contentHandler.endElement(EMPTYSTRING, "item", EMPTYSTRING);
        }
    }
}
