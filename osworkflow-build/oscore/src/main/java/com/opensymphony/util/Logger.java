/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.util;

import com.opensymphony.provider.LogProvider;
import com.opensymphony.provider.ProviderFactory;

import java.io.InputStream;

import java.util.*;


/**
 * The Logger is a quick way of logging messages of different
 * priorities.
 *
 * <p><b>Usage</b></p>
 *
 * <p>In it's simplest form, place the following field in the class
 * that uses the debugging:</p>
 *
 * <p><code>private static Logger log = new Logger(ThisClassName.class);</code></p>
 *
 * <p>Then to log messages, in your code use:</p>
 *
 * <p><code>log.info("Debug message");</code></p>
 *
 * <p>You can have several Loggers in the same class by using the <code>( Class, String )</code>
 * constructor as follows:</p>
 *
 * <blockquote><pre>
 *
 *     Logger logger     = newLogger( ThisClassName.class );         // For general logging.
 *     Logger loopLogger = newLogger( ThisClassName.class, "loop" ); // Just for logging my loop.
 *
 * </pre></blockquote>
 *
 * <p>This will create a Logger with context <code>com.somecompany.thispackage.ThisClassName.loop</code>
 * whose priority can be set from a config file independently of the main logger
 * <code>com.somecompany.thispackage.ThisClassName</code>.</p>
 *
 * <p>That's about it really. The types of log messages available
 * (in order of severity - least first) are:</p>
 *
 * <ul>
 * <li>{@link #debug(java.lang.Object)}<li>
 * <li>{@link #info(java.lang.Object)}<li>
 * <li>{@link #warn(java.lang.Object)}<li>
 * <li>{@link #error(java.lang.Object)}<li>
 * <li>{@link #fatal(java.lang.Object)}<li>
 * </ul>
 *
 * <p>Each of these methods has a variation accepting a second parameter
 * of type Throwable. This is useful for logging Exceptions or Errors that
 * are thrown.</p>
 *
 * <p>There are corresponding methods for {@link #isDebugEnabled()} ,
 * {@link #isInfoEnabled()} etc. to save the overhead of building the log string
 * if the appropriate level is not set, as follows:</p>
 *
 * <blockquote><pre>
 *
 *     if ( logger.isInfoEnabled() ) logger.info( "This " + methodCall() + " and the string "
 *                                              + "concatenation will only take place if "
 *                                              + "INFO debugging is currently enabled" );
 *
 * </pre></blockquote>
 *
 * <p>The actual logging mechanism used depends on the configured
 * {@see com.opensymphony.provider.LogProvider} - this can be set
 * using the logger.provider system property. If not specified,
 * {@see com.opensymphony.provider.log.DefaultLogProvider} is used.</p>
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @author <a href="mailto:mcannon@internet.com">Mike Cannon-Brookes</a>
 * @author <a href="mailto:dan@cadrion.com">Dan North</a>
 * @deprecated If you require this functionality, we recommend you check out
 * the Jakarta commons-logging project.
 * @version $Revision: 5 $
 */
public class Logger implements java.io.Serializable {
    //~ Static fields/initializers /////////////////////////////////////////////

    private static final LogProvider logProvider;

    static {
        ProviderFactory factory = ProviderFactory.getInstance();
        providerModify();
        logProvider = (LogProvider) factory.getProvider("logger.provider", com.opensymphony.provider.log.DefaultLogProvider.class.getName());
    }

    //~ Instance fields ////////////////////////////////////////////////////////

    private Object context;

    //~ Constructors ///////////////////////////////////////////////////////////

    /**
     * Create Logger with supplied context.
     */
    public Logger(String name) {
        init(name);
    }

    /**
     * Convenience constructor. Calls {@link #Logger(java.lang.String)} with cls.getName()
     */
    public Logger(Class cls) {
        init(cls.getName());
    }

    /**
     * Convenience constructor. Calls {@link #Logger(java.lang.String)} with obj.getClass().getName()
     */
    public Logger(Object obj) {
        init(obj.getClass().getName());
    }

    /**
     * Convenience constructor. Creates the context string as the class name appended with
     * the subcontext, with a "." if required.
     */
    public Logger(Class cls, String subCategory) {
        if (subCategory == null) {
            init(cls.getName());
        } else if (subCategory.charAt(0) == '.') {
            init(cls.getName() + subCategory);
        } else {
            init(cls.getName() + "." + subCategory);
        }
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public boolean isDebugEnabled() {
        return isEnabledFor(LogProvider.DEBUG);
    }

    public boolean isEnabledFor(int priority) {
        return logProvider.isEnabled(context, priority);
    }

    public boolean isErrorEnabled() {
        return isEnabledFor(LogProvider.ERROR);
    }

    public boolean isFatalEnabled() {
        return isEnabledFor(LogProvider.FATAL);
    }

    public boolean isInfoEnabled() {
        return isEnabledFor(LogProvider.INFO);
    }

    public boolean isWarnEnabled() {
        return isEnabledFor(LogProvider.WARN);
    }

    public void debug(Object o) {
        log(LogProvider.DEBUG, o, null);
    }

    public void debug(Object o, Throwable t) {
        log(LogProvider.DEBUG, o, t);
    }

    public void error(Object o) {
        log(LogProvider.ERROR, o, null);
    }

    public void error(Object o, Throwable t) {
        log(LogProvider.ERROR, o, t);
    }

    public void fatal(Object o) {
        log(LogProvider.FATAL, o, null);
    }

    public void fatal(Object o, Throwable t) {
        log(LogProvider.FATAL, o, t);
    }

    public void info(Object o) {
        log(LogProvider.INFO, o, null);
    }

    public void info(Object o, Throwable t) {
        log(LogProvider.INFO, o, t);
    }

    public void log(int priority, Object o) {
        log(priority, o, null);
    }

    public void log(int priority, Object o, Throwable t) {
        logProvider.log(context, priority, o, t);
    }

    public void warn(Object o) {
        log(LogProvider.WARN, o, null);
    }

    public void warn(Object o, Throwable t) {
        log(LogProvider.WARN, o, t);
    }

    private void init(String name) {
        context = logProvider.getContext(name);
    }

    /**
     * Perform some custom modifications to the logger.provider for some special cases.
     */
    private static void providerModify() {
        if (((System.getProperty("logger.config") != null) && (System.getProperty("logger.config").trim().length() > 0)) && ((System.getProperty("logger.provider") == null) || (System.getProperty("logger.provider").trim().length() == 0))) {
            System.setProperty("logger.provider", "com.opensymphony.provider.log.Log4JProvider");
        }

        // end hack
        // some aliases for common providers so full class name need not be used
        {
            Map providerAliases = new HashMap();
            providerAliases.put("default", "com.opensymphony.provider.log.DefaultLogProvider");
            providerAliases.put("null", "com.opensymphony.provider.log.NullLogProvider");
            providerAliases.put("full", "com.opensymphony.provider.log.FullLogProvider");
            providerAliases.put("log4j", "com.opensymphony.provider.log.Log4JProvider");

            if ((System.getProperty("logger.provider") != null) && providerAliases.containsKey(System.getProperty("logger.provider"))) {
                System.setProperty("logger.provider", (String) providerAliases.get(System.getProperty("logger.provider")));
            }
        }
    }
}
