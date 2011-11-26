/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.verifiers;


/**
 *
 * @author <a href="mailto:hani@fate.demon.co.uk">Hani Suleiman</a>
 * @version $Revision: 146 $
 */
public class VerifyException extends RuntimeException {
    //~ Constructors ///////////////////////////////////////////////////////////

    public VerifyException() {
        super();
    }

    public VerifyException(String msg) {
        super(msg);
    }
}
