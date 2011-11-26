/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.util;

import junit.framework.TestCase;

import java.util.HashSet;


/**
 * Tests the GUID class.
 *
 * @author $Author: hani $
 * @version $Revision: 32 $
 */
public class GUIDTest extends TestCase {
    //~ Methods ////////////////////////////////////////////////////////////////

    public void testFormattedGUID() {
        String guid = GUID.generateFormattedGUID();
        assertEquals(36, guid.length());

        // technically, this test COULD fail, but what the hell, it's a good way
        // to ensure the randomness is pretty tight
        long start = System.currentTimeMillis();
        HashSet guids = new HashSet(100);

        for (int i = 0; i < 100; i++) {
            if (!guids.add(GUID.generateFormattedGUID())) {
                fail("GUID already added");
            }

            //ensure the GUIDs are of length 36
        }

        long end = System.currentTimeMillis();

        System.out.println("Took " + (end - start) + "ms to generate 100 formatted GUIDs");
    }

    public void testGUID() {
        String guid = GUID.generateGUID();
        assertEquals(32, guid.length());

        // technically, this test COULD fail, but what the hell, it's a good way
        // to ensure the randomness is pretty tight
        long start = System.currentTimeMillis();
        HashSet guids = new HashSet(100);

        for (int i = 0; i < 100; i++) {
            if (!guids.add(GUID.generateGUID())) {
                fail("GUID already added");
            }
        }

        long end = System.currentTimeMillis();

        System.out.println("Took " + (end - start) + "ms to generate 100 GUIDs");
    }
}
