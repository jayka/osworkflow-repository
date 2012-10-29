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
public class NumberPropertyHandlerTest extends TestCase {
    //~ Instance fields ////////////////////////////////////////////////////////

    protected PropertyHandler handler;

    //~ Methods ////////////////////////////////////////////////////////////////

    public void testProcessGetWithBooleanTypeAndLongInput() {
        assertNotNull(handler.processGet(PropertySet.BOOLEAN, new Long(1)));
    }

    public void testProcessGetWithIntTypeAndLongInput() {
        assertNotNull(handler.processGet(PropertySet.INT, new Long(1)));
    }

    public void testProcessGetWithInvalidInput() {
        try {
            handler.processGet(PropertySet.INT, new Integer(1));
            fail("NumberPropertyHandler can only process Long inputs");
        } catch (PropertyException e) {
            // success
        }
    }

    public void testProcessGetWithInvalidTypeAndLongInput() {
        try {
            handler.processGet(PropertySet.STRING, new Long(1));
            fail("Shouldn't be able to processGet with String type.");
        } catch (PropertyException e) {
            // success
        }
    }

    public void testProcessGetWithLongTypeAndLongInput() {
        assertNotNull(handler.processGet(PropertySet.LONG, new Long(1)));
    }

    public void testProcessSetWithBooleanTypeAndBooleanInput() {
        assertEquals(new Long(1), handler.processSet(PropertySet.BOOLEAN, new Boolean(true)));
        assertEquals(new Long(0), handler.processSet(PropertySet.BOOLEAN, new Boolean(false)));
    }

    public void testProcessSetWithIntTypeAndIntInput() {
        assertEquals(new Long(11), handler.processSet(PropertySet.INT, new Integer(11)));
    }

    public void testProcessSetWithLongTypeAndLongInput() {
        assertEquals(new Long(100000), handler.processSet(PropertySet.LONG, new Long(100000)));
    }

    public void testProcessSetWithNullInput() {
        assertEquals(new Long(0), handler.processSet(PropertySet.LONG, null));
    }

    protected void setUp() throws Exception {
        super.setUp();
        handler = new NumberPropertyHandler();
    }
}
