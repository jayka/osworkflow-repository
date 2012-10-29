/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.ejb.types;

import com.opensymphony.module.propertyset.PropertyImplementationException;
import com.opensymphony.module.propertyset.PropertySet;

import javax.ejb.*;


/**
 * AbstractValueEntityEJB concrete implementation optimized for storing decimal numbers.
 * This can be used to store DOUBLE.
 *
 * @ejb.bean
 *  type="CMP"
 *  name="PropertyDecimal"
 *  schema="PropertyDecimal"
 *  view-type="local"
 *  reentrant="False"
 *  primkey-field="id"
 *
 * @ejb.pk class="java.lang.Long" extends="java.lang.Object"
 *
 * @ejb.persistence table-name="OS_PROPERTYDECIMAL"
 * @ejb.permission unchecked="true"
 * @ejb.transaction type="Supports"
 *
 * @author <a href="mailto:hani@formicary.net">Hani Suleiman</a>
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 169 $
 *
 * @see com.opensymphony.module.propertyset.ejb.PropertyStoreEJB
 */
public abstract class DecimalEntityEJB implements EntityBean {
    //~ Instance fields ////////////////////////////////////////////////////////

    private EntityContext context;

    //~ Methods ////////////////////////////////////////////////////////////////

    public abstract void setDecimal(double date);

    /**
     * @ejb.persistence column-name="value"
     */
    public abstract double getDecimal();

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
     * Set the value. Only java.lang.Number can be supplied.
     *
     * @ejb.interface-method
     */
    public void setValue(int type, Double value) {
        if (value == null) {
            setDecimal(0.0);

            return;
        }

        if (type == PropertySet.DOUBLE) {
            setDecimal(value.doubleValue());
        } else {
            throw new PropertyImplementationException("Cannot store this type of property.");
        }
    }

    /**
     * Create appropriate wrapper object around value (Double).
     *
     * @ejb.interface-method
     */
    public Double getValue(int type) {
        if (type == PropertySet.DOUBLE) {
            return new Double(getDecimal());
        } else {
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
     * Returns { PropertySet.DOUBLE }
     */
    protected int[] allowedTypes() {
        return new int[] {PropertySet.DOUBLE};
    }
}
