/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset;

import com.opensymphony.module.propertyset.config.PropertySetConfig;

import java.util.Map;


/**
 * The PropertySetManager is a factory for all the different types of
 * propertysets registered.
 *
 * @author $author$
 * @version $Revision: 148 $
 */
public class PropertySetManager {
    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Get a propertyset by name.
     * @param name The name of the propertyset as registered in propertyset.xml.
     * For example 'ejb', or 'memory'.
     * @param args The arguments to pass to the propertyset for initialization.
     * Consult the javadocs for a particular propertyset to see what arguments
     * it requires and supports.
     */
    public static PropertySet getInstance(String name, Map args) {
        PropertySet ps = getInstance(name, args, PropertySetManager.class.getClassLoader());

        if (ps == null) {
            ps = getInstance(name, args, Thread.currentThread().getContextClassLoader());
        }

        return ps;
    }

    /**
     * @see #getInstance(String, java.util.Map)
     * @param loader The classloader to use for loading the propertyset.
     */
    public static PropertySet getInstance(String name, Map args, ClassLoader loader) {
        PropertySetConfig psc = PropertySetConfig.getConfig();
        String clazz = psc.getClassName(name);
        Map config = psc.getArgs(name);
        Class psClass;

        try {
            psClass = loader.loadClass(clazz);
        } catch (ClassNotFoundException ex) {
            return null;
        }

        try {
            PropertySet ps = (PropertySet) psClass.newInstance();
            ps.init(config, args);

            return ps;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Copy the contents of one propertyset into another.
     * @param src The propertyset to copy from.
     * @param dest The propertyset to copy into.
     */
    public static void clone(PropertySet src, PropertySet dest) {
        PropertySetCloner cloner = new PropertySetCloner();
        cloner.setSource(src);
        cloner.setDestination(dest);
        cloner.cloneProperties();
    }
}
