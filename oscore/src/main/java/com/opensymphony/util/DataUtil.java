/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
/*
 * Created by IntelliJ IDEA.
 * User: plightbo
 * Date: Apr 23, 2002
 * Time: 9:32:46 AM
 */
package com.opensymphony.util;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 11 $
 */
public class DataUtil {
    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Returns the primitve representation of the object, false if the object is null
     *
     * @param b - the object representation
     * @return the primitive representation
     */
    public static boolean getBoolean(Boolean b) {
        if (b == null) {
            return false;
        } else {
            return b.booleanValue();
        }
    }

    /**
     * Returns the primitve representation of the object, 0 if the object is null
     *
     * @param b - the object representation
     * @return the primitive representation
     */
    public static byte getByte(Byte b) {
        if (b == null) {
            return 0;
        } else {
            return b.byteValue();
        }
    }

    /**
     * Returns the primitve representation of the object, 0 if the object is null
     *
     * @param d - the object representation
     * @return the primitive representation
     */
    public static double getDouble(Double d) {
        if (d == null) {
            return 0;
        } else {
            return d.doubleValue();
        }
    }

    /**
     * Returns the primitve representation of the object, 0 if the object is null
     *
     * @param f - the object representation
     * @return the primitive representation
     */
    public static float getFloat(Float f) {
        if (f == null) {
            return 0;
        } else {
            return f.floatValue();
        }
    }

    /**
     * Returns the primitve representation of the object, 0 if the object is null
     *
     * @param i - the object representation
     * @return the primitive representation
     */
    public static int getInt(Integer i) {
        if (i == null) {
            return 0;
        } else {
            return i.intValue();
        }
    }

    /**
     * Returns the primitve representation of the object, 0 if the object is null
     *
     * @param l - the object representation
     * @return the primitive representation
     */
    public static long getLong(Long l) {
        if (l == null) {
            return 0;
        } else {
            return l.longValue();
        }
    }
}
