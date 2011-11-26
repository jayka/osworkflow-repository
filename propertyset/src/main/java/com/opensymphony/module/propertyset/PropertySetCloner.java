/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset;

import java.util.Iterator;


/**
 * The PropertySetCloner is used to copy all the properties from one PropertySet into another.
 *
 * <h3>Example</h3>
 *
 * <blockquote><code>
 *   EJBPropertySet source = new EJBPropertySet("ejb/PropertyStore","MyEJB",7);<br>
 *   XMLPropertySet dest   = new XMLPropertySet();<br>
 *   <br>
 *   PropertySetCloner cloner = new PropertySetCloner();<br>
 *   cloner.setSource( source );<br>
 *   cloner.setDestination( dest );<br>
 *   <br>
 *   cloner.cloneProperties();<br>
 *   dest.save( new FileWriter("propertyset-MyEJB-7.xml") );<br>
 * </code></blockquote>
 *
 * <p>The above example demonstrates how a PropertySetCloner can be used to export properties
 * stores in an EJBPropertySet to an XML file.</p>
 *
 * <p>If the destination PropertySet contains any properties, they will be cleared before
 * the source properties are copied across.</p>
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 144 $
 */
public class PropertySetCloner {
    //~ Instance fields ////////////////////////////////////////////////////////

    private PropertySet destination;
    private PropertySet source;

    //~ Methods ////////////////////////////////////////////////////////////////

    public void setDestination(PropertySet destination) {
        this.destination = destination;
    }

    public PropertySet getDestination() {
        return destination;
    }

    public void setSource(PropertySet source) {
        this.source = source;
    }

    public PropertySet getSource() {
        return source;
    }

    public void cloneProperties() throws PropertyException {
        clearDestination();

        Iterator keys = source.getKeys().iterator();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            cloneProperty(key);
        }
    }

    /**
     * Clear all properties that already exist in destination PropertySet.
     */
    private void clearDestination() throws PropertyException {
        Iterator keys = destination.getKeys().iterator();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            destination.remove(key);
        }
    }

    /**
     * Copy individual property from source to destination.
     */
    private void cloneProperty(String key) throws PropertyException {
        switch (source.getType(key)) {
        case PropertySet.BOOLEAN:
            destination.setBoolean(key, source.getBoolean(key));

            break;

        case PropertySet.INT:
            destination.setInt(key, source.getInt(key));

            break;

        case PropertySet.LONG:
            destination.setLong(key, source.getLong(key));

            break;

        case PropertySet.DOUBLE:
            destination.setDouble(key, source.getDouble(key));

            break;

        case PropertySet.STRING:
            destination.setString(key, source.getString(key));

            break;

        case PropertySet.TEXT:
            destination.setText(key, source.getText(key));

            break;

        case PropertySet.DATE:
            destination.setDate(key, source.getDate(key));

            break;

        case PropertySet.OBJECT:
            destination.setObject(key, source.getObject(key));

            break;

        case PropertySet.XML:
            destination.setXML(key, source.getXML(key));

            break;

        case PropertySet.DATA:
            destination.setData(key, source.getData(key));

            break;

        case PropertySet.PROPERTIES:
            destination.setProperties(key, source.getProperties(key));

            break;
        }
    }
}
