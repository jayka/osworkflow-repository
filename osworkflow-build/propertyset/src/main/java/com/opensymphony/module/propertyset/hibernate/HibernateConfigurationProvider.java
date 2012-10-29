/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.hibernate;

import net.sf.hibernate.cfg.Configuration;

import java.util.Map;


/**
 * Use this class to provide your own configurations to the PropertySet hibernate providers.
 * <p>
 * Simply implement this interface and return a Hibernate Configuration object.
 * <p>
 * This is setup by using the configuration.provider.class property, with the classname
 * of your implementation.
 */
public interface HibernateConfigurationProvider {
    //~ Methods ////////////////////////////////////////////////////////////////

    /**
    * Get a Hibernate configuration object
    */
    public Configuration getConfiguration();

    HibernatePropertySetDAO getPropertySetDAO();

    /**
    * Setup a Hibernate configuration object with the given properties.
    *
    * This will always be called before getConfiguration().
    */
    void setupConfiguration(Map properties);
}
