/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.database;

import com.opensymphony.module.propertyset.AbstractPropertySetTest;
import com.opensymphony.module.propertyset.DatabaseHelper;
import com.opensymphony.module.propertyset.PropertySetManager;

import java.util.HashMap;


/**
 * This test case attempts to verify the database implementation.  This is also a good resource for beginners
 * to PropertySet.  This leverages straight JDBC as the persistence mechanism which requires
 * fewer dependencies then hibernate..
 *
 * @author Eric Pugh (epugh@upstate.com)
 */
public class JDBCPropertySetTest extends AbstractPropertySetTest {
    //~ Methods ////////////////////////////////////////////////////////////////

    public void setUp() throws Exception {
        super.setUp();
        DatabaseHelper.createDatabase(getClass().getResource("/mckoi.sql"));

        HashMap args = new HashMap();
        args.put("globalKey", "test");
        ps = PropertySetManager.getInstance("jdbc", args);
    }
}
