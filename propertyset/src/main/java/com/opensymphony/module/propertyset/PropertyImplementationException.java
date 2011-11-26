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
    //~ Instance fields ////////////////////////////////////////////////////////

    protected Throwable original;

    //~ Constructors ///////////////////////////////////////////////////////////

    public PropertyImplementationException() {
        super();
    }

    public PropertyImplementationException(String msg) {
        super(msg);
    }

    public PropertyImplementationException(String msg, Throwable original) {
        super(msg);
        this.original = original;
    }

    public PropertyImplementationException(Throwable original) {
        this(original.getLocalizedMessage(), original);
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Retrieve original Exception.
     */
    public Throwable getRootCause() {
        return original;
    }
}
