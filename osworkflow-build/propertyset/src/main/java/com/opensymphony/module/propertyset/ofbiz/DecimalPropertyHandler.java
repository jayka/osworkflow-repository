/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.ofbiz;

import com.opensymphony.module.propertyset.*;


/**
 * @author <a href="mailto:salaman@qoretech.com">Victor Salaman</a>
 * $Revision: 169 $
 */
public class DecimalPropertyHandler implements PropertyHandler {
    //~ Methods ////////////////////////////////////////////////////////////////

    // Attributes ----------------------------------------------------
    // Static --------------------------------------------------------
    // Constructors --------------------------------------------------
    // Public --------------------------------------------------------
    public Object processGet(int type, Object input) throws PropertyException {
        if (type == PropertySet.DOUBLE) {
            return input;
        }

        throw new InvalidPropertyTypeException();
    }

    public Object processSet(int type, Object input) throws PropertyException {
        if (type == PropertySet.DOUBLE) {
            return input;
        }

        throw new InvalidPropertyTypeException();
    }

    // Package protected ---------------------------------------------
    // Protected -----------------------------------------------------
    // Private -------------------------------------------------------
    // Inner classes -------------------------------------------------
}
