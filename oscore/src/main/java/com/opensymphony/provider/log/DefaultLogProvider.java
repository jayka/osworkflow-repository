/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.provider.log;

import com.opensymphony.provider.LogProvider;
import com.opensymphony.provider.ProviderConfigurationException;


/**
 * Default LogProvider implementation (will be used if no other is available,
 * or there is error in loading another one).
 *
 * Very simply, prints msgs of ERROR or FATAL to System.err (in a simple format).
 * Has no dependencies on non standard java packages (such as Log4J).
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 5 $
 *
 * @see com.opensymphony.provider.LogProvider
 * @see com.opensymphony.util.Logger
 */
public class DefaultLogProvider implements LogProvider {
    //~ Methods ////////////////////////////////////////////////////////////////

    public Object getContext(String name) {
        return name;
    }

    public boolean isEnabled(Object context, int level) {
        return (level == ERROR) || (level == FATAL);
    }

    public void destroy() {
    }

    public void init() throws ProviderConfigurationException {
    }

    public void log(Object context, int level, Object msg, Throwable throwable) {
        if (isEnabled(context, level)) {
            StringBuffer l = new StringBuffer();
            l.append((level == FATAL) ? "[FATAL] " : "[ERROR] ");
            l.append(context);
            l.append(" : ");

            if (msg != null) {
                l.append(msg.toString());
            }

            System.err.println(l);

            if (throwable != null) {
                throwable.printStackTrace(System.err);
            }
        }
    }
}
