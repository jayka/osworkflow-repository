/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.util;


/* ====================================================================
 * The OpenSymphony Software License, Version 1.1
 *
 * (this license is derived and fully compatible with the Apache Software
 * License - see http://www.apache.org/LICENSE.txt)
 *
 * Copyright (c) 2001 The OpenSymphony Group. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        OpenSymphony Group (http://www.opensymphony.com/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "OpenSymphony" and "The OpenSymphony Group"
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact license@opensymphony.com .
 *
 * 5. Products derived from this software may not be called "OpenSymphony"
 *    or "OSCore", nor may "OpenSymphony" or "OSCore" appear in their
 *    name, without prior written permission of the OpenSymphony Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 */
import java.lang.reflect.*;

import java.rmi.*;

import java.util.*;

import javax.ejb.*;

import javax.naming.*;

import javax.rmi.*;


/**
 * Utilities for Enterprise JavaBeans (and JNDI).
 *
 * This is a class contains static methods only and is not meant to be instantiated.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @author <a href="mailto:hani@fate.demon.co.uk">Hani Suleiman</a>
 * @author <a href="mailto:dion@almaer.com">Dion Almaer</a>
 * @version $Revision: 125 $
 */
public class EJBUtils {
    //~ Static fields/initializers /////////////////////////////////////////////

    /**
     * Method cache for finders.
     */
    private static HashMap finderMethods = new HashMap();

    /**
     * Set to cache which JNDI lookups shouldn't be checked for in java:comp/env
     */
    private static Set ignoreEnvLocations = new HashSet();

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Get root naming context (InitialContext).
     *
     * @see javax.naming.InitialContext
     */
    public final static Context getRoot() throws NamingException, RemoteException {
        //if (rootContext == null) {
        //	rootContext = new InitialContext( System.getProperties() );
        //}
        //return rootContext;
        return new InitialContext();
    }

