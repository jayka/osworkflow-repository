/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.ejb.types;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.module.sequence.SequenceGenerator;
import com.opensymphony.module.sequence.SequenceGeneratorHome;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;

import java.sql.Timestamp;

import javax.ejb.*;

import javax.naming.InitialContext;

import javax.rmi.PortableRemoteObject;


/**
 * @ejb.bean
 *  type="CMP"
 *  view-type="local"
 *  name="PropertyEntry"
 *  reentrant="False"
 *  schema="PropertyEntry"
 *  primkey-field="id"
 *
 * @ejb.pk class="java.lang.Long" extends="java.lang.Object"
 *
 * @ejb.persistence table-name="OS_PROPERTYENTRY"
 *
 * @ejb.ejb-ref
 *  ejb-name="PropertyNumber"
 *  view-type="local"
 * @ejb.ejb-ref
 *  ejb-name="PropertyDate"
 *  view-type="local"
 * @ejb.ejb-ref
 *  ejb-name="PropertyData"
 *  view-type="local"
 * @ejb.ejb-ref
 *  ejb-name="PropertyString"
 *  view-type="local"
 * @ejb.ejb-ref
 *  ejb-name="PropertyDecimal"
 *  view-type="local"
 *
 * @ejb.ejb-external-ref
 *  ref-name="ejb/SequenceGenerator"
 *  type="Session"
 *  view-type="remote"
 *  link="SequenceGenerator"
 *  home="com.opensymphony.module.sequence.SequenceGeneratorHome"
 *  business="com.opensymphony.module.sequence.SequenceGenerator"
 *
 * @ejb.finder
 *  signature="java.util.Collection findByNameAndId(java.lang.String entityName, long entityId)"
 *  query="SELECT DISTINCT OBJECT(o) FROM PropertyEntry o WHERE o.entityName = ?1 AND o.entityId = ?2"
 *
 * @ejb.finder
 *  signature="com.opensymphony.module.propertyset.ejb.types.PropertyEntry findByEntity(java.lang.String entityName, long entityId, java.lang.String key)"
 *  query="SELECT DISTINCT OBJECT(o) FROM PropertyEntry o WHERE o.entityName = ?1 AND o.entityId = ?2 AND o.key = ?3"
 *
 * @ejb.permission unchecked="true"
 * @ejb.transaction type="Supports"
 */
public abstract class PropertyEntryEJB implements EntityBean {
    //~ Static fields/initializers /////////////////////////////////////////////

    private static final Log logger = LogFactory.getLog(PropertyEntryEJB.class);

    //~ Instance fields ////////////////////////////////////////////////////////

    private EntityContext context;

    //~ Methods ////////////////////////////////////////////////////////////////

    public abstract void setEntityId(long entityId);

    /**
     * @ejb.interface-method
     * @ejb.persistence column-name="entityid"
     */
    public abstract long getEntityId();

    public abstract void setEntityName(String entityName);

    /**
     * @ejb.interface-method
     * @ejb.persistence column-name="entityname"
     */
    public abstract String getEntityName();

    public abstract void setId(Long id);

    /**
     * @ejb.pk-field
     * @ejb.interface-method
     * @ejb.persistence column-name="id"
     */
    public abstract Long getId();

    public abstract void setKey(String key);

    /**
     * @ejb.interface-method
     * @ejb.persistence column-name="keyvalue"
     */
    public abstract String getKey();

    public abstract void setType(int type);

    /**
     * @ejb.interface-method
     * @ejb.persistence column-name="type"
     */
    public abstract int getType();

    public void setEntityContext(EntityContext context) {
        this.context = context;
    }

    /**
     * @ejb.interface-method
     */
    public void setValue(Serializable value) {
        int type = getType();
        Long id = getId();

        try {
            if ((type == PropertySet.BOOLEAN) || (type == PropertySet.INT) || (type == PropertySet.LONG)) {
                PropertyNumberLocalHome home = PropertyNumberHomeFactory.getLocalHome();
                home.findByPrimaryKey(id).setValue(type, value);
            } else if (type == PropertySet.DATE) {
                PropertyDateLocalHome home = PropertyDateHomeFactory.getLocalHome();
                home.findByPrimaryKey(id).setValue(type, (Timestamp) value);
            } else if (type == PropertySet.DOUBLE) {
                PropertyDecimalLocalHome home = PropertyDecimalHomeFactory.getLocalHome();
                home.findByPrimaryKey(id).setValue(type, (Double) value);
            } else if (type == PropertySet.STRING) {
                PropertyStringLocalHome home = PropertyStringHomeFactory.getLocalHome();
                home.findByPrimaryKey(id).setValue(type, (String) value);
            } else {
                PropertyDataLocalHome home = PropertyDataHomeFactory.getLocalHome();
                home.findByPrimaryKey(id).setValue(type, value);
            }
        } catch (Exception e) {
            logger.error("Error setting value in PropertySet", e);
        }
    }

