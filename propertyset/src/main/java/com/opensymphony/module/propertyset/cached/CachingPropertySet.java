/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.cached;

import com.opensymphony.module.propertyset.*;
import com.opensymphony.module.propertyset.memory.SerializablePropertySet;

import com.opensymphony.util.DataUtil;

import org.w3c.dom.Document;

import java.io.Serializable;

import java.util.*;


/**
 * A PropertySet which decorates another PropertySet and caches the results.
 * <p>
 *
 * This is only sensible to use in a situation where your application has exclusive access
 * to the underlying PropertySet (otherwise it can be dangerous to use).
 * <p>
 *
 * You can also use this property set to bulk load data from the decorated property set
 * when the PS is created.
 * <p>
 *
 * <b>THINK BEFORE USING THIS - IT COULD HURT YOU ;)</b>
 * <p>
 *
 * <b>Required Args</b>
 * <ul>
 *  <li><b>PropertySet</b> - the PropertySet that will be cached</li>
 * </ul>
 * <p>
 *
 * <b>Optional Args</b>
 * <ul>
 *  <li><b>bulkload</b> - Boolean that, when true, causes the cache to be bulk loaded</li>
 *  <li><b>serializableName</b> - the name that can be used to retrieve a SerializablePropertySet, defaults to "serializable"</li>
 * </ul>
 * @author <a href="mailto:mike@atlassian.com">Mike Cannon-Brookes</a>
 * @version $Revision: 146 $
 */
public class CachingPropertySet implements PropertySet, Serializable {
    //~ Instance fields ////////////////////////////////////////////////////////

    PropertySet decoratedPS;
    SerializablePropertySet cachePS;

    //~ Methods ////////////////////////////////////////////////////////////////

