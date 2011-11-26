/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.ejb;

import com.opensymphony.module.propertyset.*;
import com.opensymphony.module.propertyset.ejb.types.PropertyEntryHomeFactory;
import com.opensymphony.module.propertyset.ejb.types.PropertyEntryLocal;
import com.opensymphony.module.propertyset.ejb.types.PropertyEntryLocalHome;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;

import java.util.*;

import javax.ejb.*;

import javax.naming.NamingException;


/**
 * Session bean implementation of PropertyStore.
 *
 * <p>Makes use of ValueEntityDelegator to determine which entity beans to use for
 * appropriate types.</p>
 *
 * @ejb.bean
 *  type="Stateless"
 *  name="PropertyStore"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.ejb-ref
 *  ejb-name="PropertyEntry"
 *  view-type="local"
 *
 * @ejb.permission unchecked="true"
 * @ejb.transaction type="Supports"
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @author <a href="mailto:hani@formicary.net">Hani Suleiman</a>
 * @version $Revision: 169 $
 *
 * @see com.opensymphony.module.propertyset.ejb.PropertyStore
 * @see com.opensymphony.module.propertyset.ejb.PropertyStoreHome
 */
public class PropertyStoreEJB implements SessionBean {
    //~ Static fields/initializers /////////////////////////////////////////////

    private static final Log logger = LogFactory.getLog(PropertyStoreEJB.class);

    //~ Instance fields ////////////////////////////////////////////////////////

    private PropertyEntryLocalHome entryHome;

    /*public void ejbPostCreate() throws CreateException {}*/
    private SessionContext context;

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * @ejb.interface-method
     */
    public Collection getKeys(String entityName, long entityId, String prefix, int type) {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("getKeys(" + entityName + "," + entityId + ")");
            }

            List results = new ArrayList();
            Iterator entries = entryHome.findByNameAndId(entityName, entityId).iterator();

            while (entries.hasNext()) {
                PropertyEntryLocal entry = (PropertyEntryLocal) entries.next();
                String key = entry.getKey();

                if (((prefix == null) || key.startsWith(prefix)) && ((type == 0) || (type == entry.getType()))) {
                    results.add(key);
                }
            }

            Collections.sort(results);

            return results;
        } catch (FinderException e) {
            logger.error("Could not find keys.", e);
            throw new PropertyImplementationException(e);
        }
    }

    public void setSessionContext(SessionContext ctx) {
        try {
            entryHome = PropertyEntryHomeFactory.getLocalHome();
        } catch (NamingException e) {
            logger.fatal("Could not lookup PropertyEntryHome.", e);
            throw new EJBException(e);
        }

        this.context = ctx;
    }

    /**
     * @ejb.interface-method
     */
    public int getType(String entityName, long entityId, String key) {
        if (logger.isDebugEnabled()) {
            logger.debug("getType(" + entityName + "," + entityId + ",\"" + key + "\")");
        }

        try {
            return entryHome.findByEntity(entityName, entityId, key).getType();
        } catch (ObjectNotFoundException e) {
            return 0;
        } catch (FinderException e) {
            logger.error("Could not find type.", e);
            throw new PropertyImplementationException(e);
        }
    }

    public void ejbActivate() {
    }

    /**
     * @ejb.create-method
     */
    public void ejbCreate() throws CreateException {
    }

    public void ejbPassivate() {
    }

    public void ejbRemove() {
    }

    /**
     * @ejb.interface-method
     */
    public boolean exists(String entityName, long entityId, String key) {
        if (logger.isDebugEnabled()) {
            logger.debug("exists(" + entityName + "," + entityId + ",\"" + key + "\")");
        }

        return getType(entityName, entityId, key) != 0;
    }

    /**
     * @ejb.interface-method
     */
    public Serializable get(String entityName, long entityId, int type, String key) {
        if (logger.isDebugEnabled()) {
            logger.debug("get(" + entityName + "," + entityId + "," + type + ",\"" + key + "\")");
        }

        try {
            PropertyEntryLocal entry = entryHome.findByEntity(entityName, entityId, key);

            if (type != entry.getType()) {
                // type does not match
                if (logger.isDebugEnabled()) {
                    logger.debug("wrong property type");
                }

                throw new InvalidPropertyTypeException();
            }

            return entry.getValue(); // found it
        } catch (ObjectNotFoundException e) {
            // Property does not exist.
            if (logger.isDebugEnabled()) {
                logger.debug("no property found");
            }

            return null;
        } catch (PropertyException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Could not retrieve value.", e);
            throw new PropertyImplementationException(e);
        }
    }

    /**
     * @ejb.interface-method
     * @ejb.transaction type="Required"
     */
    public void removeEntry(String entityName, long entityId) {
        try {
            Collection items = entryHome.findByNameAndId(entityName, entityId);
            Iterator iter = items.iterator();

            while (iter.hasNext()) {
                PropertyEntryLocal entry = (PropertyEntryLocal) iter.next();
                entry.remove();
            }
        } catch (FinderException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Value did not exist anyway.");
            }
        } catch (PropertyException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Could not remove value.", e);
            throw new PropertyImplementationException("Could not remove value.", e);
        }
    }

    /**
     * @ejb.interface-method
     */
    public void removeEntry(String entityName, long entityId, String key) {
        if (logger.isDebugEnabled()) {
            logger.debug("remove(" + entityName + "," + entityId + ",\"" + key + "\")");
        }

        try {
            entryHome.findByEntity(entityName, entityId, key).remove();
        } catch (ObjectNotFoundException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Value did not exist anyway.");
            }
        } catch (PropertyException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Could not remove value.", e);
            throw new PropertyImplementationException("Could not remove value.", e);
        }
    }

    /**
     * @ejb.interface-method
     */
    public void set(String entityName, long entityId, int type, String key, Serializable value) {
        if (logger.isDebugEnabled()) {
            logger.debug("set(" + entityName + "," + entityId + "," + type + ",\"" + key + "\", [" + value + "] )");
        }

        // If null, remove value.
        if (value == null) {
            removeEntry(entityName, entityId, key);

            return;
        }

        PropertyEntryLocal entry;

        try {
            entry = entryHome.findByEntity(entityName, entityId, key);

            // if we get here, then a property with that key already exists
            if (entry.getType() != type) { // verify existing property is of same type

                if (logger.isWarnEnabled()) {
                    logger.warn("property is of different type");
                }

                throw new DuplicatePropertyKeyException();
            }
        } catch (ObjectNotFoundException e) {
            // property with that key does not yet exist
            try {
                entry = entryHome.create(entityName, entityId, type, key);
            } catch (CreateException ce) {
                logger.error("Could not create new property.", ce);
                throw new PropertyImplementationException("Could not create new property.", ce);
            }
        } catch (PropertyException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Could not set property.", e);
            throw new PropertyImplementationException("Could not set property.", e);
        }

        entry.setValue(value);
    }
}
