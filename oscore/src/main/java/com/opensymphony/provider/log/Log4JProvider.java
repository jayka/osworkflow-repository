/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.provider.log;

import com.opensymphony.provider.LogProvider;
import com.opensymphony.provider.ProviderConfigurationException;

import org.apache.log4j.*;

import java.io.InputStream;

import java.util.Properties;


/**
 * <p>LogProvider that uses Categories of Log4J, see the
 * <a href="http://jakarta.apache.org/log4j">Log4J website</a>
 * for more details.</p>
 *
 * <p>There are corresponding methods for isDebugEnabled() ,
 * isInfoEnabled() etc. to save the overhead of building the log string
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
 * <p>A configuration file should be used by setting the
 * <code>logger.config</code> system property which will be processed
 * using PropertyConfigurator.</p>
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @author <a href="mailto:mcannon@internet.com">Mike Cannon-Brookes</a>
 * @author <a href="mailto:dan@cadrion.com">Dan North</a>
 * @version $Revision: 5 $
 */
public class Log4JProvider implements LogProvider {
    //~ Methods ////////////////////////////////////////////////////////////////

    public Object getContext(String name) {
        return Category.getInstance(name);
    }

    public boolean isEnabled(Object context, int level) {
        Category category = (Category) context;

        return category.isEnabledFor(getPriority(level));
    }

    public void destroy() {
    }

    /**
     * Setup Configurator.
     *
     * Looks for configuration information in the following order:
     *        - System property "logger.config" which is the path to a log4j properties file on disk
     *        - /oscore.lcf which is a log4j config file in the classpath
     */
    public void init() throws ProviderConfigurationException {
        try {
            String configurationMethod = null;

            String configFile = System.getProperty("logger.config");

            if (configFile != null) {
                // If debug configuration file set, use it.
                PropertyConfigurator.configure(configFile);
            } else {
                // try to find the config file "log4j.properties"
                Properties logProps = null;
                InputStream is = null;
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

                try {
                    is = classLoader.getResourceAsStream("log4j.properties");
                } catch (Exception e) {
                }

                if (is == null) {
                    try {
                        is = classLoader.getResourceAsStream("/log4j.properties");
                    } catch (Exception e) {
                    }
                }

                if (is != null) {
                    logProps = new Properties();
                    logProps.load(is);
                }

                if (logProps != null) {
                    PropertyConfigurator.configure(logProps);
                } else {
                    throw new ProviderConfigurationException("Log4J config file not found - specify location in logger.config property");
                }
            }
        } catch (ProviderConfigurationException e) {
            throw e;
        } catch (Exception e) {
            throw new ProviderConfigurationException("Error configuring Log4J", e);
        }
    }

    public void log(Object context, int level, Object msg, Throwable throwable) {
        Category category = (Category) context;
        category.log(getPriority(level), msg, throwable);
    }

    private Priority getPriority(int level) {
        switch (level) {
        case DEBUG:
            return Priority.DEBUG;

        case INFO:
            return Priority.INFO;

        case WARN:
            return Priority.WARN;

        case ERROR:
            return Priority.ERROR;

        case FATAL:
            return Priority.FATAL;

        default:
            return Priority.toPriority(level);
        }
    }
}
