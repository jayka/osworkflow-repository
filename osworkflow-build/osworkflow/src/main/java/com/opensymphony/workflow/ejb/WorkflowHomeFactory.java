/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
/*
 * Generated file - Do not edit!
 */
package com.opensymphony.workflow.ejb;


/**
 * Utility class for Workflow.
 * @author <a href="mailto:plightbo@hotmail.com">Pat Lightbody</a>
 * @author <a href="mailto:hani@formicary.net">Hani Suleiman</a>
 * @version $Revision: 1.7 $
 */
public class WorkflowHomeFactory {
    //~ Static fields/initializers /////////////////////////////////////////////

    public static final String COMP_NAME = "java:comp/env/ejb/Workflow";
    public static final String JNDI_NAME = "Workflow";

    /** Cached remote home (EJBHome). Uses lazy loading to obtain its value (loaded by getHome() methods). */
    private static com.opensymphony.workflow.ejb.WorkflowHome cachedRemoteHome = null;

    //~ Methods ////////////////////////////////////////////////////////////////

    // Home interface lookup methods

    /**
     * Obtain remote home interface from default initial context
     * @return Home interface for Workflow. Lookup using COMP_NAME
     */
    public static com.opensymphony.workflow.ejb.WorkflowHome getHome() throws javax.naming.NamingException {
        if (cachedRemoteHome == null) {
            cachedRemoteHome = (com.opensymphony.workflow.ejb.WorkflowHome) lookupHome(null, COMP_NAME, com.opensymphony.workflow.ejb.WorkflowHome.class);
        }

        return cachedRemoteHome;
    }

    /**
     * Obtain remote home interface from parameterised initial context
     * @param environment Parameters to use for creating initial context
     * @return Home interface for Workflow. Lookup using COMP_NAME
     */
    public static com.opensymphony.workflow.ejb.WorkflowHome getHome(java.util.Hashtable environment) throws javax.naming.NamingException {
        return (com.opensymphony.workflow.ejb.WorkflowHome) lookupHome(environment, COMP_NAME, com.opensymphony.workflow.ejb.WorkflowHome.class);
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