    public void setAsActualType(String key, Object value) throws PropertyException {
        if (value instanceof Boolean) {
            setBoolean(key, DataUtil.getBoolean((Boolean) value));
        } else if (value instanceof Integer) {
            setInt(key, DataUtil.getInt((Integer) value));
        } else if (value instanceof Long) {
            setLong(key, DataUtil.getLong((Long) value));
        } else if (value instanceof Double) {
            setDouble(key, DataUtil.getDouble((Double) value));
        } else if (value instanceof String) {
            if (value.toString().length() > 255) {
                setText(key, (String) value);
            } else {
                setString(key, (String) value);
            }
        } else if (value instanceof Date) {
            setDate(key, (Date) value);
        } else if (value instanceof Document) {
            setXML(key, (Document) value);
        } else if (value instanceof byte[]) {
            setData(key, (byte[]) value);
        } else if (value instanceof Properties) {
            setProperties(key, (Properties) value);
        } else {
            setObject(key, value);
        }
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

    public void setBoolean(String key, boolean value) throws PropertyException {
        decoratedPS.setBoolean(key, value);
        cachePS.setBoolean(key, value);
    }

    public boolean getBoolean(String key) throws PropertyException {
        if (!cachePS.exists(key)) {
            cachePS.setBoolean(key, decoratedPS.getBoolean(key));
        }

        return cachePS.getBoolean(key);
    }

    public void setData(String key, byte[] value) throws PropertyException {
        decoratedPS.setData(key, value);
        cachePS.setData(key, value);
    }

    public byte[] getData(String key) throws PropertyException {
        if (!cachePS.exists(key)) {
            cachePS.setData(key, decoratedPS.getData(key));
        }

        return cachePS.getData(key);
    }

    public void setDate(String key, Date value) throws PropertyException {
        decoratedPS.setDate(key, value);
        cachePS.setDate(key, value);
    }

    public Date getDate(String key) throws PropertyException {
        if (!cachePS.exists(key)) {
            cachePS.setDate(key, decoratedPS.getDate(key));
        }

        return cachePS.getDate(key);
    }

    public void setDouble(String key, double value) throws PropertyException {
        decoratedPS.setDouble(key, value);
        cachePS.setDouble(key, value);
    }

    public double getDouble(String key) throws PropertyException {
        if (!cachePS.exists(key)) {
            cachePS.setDouble(key, decoratedPS.getDouble(key));
        }

        return cachePS.getDouble(key);
    }

    public void setInt(String key, int value) throws PropertyException {
        decoratedPS.setInt(key, value);
        cachePS.setInt(key, value);
    }

    public int getInt(String key) throws PropertyException {
        if (!cachePS.exists(key)) {
            cachePS.setInt(key, decoratedPS.getInt(key));
        }

        return cachePS.getInt(key);
    }

    public Collection getKeys() throws PropertyException {
        return decoratedPS.getKeys();
    }

    public Collection getKeys(int type) throws PropertyException {
        return decoratedPS.getKeys(type);
    }

    public Collection getKeys(String prefix) throws PropertyException {
        return decoratedPS.getKeys(prefix);
    }

    public Collection getKeys(String prefix, int type) throws PropertyException {
        return decoratedPS.getKeys(prefix, type);
    }

    public void setLong(String key, long value) throws PropertyException {
        decoratedPS.setLong(key, value);
        cachePS.setLong(key, value);
    }

    public long getLong(String key) throws PropertyException {
        if (!cachePS.exists(key)) {
            cachePS.setLong(key, decoratedPS.getLong(key));
        }

        return cachePS.getLong(key);
    }

    public void setObject(String key, Object value) throws PropertyException {
        decoratedPS.setObject(key, value);
        cachePS.setObject(key, value);
    }

    public Object getObject(String key) throws PropertyException {
        if (!cachePS.exists(key)) {
            cachePS.setObject(key, decoratedPS.getObject(key));
        }

        return cachePS.getObject(key);
    }

    public void setProperties(String key, Properties value) throws PropertyException {
        decoratedPS.setProperties(key, value);
        cachePS.setProperties(key, value);
    }

    public Properties getProperties(String key) throws PropertyException {
        if (!cachePS.exists(key)) {
            cachePS.setProperties(key, decoratedPS.getProperties(key));
        }

        return cachePS.getProperties(key);
    }

    public void setSchema(PropertySetSchema schema) throws PropertyException {
        decoratedPS.setSchema(schema);
    }

    public PropertySetSchema getSchema() throws PropertyException {
        return decoratedPS.getSchema();
    }

    public boolean isSettable(String property) {
        return decoratedPS.isSettable(property);
    }

    public void setString(String key, String value) throws PropertyException {
        decoratedPS.setString(key, value);
        cachePS.setString(key, value);
    }

    public String getString(String key) throws PropertyException {
        if (!cachePS.exists(key)) {
            cachePS.setString(key, decoratedPS.getString(key));
        }

        return cachePS.getString(key);
    }

    public void setText(String key, String value) throws PropertyException {
        decoratedPS.setText(key, value);
        cachePS.setText(key, value);
    }

    public String getText(String key) throws PropertyException {
        if (!cachePS.exists(key)) {
            cachePS.setText(key, decoratedPS.getText(key));
        }

        return cachePS.getText(key);
    }

    public int getType(String key) throws PropertyException {
        return decoratedPS.getType(key);
    }

    public void setXML(String key, Document value) throws PropertyException {
        decoratedPS.setXML(key, value);
        cachePS.setXML(key, value);
    }

    public Document getXML(String key) throws PropertyException {
        if (!cachePS.exists(key)) {
            cachePS.setXML(key, decoratedPS.getXML(key));
        }

        return cachePS.getXML(key);
    }

    public boolean exists(String key) throws PropertyException {
        return decoratedPS.exists(key);
    }

    public void init(Map config, Map args) {
        decoratedPS = (PropertySet) args.get("PropertySet");

        String serializableName = (String) config.get("serializableName");

        if (serializableName == null) {
            serializableName = "serializable";
        }

        cachePS = (SerializablePropertySet) PropertySetManager.getInstance(serializableName, null);

        Boolean bulkload = (Boolean) args.get("bulkload");

        if ((bulkload != null) && bulkload.booleanValue()) {
            PropertySetManager.clone(decoratedPS, cachePS);
        }
    }

    public void remove() throws PropertyException {
        cachePS.remove();
        decoratedPS.remove();
    }

    public void remove(String key) throws PropertyException {
        cachePS.remove(key);
        decoratedPS.remove(key);
    }

    public boolean supportsType(int type) {
        return decoratedPS.supportsType(type);
    }

    public boolean supportsTypes() {
        return decoratedPS.supportsTypes();
    }
}
