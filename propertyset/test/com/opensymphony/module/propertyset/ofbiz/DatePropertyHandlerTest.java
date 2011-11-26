/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.ofbiz;

import com.opensymphony.module.propertyset.PropertyException;
import com.opensymphony.module.propertyset.PropertySet;

import junit.framework.TestCase;

import java.sql.Timestamp;

import java.text.DateFormat;

import java.util.Date;


/**
 * User: bbulger
 * Date: May 24, 2004
 */
public class DatePropertyHandlerTest extends TestCase {
    //~ Instance fields ////////////////////////////////////////////////////////

    protected Date now;
    protected DateFormat df;
    protected PropertyHandler handler;

    //~ Methods ////////////////////////////////////////////////////////////////

    public void testProcessGetWithDateType() {
        assertEquals(df.format(now), df.format(handler.processGet(PropertySet.DATE, now)));
    }

    public void testProcessSetWithDateTypeAndSqlDate() {
        java.sql.Date sqlDate = new java.sql.Date(now.getTime());
        assertEquals(df.format(sqlDate), df.format(handler.processSet(PropertySet.DATE, sqlDate)));
    }

    public void testProcessSetWithDateTypeAndSqlTimestamp() {
        Timestamp timestamp = new Timestamp(now.getTime());
        assertEquals(df.format(timestamp), df.format(handler.processSet(PropertySet.DATE, timestamp)));
    }

    public void testProcessSetWithDateTypeAndUtilDate() {
        assertEquals(df.format(now), df.format(handler.processSet(PropertySet.DATE, now)));
    }

    public void testProcessSetWithInvalidType() {
        try {
            handler.processSet(PropertySet.STRING, "");
            fail("Shouldn't be able to processSet with String type");
        } catch (PropertyException e) {
            // success
        }
    }

    protected void setUp() throws Exception {
        super.setUp();
        handler = new DatePropertyHandler();
        df = DateFormat.getInstance();
        now = new Date();
    }
}
