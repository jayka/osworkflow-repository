/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author <a href="mailto:hani@fate.demon.co.uk">Hani Suleiman</a>
 * @version $Revision: 144 $
 */
public class PropertySetSchema implements Serializable {
    //~ Instance fields ////////////////////////////////////////////////////////

    private Map propertySchemas;
    private boolean restricted;

    //~ Constructors ///////////////////////////////////////////////////////////

    public PropertySetSchema() {
        propertySchemas = new HashMap();
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public void setPropertySchema(String key, PropertySchema ps) {
        if (ps.getPropertyName() == null) {
            ps.setPropertyName(key);
        }

        propertySchemas.put(key, ps);
    }

    public PropertySchema getPropertySchema(String key) {
        return (PropertySchema) propertySchemas.get(key);
    }

    public void setRestricted(boolean b) {
        restricted = b;
    }

    public boolean isRestricted() {
        return restricted;
    }

    public void addPropertySchema(PropertySchema ps) {
        propertySchemas.put(ps.getPropertyName(), ps);
    }
}
