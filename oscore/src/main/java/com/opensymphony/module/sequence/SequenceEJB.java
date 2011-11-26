/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.sequence;


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
 * Sequence Entity EJB implementation.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @author <a href="mailto:hani@formicary.net">Hani Suleiman</a>
 * @version $Revision: 127 $
 *
 * @ejb.bean
 *  type="CMP"
 *  view-type="local"
 *  name="Sequence"
 *  reentrant="False"
 *  primkey-field="name"
 *  transaction-type="Container"
 *  schema="Sequence"
 *
 * @ejb.finder
 *                 signature="Collection findAll()"
 *    query="SELECT DISTINCT OBJECT(o) FROM Sequence o"
 *                 result-type-mapping="Local"
 *
 * @ejb.pk class="java.lang.String" extends="java.lang.Object"
 * @ejb.interface class="com.opensymphony.module.sequence.Sequence" extends="javax.ejb.EJBLocalObject"
 * @ejb.home class="com.opensymphony.module.sequence.SequenceHome" extends="javax.ejb.EJBLocalHome"
 *
 * @ejb.persistence table-name="OS_SEQUENCE"
 * @ejb.transaction type="Supports"
 * @ejb.permission unchecked="true"
 */
public abstract class SequenceEJB implements EntityBean {
    //~ Instance fields ////////////////////////////////////////////////////////

    private EntityContext context;

    //~ Methods ////////////////////////////////////////////////////////////////

    public abstract void setActualCount(long count);

    /**
     * @ejb.persistence column-name="VALUE"
     * @ejb.interface-method
     */
    public abstract long getActualCount();

    public abstract void setName(String name);

    /**
     * @ejb.pk-field
     * @ejb.interface-method
     * @ejb.persistence column-name="NAME"
     */
    public abstract String getName();

    /**
     * @ejb.interface-method
     */
    public long getCount(int increment) {
        long temp = getActualCount();
        temp += increment;
        setActualCount(temp);

        return temp;
    }

    public void setEntityContext(EntityContext context) {
        this.context = context;
    }

    public void ejbActivate() {
    }

    /**
     * @ejb.create-method
     */
    public String ejbCreate(String name) throws CreateException {
        setName(name);

        return null;
    }

    public void ejbLoad() {
    }

    public void ejbPassivate() {
    }

    public void ejbPostCreate(String name) throws CreateException {
    }

    public void ejbRemove() throws RemoveException {
    }

    public void ejbStore() {
    }

    public void unsetEntityContext() {
        context = null;
    }
}
