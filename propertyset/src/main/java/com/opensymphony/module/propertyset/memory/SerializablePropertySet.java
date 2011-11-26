/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.memory;

import com.opensymphony.module.propertyset.*;

import java.io.*;

import java.util.HashMap;
import java.util.Map;


/**
 * The SerializablePropertySet is a PropertySet implementation that
 * will store any primitive of serializable object in an internal Map
 * which is stored in memory and can be loaded/saved by serializing the
 * entire SerializablePropertySet.
 *
 * <p>This offers the most basic form of persistence. Note that
 * <code>setObject()</code> will throw an IllegalPropertyException if
 * the passed object does not implement Serializable.</p>
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 169 $
 *
 * @see com.opensymphony.module.propertyset.PropertySet
 * @see com.opensymphony.module.propertyset.memory.MemoryPropertySet
 */
public class SerializablePropertySet extends MemoryPropertySet implements Serializable {
    //~ Static fields/initializers /////////////////////////////////////////////

    private static final long serialVersionUID = -4597532064799568453L;

    //~ Instance fields ////////////////////////////////////////////////////////

    private HashMap serialMap;

    //~ Methods ////////////////////////////////////////////////////////////////

    public void init(Map config, Map args) {
        serialMap = new HashMap();
    }

    public void remove() throws PropertyException {
        serialMap.clear();
    }

    protected synchronized void setImpl(int type, String key, Object value) throws IllegalPropertyException, DuplicatePropertyKeyException {
        if ((value != null) && !(value instanceof Serializable)) {
            //throw new IllegalPropertyException("Cannot set " + key + ". Value type " + value.getClass() + " not Serializable");
        }

        super.setImpl(type, key, value);
    }

    protected HashMap getMap() {
        return serialMap;
    }
}
