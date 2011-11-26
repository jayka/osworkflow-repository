/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset;

import com.opensymphony.util.Data;

import org.w3c.dom.Document;

import java.util.*;


/**
 * Base implementation of PropertySet.
 *
 * <p>Performs necessary casting for get???/set??? methods which wrap around the
 * following 2 methods which are declared <code>protected abstract</code> and need
 * to be implemented by subclasses:</p>
 *
 * <ul>
 * <li> {@link #get(int,java.lang.String)} </li>
 * <li> {@link #setImpl(int,java.lang.String,java.lang.Object)} </li>
 * </ul>
 *
 * <p>The following methods are declared <code>public abstract</code> and are the
 * remainder of the methods that need to be implemented at the very least:</p>
 *
 * <ul>
 * <li> {@link #exists(java.lang.String)} </li>
 * <li> {@link #remove(java.lang.String)} </li>
 * <li> {@link #getType(java.lang.String)} </li>
 * <li> {@link #getKeys(java.lang.String,int)} </li>
 * </ul>
 *
 * <p>The <code>supports???</code> methods are implemented and all return true by default.
 * Override if necessary.</p>
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @author <a href="mailto:hani@fate.demon.co.uk">Hani Suleiman</a>
 * @version $Revision: 151 $
 */
public abstract class AbstractPropertySet implements PropertySet {
    //~ Instance fields ////////////////////////////////////////////////////////

    protected PropertySetSchema schema;

    //~ Methods ////////////////////////////////////////////////////////////////

    public void setAsActualType(String key, Object value) throws PropertyException {
        int type;

        if (value instanceof Boolean) {
            type = BOOLEAN;
        } else if (value instanceof Integer) {
            type = INT;
        } else if (value instanceof Long) {
            type = LONG;
        } else if (value instanceof Double) {
            type = DOUBLE;
        } else if (value instanceof String) {
            if (value.toString().length() > 255) {
                type = TEXT;
            } else {
                type = STRING;
            }
        } else if (value instanceof Date) {
            type = DATE;
        } else if (value instanceof Document) {
            type = XML;
        } else if (value instanceof byte[]) {
            type = DATA;
        } else if (value instanceof Properties) {
            type = PROPERTIES;
        } else {
            type = OBJECT;
        }

        set(type, key, value);
    }

    public Object getAsActualType(String key) throws PropertyException {
        int type = getType(key);
        Object value = null;

        switch (type) {
        case BOOLEAN:
            value = new Boolean(getBoolean(key));

            break;

        case INT:
            value = new Integer(getInt(key));

            break;

        case LONG:
            value = new Long(getLong(key));

            break;

        case DOUBLE:
            value = new Double(getDouble(key));

            break;

        case STRING:
            value = getString(key);

            break;

        case TEXT:
            value = getText(key);

            break;

        case DATE:
            value = getDate(key);

            break;

        case XML:
            value = getXML(key);

            break;

        case DATA:
            value = getData(key);

            break;

        case PROPERTIES:
            value = getProperties(key);

            break;

        case OBJECT:
            value = getObject(key);

            break;
        }

        return value;
    }

    public void setBoolean(String key, boolean value) {
        set(BOOLEAN, key, value ? Boolean.TRUE : Boolean.FALSE);
    }

