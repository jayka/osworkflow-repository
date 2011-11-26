/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.hibernate;

import com.opensymphony.module.propertyset.PropertyException;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

import java.util.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 169 $
 */
public class HibernatePropertySetDAOImpl implements HibernatePropertySetDAO {
    //~ Instance fields ////////////////////////////////////////////////////////

    private SessionFactory sessionFactory;

    //~ Constructors ///////////////////////////////////////////////////////////

    public HibernatePropertySetDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public void setImpl(PropertySetItem item, boolean isUpdate) {
        Session session = null;

        try {
            session = this.sessionFactory.openSession();

            if (isUpdate) {
                session.update(item);
            } else {
                session.save(item);
            }

            session.flush();
        } catch (HibernateException he) {
            throw new PropertyException("Could not save key '" + item.getKey() + "':" + he.getMessage());
        } finally {
            try {
                if (session != null) {
                    if (!session.connection().getAutoCommit()) {
                        session.connection().commit();
                    }

                    session.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public Collection getKeys(String entityName, Long entityId, String prefix, int type) {
        Session session = null;
        List list = null;

        try {
            session = this.sessionFactory.openSession();
            list = HibernatePropertySetDAOUtils.getKeysImpl(session, entityName, entityId, prefix, type);
        } catch (HibernateException e) {
            list = Collections.EMPTY_LIST;
        } finally {
            try {
                if (session != null) {
                    session.flush();
                    session.close();
                }
            } catch (Exception e) {
            }
        }

        return list;
    }

    public PropertySetItem create(String entityName, long entityId, String key) {
        return new PropertySetItemImpl(entityName, entityId, key);
    }

    public PropertySetItem findByKey(String entityName, Long entityId, String key) {
        Session session = null;
        PropertySetItem item = null;

        try {
            session = this.sessionFactory.openSession();
            item = HibernatePropertySetDAOUtils.getItem(session, entityName, entityId, key);
            session.flush();
        } catch (HibernateException e) {
            return null;
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
            } catch (Exception e) {
            }
        }

        return item;
    }

    public void remove(String entityName, Long entityId) {
        Session session = null;

        try {
            session = this.sessionFactory.openSession();

            //hani: todo this needs to be optimised rather badly, but I have no idea how
            Collection keys = getKeys(entityName, entityId, null, 0);
            Iterator iter = keys.iterator();

            while (iter.hasNext()) {
                String key = (String) iter.next();
                session.delete(HibernatePropertySetDAOUtils.getItem(session, entityName, entityId, key));
            }

            session.flush();
        } catch (HibernateException e) {
            throw new PropertyException("Could not remove all keys: " + e.getMessage());
        } finally {
            try {
                if (session != null) {
                    if (!session.connection().getAutoCommit()) {
                        session.connection().commit();
                    }

                    session.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public void remove(String entityName, Long entityId, String key) {
        Session session = null;

        try {
            session = this.sessionFactory.openSession();
            session.delete(HibernatePropertySetDAOUtils.getItem(session, entityName, entityId, key));
            session.flush();
        } catch (HibernateException e) {
            throw new PropertyException("Could not remove key '" + key + "': " + e.getMessage());
        } finally {
            try {
                if (session != null) {
                    if (!session.connection().getAutoCommit()) {
                        session.connection().commit();
                    }

                    session.close();
                }
            } catch (Exception e) {
            }
        }
    }
}
