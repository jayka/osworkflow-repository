/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.ofbiz;

import com.opensymphony.module.propertyset.AbstractPropertySetTest;

import java.util.HashMap;


/**
 * User: bbulger
 * Date: May 24, 2004
 */
public class OFBizPropertySetTest extends AbstractPropertySetTest {
    //~ Methods ////////////////////////////////////////////////////////////////

    protected void setUp() throws Exception {
        super.setUp();
        ps = new OFBizPropertySet();

        HashMap args = new HashMap();
        args.put("entityId", new Long(1));
        args.put("entityName", "test");
        ps.init(new HashMap(), args);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        ((OFBizPropertySet) ps).remove();
    }
}
