/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.util;

import com.opensymphony.util.DateUtil;

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
import junit.framework.TestCase;

import java.text.ParseException;

import java.util.*;


/**
 * JUnit test case for DateUtil
 *
 * @author @author <a href="mailto:snowwolf@wabunoh.com">SnowWolf Wagner</a>
 * @version $Revision: 20 $
 */
public class DateUtilsTest extends TestCase {
    //~ Constructors ///////////////////////////////////////////////////////////

    public DateUtilsTest(String name) {
        super(name);
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public void testDateToISO() {
    }

    public void testISOtoDate() {
        try {
            DateUtil.isoToDate("2002-10-03T00:00:00,001-0500", true);
            DateUtil.isoToDate("20021003T000000001-0500");
            DateUtil.isoToDate("20021003T000000001-0500", false);
            DateUtil.isoToSQLDate("2002-10-03T00:00:00,001-0500", true);
            DateUtil.isoToSQLDate("20021003T000000001-0500");
            DateUtil.isoToSQLDate("20021003T000000001-0500", false);
            DateUtil.isoToTime("12:53:02,000-0500", true);
            DateUtil.isoToTime("125302000-0500", false);
            DateUtil.isoToTime("125302000-0500");
            DateUtil.isoToTimestamp("2002-10-03T00:00:00,001-0500", true);
            DateUtil.isoToTimestamp("20021003T000000001-0500");
            DateUtil.isoToTimestamp("20021003T000000001-0500", false);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    public void testJulian() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, 3);
        c.set(Calendar.DAY_OF_MONTH, 10);
        c.set(Calendar.YEAR, 2002);

        assertEquals(2452343.5f, DateUtil.toJulian(c), 0.01f);
    }

    /**
     * run test using the new julian date methods
     *
     */
    public void testNewJulian() {
        try {
            Date date = DateUtil.isoToDate("2002-10-03T00:00:00,001-0500", true);

            assertEquals(731125, DateUtil.millisToJulianDay(date.getTime()));

            assertEquals(DateUtil.julianDayToDate(731125), date);

            //There are 216 days between March 3, 2002 and October 3, 2002
            assertEquals(216, DateUtil.daysBetween("2002-03-01", "2002-10-03", true));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
}
