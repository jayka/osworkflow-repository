/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.random;


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
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import java.util.Enumeration;
import java.util.Properties;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 131 $
 */
public class Rijndael_Properties // implicit no-argument constructor
 {
    //~ Static fields/initializers /////////////////////////////////////////////

    // Constants and variables with relevant static code
    //...........................................................................
    static final boolean GLOBAL_DEBUG = false;
    static final String ALGORITHM = "Rijndael";
    static final double VERSION = 0.1;
    static final String FULL_NAME = ALGORITHM + " ver. " + VERSION;
    static final String NAME = "Rijndael_Properties";
    static final Properties properties = new Properties();

    /** Default properties in case .properties file was not found. */
    private static final String[][] DEFAULT_PROPERTIES = {
        {"Trace.Rijndael_Algorithm", "true"},
        {"Debug.Level.*", "1"},
        {"Debug.Level.Rijndael_Algorithm", "9"},
    };

    static {
        if (GLOBAL_DEBUG) {
            System.err.println(">>> " + NAME + ": Looking for " + ALGORITHM + " properties");
        }

        String it = ALGORITHM + ".properties";
        InputStream is = Rijndael_Properties.class.getResourceAsStream(it);
        boolean ok = is != null;

        if (ok) {
            try {
                properties.load(is);
                is.close();

                if (GLOBAL_DEBUG) {
                    System.err.println(">>> " + NAME + ": Properties file loaded OK...");
                }
            } catch (Exception x) {
                ok = false;
            }
        }

        if (!ok) {
            if (GLOBAL_DEBUG) {
                System.err.println(">>> " + NAME + ": WARNING: Unable to load \"" + it + "\" from CLASSPATH.");
            }

            if (GLOBAL_DEBUG) {
                System.err.println(">>> " + NAME + ":          Will use default values instead...");
            }

            int n = DEFAULT_PROPERTIES.length;

            for (int i = 0; i < n; i++) {
                properties.put(DEFAULT_PROPERTIES[i][0], DEFAULT_PROPERTIES[i][1]);
            }

            if (GLOBAL_DEBUG) {
                System.err.println(">>> " + NAME + ": Default properties now set...");
            }
        }
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    // Properties methods (excluding load and save, which are deliberately not
    // supported).
    //...........................................................................

    /** Get the value of a property for this algorithm. */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Get the value of a property for this algorithm, or return
     * <i>value</i> if the property was not set.
     */
    public static String getProperty(String key, String value) {
        return properties.getProperty(key, value);
    }

    /** List algorithm properties to the PrintStream <i>out</i>. */
    public static void list(PrintStream out) {
        list(new PrintWriter(out, true));
    }

    /** List algorithm properties to the PrintWriter <i>out</i>. */
    public static void list(PrintWriter out) {
        out.println("#");
        out.println("# ----- Begin " + ALGORITHM + " properties -----");
        out.println("#");

        String key;
        String value;
        Enumeration e = properties.propertyNames();

        while (e.hasMoreElements()) {
            key = (String) e.nextElement();
            value = getProperty(key);
            out.println(key + " = " + value);
        }

        out.println("#");
        out.println("# ----- End " + ALGORITHM + " properties -----");
    }

    //    public synchronized void load(InputStream in) throws IOException {}
    public static Enumeration propertyNames() {
        return properties.propertyNames();
    }

    /**
     * Return the debug level for a given class.<p>
     *
     * User indicates this by setting the numeric property with key
     * "<code>Debug.Level.<i>label</i></code>".<p>
     *
     * If this property is not set, "<code>Debug.Level.*</code>" is looked up
     * next. If neither property is set, or if the first property found is
     * not a valid decimal integer, then this method returns 0.
     *
     * @param label  The name of a class.
     * @return  The required debugging level for the designated class.
     */
    static int getLevel(String label) {
        String s = getProperty("Debug.Level." + label);

        if (s == null) {
            s = getProperty("Debug.Level.*");

            if (s == null) {
                return 0;
            }
        }

        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Return the PrintWriter to which tracing and debugging output is to
     * be sent.<p>
     *
     * User indicates this by setting the property with key <code>Output</code>
     * to the literal <code>out</code> or <code>err</code>.<p>
     *
     * By default or if the set value is not allowed, <code>System.err</code>
     * will be used.
     */
    static PrintWriter getOutput() {
        PrintWriter pw;
        String name = getProperty("Output");

        if ((name != null) && name.equals("out")) {
            pw = new PrintWriter(System.out, true);
        } else {
            pw = new PrintWriter(System.err, true);
        }

        return pw;
    }

    //    public void save (OutputStream os, String comment) {}
    // Developer support: Tracing and debugging enquiry methods (package-private)
    //...........................................................................

    /**
     * Return true if tracing is requested for a given class.<p>
     *
     * User indicates this by setting the tracing <code>boolean</code>
     * property for <i>label</i> in the <code>(algorithm).properties</code>
     * file. The property's key is "<code>Trace.<i>label</i></code>".<p>
     *
     * @param label  The name of a class.
     * @return True iff a boolean true value is set for a property with
     *      the key <code>Trace.<i>label</i></code>.
     */
    static boolean isTraceable(String label) {
        String s = getProperty("Trace." + label);

        if (s == null) {
            return false;
        }

        return new Boolean(s).booleanValue();
    }
}
