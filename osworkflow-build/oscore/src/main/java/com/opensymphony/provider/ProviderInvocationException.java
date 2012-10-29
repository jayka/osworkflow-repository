/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.provider;


/**
 * Exception thrown by Provider to encapsulate propriertry exception thrown by
 * underlying implementation provider.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 5 $
 */
public class ProviderInvocationException extends Exception {
    //~ Instance fields ////////////////////////////////////////////////////////

    private Throwable cause;

    //~ Constructors ///////////////////////////////////////////////////////////

    public ProviderInvocationException(Throwable cause) {
        super();
        this.cause = cause;
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public Throwable getCause() {
        return cause;
    }
}
