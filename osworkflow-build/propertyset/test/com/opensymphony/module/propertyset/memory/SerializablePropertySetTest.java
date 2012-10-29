/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.memory;

import com.opensymphony.module.propertyset.AbstractPropertySetTest;


/**
 * User: bbulger
 * Date: May 22, 2004
 */
public class SerializablePropertySetTest extends AbstractPropertySetTest {
    //~ Methods ////////////////////////////////////////////////////////////////

    protected void setUp() throws Exception {
        super.setUp();
        ps = new SerializablePropertySet();
        ps.init(null, null);
    }
}
