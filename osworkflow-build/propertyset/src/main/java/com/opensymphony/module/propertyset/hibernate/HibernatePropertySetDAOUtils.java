/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.hibernate;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Mike
 * Date: Jul 26, 2003
 * Time: 5:46:58 PM
 */
public class HibernatePropertySetDAOUtils {
    //~ Methods ////////////////////////////////////////////////////////////////

    public static PropertySetItem getItem(Session session, String entityName, Long entityId, String key) throws HibernateException {
        return (PropertySetItem) session.load(PropertySetItemImpl.class, new PropertySetItemImpl(entityName, entityId.longValue(), key));
    }

    /**
     * This is the body of the getKeys() method, so that you can reuse it wrapped by your own session management.
     */
    public static List getKeysImpl(Session session, String entityName, Long entityId, String prefix, int type) throws HibernateException {
        Query query;

        if ((prefix != null) && (type > 0)) {
            query = session.getNamedQuery("all_keys_with_type_like");
            query.setString("like", prefix + '%');
            query.setInteger("type", type);
        } else if (prefix != null) {
            query = session.getNamedQuery("all_keys_like");
            query.setString("like", prefix + '%');
        } else if (type > 0) {
            query = session.getNamedQuery("all_keys_with_type");
            query.setInteger("type", type);
        } else {
            query = session.getNamedQuery("all_keys");
        }

        query.setString("entityName", entityName);
        query.setLong("entityId", entityId.longValue());

        return query.list();
    }
}
