/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.hibernate;

import java.util.Date;


/**
 * Interface to be implemented by the concrete class that hibernate persists.
 * @author $author$
 * @version $Revision: 169 $
 */
public interface PropertySetItem {
    //~ Methods ////////////////////////////////////////////////////////////////

    void setBooleanVal(boolean booleanVal);

    boolean getBooleanVal();

    void setDateVal(Date dateVal);

    Date getDateVal();

    void setDoubleVal(double doubleVal);

    double getDoubleVal();

    void setEntityId(long entityId);

    long getEntityId();

    void setEntityName(String entityName);

    String getEntityName();

    void setIntVal(int intVal);

    int getIntVal();

    void setKey(String key);

    String getKey();

    void setLongVal(long longVal);

    long getLongVal();

    void setStringVal(String stringVal);

    String getStringVal();

    void setType(int type);

    int getType();
}
