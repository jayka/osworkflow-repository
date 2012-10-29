/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.ejb;

import java.rmi.RemoteException;

/* ====================================================================
 * The OpenSymphony Software License, Version 1.1
 *
 * (this license is derived and fully compatible with the Apache Software
 * License - see http://www.apache.org/LICENSE.txt)
 *
 * Copyright (c) 2001 The OpenSymphony Group. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        OpenSymphony Group (http://www.opensymphony.com/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "OpenSymphony" and "The OpenSymphony Group"
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact license@opensymphony.com .
 *
 * 5. Products derived from this software may not be called "OpenSymphony"
 *    or "OSCore", nor may "OpenSymphony" or "OSCore" appear in their
 *    name, without prior written permission of the OpenSymphony Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 */
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
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 169 $
 */
public abstract class EntityAdapter extends AbstractEntityAdapter implements EntityBean {
    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Required to implement EntityBean. Sets the EntityContext.
     * Also, attempts to detemine the <code>sequenceName</code>.
     */
    public void setEntityContext(EntityContext context) throws EJBException, RemoteException {
        this.setContext(context);
    }

    /**
     * Required to implement EntityBean. Not implemented.
     */
    public void ejbActivate() throws EJBException, RemoteException {
    }

    /**
     * Required to implement EntityBean. Not implemented.
     */
    public void ejbLoad() throws EJBException, RemoteException {
    }

    /**
     * Required to implement EntityBean. Not implemented.
     */
    public void ejbPassivate() throws EJBException, RemoteException {
    }

    /**
     * Required to implement EntityBean. Not implemented.
     */
    public void ejbRemove() throws RemoveException, EJBException, RemoteException {
    }

    /**
     * Required to implement EntityBean. Not implemented.
     */
    public void ejbStore() throws EJBException, RemoteException {
    }

    /**
     * Required to implement EntityBean. Sets the EntityContext to null.
     */
    public void unsetEntityContext() throws EJBException, RemoteException {
        context = null;
    }

    /**
     * Return EntityContext. To be used by classes extending this.
     */
    protected EntityContext getEntityContext() {
        return context;
    }
}
