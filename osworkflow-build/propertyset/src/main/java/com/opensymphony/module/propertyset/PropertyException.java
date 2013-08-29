/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset;


/**
 * Parent class of all exceptions thrown by PropertySet.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 146 $
 */
public class PropertyException extends RuntimeException {
    //~ Constructors ///////////////////////////////////////////////////////////

    private static final long serialVersionUID = 3346921495358285461L;

    public PropertyException() {
        super();
    }

    public PropertyException(String msg) {
        super(msg);
    }

    public PropertyException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyException(Throwable cause) {
        super(cause);
    }

}
