/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.verifiers;

import junit.framework.TestCase;


/**
 * User: bbulger
 * Date: May 22, 2004
 */
public class NumberVerifierTest extends TestCase {
    //~ Instance fields ////////////////////////////////////////////////////////

    protected NumberVerifier verifier;

    //~ Methods ////////////////////////////////////////////////////////////////

    public void testVerifyNumberBetweenMaxAndMin() {
        assertEquals(new Integer(7), verifier.getMin());
        assertEquals(new Integer(11), verifier.getMax());
        verifier.verify(new Integer(10));
    }

    public void testVerifyNumberGreaterThanMax() {
        try {
            verifier.verify(new Integer(15));
            fail("Should not be able to verify a number greater than the max.");
        } catch (VerifyException e) {
            // success
        }
    }

    public void testVerifyNumberLessThanMin() {
        try {
            verifier.verify(new Integer(1));
            fail("Should not be able to verify a number less than the min.");
        } catch (VerifyException e) {
            // success
        }
    }

    public void testVerifyNumberWithDifferentTypes() {
        assertEquals(Integer.class, verifier.getType());

        try {
            verifier.verify(new Long(100000));
            fail("Should not be able to verify a type of number other than the type specified.");
        } catch (VerifyException e) {
            // success
        }
    }

    protected void setUp() throws Exception {
        super.setUp();
        verifier = new NumberVerifier();
        verifier.setMin(new Integer(7));
        verifier.setMax(new Integer(11));
        verifier.setType(Integer.class);
    }
}
