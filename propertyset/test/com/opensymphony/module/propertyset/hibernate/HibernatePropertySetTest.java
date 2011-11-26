/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.hibernate;

import com.opensymphony.module.propertyset.AbstractPropertySetTest;
import com.opensymphony.module.propertyset.DatabaseHelper;
import com.opensymphony.module.propertyset.PropertySetManager;

import net.sf.hibernate.cfg.Configuration;

import java.util.HashMap;


/**
 * This test case tests a propertyset backed by Hibernate
 *
 * @author Eric Pugh (epugh@upstate.com)
 */
public class HibernatePropertySetTest extends AbstractPropertySetTest {
    //~ Methods ////////////////////////////////////////////////////////////////

    public void setUp() throws Exception {
        super.setUp();
        DatabaseHelper.createDatabase("");

        Configuration config = DatabaseHelper.createHibernateConfiguration();
        DefaultHibernateConfigurationProvider configurationProvider = new DefaultHibernateConfigurationProvider();
        configurationProvider.setConfiguration(config);
        configurationProvider.setSessionFactory(config.buildSessionFactory());

        HashMap args = new HashMap();
        args.put("entityName", "testHibernate");
        args.put("entityId", new Long(3));
        args.put("configurationProvider", configurationProvider);
        ps = PropertySetManager.getInstance("hibernate", args);
    }
}
