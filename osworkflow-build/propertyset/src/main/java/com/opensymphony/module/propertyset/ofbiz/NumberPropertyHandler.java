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
public class NumberPropertyHandler implements PropertyHandler {
    //~ Static fields/initializers /////////////////////////////////////////////

    // Attributes ----------------------------------------------------
    // Static --------------------------------------------------------
    private static final Long ZERO = new Long(0);

    //~ Methods ////////////////////////////////////////////////////////////////

    // Constructors --------------------------------------------------
    // Public --------------------------------------------------------
    public Object processGet(int type, Object input) throws PropertyException {
        if (input instanceof Long) {
            Long value = (Long) input;

            switch (type) {
            case PropertySet.BOOLEAN:
                return value.equals(ZERO) ? Boolean.FALSE : Boolean.TRUE;

            case PropertySet.INT:
                return new Integer(value.intValue());

            case PropertySet.LONG:
                return value;

            default:
                throw new PropertyImplementationException("Cannot retrieve this type of property");
            }
        }

        throw new PropertyImplementationException("Unexepected type of property");
    }

    public Object processSet(int type, Object input) throws PropertyException {
        if (input == null) {
            return new Long(0);
        }

        try {
            switch (type) {
            case PropertySet.BOOLEAN:
                return new Long((((Boolean) input).booleanValue() ? 1L : 0L));

            case PropertySet.INT:
            case PropertySet.LONG:
                return new Long(((Number) input).longValue());

            default:
                throw new PropertyImplementationException("Cannot store this type of property");
            }
        } catch (ClassCastException e) {
            throw new IllegalPropertyException("Cannot cast value to appropiate type");
        }
    }

    // Package protected ---------------------------------------------
    // Protected -----------------------------------------------------
    // Private -------------------------------------------------------
    // Inner classes -------------------------------------------------
}
