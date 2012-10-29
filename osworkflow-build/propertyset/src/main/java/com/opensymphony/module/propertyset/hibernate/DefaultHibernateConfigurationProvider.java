/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.hibernate;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;

import java.util.Iterator;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: Mike
 * Date: Jul 26, 2003
 * Time: 5:05:55 PM
 * To change this template use Options | File Templates.
 */
public class DefaultHibernateConfigurationProvider implements HibernateConfigurationProvider {
    //~ Instance fields ////////////////////////////////////////////////////////

    private Configuration configuration;
    private HibernatePropertySetDAO propertySetDAO;
    private SessionFactory sessionFactory;

    //~ Methods ////////////////////////////////////////////////////////////////

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public HibernatePropertySetDAO getPropertySetDAO() {
        if (propertySetDAO == null) {
            propertySetDAO = new HibernatePropertySetDAOImpl(sessionFactory);
        }

        return propertySetDAO;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setupConfiguration(Map configurationProperties) {
        // loaded hibernate config
        try {
            configuration = new Configuration().addClass(PropertySetItemImpl.class);

            Iterator itr = configurationProperties.keySet().iterator();

            while (itr.hasNext()) {
                String key = (String) itr.next();

                if (key.startsWith("hibernate")) {
                    configuration.setProperty(key, (String) configurationProperties.get(key));
                }
            }

            this.sessionFactory = configuration.buildSessionFactory();
        } catch (HibernateException e) {
        }
    }
}
