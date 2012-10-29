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
public class DecimalPropertyHandlerTest extends TestCase {
    //~ Instance fields ////////////////////////////////////////////////////////

    protected PropertyHandler handler;

    //~ Methods ////////////////////////////////////////////////////////////////

    public void testProcessGetWithDoubleType() {
        assertEquals(new Double(10.245), handler.processGet(PropertySet.DOUBLE, new Double(10.245)));
    }

    public void testProcessGetWithInvalidType() {
        try {
            handler.processGet(PropertySet.STRING, new Double(10.245));
            fail("Shouldn't be able to processGet with String type");
        } catch (PropertyException e) {
            // success
        }
    }

    public void testProcessSetWithDoubleType() {
        assertEquals(new Double(10.245), handler.processSet(PropertySet.DOUBLE, new Double(10.245)));
    }

    public void testProcessSetWithInvalidType() {
        try {
            handler.processSet(PropertySet.STRING, new Double(10.245));
            fail("Shouldn't be able to processSet with String type");
        } catch (PropertyException e) {
            // success
        }
    }

    protected void setUp() throws Exception {
        super.setUp();
        handler = new DecimalPropertyHandler();
    }
}
