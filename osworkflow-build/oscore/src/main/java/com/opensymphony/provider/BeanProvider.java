/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.provider;


/**
 * BeanProvider providers an abstraction for getting and setting methods
 * of a bean (or object).
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 5 $
 *
 * @see com.opensymphony.util.BeanUtils
 */
public interface BeanProvider extends Provider {
    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Set a single property of the bean.
     *
     * @param obj The object to be manipulated.
     * @param property Name of property to set.
     * @param value Value to set property to.
     * @returns Boolean indicating success.
     */
    boolean setProperty(Object object, String property, Object value);

    /**
     * Get a single property of the bean.
     *
     * @param obj The object to be accessed.
     * @param property Name of property to get.
     * @returns Value of property. If property was not found, null is returned.
     */
    Object getProperty(Object object, String property);
}
