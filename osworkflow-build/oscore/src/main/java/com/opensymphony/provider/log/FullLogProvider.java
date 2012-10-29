/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.provider.log;

import com.opensymphony.provider.LogProvider;
import com.opensymphony.provider.ProviderConfigurationException;


/**
 * Prints all msgs to System.err (in a simple format).
 * Has no dependencies on non standard java packages (such as Log4J).
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 5 $
 *
 * @see com.opensymphony.provider.LogProvider
 * @see com.opensymphony.util.Logger
 */
public class FullLogProvider implements LogProvider {
    //~ Methods ////////////////////////////////////////////////////////////////

    public Object getContext(String name) {
        return name;
    }

    public boolean isEnabled(Object context, int level) {
        return true;
    }

    public void destroy() {
    }

    public void init() throws ProviderConfigurationException {
    }

    public void log(Object context, int level, Object msg, Throwable throwable) {
        StringBuffer l = new StringBuffer();
        l.append('[');
        l.append(getLevelDescription(level));
        l.append("] ");
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

    private String getLevelDescription(int level) {
        switch (level) {
        case DEBUG:
            return "DEBUG";

        case INFO:
            return "INFO ";

        case WARN:
            return "WARN ";

        case ERROR:
            return "ERROR";

        case FATAL:
            return "FATAL";

        default:
            return "?????";
        }
    }
}
