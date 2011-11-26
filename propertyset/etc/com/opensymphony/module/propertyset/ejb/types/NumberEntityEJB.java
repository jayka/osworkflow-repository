/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.ejb.types;

import com.opensymphony.module.propertyset.*;

import java.io.Serializable;

import javax.ejb.*;


/**
 * AbstractValueEntityEJB concrete implementation optimized for storing numbers.
 *  This can be used to store BOOLEAN, INT and LONG.
 *
 * @ejb.bean
 *  type="CMP"
 *  name="PropertyNumber"
 *  schema="PropertyNumber"
 *  view-type="local"
 *  reentrant="False"
 *  primkey-field="id"
 *
 * @ejb.pk class="java.lang.Long" extends="java.lang.Object"
 *
 * @ejb.persistence table-name="OS_PROPERTYNUMBER"
 * @ejb.permission unchecked="true"
 * @ejb.transaction type="Supports"
 *
 * @author <a href="mailto:hani@formicary.net">Hani Suleiman</a>
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 169 $
 *
 * @see com.opensymphony.module.propertyset.ejb.PropertyStoreEJB
 */
public abstract class NumberEntityEJB implements EntityBean {
    //~ Instance fields ////////////////////////////////////////////////////////

    private EntityContext context;

    //~ Methods ////////////////////////////////////////////////////////////////

    public abstract void setId(Long id);

    /**
     * @ejb.pk-field
     * @ejb.interface-method
     * @ejb.persistence column-name="id"
     */
    public abstract Long getId();

    public abstract void setNumber(long date);

    /**
     * @ejb.persistence column-name="value"
     */
    public abstract long getNumber();

    public void setEntityContext(EntityContext context) {
        this.context = context;
    }

    /**
     * Set the value. Only java.lang.Number, java.lang.Boolean or java.util.Date can be supplied.
     *
     * @ejb.interface-method
     */
    public void setValue(int type, Serializable value) {
        if (value == null) {
            setNumber(0L);

            return;
        }

        try {
            switch (type) {
            case PropertySet.BOOLEAN:
                setNumber(((Boolean) value).booleanValue() ? 1L : 0L);

                return;

            case PropertySet.INT:
            case PropertySet.LONG:
                setNumber(((Number) value).longValue());

                return;

            default:

                // this should never happen.
                throw new PropertyImplementationException("Cannot store this type of property.");
            }
        } catch (ClassCastException ce) {
            throw new IllegalPropertyException("Cannot cast value to appropriate type for persistence.");
        }
    }

    /**
     * Create appropriate wrapper object around value (Boolean, Integer, Long).
     *
     * @ejb.interface-method
     */
    public Serializable getValue(int type) {
        long value = getNumber();

        switch (type) {
        case PropertySet.BOOLEAN:
            return (value == 0L) ? Boolean.FALSE : Boolean.TRUE;

        case PropertySet.INT:
            return new Integer((int) value);

        case PropertySet.LONG:
            return new Long(value);

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
     * Returns { PropertySet.BOOLEAN, PropertySet.INT, PropertySet.LONG }
     */
    protected int[] allowedTypes() {
        return new int[] {PropertySet.BOOLEAN, PropertySet.INT, PropertySet.LONG};
    }
}
