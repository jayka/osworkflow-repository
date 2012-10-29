/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.hibernate;

import com.opensymphony.module.propertyset.AbstractPropertySet;
import com.opensymphony.module.propertyset.PropertyException;
import com.opensymphony.module.propertyset.PropertySet;

import com.opensymphony.util.ClassLoaderUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;
import java.util.Date;
import java.util.Map;


/**
 * This is the property set implementation for storing properties using Hibernate.
 * <p>
 *
 * <b>Required Args</b>
 * <ul>
 *  <li><b>entityId</b> - Long that holds the ID of this entity.</li>
 *  <li><b>entityName</b> - String that holds the name of this entity type</li>
 * </ul>
 *
 * if "<b>sessionFactory</b> - hibernate sessionFactory" is not passed as an arg then init will use: <br />
 *  <b>hibernate.*</b> - config params needed to create a hibernate sessionFactory in the propertyset config xml.
 * <br />
 * This can be any of the configs avail from hibernate.
 * <p>
 *
 * @author $Author: hani $
 * @version $Revision: 169 $
 */
public class HibernatePropertySet extends AbstractPropertySet {
    //~ Static fields/initializers /////////////////////////////////////////////

    protected static Log log = LogFactory.getLog(HibernatePropertySet.class.getName());

    //~ Instance fields ////////////////////////////////////////////////////////

    private HibernateConfigurationProvider configProvider;
    private Long entityId;
    private String entityName;

    //~ Methods ////////////////////////////////////////////////////////////////

    public Collection getKeys(String prefix, int type) throws PropertyException {
        return configProvider.getPropertySetDAO().getKeys(entityName, entityId, prefix, type);
    }

    public int getType(String key) throws PropertyException {
        return findByKey(key).getType();
    }

    public boolean exists(String key) throws PropertyException {
        try {
            if (findByKey(key) != null) {
                return true;
            }

            return false;
        } catch (PropertyException e) {
            return false;
        }
    }

    public void init(Map config, Map args) {
        super.init(config, args);
        this.entityId = (Long) args.get("entityId");
        this.entityName = (String) args.get("entityName");

        // first let's see if we got given a configuration provider to use already
        configProvider = (HibernateConfigurationProvider) args.get("configurationProvider");

        if (configProvider == null) // if we did not get given one in the args, we need to set a config provider up
         {
            // lets see if we need to use a configurationProvider from a class
            String configProviderClass = (String) config.get("configuration.provider.class");

            if (configProviderClass != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Setting up property set provider of class: " + configProviderClass);
                }

                try {
                    configProvider = (HibernateConfigurationProvider) ClassLoaderUtil.loadClass(configProviderClass, this.getClass()).newInstance();
                } catch (Exception e) {
                    log.error("Unable to load configuration provider class: " + configProviderClass, e);

                    return;
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Setting up property set with DefaultHibernateConfigurationProvider");
                }

                configProvider = new DefaultHibernateConfigurationProvider();
            }

            configProvider.setupConfiguration(config);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Setting up property set with hibernate provider passed in args.");
            }
        }
    }

    public void remove(String key) throws PropertyException {
        configProvider.getPropertySetDAO().remove(entityName, entityId, key);
    }

    public void remove() throws PropertyException {
        configProvider.getPropertySetDAO().remove(entityName, entityId);
    }

    public boolean supportsType(int type) {
        switch (type) {
        case PropertySet.DATA:
        case PropertySet.OBJECT:
        case PropertySet.PROPERTIES:
        case PropertySet.XML:
            return false;
        }

        return true;
    }

    protected void setImpl(int type, String key, Object value) throws PropertyException {
        PropertySetItem item = null;

        boolean update = true;
        item = configProvider.getPropertySetDAO().findByKey(entityName, entityId, key);

        if (item == null) {
            update = false;
            item = configProvider.getPropertySetDAO().create(entityName, entityId.longValue(), key);
        } else if (item.getType() != type) {
            throw new PropertyException("Existing key '" + key + "' does not have matching type of " + type);
        }

        switch (type) {
        case BOOLEAN:
            item.setBooleanVal(((Boolean) value).booleanValue());

            break;

        case DOUBLE:
            item.setDoubleVal(((Double) value).doubleValue());

            break;

        case STRING:
        case TEXT:
            item.setStringVal((String) value);

            break;

        case LONG:
            item.setLongVal(((Long) value).longValue());

            break;

        case INT:
            item.setIntVal(((Integer) value).intValue());

            break;

        case DATE:
            item.setDateVal((Date) value);

            break;

        default:
            throw new PropertyException("type " + type + " not supported");
        }

        item.setType(type);

        configProvider.getPropertySetDAO().setImpl(item, update);
    }

    protected Object get(int type, String key) throws PropertyException {
        PropertySetItem item = findByKey(key);

        if (item == null) {
            return null;
        }

        if (item.getType() != type) {
            throw new PropertyException("key '" + key + "' does not have matching type of " + type);
        }

        switch (type) {
        case BOOLEAN:
            return new Boolean(item.getBooleanVal());

        case DOUBLE:
            return new Double(item.getDoubleVal());

        case STRING:
        case TEXT:
            return item.getStringVal();

        case LONG:
            return new Long(item.getLongVal());

        case INT:
            return new Integer(item.getIntVal());

        case DATE:
            return item.getDateVal();
        }

        throw new PropertyException("type " + type + " not supported");
    }

    private PropertySetItem findByKey(String key) throws PropertyException {
        return configProvider.getPropertySetDAO().findByKey(entityName, entityId, key);
    }
}
