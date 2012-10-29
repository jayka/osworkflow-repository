/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.ejb3;

import com.opensymphony.module.propertyset.AbstractPropertySetTest;
import com.opensymphony.module.propertyset.DatabaseHelper;
import com.opensymphony.module.propertyset.PropertySetManager;

import java.util.HashMap;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 * @author Hani Suleiman
 *         Date: Nov 8, 2005
 *         Time: 6:11:09 PM
 */
public class EJBPropertySetTest extends AbstractPropertySetTest {
    //~ Static fields/initializers /////////////////////////////////////////////

    private static Properties props = new Properties();

    static {
        DatabaseHelper.createDatabase("");
        props.put("hibernate.dialect", "org.hibernate.dialect.MckoiDialect");
        props.put("javax.persistence.transactionType", "RESOURCE_LOCAL");
    }

    private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("propertyset", props);

    //~ Methods ////////////////////////////////////////////////////////////////

    public void setUp() throws Exception {
        HashMap args = new HashMap();
        args.put("entityName", "testejb3");
        args.put("entityId", new Long(3));

        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        args.put("manager", em);
        ps = PropertySetManager.getInstance("ejb3", args);
    }

    protected void tearDown() throws Exception {
        ps.remove();
    }
}
