/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.provider;


/**
 * Thrown by Provider if it cannot initialize properly. If this is thrown,
 * then it is the job of the ProviderFactory to log the reason, and try
 * another Provider.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 5 $
 *
 * @see com.opensymphony.provider.Provider
 * @see com.opensymphony.provider.ProviderFactory
 */
public class ProviderConfigurationException extends Exception {
    //~ Instance fields ////////////////////////////////////////////////////////

    private Throwable cause;

    //~ Constructors ///////////////////////////////////////////////////////////

    public ProviderConfigurationException() {
        super();
    }

    public ProviderConfigurationException(String msg) {
        super(msg);
    }

    public ProviderConfigurationException(Throwable cause) {
        super();
        this.cause = cause;
    }

    public ProviderConfigurationException(String msg, Throwable cause) {
        super(msg);
        this.cause = cause;
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public Throwable getCause() {
        return cause;
    }
}
