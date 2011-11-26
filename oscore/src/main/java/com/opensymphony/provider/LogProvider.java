/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.provider;


/**
 * Interface to plug in provider for providing logging.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 5 $
 */
public interface LogProvider extends Provider {
    //~ Instance fields ////////////////////////////////////////////////////////

    int DEBUG = 1;
    int ERROR = 4;
    int FATAL = 5;
    int INFO = 2;
    int WARN = 3;

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Get object representing context of logger. (For example with Log4J, Category).
     * This object is not used anywhere, except when it is passed back to the log()
     * or isEnabled() methods.
     */
    Object getContext(String name);

    /**
     * Check whether logging is enabled for particular context/level combination.
     */
    boolean isEnabled(Object context, int level);

    /**
     * Log a message.
     */
    void log(Object context, int level, Object msg, Throwable throwable);
}
