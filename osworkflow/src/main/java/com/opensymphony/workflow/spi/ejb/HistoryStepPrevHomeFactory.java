/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
/*
 * Generated file - Do not edit!
 */
package com.opensymphony.workflow.spi.ejb;


/**
 * Utility class for HistoryStepPrev.
 * @author Hani Suleiman (hani@formicary.net) Date: Apr 7, 2003 Time: 8:17:09 PM
 */
public class HistoryStepPrevHomeFactory {
    //~ Static fields/initializers /////////////////////////////////////////////

    public static final String COMP_NAME = "java:comp/env/ejb/HistoryStepPrev";
    public static final String JNDI_NAME = "HistoryStepPrev";

    /** Cached local home (EJBLocalHome). Uses lazy loading to obtain its value (loaded by getLocalHome() methods). */
    private static com.opensymphony.workflow.spi.ejb.HistoryStepPrevLocalHome cachedLocalHome = null;

    //~ Methods ////////////////////////////////////////////////////////////////

    // Home interface lookup methods

    /**
     * Obtain local home interface from default initial context
     * @return Local home interface for HistoryStepPrev. Lookup using COMP_NAME
     */
    public static com.opensymphony.workflow.spi.ejb.HistoryStepPrevLocalHome getLocalHome() throws javax.naming.NamingException {
        if (cachedLocalHome == null) {
            cachedLocalHome = (com.opensymphony.workflow.spi.ejb.HistoryStepPrevLocalHome) lookupHome(null, COMP_NAME, com.opensymphony.workflow.spi.ejb.HistoryStepPrevLocalHome.class);
        }

        return cachedLocalHome;
    }

    private static Object lookupHome(java.util.Hashtable environment, String jndiName, Class narrowTo) throws javax.naming.NamingException {
        // Obtain initial context
        javax.naming.InitialContext initialContext = new javax.naming.InitialContext(environment);

        try {
            Object objRef = initialContext.lookup(jndiName);

            // only narrow if necessary
            if (java.rmi.Remote.class.isAssignableFrom(narrowTo)) {
                return javax.rmi.PortableRemoteObject.narrow(objRef, narrowTo);
            } else {
                return objRef;
            }
        } finally {
            initialContext.close();
        }
    }
}
