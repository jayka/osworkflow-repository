/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.ejb;

import com.opensymphony.module.propertyset.*;

import com.opensymphony.util.DataUtil;
import com.opensymphony.util.EJBUtils;

import java.io.Serializable;

import java.rmi.RemoteException;

import java.util.Collection;
import java.util.Map;

import javax.ejb.CreateException;

import javax.naming.NamingException;


/**
 * The EJBPropertySet is an implementation of
 * {@link com.opensymphony.module.propertyset.PropertySet} that
 * uses Enterprise JavaBeans to store and retrieve Properties.
 *
 * <p>This class is a proxy to the
 * {@link com.opensymphony.module.propertyset.ejb.PropertyStore}
 * Session Bean that handles the PropertySet and behind the scenes
 * delegates to various Entity Beans to persist the data in an
 * efficient way.</p>
 *
 * <p>Each method in the proxy will catch any thrown
 * {@link java.rmi.RemoteException} and rethrow it wrapped in a
 * {@link com.opensymphony.module.propertyset.PropertyImplementationException} .</p>
 *
 * <h3>Usage</h3>
 *
 * <p>In order to use an EJBPropertySet, a PropertyStore Session Bean
 * must first be retrieved that represents the PropertySet data. This
 * is typically either returned by another EJB, or looked up using
 * an JNDI location for PropertyStoreHome and an int ID for the actual
 * PropertySet used.</p>
 *
 * <b>Required Args</b>
 * <ul>
 *  <li><b>entityId</b> - Long that holds the ID of this entity</li>
 *  <li><b>entityName</b> - String that holds the name of this entity type</li>
 * </ul>
 * <p>
 *
 * <b>Optional Configuration</b>
 * <ul>
 *  <li><b>storeLocation</b> - the JNDI name for the PropertyStore EJB lookup (defaults to os.PropertyStore)</li>
 * </ul>
 *
 * @author <a href="mailto:joe@wirestation.co.uk">
 * @author <a href="mailto:plightbo@hotmail.com">Pat Lightbody</a>
 * @version $Revision: 169 $
 *
 * @see com.opensymphony.module.propertyset.PropertySet
 * @see com.opensymphony.module.propertyset.ejb.PropertyStore
 * @see com.opensymphony.module.propertyset.ejb.PropertyStoreHome
 */
public class EJBPropertySet extends AbstractPropertySet implements Serializable {
    //~ Instance fields ////////////////////////////////////////////////////////

    private PropertyStore store;
    private String entityName;
    private long entityId;

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
    * Proxy to {@link com.opensymphony.module.propertyset.ejb.PropertyStore#getKeys(java.lang.String,long,java.lang.String,int)}
    */
    public Collection getKeys(String prefix, int type) throws PropertyException {
        try {
            return store.getKeys(entityName, entityId, prefix, type);
        } catch (RemoteException re) {
            throw new PropertyImplementationException(re);
        }
    }

    /**
    * Proxy to {@link com.opensymphony.module.propertyset.ejb.PropertyStore#getType(java.lang.String,long,java.lang.String)}
    */
    public int getType(String key) throws PropertyException {
        try {
            return store.getType(entityName, entityId, key);
        } catch (RemoteException re) {
            throw new PropertyImplementationException(re);
        }
    }

    /**
    * Proxy to {@link com.opensymphony.module.propertyset.ejb.PropertyStore#exists(java.lang.String,long,java.lang.String)}
    */
    public boolean exists(String key) throws PropertyException {
        try {
            return store.exists(entityName, entityId, key);
        } catch (RemoteException re) {
            throw new PropertyImplementationException(re);
        }
    }

    public void init(Map config, Map args) {
        entityId = DataUtil.getLong((Long) args.get("entityId"));
        entityName = (String) args.get("entityName");

        String storeLocation = (String) config.get("storeLocation");

        if (storeLocation == null) {
            storeLocation = "PropertyStore";
        }

        try {
            PropertyStoreHome home = (PropertyStoreHome) EJBUtils.lookup(storeLocation, PropertyStoreHome.class);
            store = home.create();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (CreateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Proxy to {@link PropertyStore#removeEntry(String, long, String)}
     */
    public void remove() throws PropertyException {
        try {
            store.removeEntry(entityName, entityId);
        } catch (RemoteException re) {
            throw new PropertyImplementationException(re);
        }
    }

    /**
    * Proxy to {@link PropertyStore#removeEntry(String, long, String)}
    */
    public void remove(String key) throws PropertyException {
        try {
            store.removeEntry(entityName, entityId, key);
        } catch (RemoteException re) {
            throw new PropertyImplementationException(re);
        }
    }

    /**
    * Proxy to {@link com.opensymphony.module.propertyset.ejb.PropertyStore#set(java.lang.String,long,int,java.lang.String,java.io.Serializable)}
    */
    protected void setImpl(int type, String key, Object value) throws PropertyException {
        try {
            store.set(entityName, entityId, type, key, (Serializable) value);
        } catch (RemoteException re) {
            throw new PropertyImplementationException(re);
        }
    }

    /**
    * Proxy to {@link com.opensymphony.module.propertyset.ejb.PropertyStore#get(java.lang.String,long,int,java.lang.String)}
    */
    protected Object get(int type, String key) throws PropertyException {
        try {
            return store.get(entityName, entityId, type, key);
        } catch (RemoteException re) {
            throw new PropertyImplementationException(re);
        }
    }
}
