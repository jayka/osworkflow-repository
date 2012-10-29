/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.ofbiz;

import com.opensymphony.module.propertyset.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.ofbiz.core.entity.*;
import org.ofbiz.core.util.UtilMisc;

import java.io.Serializable;

import java.util.*;


/**
 * This is the property set implementation for storing properties in the OFBiz Entity Engine.
 * <p>
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
 *  <li><b>delegator.name</b> - the name of the Ofbiz delegator, defaults to "default"</li>
 * </ul>
 *
 * @author <a href="mailto:salaman@qoretech.com">Victor Salaman</a>
 * @author <a href="mailto:mike@atlassian.com">Mike Cannon-Brookes</a>
 * $Revision: 169 $
 */
public class OFBizPropertySet extends AbstractPropertySet implements Serializable {
    //~ Static fields/initializers /////////////////////////////////////////////

    private static final Log log = LogFactory.getLog(OFBizPropertySet.class);

    // Attributes ----------------------------------------------------
    static Map entityTypeMap;

    static {
        PropertyHandler stringHandler = new StringPropertyHandler();
        PropertyHandler textHandler = new StringPropertyHandler();
        PropertyHandler dateHandler = new DatePropertyHandler();
        PropertyHandler dataHandler = new DataPropertyHandler();
        PropertyHandler numberHandler = new NumberPropertyHandler();
        PropertyHandler decimalHandler = new DecimalPropertyHandler();

        entityTypeMap = new HashMap();
        entityTypeMap.put(new Integer(PropertySet.BOOLEAN), new TypeMap("OSPropertyNumber", numberHandler));
        entityTypeMap.put(new Integer(PropertySet.INT), new TypeMap("OSPropertyNumber", numberHandler));
        entityTypeMap.put(new Integer(PropertySet.LONG), new TypeMap("OSPropertyNumber", numberHandler));
        entityTypeMap.put(new Integer(PropertySet.DOUBLE), new TypeMap("OSPropertyDecimal", decimalHandler));
        entityTypeMap.put(new Integer(PropertySet.STRING), new TypeMap("OSPropertyString", stringHandler));
        entityTypeMap.put(new Integer(PropertySet.TEXT), new TypeMap("OSPropertyText", textHandler));
        entityTypeMap.put(new Integer(PropertySet.DATE), new TypeMap("OSPropertyDate", dateHandler));
        entityTypeMap.put(new Integer(PropertySet.OBJECT), new TypeMap("OSPropertyData", dataHandler));
        entityTypeMap.put(new Integer(PropertySet.XML), new TypeMap("OSPropertyData", dataHandler));
        entityTypeMap.put(new Integer(PropertySet.DATA), new TypeMap("OSPropertyData", dataHandler));
        entityTypeMap.put(new Integer(PropertySet.PROPERTIES), new TypeMap("OSPropertyData", dataHandler));
    }

    //~ Instance fields ////////////////////////////////////////////////////////

    Long entityId;
    String delegatorName;
    String entityName;

    //~ Methods ////////////////////////////////////////////////////////////////

    public Collection getKeys() throws PropertyException {
        List results = new ArrayList();

        try {
            Collection c = getDelegator().findByAnd("OSPropertyEntry", UtilMisc.toMap("entityId", entityId, "entityName", entityName));

            for (Iterator iterator = c.iterator(); iterator.hasNext();) {
                GenericValue value = (GenericValue) iterator.next();
                String propertyKey = value.getString("propertyKey");

                if (propertyKey != null) {
                    results.add(propertyKey);
                }
            }

            Collections.sort(results);
        } catch (GenericEntityException e) {
            throw new PropertyImplementationException(e);
        }

        return results;
    }

    public Collection getKeys(String prefix) throws PropertyException {
        List results = new ArrayList();

        try {
            // chowda hack
            if (prefix == null) {
                prefix = "";
            }

            Collection c = getDelegator().findByAnd("OSPropertyEntry", UtilMisc.toMap("entityId", entityId, "entityName", entityName));

            for (Iterator iterator = c.iterator(); iterator.hasNext();) {
                GenericValue value = (GenericValue) iterator.next();
                String propertyKey = value.getString("propertyKey");

                if ((propertyKey != null) && propertyKey.startsWith(prefix)) {
                    results.add(propertyKey);
                }
            }

            Collections.sort(results);
        } catch (GenericEntityException e) {
            throw new PropertyImplementationException(e);
        }

        return results;
    }

    public Collection getKeys(String prefix, int type) throws PropertyException {
        List results = new ArrayList();

        try {
            // chowda hack
            if (prefix == null) {
                prefix = "";
            }

            Collection c = getDelegator().findByAnd("OSPropertyEntry", UtilMisc.toMap("entityId", entityId, "entityName", entityName));

            for (Iterator iterator = c.iterator(); iterator.hasNext();) {
                GenericValue value = (GenericValue) iterator.next();
                String propertyKey = value.getString("propertyKey");
                Integer entryType = value.getInteger("type");

                if (((propertyKey != null) && propertyKey.startsWith(prefix)) && (entryType.intValue() == type)) {
                    results.add(propertyKey);
                }
            }

            Collections.sort(results);
        } catch (GenericEntityException e) {
            throw new PropertyImplementationException(e);
        }

        return results;
    }

    public int getType(String key) throws PropertyException {
        GenericValue v = findPropertyEntry(key);

        if (v == null) {
            throw new PropertyImplementationException("Property '" + key + "' not found");
        }

        return v.getInteger("type").intValue();
    }

