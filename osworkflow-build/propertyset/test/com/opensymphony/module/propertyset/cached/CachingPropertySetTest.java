/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.cached;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.module.propertyset.memory.MemoryPropertySet;
import com.opensymphony.module.propertyset.memory.SerializablePropertySetTest;

import java.util.HashMap;


/**
 * User: bbulger
 * Date: May 24, 2004
 */
public class CachingPropertySetTest extends SerializablePropertySetTest {
    //~ Methods ////////////////////////////////////////////////////////////////

    protected void setUp() throws Exception {
        PropertySet memoryPropertySet = new MemoryPropertySet();
        memoryPropertySet.init(null, null);

        HashMap args = new HashMap();
        args.put("PropertySet", memoryPropertySet);
        ps = new CachingPropertySet();
        ps.init(new HashMap(), args);
    }
}
