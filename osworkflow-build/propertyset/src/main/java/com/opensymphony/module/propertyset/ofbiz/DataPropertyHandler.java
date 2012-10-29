/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.ofbiz;

import com.opensymphony.module.propertyset.*;

import com.opensymphony.util.Data;
import com.opensymphony.util.XMLUtils;

import org.w3c.dom.Document;

import java.io.*;

import java.util.Properties;


/**
 * @author <a href="mailto:salaman@qoretech.com">Victor Salaman</a>
 * $Revision: 169 $
 */
public class DataPropertyHandler implements PropertyHandler {
    //~ Static fields/initializers /////////////////////////////////////////////

    // Attributes ----------------------------------------------------
    private static final byte[] NULL_DATA = "hello world".getBytes();

    //~ Methods ////////////////////////////////////////////////////////////////

    // Static --------------------------------------------------------
    // Constructors --------------------------------------------------
    // Public --------------------------------------------------------
    public Object processGet(int type, Object input) throws PropertyException {
        byte[] value = (byte[]) input;

        switch (type) {
        case PropertySet.TEXT:
            return new String(value);

        case PropertySet.OBJECT:
            return readObject(value);

        case PropertySet.XML:
            return readXML(value);

        case PropertySet.DATA:
            return new Data(value);

        case PropertySet.PROPERTIES:
            return readProperties(value);

        default:
            throw new PropertyImplementationException("Cannot retrieve this type of property.");
        }
    }

    public Object processSet(int type, Object input) throws PropertyException {
        if (input == null) {
            return (NULL_DATA);
        }

        try {
            switch (type) {
            case PropertySet.TEXT:
                return (((String) input).getBytes());

            case PropertySet.OBJECT:

                if (!(input instanceof Serializable)) {
                    throw new IllegalPropertyException("Object not serializable.");
                }

                return (writeObject(input));

            case PropertySet.XML:
                return (writeXML((Document) input));

            case PropertySet.DATA:

                if (input instanceof Data) {
                    return (((Data) input).getBytes());
                } else if (input instanceof byte[]) {
                    return (byte[]) input;
                } else {
                    throw new IllegalPropertyException("DATA is not an instance of Data or byte[].");
                }

            case PropertySet.PROPERTIES:
                return (writeProperties((Properties) input));

            default:
                throw new PropertyImplementationException("Cannot store this type of property.");
            }
        } catch (ClassCastException ce) {
            throw new IllegalPropertyException("Cannot cast value to appropriate type for persistence.");
        }
    }

    /**
     * DeSerialize an Object from byte array.
     */
    private Object readObject(byte[] data) {
        try {
            ByteArrayInputStream bytes = new ByteArrayInputStream(data);
            ObjectInputStream stream = new ObjectInputStream(bytes);
            Object result = stream.readObject();
            stream.close();

            return result;
        } catch (IOException e) {
            throw new PropertyImplementationException("Cannot deserialize Object", e);
        } catch (ClassNotFoundException e) {
            throw new PropertyImplementationException("Class not found for Object", e);
        }
    }

    /**
     * Load java.util.Properties from byte array.
     */
    private Properties readProperties(byte[] data) {
        try {
            ByteArrayInputStream bytes = new ByteArrayInputStream(data);
            Properties result = new Properties();
            result.load(bytes);

            return result;
        } catch (Exception e) {
            throw new PropertyImplementationException("Cannot load Properties.", e);
        }
    }

    /**
     * Parse XML document from String in byte array.
     */
    private Document readXML(byte[] data) {
        try {
            ByteArrayInputStream bytes = new ByteArrayInputStream(data);

            return XMLUtils.parse(bytes);
        } catch (Exception e) {
            throw new PropertyImplementationException("Cannot parse XML data.", e);
        }
    }

    // Package protected ---------------------------------------------
    // Protected -----------------------------------------------------
    // Private -------------------------------------------------------

    /**
     * Serialize an Object to byte array.
     */
    private byte[] writeObject(Object o) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream stream = new ObjectOutputStream(bytes);
            stream.writeObject(o);
            stream.close();
            bytes.flush();

            return bytes.toByteArray();
        } catch (IOException e) {
            throw new PropertyImplementationException("Cannot serialize Object", e);
        }
    }

    /**
     * Store java.util.Properties to byte array.
     */
    private byte[] writeProperties(Properties p) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            p.store(bytes, null);
            bytes.flush();

            return bytes.toByteArray();
        } catch (IOException e) {
            throw new PropertyImplementationException("Cannot store Properties.", e);
        }
    }

    /**
     * Serialize (print) XML document to byte array (as String).
     */
    private byte[] writeXML(Document doc) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            XMLUtils.print(doc, bytes);
            bytes.flush();

            return bytes.toByteArray();
        } catch (IOException e) {
            throw new PropertyImplementationException("Cannot serialize XML", e);
        }
    }

    // Inner classes -------------------------------------------------
}
