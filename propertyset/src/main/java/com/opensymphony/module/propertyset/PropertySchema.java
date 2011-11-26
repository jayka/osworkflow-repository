/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset;

import com.opensymphony.module.propertyset.verifiers.PropertyVerifier;
import com.opensymphony.module.propertyset.verifiers.VerifyException;

import java.io.Serializable;

import java.util.*;


/**
 * Describes the meta data for a given property.
 * The meta data for a property includes its type as well as
 * any verifiers that constrain it.
 *
 * todo: add multiplicity?
 *
 * @author <a href="mailto:hani@fate.demon.co.uk">Hani Suleiman</a>
 * @version $Revision: 144 $
 */
public class PropertySchema implements Serializable {
    //~ Instance fields ////////////////////////////////////////////////////////

    private Collection verifiers;
    private String name;
    private int type;

    //~ Constructors ///////////////////////////////////////////////////////////

    public PropertySchema() {
        this(null);
    }

    public PropertySchema(String name) {
        super();
        this.name = name;
        verifiers = new HashSet();
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public void setPropertyName(String s) {
        name = s;
    }

    public String getPropertyName() {
        return name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    /**
     * Returns unmodifiable List of verifiers.
     */
    public Collection getVerifiers() {
        return Collections.unmodifiableCollection(verifiers);
    }

    public boolean addVerifier(PropertyVerifier pv) {
        return verifiers.add(pv);
    }

    public boolean removeVerifier(PropertyVerifier pv) {
        return verifiers.remove(pv);
    }

    /**
     * Validate a given value against all verifiers.
     * Default behaviour is to AND all verifiers.
     */
    public void validate(Object value) throws PropertyException {
        Iterator i = verifiers.iterator();

        while (i.hasNext()) {
            PropertyVerifier pv = (PropertyVerifier) i.next();

            //Hmm, do we need a try/catch?
            try {
                pv.verify(value);
            } catch (VerifyException ex) {
                throw new IllegalPropertyException(ex.getMessage());
            }
        }
    }
}
