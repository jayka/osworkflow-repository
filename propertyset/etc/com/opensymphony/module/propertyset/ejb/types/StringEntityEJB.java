/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.ejb.types;

import com.opensymphony.module.propertyset.PropertyImplementationException;
import com.opensymphony.module.propertyset.PropertySet;

import javax.ejb.*;


/**
 * AbstractValueEntityEJB concrete implementation optimized for storing short Strings.
 * This can be used to store STRING.
 *
 * @ejb.bean
 *  type="CMP"
 *  name="PropertyString"
 *  schema="PropertyString"
 *  view-type="local"
 *  reentrant="False"
 *  primkey-field="id"
 *
 * @ejb.pk class="java.lang.Long" extends="java.lang.Object"
 *
 * @ejb.persistence table-name="OS_PROPERTYSTRING"
 * @ejb.permission unchecked="true"
 * @ejb.transaction type="Supports"
 *
 * @author <a href="mailto:hani@formicary.net">Hani Suleiman</a>
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 169 $
 *
 * @see com.opensymphony.module.propertyset.ejb.PropertyStoreEJB
 */
public abstract class StringEntityEJB implements EntityBean {
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

    public abstract void setString(String string);

    /**
     * @ejb.persistence column-name="value"
     */
    public abstract String getString();

    public void setEntityContext(EntityContext context) {
        this.context = context;
    }

    /**
     * Set the value. Only java.lang.String can be supplied.
     *
     * @ejb.interface-method
     */
    public void setValue(int type, String value) {
        if (value == null) {
            setString("");

            return;
        }

        if (type == PropertySet.STRING) {
            setString(value);
        } else {
            throw new PropertyImplementationException("Cannot store this type of property.");
        }
    }

    /**
     * Return String based value.
     *
     * @ejb.interface-method
     */
    public String getValue(int type) {
        if (type == PropertySet.STRING) {
            return getString();
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
     * Returns { PropertySet.STRING }
     */
    protected int[] allowedTypes() {
        return new int[] {PropertySet.STRING};
    }
}
