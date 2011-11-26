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
public class StringVerifierTest extends TestCase {
    //~ Methods ////////////////////////////////////////////////////////////////

    public void testVerifyStringContainsInValidString() {
        StringVerifier verifier = new StringVerifier();
        verifier.setContains("thing");

        try {
            verifier.verify("tryagain");
            fail("Should not be able to verify a string contains an invalid string.");
        } catch (VerifyException e) {
            // success
        }
    }

    public void testVerifyStringContainsValidString() {
        StringVerifier verifier = new StringVerifier();
        verifier.setContains("thing");
        assertEquals("thing", verifier.getContains());
        verifier.verify("somethingelse");
    }

    public void testVerifyStringInAllowableValues() {
        String[] allowed = {"value1", "value2"};
        StringVerifier verifier = new StringVerifier(allowed);
        verifier.verify("value2");
    }

    public void testVerifyStringLengthLongerThanMax() {
        StringVerifier verifier = new StringVerifier();
        verifier.setMaxLength(10);
        assertEquals(10, verifier.getMaxLength());

        try {
            verifier.verify("123456789012");
            fail("Should not be able to verify a string longer than the max.");
        } catch (VerifyException e) {
            // success
        }
    }

    public void testVerifyStringLengthShorterThanMin() {
        StringVerifier verifier = new StringVerifier();
        verifier.setMinLength(10);
        assertEquals(10, verifier.getMinLength());

        try {
            verifier.verify("value1");
            fail("Should not be able to verify a string shorter than the min.");
        } catch (VerifyException e) {
            // success
        }
    }

    public void testVerifyStringNotInAllowableValues() {
        String[] allowed = {"value1", "value2"};
        StringVerifier verifier = new StringVerifier();
        verifier.setAllowableValues(allowed);

        try {
            verifier.verify("not there");
            fail("Should not be able to verify a String that isn't in the allowed values.");
        } catch (VerifyException e) {
            // success
        }
    }

    public void testVerifyStringWithInvalidPrefix() {
        StringVerifier verifier = new StringVerifier();
        verifier.setPrefix("test");

        try {
            verifier.verify("username");
            fail("Should not be able to verify a string with an invalid prefix.");
        } catch (VerifyException e) {
            // success
        }
    }

    public void testVerifyStringWithInvalidSuffix() {
        StringVerifier verifier = new StringVerifier();
        verifier.setSuffix("Int");

        try {
            verifier.verify("testDouble");
            fail("Should not be able to verify string with an invalid suffix.");
        } catch (VerifyException e) {
            // success
        }
    }

    public void testVerifyStringWithLengthBetweenMinAndMax() {
        StringVerifier verifier = new StringVerifier(0, 100);
        verifier.verify("this should work!");
    }

    public void testVerifyStringWithValidPrefix() {
        StringVerifier verifier = new StringVerifier();
        verifier.setPrefix("test");
        assertEquals("test", verifier.getPrefix());
        verifier.verify("testInt");
    }

    public void testVerifyStringWithValidSuffix() {
        StringVerifier verifier = new StringVerifier();
        verifier.setSuffix("Int");
        assertEquals("Int", verifier.getSuffix());
        verifier.verify("testInt");
    }
}