    // Public --------------------------------------------------------
    public boolean exists(String key) throws PropertyException {
        if (findPropertyEntry(key) != null) {
            return true;
        } else {
            return false;
        }
    }

    // Constructors --------------------------------------------------
    public void init(Map config, Map args) {
        delegatorName = (String) config.get("delegator.name");

        if (delegatorName == null) {
            delegatorName = "default";
        }

        entityId = (Long) args.get("entityId");
        entityName = (String) args.get("entityName");
    }

    public void remove() throws PropertyException {
        //hani: todo this needs to be optimised rather badly, but I have no idea how
        Collection keys = getKeys();
        Iterator iter = keys.iterator();

        while (iter.hasNext()) {
            String key = (String) iter.next();
            remove(key);
        }
    }

    public void remove(String key) throws PropertyException {
        try {
            // remove actual property
            int type = this.getType(key);
            GenericValue v = findPropertyEntry(key);

            if (v != null) {
                Integer t = v.getInteger("type");
                Long id = v.getLong("id");

                if (type != t.intValue()) {
                    throw new InvalidPropertyTypeException();
                }

                TypeMap tm = (TypeMap) entityTypeMap.get(t);
                getDelegator().removeByAnd(tm.getEntity(), UtilMisc.toMap("id", id));
            }

            // now remove the property entry
            getDelegator().removeByAnd("OSPropertyEntry", makePropertyEntryFields(key));
        } catch (GenericEntityException e) {
            log.error("Error removing value from PropertySet", e);
            throw new PropertyImplementationException(e);
        }
    }

    public boolean supportsType(int type) {
        switch (type) {
        case DATA:
        case OBJECT:
        case PROPERTIES:
        case XML:
            return false;
        }

        return true;
    }

    protected void setImpl(int type, String key, Object obj) throws PropertyException {
        try {
            Long id;

            GenericValue propertyEntry = findPropertyEntry(key);

            if (propertyEntry == null) {
                id = getDelegator().getNextSeqId("OSPropertyEntry");
                propertyEntry = getDelegator().makeValue("OSPropertyEntry", UtilMisc.toMap("entityId", entityId, "id", id, "entityName", entityName, "type", new Integer(type), "propertyKey", key));
            } else {
                id = propertyEntry.getLong("id");
            }

            TypeMap tm = (TypeMap) entityTypeMap.get(new Integer(type));
            GenericValue propertyTypeEntry = getDelegator().makeValue(tm.getEntity(), UtilMisc.toMap("id", id, "value", processSet(type, obj)));

            List entities = new ArrayList();
            entities.add(propertyEntry);
            entities.add(propertyTypeEntry);
            getDelegator().storeAll(entities);
        } catch (GenericEntityException e) {
            log.error("Error setting value in PropertySet", e);
            throw new PropertyImplementationException(e);
        }
    }

    protected Object get(int type, String key) throws PropertyException {
        try {
            GenericValue v = findPropertyEntry(key);

            if (v != null) {
                Integer t = v.getInteger("type");
                Long id = v.getLong("id");

                if (type != t.intValue()) {
                    throw new InvalidPropertyTypeException();
                }

                TypeMap tm = (TypeMap) entityTypeMap.get(t);
                GenericValue property = (GenericValue) getDelegator().findByPrimaryKey(tm.getEntity(), UtilMisc.toMap("id", id));

                if (property == null) {
                    return null;
                }

                return processGet(type, property.get("value"));
            }
        } catch (GenericEntityException e) {
            throw new PropertyImplementationException(e);
        }

        return null;
    }

    private GenericDelegator getDelegator() {
        return GenericDelegator.getGenericDelegator(delegatorName);
    }

    private GenericValue findPropertyEntry(String key) throws PropertyException {
        try {
            Collection c = getDelegator().findByAnd("OSPropertyEntry", makePropertyEntryFields(key));

            if ((c == null) || (c.size() == 0)) {
                return null;
            }

            return (GenericValue) c.iterator().next();
        } catch (GenericEntityException e) {
            throw new PropertyImplementationException(e);
        }
    }

    private Map makePropertyEntryFields(String key) {
        return UtilMisc.toMap("propertyKey", key, "entityName", entityName, "entityId", entityId);
    }

    // Package protected ---------------------------------------------
    // Protected -----------------------------------------------------
    // Private -------------------------------------------------------
    // @todo implement processRemove
    private Object processGet(int type, Object input) throws PropertyException {
        if (input == null) {
            return null;
        }

        TypeMap typeMap = (TypeMap) entityTypeMap.get(new Integer(type));
        PropertyHandler handler = typeMap.getHandler();

        return handler.processGet(type, input);
    }

    private Object processSet(int type, Object input) throws PropertyException {
        if (input == null) {
            return null;
        }

        TypeMap typeMap = (TypeMap) entityTypeMap.get(new Integer(type));
        PropertyHandler handler = typeMap.getHandler();

        return handler.processSet(type, input);
    }

    //~ Inner Classes //////////////////////////////////////////////////////////

    // Static --------------------------------------------------------
    static class TypeMap {
        PropertyHandler handler;
        String entity;

        public TypeMap(String entity, PropertyHandler handler) {
            this.entity = entity;
            this.handler = handler;
        }

        public String getEntity() {
            return entity;
        }

        public PropertyHandler getHandler() {
            return handler;
        }
    }

    // Inner classes -------------------------------------------------
}
