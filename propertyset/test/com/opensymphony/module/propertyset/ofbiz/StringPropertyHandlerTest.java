/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.ofbiz;

import com.opensymphony.module.propertyset.PropertyException;
import com.opensymphony.module.propertyset.PropertySet;

import junit.framework.TestCase;


/**
 * User: bbulger
 * Date: May 24, 2004
 */
public class StringPropertyHandlerTest extends TestCase {
    //~ Instance fields ////////////////////////////////////////////////////////

    protected PropertyHandler handler;

    //~ Methods ////////////////////////////////////////////////////////////////

    public void testProcessGetWithInvalidType() {
        try {
            handler.processGet(PropertySet.INT, "1");
            fail("Shouldn't be able to call processGet with an Int");
        } catch (PropertyException e) {
            // success
        }
    }

    public void testProcessGetWithString() {
        assertNotNull(handler.processGet(PropertySet.STRING, "value1"));
    }

    public void testProcessGetWithText() {
        assertNotNull(handler.processGet(PropertySet.TEXT, "value1"));
    }

    public void testProcessSetWithInvalidType() {
        try {
            handler.processSet(PropertySet.INT, "1");
            fail("Shouldn't be able to call processSet with an Int");
        } catch (PropertyException e) {
            // success
        }
    }

    public void testProcessSetWithString() {
        assertNotNull(handler.processSet(PropertySet.STRING, "value1"));
    }

    public void testProcessSetWithText() {
        assertNotNull(handler.processSet(PropertySet.TEXT, "value1"));
    }

    protected void setUp() throws Exception {
        super.setUp();
        handler = new StringPropertyHandler();
    }
}
