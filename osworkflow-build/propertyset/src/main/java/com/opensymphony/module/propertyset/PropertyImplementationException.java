/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset;


/**
 * Thrown if a specific implementation exception is thrown
 * (such as EJBException, RemoteException, NamingException, IOException, etc).
 *
 * <p>A specific Exception can be wrapped in this Exception, by being
 * passed to the constructor. It can be retrieved via
 * {@link #getRootCause()} .</p>
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 146 $
 */
public class PropertyImplementationException extends PropertyException {
    private static final long serialVersionUID = -6244745861818118797L;
    //~ Instance fields ////////////////////////////////////////////////////////

    //~ Constructors ///////////////////////////////////////////////////////////

    public PropertyImplementationException() {
        super();
    }

    public PropertyImplementationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyImplementationException(String msg) {
        super(msg);
    }

    public PropertyImplementationException(Throwable cause) {
        super(cause);
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Retrieve original Exception.
     */
    public Throwable getRootCause() {
        return getCause();
    }
}
