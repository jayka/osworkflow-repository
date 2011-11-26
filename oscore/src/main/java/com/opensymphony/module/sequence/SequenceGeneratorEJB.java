/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.sequence;

import java.util.HashMap;

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
import java.util.Map;

import javax.ejb.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.rmi.PortableRemoteObject;


/**
 * SequenceGenerator Session EJB implementation.
 *
 * <p>Utilizes the HIGH/LOW OID strategy as described at
 * <a href="http://www.ambysoft.com/mappingObjects.html">
 *  http://www.ambysoft.com/mappingObjects.html </a>.</p>
 *
 * @ejb.bean
 *  type="Stateless"
 *  name="SequenceGenerator"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.ejb-ref
 *  ejb-name="Sequence"
 *  view-type="local"
 *
 * @ejb.transaction type="Supports"
 * @ejb.permission unchecked="true"
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @author <a href="mailto:hani@formicary.net">Hani Suleiman</a>
 * @version $Revision: 130 $
 */
public class SequenceGeneratorEJB implements SessionBean {
    //~ Instance fields ////////////////////////////////////////////////////////

    /**
     * Map of sequenceNames to appropriate MemorySequence's.
     */
    private Map sequenceStore;

    /**
     * Home for Sequence Entity
     */
    private SequenceLocalHome sequenceHome;
    private SessionContext context;

    /**
     * Increment size (size of HIGH block).
     */
    private int increment;

    /**
     * Number of times to attempt to retry.
     */
    private int retry;

    /**
     * Time (in milliseconds) to pause between retries.
     */
    private int retryPause;

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * @ejb.interface-method
     * @ejb.transaction type="Required"
     */
    public long getCount(String sequenceName) {
        try {
            // Allow a sequence for null.
            if (sequenceName == null) {
                sequenceName = "";
            }

            // Get appropriate memorySequence
            MemorySequence memorySequence = getMemorySequence(sequenceName);

            // If LOW count has cycled through increment, get next HIGH count
            if ((memorySequence.last % increment) == 0) {
                getNextHighCount(memorySequence);
            }

            // Increment LOW count and return value.
            return memorySequence.last++;
        } catch (Exception e) {
            // Kick up a fuss about anything that went wrong.
            throw new EJBException(e);
        }
    }

    /**
     * Session context set. Lookup Home interface for Sequence Entity
     */
    public void setSessionContext(SessionContext context) {
        this.context = context;

        try {
            InitialContext ctx = new InitialContext();
            sequenceHome = (SequenceLocalHome) PortableRemoteObject.narrow(ctx.lookup(SequenceLocalHome.COMP_NAME), SequenceLocalHome.class);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }

    public void ejbActivate() {
    }

    /**
     * Session created. Initialize.
     * @ejb.create-method
     */
    public void ejbCreate() throws CreateException {
        // Get values from env
        increment = getIntEnv("increment", 10);
        retry = getIntEnv("retry", 5);
        retryPause = getIntEnv("retryPause", 100);

        // Initialise sequenceStore
        sequenceStore = new HashMap();
    }

    public void ejbPassivate() {
    }

    public void ejbRemove() {
    }

    /**
     * Get an env-entry value that is an int.
     *
     * @param envName Name of env-entry to retrieve
     * @param defaultValue Value it should have if it is not found.
     */
    private int getIntEnv(String envName, int defaultValue) {
        try {
            return ((Integer) PortableRemoteObject.narrow(new InitialContext().lookup(envName), Integer.class)).intValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Get the MemorySequence for a given sequenceName. Try to get from
     * sequenceStore first - if that fails, find or create appropriate
     * entity and add to sequenceStore.
     *
     * @param sequenceName Name of the sequence to lookup
     * @exception javax.ejb.CreateException thrown if cannot create new sequence
     */
    private MemorySequence getMemorySequence(String sequenceName) throws CreateException {
        // Get the MemorySequence for the given sequenceName from the sequenceStore
        MemorySequence memorySequence = (MemorySequence) sequenceStore.get(sequenceName);

        // If the MemorySequence was not already in the sequenceStore
        if (memorySequence == null) {
            // Create a new MemorySequence
            memorySequence = new MemorySequence();

            try {
                // Lookup 'real' sequence and assign to memorySequence
                memorySequence.sequence = sequenceHome.findByPrimaryKey(sequenceName);
            } catch (FinderException e) {
                // ... if 'real' sequence not found, create it.
                memorySequence.sequence = sequenceHome.create(sequenceName);
            }

            // Put memorySequence into sequenceStore
            sequenceStore.put(sequenceName, memorySequence);
        }

        return memorySequence;
    }

    /**
     * Increment the <code>last</code> field of a memoryStore to the next
     * HIGH count for the actual <code>Sequence</code>. Seeing as this is
     * quite an important operation and other Entities are dependant on it
     * working, if there is a failure it will make a few retries.
     *
     * @param memorySequence MemorySequence to obtain next HIGH count for
     */
    private void getNextHighCount(MemorySequence memorySequence) {
        // Start looping and keep track of how many retryAttempts.
        for (int retryAttempt = 0; true; retryAttempt++) {
            try {
                // Attempt to get next HIGH count for memorySequence
                memorySequence.last = memorySequence.sequence.getCount(increment);

                // Success! - break out of loop
                break;
            } catch (Exception tre) {
                // Unsuccessful - worry not...
                if (retryAttempt < retry) {
                    // If we have still got another retry, pause then continue loop
                    try {
                        Thread.sleep(retryPause);
                    } catch (InterruptedException ie) {
                    }

                    continue;
                } else {
                    // If we're out of retries, give up and throw EJBException
                    throw new EJBException(tre);
                }
            }
        }
    }

    //~ Inner Classes //////////////////////////////////////////////////////////

    /**
     * This classrepresents an in-memory sequence. (The LOW bit of
     * a HIGH/LOW counter). It has a reference to the HIGH sequence
     * (that stored in the database), and a LOW in-memory count.
     */
    private class MemorySequence {
        SequenceLocal sequence;
        long last;
    }
}
