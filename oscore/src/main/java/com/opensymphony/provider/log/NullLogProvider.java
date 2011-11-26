/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.provider.log;

import com.opensymphony.provider.LogProvider;
import com.opensymphony.provider.ProviderConfigurationException;


/**
 * LogProvider implementation that does not log any messages anywhere.
 *
 * Can be used for efficiency, although DefaultLogProvider is recommended.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 5 $
 *
 * @see com.opensymphony.provider.LogProvider
 * @see com.opensymphony.util.Logger
 */
public class NullLogProvider implements LogProvider {
    //~ Static fields/initializers /////////////////////////////////////////////

    private static final Object dummyContext = new Object();

    //~ Methods ////////////////////////////////////////////////////////////////

    public Object getContext(String name) {
        // return nothing interesting
        return dummyContext;
    }

    public boolean isEnabled(Object context, int level) {
        return false;
    }

    public void destroy() {
    }

    public void init() throws ProviderConfigurationException {
    }

    public void log(Object context, int level, Object msg, Throwable throwable) {
        // do nothing
    }
}
