/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.verifiers;

import java.io.Serializable;


/**
 *
 * @author <a href="mailto:hani@fate.demon.co.uk">Hani Suleiman</a>
 * @version $Revision: 144 $
 */
public interface PropertyVerifier extends Serializable {
    //~ Methods ////////////////////////////////////////////////////////////////

    public void verify(Object value) throws VerifyException;
}
