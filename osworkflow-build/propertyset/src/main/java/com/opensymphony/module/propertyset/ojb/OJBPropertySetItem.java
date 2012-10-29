/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.ojb;

import java.io.Serializable;

import java.util.Date;


/**
 * @author picard
 */
public class OJBPropertySetItem implements Serializable {
    //~ Instance fields ////////////////////////////////////////////////////////

    Date dateValue;
    String globalKey;
    String itemKey;
    String stringValue;
    byte[] byteValue;
    double doubleValue;
    int itemType;
    long longValue;

    //~ Constructors ///////////////////////////////////////////////////////////

    public OJBPropertySetItem() {
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public void setByteValue(byte[] bs) {
        byteValue = bs;
    }

    public byte[] getByteValue() {
        return byteValue;
    }

    public void setDateValue(Date date) {
        dateValue = date;
    }

    public Date getDateValue() {
        return dateValue;
    }

    /**
     * @param d
     */
    public void setDoubleValue(double d) {
        doubleValue = d;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public void setGlobalKey(String string) {
        globalKey = string;
    }

    public String getGlobalKey() {
        return globalKey;
    }

    public void setItemKey(String string) {
        itemKey = string;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemType(int i) {
        itemType = i;
    }

    public int getItemType() {
        return itemType;
    }

    public void setLongValue(long l) {
        longValue = l;
    }

    public long getLongValue() {
        return longValue;
    }

    public void setStringValue(String string) {
        stringValue = string;
    }

    public String getStringValue() {
        return stringValue;
    }
}