    /**
     * @ejb.interface-method
     */
    public Serializable getValue() {
        int type = getType();
        Long id = getId();

        try {
            if ((type == PropertySet.BOOLEAN) || (type == PropertySet.INT) || (type == PropertySet.LONG)) {
                PropertyNumberLocalHome home = PropertyNumberHomeFactory.getLocalHome();

                return home.findByPrimaryKey(id).getValue(type);
            } else if (type == PropertySet.DATE) {
                PropertyDateLocalHome home = PropertyDateHomeFactory.getLocalHome();

                return home.findByPrimaryKey(id).getValue(type);
            } else if (type == PropertySet.DOUBLE) {
                PropertyDecimalLocalHome home = PropertyDecimalHomeFactory.getLocalHome();

                return home.findByPrimaryKey(id).getValue(type);
            } else if (type == PropertySet.STRING) {
                PropertyStringLocalHome home = PropertyStringHomeFactory.getLocalHome();

                return home.findByPrimaryKey(id).getValue(type);
            } else {
                PropertyDataLocalHome home = PropertyDataHomeFactory.getLocalHome();

                return home.findByPrimaryKey(id).getValue(type);
            }
        } catch (Exception e) {
            logger.warn("Error getting value from PropertySet", e);

            return null;
        }
    }

    public void ejbActivate() {
    }

    /**
     * @ejb.create-method
     */
    public Long ejbCreate(String entityName, long entityId, int type, String key) throws CreateException {
        Long id = null;

        try {
            InitialContext ctx = new InitialContext();

            SequenceGeneratorHome genHome = (SequenceGeneratorHome) PortableRemoteObject.narrow(ctx.lookup("java:comp/env/ejb/SequenceGenerator"), SequenceGeneratorHome.class);
            SequenceGenerator gen = genHome.create();
            id = new Long(gen.getCount("os.PropertyEntry"));

            setId(id);
            setEntityName(entityName);
            setEntityId(entityId);
            setType(type);
            setKey(key);

            if ((type == PropertySet.BOOLEAN) || (type == PropertySet.INT) || (type == PropertySet.LONG)) {
                PropertyNumberLocalHome home = PropertyNumberHomeFactory.getLocalHome();
                home.create(type, id.longValue());
            } else if (type == PropertySet.DATE) {
                PropertyDateLocalHome home = PropertyDateHomeFactory.getLocalHome();
                home.create(type, id.longValue());
            } else if (type == PropertySet.DOUBLE) {
                PropertyDecimalLocalHome home = PropertyDecimalHomeFactory.getLocalHome();
                home.create(type, id.longValue());
            } else if (type == PropertySet.STRING) {
                PropertyStringLocalHome home = PropertyStringHomeFactory.getLocalHome();
                home.create(type, id.longValue());
            } else {
                PropertyDataLocalHome home = PropertyDataHomeFactory.getLocalHome();
                home.create(type, id.longValue());
            }
        } catch (Exception e) {
            logger.error("Error creating new PropertyEntry", e);
            throw new CreateException(e.toString());
        }

        return null;
    }

    public void ejbLoad() {
    }

    public void ejbPassivate() {
    }

    public void ejbPostCreate(String entityName, long entityId, int type, String key) throws CreateException {
    }

    public void ejbRemove() throws RemoveException {
        int type = getType();
        Long id = getId();

        try {
            InitialContext ctx = new InitialContext();

            if ((type == PropertySet.BOOLEAN) || (type == PropertySet.INT) || (type == PropertySet.LONG)) {
                PropertyNumberLocalHome home = (PropertyNumberLocalHome) PortableRemoteObject.narrow(ctx.lookup(PropertyNumberLocalHome.COMP_NAME), PropertyNumberLocalHome.class);
                home.findByPrimaryKey(id).remove();
            } else if (type == PropertySet.DATE) {
                PropertyDateLocalHome home = (PropertyDateLocalHome) PortableRemoteObject.narrow(ctx.lookup(PropertyDateLocalHome.COMP_NAME), PropertyDateLocalHome.class);
                home.findByPrimaryKey(id).remove();
            } else if (type == PropertySet.DOUBLE) {
                PropertyDecimalLocalHome home = (PropertyDecimalLocalHome) PortableRemoteObject.narrow(ctx.lookup(PropertyDecimalLocalHome.COMP_NAME), PropertyDecimalLocalHome.class);
                home.findByPrimaryKey(id).remove();
            } else if (type == PropertySet.STRING) {
                PropertyStringLocalHome home = (PropertyStringLocalHome) PortableRemoteObject.narrow(ctx.lookup(PropertyStringLocalHome.COMP_NAME), PropertyStringLocalHome.class);
                home.findByPrimaryKey(id).remove();
            } else {
                PropertyDataLocalHome home = (PropertyDataLocalHome) PortableRemoteObject.narrow(ctx.lookup(PropertyDataLocalHome.COMP_NAME), PropertyDataLocalHome.class);
                home.findByPrimaryKey(id).remove();
            }
        } catch (Exception e) {
            logger.error("Error removing PropertySet", e);
        }
    }

    public void ejbStore() {
    }

    public void unsetEntityContext() {
        context = null;
    }
}
