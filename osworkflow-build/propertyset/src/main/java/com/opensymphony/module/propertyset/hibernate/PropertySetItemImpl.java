/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.hibernate;

import java.io.Serializable;

import java.util.Date;


/**
 *
 *
 * @author $Author: hani $
 * @version $Revision: 169 $
 */
public class PropertySetItemImpl implements Serializable, PropertySetItem {
    //~ Instance fields ////////////////////////////////////////////////////////

    Date dateVal;
    String entityName;
    String key;
    String stringVal;
    boolean booleanVal;
    double doubleVal;
    int intVal;
    int type;
    long entityId;
    long longVal;

    //~ Constructors ///////////////////////////////////////////////////////////

    //needed for hibernate
    public PropertySetItemImpl() {
    }

    public PropertySetItemImpl(String entityName, long entityId, String key) {
        this.entityName = entityName;
        this.entityId = entityId;
        this.key = key;
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public void setBooleanVal(boolean booleanVal) {
        this.booleanVal = booleanVal;
    }

    public boolean getBooleanVal() {
        return booleanVal;
    }

    public void setDateVal(Date dateVal) {
        this.dateVal = dateVal;
    }

    public Date getDateVal() {
        return dateVal;
    }

    public void setDoubleVal(double doubleVal) {
        this.doubleVal = doubleVal;
    }

    public double getDoubleVal() {
        return doubleVal;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setIntVal(int intVal) {
        this.intVal = intVal;
    }

    public int getIntVal() {
        return intVal;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setLongVal(long longVal) {
        this.longVal = longVal;
    }

    public long getLongVal() {
        return longVal;
    }

    public void setStringVal(String stringVal) {
        this.stringVal = stringVal;
    }

    public String getStringVal() {
        return stringVal;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PropertySetItem)) {
            return false;
        }

        PropertySetItem item = (PropertySetItem) obj;

        return ((item.getEntityId() == entityId) && item.getEntityName().equals(entityName) && item.getKey().equals(key));
    }

    public int hashCode() {
        return (int) (entityId + entityName.hashCode() + key.hashCode());
    }
}
