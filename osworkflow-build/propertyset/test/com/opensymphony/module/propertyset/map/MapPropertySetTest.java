/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.map;

import com.opensymphony.module.propertyset.AbstractPropertySetTest;

import java.util.HashMap;


/**
 * User: bbulger
 * Date: May 22, 2004
 */
public class MapPropertySetTest extends AbstractPropertySetTest {
    //~ Methods ////////////////////////////////////////////////////////////////

    public void testGetMapSetMap() {
        HashMap map = new HashMap();
        map.put("key1", "value1");
        ((MapPropertySet) ps).setMap(map);
        assertEquals(map, ((MapPropertySet) ps).getMap());
    }

    protected void setUp() throws Exception {
        super.setUp();

        HashMap args = new HashMap();
        ps = new MapPropertySet();
        ps.init(null, args);
    }
}
