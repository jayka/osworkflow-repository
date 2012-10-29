/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.map;

import com.opensymphony.module.propertyset.AbstractPropertySet;
import com.opensymphony.module.propertyset.PropertyException;

import java.util.*;


/**
 * The MapPropertySet is an UNTYPED PropertySet implementation that
 * acts as a wrapper around a standard {@link java.util.Map} .
 *
 * <p>Because Map's will only store the value but not the type, this
 * is untyped. See {@link com.opensymphony.module.propertyset.PropertySet}
 * for explanation.</p>
 *
 * <b>Optional Args</b>
 * <ul>
 *  <li><b>map</b> - the map that will back this PropertySet</li>
 * </ul>
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 144 $
 *
 * @see com.opensymphony.module.propertyset.PropertySet
 */
public class MapPropertySet extends AbstractPropertySet {
    //~ Instance fields ////////////////////////////////////////////////////////

    /**
    * Underlying Map storing properties.
    */
    protected Map map;

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
    * The type parameter is ignored.
    */
    public synchronized Collection getKeys(String prefix, int type) {
        Iterator keys = map.keySet().iterator();
        List result = new LinkedList();

        while (keys.hasNext()) {
            String key = (String) keys.next();

            if ((prefix == null) || key.startsWith(prefix)) {
                result.add(key);
            }
        }

        Collections.sort(result);

        return result;
    }

    /**
    * Set underlying map.
    */
    public synchronized void setMap(Map map) {
        if (map == null) {
            throw new NullPointerException("Map cannot be null.");
        }

        this.map = map;
    }

    /**
    * Retrieve underlying map.
    */
    public synchronized Map getMap() {
        return map;
    }

    /**
    * This is an untyped PropertySet implementation so this method will always
    * throw {@link java.lang.UnsupportedOperationException} .
    */
    public int getType(String key) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("PropertySet does not support types");
    }

    public synchronized boolean exists(String key) {
        return map.containsKey(key);
    }

    public void init(Map config, Map args) {
        map = (Map) args.get("map");

        if (map == null) {
            map = new HashMap();
        }
    }

    public synchronized void remove(String key) {
        map.remove(key);
    }

    public void remove() throws PropertyException {
        map.clear();
    }

    /**
    * Returns false.
    */
    public boolean supportsType(int type) {
        return false;
    }

    /**
    * Returns false.
    */
    public boolean supportsTypes() {
        return false;
    }

    /**
    * The type parameter is ignored.
    */
    protected synchronized void setImpl(int type, String key, Object value) {
        map.put(key, value);
    }

    /**
    * The type parameter is ignored.
    */
    protected synchronized Object get(int type, String key) {
        return exists(key) ? map.get(key) : null;
    }
}
