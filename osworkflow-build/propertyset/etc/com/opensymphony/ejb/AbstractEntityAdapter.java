/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.ejb;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.module.propertyset.PropertySetManager;
import com.opensymphony.module.sequence.SequenceGeneratorHome;

import com.opensymphony.util.EJBUtils;
import com.opensymphony.util.GUID;

import java.rmi.RemoteException;

import java.util.HashMap;

import javax.ejb.CreateException;
import javax.ejb.EntityContext;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 169 $
 */
public class AbstractEntityAdapter {
    //~ Instance fields ////////////////////////////////////////////////////////

    /**
     * Reference to EntityContext.
     */
    protected EntityContext context;

    /**
     * Name of the auto-generating sequence.
     */
    protected String sequenceName;

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Sets the context - should be used only by direct subclasses
     * Also, attempts to determine the <code>sequenceName</code>.
     */
    protected void setContext(EntityContext context) {
        this.context = context;

        try {
            sequenceName = (String) EJBUtils.lookup("sequenceName", String.class);
        } catch (Exception e) {
            sequenceName = "";
        }
    }

    /**
     * Generate random GUID.
     *
     * @see com.opensymphony.util.GUID
     */
    protected String generateGUID() {
        return GUID.generateGUID();
    }

    /**
     * Locate PropertySet using PropertyStore for this sequenceName/sequenceId mapping.
     */
    protected PropertySet locatePropertySet(long id) throws RemoteException {
        HashMap args = new HashMap(2);
        args.put("entityId", new Long(id));
        args.put("entityName", sequenceName);

        return PropertySetManager.getInstance("ejb", args);
    }

    /**
     * @deprecated Use {@link #nextInt()} or {@link #nextLong()} instead.
     */
    protected int nextId() throws CreateException, RemoteException {
        return nextInt();
    }

    /**
     * Return int unique id key from a unique instance key generator.
     */
    protected int nextInt() throws CreateException, RemoteException {
        try {
            return (int) nextLong();
        } catch (ClassCastException e) {
            throw new CreateException("Cannot generate id: Sequence cannot be downcasted to long.");
        } catch (NullPointerException e) {
            throw new CreateException("Cannot generate id: Sequence returning null.");
        }
    }

    /**
     * Return long unique id key from a unique instance key generator.
     */
    protected long nextLong() throws CreateException, RemoteException {
        try {
            SequenceGeneratorHome sgHome;

            try {
                if (sequenceName == null) {
                    sequenceName = (String) EJBUtils.lookup("sequenceName", String.class);
                }

                sgHome = (SequenceGeneratorHome) EJBUtils.lookup("ejb/SequenceGenerator", SequenceGeneratorHome.class);
            } catch (NameNotFoundException e) {
                sgHome = (SequenceGeneratorHome) EJBUtils.lookup("SequenceGenerator", SequenceGeneratorHome.class);
            }

            return sgHome.create().getCount(sequenceName);
        } catch (NamingException e) {
            throw new CreateException("Cannot generate id: " + e.toString());
        }
    }
}
