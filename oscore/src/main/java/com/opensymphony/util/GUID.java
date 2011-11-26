/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.util;


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
import java.math.BigInteger;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


/**
 * Convenience object for generating GUIDs.
 * Uses the SHA1PRNG algorithm if available.
 * If unavailable, it leaves this up to the JRE to provide a default.
 * See http://java.sun.com/j2se/1.4.2/docs/api/java/security/SecureRandom.html for more details.
 *
 * @author <a href="mailto:salaman@teknos.com">Victor Salaman</a>
 * @version $Revision: 156 $
 */
public final class GUID {
    //~ Static fields/initializers /////////////////////////////////////////////

    private static SecureRandom rnd;

    static {
        try {
            rnd = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            rnd = new SecureRandom(); //Use default if prefered provider is unavailable
        }

        byte[] seed = rnd.generateSeed(64);
        rnd.setSeed(seed);
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public static String generateFormattedGUID() {
        //xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
        String guid = generateGUID();

        return guid.substring(0, 8) + '-' + guid.substring(8, 12) + '-' + guid.substring(12, 16) + '-' + guid.substring(16, 20) + '-' + guid.substring(20);
    }

    public static String generateGUID() {
        return new BigInteger(165, rnd).toString(36).toUpperCase();
    }
}
