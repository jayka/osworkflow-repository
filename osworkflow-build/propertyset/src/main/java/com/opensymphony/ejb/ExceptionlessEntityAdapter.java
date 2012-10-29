/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.ejb;

import javax.ejb.*;


/**
 * <p>
 *   Abstract base adapter class to be extended by EJB EntityBeans. <br>
 *   Contains default implementations for all require methods to implement EntityBean.
 * </p>
 * <p>
 *   To create an implementation of an Entity, extend this class and add appropriate ejbCreate(),
 *   ejbPostCreate() and getter/setter methods.
 * </p>
 * <p>
 *   If an auto-generated primary key (<code>long</code>/<code>int</code>) is to be used,
 * </p>
 * <p>
 *  <b>This is an identical class in terms of functionality to EntityAdapter, it just does
 * not throw as many exceptions. This sometimes works much nicer with other tools such as
 * XDoclet.</b>
 * </p>
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @author <a href="mailto:mike@atlassian.com">Mike Cannon-Brookes</a>
 * @version $Revision: 169 $
 */
public abstract class ExceptionlessEntityAdapter extends AbstractEntityAdapter implements EntityBean {
    //~ Methods ////////////////////////////////////////////////////////////////

    /**
    * Required to implement EntityBean. Sets the EntityContext.
    * Also, attempts to detemine the <code>sequenceName</code>.
    */
    public void setEntityContext(EntityContext context) {
        this.setContext(context);
    }

    /**
    * Required to implement EntityBean. Not implemented.
    */
    public void ejbActivate() {
    }

    /**
    * Required to implement EntityBean. Not implemented.
    */
    public void ejbLoad() {
    }

    /**
    * Required to implement EntityBean. Not implemented.
    */
    public void ejbPassivate() {
    }

    /**
    * Required to implement EntityBean. Not implemented.
    */
    public void ejbRemove() throws RemoveException {
    }

    /**
    * Required to implement EntityBean. Not implemented.
    */
    public void ejbStore() {
    }

    /**
    * Required to implement EntityBean. Sets the EntityContext to null.
    */
    public void unsetEntityContext() {
        context = null;
    }

    /**
    * Return EntityContext. To be used by classes extending this.
    */
    protected EntityContext getEntityContext() {
        return context;
    }
}
