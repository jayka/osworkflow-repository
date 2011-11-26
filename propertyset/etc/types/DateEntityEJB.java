/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.ejb.types;

import com.opensymphony.module.propertyset.PropertyImplementationException;
import com.opensymphony.module.propertyset.PropertySet;

import java.sql.Timestamp;

import javax.ejb.*;


/**
 * AbstractValueEntityEJB concrete implementation optimized for storing Dates.
 *
 * @ejb.bean
 *  type="CMP"
 *  view-type="local"
 *  name="PropertyDate"
 *  schema="PropertyDate"
 *  reentrant="False"
 *  primkey-field="id"
 *
 * @ejb.pk class="java.lang.Long" extends="java.lang.Object"
 *
 * @ejb.persistence table-name="OS_PROPERTYDATE"
 *
 * @ejb.permission unchecked="true"
 * @ejb.transaction type="Supports"
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 169 $
 *
 * @see com.opensymphony.module.propertyset.ejb.PropertyStoreEJB
 */
public abstract class DateEntityEJB implements EntityBean {
    //~ Instance fields ////////////////////////////////////////////////////////

    private EntityContext context;

    //~ Methods ////////////////////////////////////////////////////////////////

    public abstract void setDate(Timestamp date);

    /**
     * @ejb.persistence column-name="value"
     */
    public abstract Timestamp getDate();

    public abstract void setId(Long id);

    /**
     * @ejb.pk-field
     * @ejb.interface-method
     * @ejb.persistence column-name="id"
     */
    public abstract Long getId();

    public void setEntityContext(EntityContext context) {
        this.context = context;
    }

    /**
     * Set the value. Only java.util.Timestamp can be supplied.
     *
     * @ejb.interface-method
     */
    public void setValue(int type, Timestamp value) {
        if (value == null) {
            setDate(new Timestamp(0L));

            return;
        }

        if (type == PropertySet.DATE) {
            setDate(value);

            return;
        } else {
            throw new PropertyImplementationException("Cannot store this type of property.");
        }
    }

    /**
     * Return Date
     *
     * @ejb.interface-method
     */
    public Timestamp getValue(int type) {
        switch (type) {
        case PropertySet.DATE:
            return getDate();

        default:

            // this should never happen.
            throw new PropertyImplementationException("Cannot retrieve this type of property.");
        }
    }

    public void ejbActivate() {
    }

    /**
     * @ejb.create-method
     */
    public Long ejbCreate(int type, long id) throws CreateException {
        setId(new Long(id));

        // the concrete setValue() method shall set value to default (not null).
        setValue(type, null);

        return null;
    }

    public void ejbLoad() {
    }

    public void ejbPassivate() {
    }

    public void ejbPostCreate(int type, long id) throws CreateException {
    }

    public void ejbRemove() throws RemoveException {
    }

    public void ejbStore() {
    }

    public void unsetEntityContext() {
        context = null;
    }

    /**
     * Returns { PropertySet.DATE }
     */
    protected int[] allowedTypes() {
        return new int[] {PropertySet.DATE};
    }
}
