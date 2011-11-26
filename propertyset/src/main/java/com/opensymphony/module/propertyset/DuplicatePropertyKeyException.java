/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset;


/**
 * Thrown if a property is set who's key matches a key of an
 * existing property with different type.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 146 $
 */
public class DuplicatePropertyKeyException extends PropertyException {
    //~ Constructors ///////////////////////////////////////////////////////////

    public DuplicatePropertyKeyException() {
        super();
    }

    public DuplicatePropertyKeyException(String msg) {
        super(msg);
    }
}