    public boolean getBoolean(String key) {
        try {
            return ((Boolean) get(BOOLEAN, key)).booleanValue();
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Constructs {@link com.opensymphony.util.Data} wrapper around bytes.
     */
    public void setData(String key, byte[] value) {
        set(DATA, key, new Data(value));
    }

    /**
     * Casts to {@link com.opensymphony.util.Data} and returns bytes.
     */
    public byte[] getData(String key) {
        try {
            Object data = get(DATA, key);

            if (data instanceof Data) {
                return ((Data) data).getBytes();
            } else if (data instanceof byte[]) {
                return (byte[]) data;
            }
        } catch (NullPointerException e) {
            return null;
        }

        return null;
    }

    public void setDate(String key, Date value) {
        set(DATE, key, value);
    }

    public Date getDate(String key) {
        try {
            return (Date) get(DATE, key);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void setDouble(String key, double value) {
        set(DOUBLE, key, new Double(value));
    }

    public double getDouble(String key) {
        try {
            return ((Double) get(DOUBLE, key)).doubleValue();
        } catch (NullPointerException e) {
            return 0.0;
        }
    }

    public void setInt(String key, int value) {
        set(INT, key, new Integer(value));
    }

    public int getInt(String key) {
        try {
            return ((Integer) get(INT, key)).intValue();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    /**
     * Calls <code>getKeys(null,0)</code>
     */
    public Collection getKeys() throws PropertyException {
        return getKeys(null, 0);
    }

    /**
     * Calls <code>getKeys(null,type)</code>
     */
    public Collection getKeys(int type) throws PropertyException {
        return getKeys(null, type);
    }

    /**
     * Calls <code>getKeys(prefix,0)</code>
     */
    public Collection getKeys(String prefix) throws PropertyException {
        return getKeys(prefix, 0);
    }

    public void setLong(String key, long value) {
        set(LONG, key, new Long(value));
    }

    public long getLong(String key) {
        try {
            return ((Long) get(LONG, key)).longValue();
        } catch (NullPointerException e) {
            return 0L;
        }
    }

    public void setObject(String key, Object value) {
        set(OBJECT, key, value);
    }

    public Object getObject(String key) {
        try {
            return get(OBJECT, key);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void setProperties(String key, Properties value) {
        set(PROPERTIES, key, value);
    }

    public Properties getProperties(String key) {
        try {
            return (Properties) get(PROPERTIES, key);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void setSchema(PropertySetSchema schema) {
        this.schema = schema;
    }

    public PropertySetSchema getSchema() {
        return schema;
    }

    /**
     * Returns true.
     */
    public boolean isSettable(String property) {
        return true;
    }

    /**
     * Throws IllegalPropertyException if value length greater than 255.
     */
    public void setString(String key, String value) {
        if ((value != null) && (value.length() > 255)) {
            throw new IllegalPropertyException("String exceeds 255 characters.");
        }

        set(STRING, key, value);
    }

    public String getString(String key) {
        try {
            return (String) get(STRING, key);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void setText(String key, String value) {
        set(TEXT, key, value);
    }

    public String getText(String key) {
        try {
            return (String) get(TEXT, key);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void setXML(String key, Document value) {
        set(XML, key, value);
    }

    public Document getXML(String key) {
        try {
            return (Document) get(XML, key);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void init(Map config, Map args) {
        // nothing
    }

    /**
     * Returns true.
     */
    public boolean supportsType(int type) {
        return true;
    }

    /**
     * Returns true.
     */
    public boolean supportsTypes() {
        return true;
    }

    /**
     * Simple human readable representation of contents of PropertySet.
     */
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(getClass().getName());
        result.append(" {\n");

        try {
            Iterator keys = getKeys().iterator();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                int type = getType(key);

                if (type > 0) {
                    result.append('\t');
                    result.append(key);
                    result.append(" = ");
                    result.append(get(type, key));
                    result.append('\n');
                }
            }
        } catch (PropertyException e) {
            // toString should never throw an exception.
        }

        result.append("}\n");

        return result.toString();
    }

    protected abstract void setImpl(int type, String key, Object value) throws PropertyException;

    protected abstract Object get(int type, String key) throws PropertyException;

    protected String type(int type) {
        switch (type) {
        case PropertySet.BOOLEAN:
            return "boolean";

        case PropertySet.INT:
            return "int";

        case PropertySet.LONG:
            return "long";

        case PropertySet.DOUBLE:
            return "double";

        case PropertySet.STRING:
            return "string";

        case PropertySet.TEXT:
            return "text";

        case PropertySet.DATE:
            return "date";

        case PropertySet.OBJECT:
            return "object";

        case PropertySet.XML:
            return "xml";

        case PropertySet.DATA:
            return "data";

        case PropertySet.PROPERTIES:
            return "properties";

        default:
            return null;
        }
    }

    protected int type(String type) {
        if (type == null) {
            return 0;
        }

        type = type.toLowerCase();

        if (type.equals("boolean")) {
            return PropertySet.BOOLEAN;
        }

        if (type.equals("int")) {
            return PropertySet.INT;
        }

        if (type.equals("long")) {
            return PropertySet.LONG;
        }

        if (type.equals("double")) {
            return PropertySet.DOUBLE;
        }

        if (type.equals("string")) {
            return PropertySet.STRING;
        }

        if (type.equals("text")) {
            return PropertySet.TEXT;
        }

        if (type.equals("date")) {
            return PropertySet.DATE;
        }

        if (type.equals("object")) {
            return PropertySet.OBJECT;
        }

        if (type.equals("xml")) {
            return PropertySet.XML;
        }

        if (type.equals("data")) {
            return PropertySet.DATA;
        }

        if (type.equals("properties")) {
            return PropertySet.PROPERTIES;
        }

        return 0;
    }

    private void set(int type, String key, Object value) throws PropertyException {
        //If we have a schema, validate data against it.
        if (schema != null) {
            PropertySchema ps = schema.getPropertySchema(key);

            //Restricted schemas have to explicitly list all permissible values
            if ((ps == null) && schema.isRestricted()) {
                throw new IllegalPropertyException("Property " + key + " not explicitly specified in restricted schema.");
            }

            //Check the property type matches
            if (supportsTypes() && (ps.getType() != type)) {
                throw new InvalidPropertyTypeException("Property " + key + " has invalid type " + type + " expected type=" + ps.getType());
            }

            ps.validate(value);
        }

        //we're ok this far, so call the actual setter.
        setImpl(type, key, value);
    }
}
