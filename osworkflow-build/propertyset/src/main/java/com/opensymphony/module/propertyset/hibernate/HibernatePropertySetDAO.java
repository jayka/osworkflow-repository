/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.hibernate;

import java.util.Collection;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 169 $
 */
public interface HibernatePropertySetDAO {
    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Save the implementation of a PropertySetItem.
     *
     * @param item
     * @param isUpdate Boolean indicating whether or not this item already exists
     */
    void setImpl(PropertySetItem item, boolean isUpdate);

    Collection getKeys(String entityName, Long entityId, String prefix, int type);

    PropertySetItem create(String entityName, long entityId, String key);

    PropertySetItem findByKey(String entityName, Long entityId, String key);

    void remove(String entityName, Long entityId, String key);

    void remove(String entityName, Long entityId);
}
