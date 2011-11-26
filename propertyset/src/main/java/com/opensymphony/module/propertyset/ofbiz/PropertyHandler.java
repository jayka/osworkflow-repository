/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.ofbiz;

import com.opensymphony.module.propertyset.PropertyException;


/**
 * @author <a href="mailto:salaman@qoretech.com">Victor Salaman</a>
 * $Revision: 169 $
 */
public interface PropertyHandler {
    //~ Methods ////////////////////////////////////////////////////////////////

    // Public Interfaces ---------------------------------------------
    public Object processGet(int type, Object input) throws PropertyException;

    public Object processSet(int type, Object input) throws PropertyException;
}
