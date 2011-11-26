/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.provider;


/**
 * Provider interface. A provider is a pluggable runtime resource and is used
 * when different behaviours are required in different situations. For example
 * a LogProvider is used to plug in a logging mechanism.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 5 $
 *
 * @see com.opensymphony.provider.ProviderFactory
 */
public interface Provider {
    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Shutdown Provider.
     */
    void destroy();

    /**
     * Startup Provider.
     *
     * @exception com.opensymphony.provider.ProviderConfigurationException thrown if error in startup
     *            or configuration.
     */
    void init() throws ProviderConfigurationException;
}
