/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
/*
 * Generated file - Do not edit!
 */
package com.opensymphony.workflow.spi.ejb;


/**
 * Utility class for WorkflowEntry.
 * @author <a href="mailto:hani@formicary.net">Hani Suleiman</a>
 * @author <a href="mailto:plightbo@hotmail.com">Pat Lightbody</a>
 */
public class WorkflowEntryHomeFactory {
    //~ Static fields/initializers /////////////////////////////////////////////

    public static final String COMP_NAME = "java:comp/env/ejb/WorkflowEntry";
    public static final String JNDI_NAME = "WorkflowEntry";

    /** Cached local home (EJBLocalHome). Uses lazy loading to obtain its value (loaded by getLocalHome() methods). */
    private static com.opensymphony.workflow.spi.ejb.WorkflowEntryLocalHome cachedLocalHome = null;

    //~ Methods ////////////////////////////////////////////////////////////////

    // Home interface lookup methods

    /**
     * Obtain local home interface from default initial context
     * @return Local home interface for WorkflowEntry. Lookup using COMP_NAME
     */
    public static com.opensymphony.workflow.spi.ejb.WorkflowEntryLocalHome getLocalHome() throws javax.naming.NamingException {
        if (cachedLocalHome == null) {
            cachedLocalHome = (com.opensymphony.workflow.spi.ejb.WorkflowEntryLocalHome) lookupHome(null, COMP_NAME, com.opensymphony.workflow.spi.ejb.WorkflowEntryLocalHome.class);
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
