/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.util;

import junit.framework.TestCase;


/**
 * Atlassian Source Code Template.
 * CVS Revision: $Revision: 153 $
 * Last CVS Commit: $Date: 2008-01-21 20:39:47 +0100 (lun, 21 gen 2008) $
 * Author of last CVS Commit: $Author: scott@atlassian.com $
 */
public class TestUrlUtils extends TestCase {
    //~ Constructors ///////////////////////////////////////////////////////////

    public TestUrlUtils(String string) {
        super(string);
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public void testHierachicalUri() {
        assertTrue(UrlUtils.verifyHierachicalURI("http://abc"));
        assertTrue(UrlUtils.verifyHierachicalURI("notes://aba"));
        assertTrue(UrlUtils.verifyHierachicalURI("abc123://aba"));
        assertTrue(UrlUtils.verifyHierachicalURI("http://amos.shop.com/amos/cc/main/catalog/pcd/11373729/ccsyn/66/_x_/New-Balance-Shoes-New-Balance-W763BL---Women's-Clearance-Shoes"));

        assertFalse(UrlUtils.verifyHierachicalURI("http://ab{c}a"));
        assertFalse(UrlUtils.verifyHierachicalURI("1otes://aba"));
    }

    public void testHierachicalUriEmptyInvalidSchemes() throws Exception {
        // ok ensure a null and empty list of invalid schems actually means all schemes are in fact valid
        assertTrue(UrlUtils.verifyHierachicalURI("http://perfectly/normal/url", new String[] {
                    
                }));
        assertTrue(UrlUtils.verifyHierachicalURI("javascript://alert('ifweallowitthenweallowit');", new String[] {
                    
                }));

        assertTrue(UrlUtils.verifyHierachicalURI("http://perfectly/normal/url", null));
        assertTrue(UrlUtils.verifyHierachicalURI("javascript://alert('ifweallowitthenweallowit');", null));
    }

    public void testHierachicalUriInvalidSchemes() throws Exception {
        // dont allow some schemes explicitly
        assertFalse(UrlUtils.verifyHierachicalURI("http://perfectly/normal/url", new String[] {
                    "http"
                }));
        assertFalse(UrlUtils.verifyHierachicalURI("ftp://perfectly/normal/url", new String[] {
                    "http", "ftp"
                }));
    }

    public void testHierachicalUriJavaScriptURIS() throws Exception {
        String[] javascriptURIS = {
            "javascript://alert('XSS')", "javascript://**/;alert('XSS')",
        };

        // check that by default the java script URIs are invalid
        for (int i = 0; i < javascriptURIS.length; i++) {
            String uri = javascriptURIS[i];
            assertFalse(UrlUtils.verifyHierachicalURI(uri));
        }

        // now make an exception and allow javascript in 
        for (int i = 0; i < javascriptURIS.length; i++) {
            String uri = javascriptURIS[i];
            assertTrue(UrlUtils.verifyHierachicalURI(uri, new String[0]));
            assertTrue(UrlUtils.verifyHierachicalURI(uri, null));
            assertTrue(UrlUtils.verifyHierachicalURI(uri, new String[] {"http"}));
        }

        // an edge case we always want to be invalid is javascript:xxxx.  because its
        // not got :// the basic validation will fail it but we always want it to be false
        /// so lets test just in case
        assertFalse(UrlUtils.verifyHierachicalURI("javascript:alert('XSS')"));
        assertFalse(UrlUtils.verifyHierachicalURI("javascript:alert('XSS')://"));
    }
}