    /**
     * Method to lookup a stateless session EJB's home interface, and create
     * an instance using the create() method. Any exceptions thrown by the
     * create() method shall be rethrown (java.lang.Throwable).
     */
    public final static Object createStateless(String location) throws Throwable {
        try {
            Object home = EJBUtils.lookup(location);
            Method createMethod = home.getClass().getDeclaredMethod("create", null);

            return createMethod.invoke(home, null);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    /**
     * Utility method for attempting to find a specific entity bean given it's home
     * interface and primary key.
     *
     * <p>Attempts to call the following methods in order:</p>
     * <pre>
     *   home.findByPrimaryKey(int id);
     *   home.findByPrimaryKey(long id);
     *   home.findByPrimaryKey(Integer id);
     *   home.findByPrimaryKey(Long id);
     *   home.findByPrimaryKey(String id);
     * </pre>
     *
     * @param home Reference to entity home interface.
     * @param id Value of primary key.
     * @return Reference to <code>EJBObject</code> to be casted to desired type.
     * @exception java.rmi.RemoteException Rethrown if thrown by finder method.
     * @exception javax.ejb.FinderException Rethrown if thrown by finder method.
     */
    public final static EJBObject findEntity(EJBHome home, String id) throws RemoteException, FinderException {
        // Use reflection to try and find a suitable findByPrimaryKey method.
        try {
            // Class of implementation of EJBHome
            Class homeClass = home.getClass();

            // Possible param types (type->value)
            OrderedMap params = new OrderedMap();
            params.put(Integer.TYPE, new Integer(TextUtils.parseInt(id)));
            params.put(Long.TYPE, new Long(TextUtils.parseLong(id)));
            params.put("java.lang.Integer", new Integer(TextUtils.parseInt(id)));
            params.put("java.lang.Long", new Long(TextUtils.parseLong(id)));
            params.put("java.lang.String", id);

            // Loop through different types of params.
            Iterator it = params.iterator();

            while (it.hasNext()) {
                Object classType = it.next();

                // Attempt to invoke and return result from method.
                try {
                    Method m = null;

                    if (!finderMethods.containsKey(homeClass)) {
                        // first create array containing a single element, which is our class
                        Class[] theClass = new Class[] {
                            (classType instanceof String) ? Class.forName((String) classType) : (Class) classType
                        };

                        // find method with signature of above classes.
                        m = homeClass.getMethod("findByPrimaryKey", theClass);

                        //Cache the method, for performance.
                        finderMethods.put(homeClass, m);
                    } else {
                        //It's in the cache, so use the method from there.
                        m = (Method) finderMethods.get(homeClass);
                    }

                    Object[] args = new Object[] {params.get(classType)}; // these are the params
                    EJBObject result = (EJBObject) m.invoke(home, args);

                    // If a result is returned, return it.
                    if (result != null) {
                        return result; // SUCCESS!!
                    } else {
                    }
                } catch (ClassCastException e) {
                    //if (
                    // The method was invoked but didn't return an instance of EJBObject
                    //logger.isDebugEnabled()) {
                    // The method was invoked but didn't return an instance of EJBObject
                    //logger.debug(e);
                    //}
                } catch (ClassNotFoundException e) {
                    //if (
                    // This is never going to happen - it means java.lang.Integer is missing!!
                    // Carry on anyway and try the next one.
                    //logger.isDebugEnabled()) {
                    // This is never going to happen - it means java.lang.Integer is missing!!
                    // Carry on anyway and try the next one.
                    //logger.debug(e);
                    //}
                } catch (NoSuchMethodException e) {
                    //if (
                    // Missing a findByPrimaryKey for this type - let's try another one.
                    //logger.isDebugEnabled()) {
                    // Missing a findByPrimaryKey for this type - let's try another one.
                    //logger.debug(e);
                    //}
                } catch (IllegalAccessException e) {
                    //if (
                    // This findByPrimaryKey is protected/private - let's try another one.
                    //logger.isDebugEnabled()) {
                    // This findByPrimaryKey is protected/private - let's try another one.
                    //logger.debug(e);
                    //}
                } catch (InvocationTargetException e) {
                    // Unwrap to find the actual exception
                    Throwable t = e.getTargetException();

                    //if (
                    // Rethrow actual exception if RemoteException or FinderException.
                    //logger.isDebugEnabled()) {
                    // Rethrow actual exception if RemoteException or FinderException.
                    //logger.debug(e);
                    //}
                    if (t instanceof RemoteException) {
                        throw (RemoteException) t;
                    }

                    if (t instanceof FinderException) {
                        throw (FinderException) t;
                    }
                }
            }
        } catch (NullPointerException e) {
            //if (
            //    // home passed across was null. Can't really do anything with that.
            //    logger.isDebugEnabled()) {
            //    // home passed across was null. Can't really do anything with that.
            //    logger.debug(e);
            //}
        }

        //if (
        //    // Nothing was found. Oh well. Worth a try.
        //    logger.isDebugEnabled()) {
        //    // Nothing was found. Oh well. Worth a try.
        //    logger.debug("findEntity() found nothing");
        //}
        return null;
    }

    /**
     * @see #findEntity(javax.ejb.EJBHome, String)
     */
    public final static EJBObject findEntity(EJBHome home, int id) throws RemoteException, FinderException {
        return findEntity(home, "" + id);
    }

    /**
     * @see #findEntity(javax.ejb.EJBHome, String)
     */
    public final static EJBObject findEntity(EJBHome home, long id) throws RemoteException, FinderException {
        return findEntity(home, "" + id);
    }

    /**
     * Utility method for looking up and narrowing an Object via JNDI. Used when using RMI-IIOP.
     *
     * @see #lookup(String)
     * @see #narrow(Object, Class)
     */
    public final static Object lookup(String location, Class classType) throws NamingException, RemoteException {
        return narrow(lookup(location), classType);
    }

    /**
     * Utility method for looking up an Object via JNDI. Prefixes <code>java:comp/env/</code>
     * to <code>location</code>. If that is not found, it retries without the prefix.
     *
     * @param location JNDI location
     */
    public final static Object lookup(String location) throws NamingException, RemoteException {
        // if already determined that env location should be ignored...
        NamingException notFoundE = null;

        if (ignoreEnvLocations.contains(location)) {
            try {
                return getRoot().lookup(location);
            } catch (NamingException e) {
                // hmmm... maybe it's changed location in the JNDI tree
                ignoreEnvLocations.remove(location);
                notFoundE = e;
            }
        }

        try {
            return getRoot().lookup("java:comp/env/" + location);
        } catch (NamingException e) {
            if (notFoundE != null) {
                throw notFoundE; // throw first exception if it exists
            }

            ignoreEnvLocations.add(location);

            return getRoot().lookup(location);
        }
    }

    /**
     * Utility method for narrowing portable object to a class
     */
    public final static Object narrow(Object o, Class classType) {
        return PortableRemoteObject.narrow(o, classType);
    }
}
