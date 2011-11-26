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
import java.sql.Time;
import java.sql.Timestamp;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


/**
 * This class is used to convert Dates to ISO 8601 formatted dates and back.
 *
 * This class is an implementation of the ISO 8601 International Date Format standard.
 * The ISO 8601 helps to alleviate the following problem:
 *
 * <p>Ever been to a webpage to see that the time-sensitive information you are interested
 * in is dated 03/05/01?</p>
 * <p>Is this date the 3rd of May 2001 or the 5th of March 2001 or the 1st of May 2003 and
 * does the 01 refer to 2001 in the first two cases?</p>
 * <p>In order to make the right choice, you must ask yourself some questions :</p>
 * <ul>
 *                 <li>Where is the website you are visiting based?</li>
 *                 <li>Is the website in a country which supports the U.S.A. style date format
 *         (mm/dd/yy) or the European style (dd/mm/yy)?</li>
 *                 <li>Which countries do officially use the U.S.A. style date format? Is there an official list?</li>
 *                 <li>Is the page author :
 *                         <ul style="square">
 *                                <li>American?</li>
 *                                <li>European?</li>
 *                                <li>Japanese?</li>
 *                                <li>someone who thinks or wishes he/she was one of these nationalities?</li>
 *                                <li>one of these nationalities living in a foreign country?</li>
 *                                <li>someone using a particular format because they think it is a world standard?</li>
 *                                <li>confused, and has got the numeric fields in an order different to what they intended?</li>
 *                         </ul>
 *                 </li>
 * </ul>
 * <b>Which one is it?</b> It could be very important to you.
 *
 * <p>The Internet is a truly International method of communicating - there are no political or cultural
 * boundaries drawn on the www page you call up - the page could have been stored in the Smithsonian
 * Institute or on a small server in a basement in Ulan Bator, Mongolia. Often, you have no way of telling.
 * So, if anyone in the world can read your page, why not ensure that any date references on that page can
 * be read correctly and unambiguously by that person, by using the ISO 8601:1988 International Date Format?</p>
 *
 * <p>The basic format is: <b><i>"CCYYMMDDThhmmsssss&plusmn;nnn"</i></b></p>
 *
 * <table border="1"><tr><th colspan="2">Characters used in place of digits or signs</th></tr>
 * <tr><td nowrap="nowrap">&nbsp;[Y]&nbsp;</td><td>&nbsp;represents a digit used in the time element <b>year</b></td></tr>
 * <tr><td>&nbsp;[M]&nbsp;</td><td>&nbsp;represents a digit used in the time element <b>month</b></td></tr>
 * <tr><td>&nbsp;[D]&nbsp;</td><td>&nbsp;represents a digit used in the time element <b>day</b></td></tr>
 * <tr><td>&nbsp;[T]&nbsp;</td><td>&nbsp;place holder denoting time</td></tr>
 * <tr><td>&nbsp;[h]&nbsp;</td><td>&nbsp;represents a digit used in the time element <b>hour</b></td></tr>
 * <tr><td>&nbsp;[m]&nbsp;</td><td>&nbsp;represents a digit used in the time element <b>minute</b></td></tr>
 * <tr><td>&nbsp;[s]&nbsp;</td><td>&nbsp;represents a digit used in the time element <b>second</b></td></tr>
 * <tr><td>&nbsp;[n]&nbsp;</td><td>&nbsp;represents digit(s), constituting a positive integer or zero</td></tr>
 * <tr><td>&nbsp;[&plusmn;]&nbsp;</td><td>&nbsp;represents a plus sign [+] if in combination with the following
 *  element a positive value or zero needs to be represented, or a minus sign [&shy;] if in combination with the
 * following element a negative value needs to be represented.</td></tr>
 * </table>
 *
 * <p>The expanded format includes formating: <b><i>"CCYY-MM-DDThh:mm:ss,sss&plusmn;nnn"</i></b></p>
 *
 * <p>Further there are some date math functions for ISO Dates.</p>
 *
 * <h2>Other Functions</h2>
 * <p> funtions for julian dates </p>
 *
 * @author <a href="mailto:snowwolf@wabunoh.com">SnowWolf Wagner</a>
 * @author <a href="mailto:joeo@enigmastation.com">Joseph B. Ottinger</a>
 * @version $Revision: 17 $
 */
public class DateUtil {
    //~ Static fields/initializers /////////////////////////////////////////////

    /**
     * Base ISO 8601 Date format yyyyMMdd i.e., 20021225 for the 25th day of December in the year 2002
     */
    public static final String ISO_DATE_FORMAT = "yyyyMMdd";

    /**
     * Expanded ISO 8601 Date format yyyy-MM-dd i.e., 2002-12-25 for the 25th day of December in the year 2002
     */
    public static final String ISO_EXPANDED_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * Basic ISO 8601 Time format HHmmssSSSzzz i.e., 143212333-500 for 2 pm 32 min 12 secs 333 mills -5 hours from GMT
     * 24 hour clock
     */
    public static final String ISO_TIME_FORMAT = "HHmmssSSSzzz";

    /**
     * Basic ISO 8601 Time format HH:mm:ss,SSSzzz i.e., 14:32:12,333-500 for 2 pm 32 min 12 secs 333 mills -5 hours from GMT
     * 24 hour clock
     */
    public static final String ISO_EXPANDED_TIME_FORMAT = "HH:mm:ss,SSSzzz";

    /**
     * Base ISO 8601 Date format yyyyMMddTHHmmssSSSzzz i.e., 20021225T143212333-500 for
     * the 25th day of December in the year 2002 at 2 pm 32 min 12 secs 333 mills -5 hours from GMT
     */
    public static final String ISO_DATE_TIME_FORMAT = "yyyyMMdd'T'HHmmssSSSzzz";

    /**
     * Base ISO 8601 Date format yyyy-MM-ddTHH:mm:ss,SSSzzz i.e., 2002-12-25T14:32:12,333-500 for
     * the 25th day of December in the year 2002 at 2 pm 32 min 12 secs 333 mills -5 hours from GMT
     */
    public static final String ISO_EXPANDED_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss,SSSzzz";
    public static final DateFormatSymbols dateFormatSymbles;
    private static final String[][] foo;
    private static final int JAN_1_1_JULIAN_DAY = 1721426; // January 1, year 1 (Gregorian)
    private static final int EPOCH_JULIAN_DAY = 2440588; // Jaunary 1, 1970 (Gregorian)
    private static final int EPOCH_YEAR = 1970;

    // Useful millisecond constants.  Although ONE_DAY and ONE_WEEK can fit
    // into ints, they must be longs in order to prevent arithmetic overflow
    // when performing (bug 4173516).
    private static final int ONE_SECOND = 1000;
    private static final int ONE_MINUTE = 60 * ONE_SECOND;
    private static final int ONE_HOUR = 60 * ONE_MINUTE;
    private static final long ONE_DAY = 24 * ONE_HOUR;
    private static final long ONE_WEEK = 7 * ONE_DAY;

    static {
        // override the timezone strings
        foo = new String[0][];
        dateFormatSymbles = new DateFormatSymbols();
        dateFormatSymbles.setZoneStrings(foo);
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     *
     * @param isoString
     * @param expanded
     * @return True id leap year
     * @throws ParseException
     */
    public static final boolean isLeapYear(String isoString, boolean expanded) throws ParseException {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(isoToDate(isoString, expanded));

        return cal.isLeapYear(cal.get(Calendar.YEAR));
    }

    /**
     *
     * @param isoString
     * @return true if is leap year
     * @throws ParseException
     */
    public static final boolean isLeapYear(String isoString) throws ParseException {
        return isLeapYear(isoString, false);
    }

    public static final TimeZone getTimeZoneFromDateTime(String date, boolean expanded) throws ParseException {
        SimpleDateFormat formatter;

        if (expanded) {
            formatter = new SimpleDateFormat(ISO_EXPANDED_DATE_FORMAT, dateFormatSymbles);
        } else {
            formatter = new SimpleDateFormat(ISO_DATE_FORMAT, dateFormatSymbles);
        }

        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        formatter.parse(date);

        return formatter.getTimeZone();
    }

    public static final TimeZone getTimeZoneFromDateTime(String date) throws ParseException {
        return getTimeZoneFromDateTime(date, false);
    }

    /**
     * Date Arithmetic function.
     * Adds the specified (signed) amount of time to the given time field,
     * based on the GregorianCalendar's rules.

     * @param isoString
     * @param field
     * @param amount
     * @param expanded use formating char's
     * @return ISO 8601 Date String
     * @throws ParseException
     */
    public static final String add(String isoString, int field, int amount, boolean expanded) throws ParseException {
        Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.setTime(isoToDate(isoString, expanded));
        cal.add(field, amount);

        return dateToISO(cal.getTime(), expanded);
    }

    /**
     * Date Arithmetic function.
     * Adds the specified (signed) amount of time to the given time field,
     * based on the GregorianCalendar's rules.
     * no formating char's
     *
     * @param isoString
     * @param field
     * @param amount
     * @return ISO 8601 Date String
     * @throws ParseException
     */
    public static final String add(String isoString, int field, int amount) throws ParseException {
        return add(isoString, field, amount, false);
    }

    /**
     * Return an ISO date string
     *
     * @param date
     * @param expanded use formating char's
     * @return ISO date String
     */
    public static final String dateToISO(Date date, boolean expanded) {
        SimpleDateFormat formatter;

        if (expanded) {
            formatter = new SimpleDateFormat(ISO_EXPANDED_DATE_FORMAT, dateFormatSymbles);
        } else {
            formatter = new SimpleDateFormat(ISO_DATE_FORMAT, dateFormatSymbles);
        }

        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        return formatter.format(date);
    }

    /**
     * non-expanded
     *
     * @param date
     * @return ISO Date String
     */
    public static final String dateToISO(Date date) {
        return dateToISO(date, false);
    }

    /**
     * Converts java Date to Julian day count
     * A Julian day is defined as the number of days since Jan 1, 1.
     *
     * @param date
     * @return julian day
     */
    public static final long dateToJulianDay(Date date) {
        return millisToJulianDay(date.getTime());
    }

    /**
     * Returns the days between two dates. Positive values indicate that
     * the second date is after the first, and negative values indicate, well,
     * the opposite. Relying on specific times is problematic.
     *
     * @param early the "first date"
     * @param late the "second date"
     * @return the days between the two dates
         * @deprecated
     */
    public static final int daysBetween(Date early, Date late) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(early);
        c2.setTime(late);

        return daysBetween(c1, c2);
    }

    /**
     * Returns the days between two dates. Positive values indicate that
     * the second date is after the first, and negative values indicate, well,
     * the opposite.
     *
     * @param early
     * @param late
     * @return the days between two dates.
         * @deprecated
     */
    public static final int daysBetween(Calendar early, Calendar late) {
        return (int) (toJulian(late) - toJulian(early));
    }

    /**
    * Returns the days between two dates. Positive values indicate that
    * the second date is after the first, and negative values indicate, well,
    * the opposite.
    *
    * @param isoEarly the "first date" in ISO DateTime Format
    * @param isoLate the "second date" in ISO Date Time format
    * @return the days between the two dates
    */
    public static final long daysBetween(String isoEarly, String isoLate, boolean expanded) throws ParseException {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTimeZone(getTimeZoneFromDateTime(isoEarly, expanded));
        c1.setTime(isoToDate(isoEarly, expanded));
        c2.setTimeZone(getTimeZoneFromDateTime(isoLate, expanded));
        c2.setTime(isoToDate(isoLate, expanded));

        return millisToJulianDay(c2.getTime().getTime()) - millisToJulianDay(c1.getTime().getTime());
    }

    /**
     * Return an ISO date string as a java.util.Date
     *
     *
     * @param dateString
     * @param expanded use formating charaters
     * @return java.util.Date from the ISO Date in GMT
     * @throws java.text.ParseException
     */
    public static final Date isoToDate(String dateString, boolean expanded) throws ParseException {
        SimpleDateFormat formatter;

        if (expanded) {
            formatter = new SimpleDateFormat(ISO_EXPANDED_DATE_FORMAT, dateFormatSymbles);
        } else {
            formatter = new SimpleDateFormat(ISO_DATE_FORMAT, dateFormatSymbles);
        }

        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        return new Date(formatter.parse(dateString).getTime());
    }

    /**
     * non-expanded
     *
     * @param dateString
     * @return ISO java.util.Date
     * @throws ParseException
     */
    public static final Date isoToDate(String dateString) throws ParseException {
        return isoToDate(dateString, false);
    }

    /**
     * Return an ISO date string as a java.sql.Date
     *
     * @param dateString
     * @param expanded    expanded use formating charaters
     * @return java.util.Date from the ISO Date in GMT
     * @throws java.text.ParseException
     */
    public static final java.sql.Date isoToSQLDate(String dateString, boolean expanded) throws ParseException {
        SimpleDateFormat formatter;

        if (expanded) {
            formatter = new SimpleDateFormat(ISO_EXPANDED_DATE_FORMAT, dateFormatSymbles);
        } else {
            formatter = new SimpleDateFormat(ISO_DATE_FORMAT, dateFormatSymbles);
        }

        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        return new java.sql.Date(formatter.parse(dateString).getTime());
    }

    /**
     * non-expanded
     *
     * @param dateString
     * @return java.sql.Date
     * @throws ParseException
     */
    public static final java.sql.Date isoToSQLDate(String dateString) throws ParseException {
        return isoToSQLDate(dateString, false);
    }

    /**
     *
     * @param expanded expanded use formating charaters
     * @param dateString
     * @return ISO Date String
     * @throws java.text.ParseException
     */
    public static final Time isoToTime(String dateString, boolean expanded) throws ParseException {
        SimpleDateFormat formatter;

        if (expanded) {
            formatter = new SimpleDateFormat(ISO_EXPANDED_TIME_FORMAT, dateFormatSymbles);
        } else {
            formatter = new SimpleDateFormat(ISO_TIME_FORMAT, dateFormatSymbles);
        }

        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        return new Time(formatter.parse(dateString).getTime());
    }

    /**
     * non-expanded
     *
     * @param dateString
     * @return Time
     * @throws java.text.ParseException
     */
    public static final Time isoToTime(String dateString) throws ParseException {
        return isoToTime(dateString, false);
    }

    /**
     *
     * @param expanded expanded use formating charaters
     * @param dateString
     * @return ISO Time String
     * @throws java.text.ParseException
     */
    public static final Timestamp isoToTimestamp(String dateString, boolean expanded) throws ParseException {
        SimpleDateFormat formatter;

        if (expanded) {
            formatter = new SimpleDateFormat(ISO_EXPANDED_DATE_TIME_FORMAT, dateFormatSymbles);
        } else {
            formatter = new SimpleDateFormat(ISO_DATE_TIME_FORMAT, dateFormatSymbles);
        }

        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        return new Timestamp(formatter.parse(dateString).getTime());
    }

    /**
     * non-expanded
     *
     * @param dateString
     * @return Timestamp
     * @throws java.text.ParseException
     */
    public static final Timestamp isoToTimestamp(String dateString) throws ParseException {
        return isoToTimestamp(dateString, false);
    }

    /**
     * Convert a julian day count to a java sql Date @ midnight
     *
     * @param julian the given Julian day number.
     * @return java.sql.Date
     */
    public static final java.sql.Date julianDayCountToDate(long julian) {
        return new java.sql.Date(julianDayToMillis(julian));
    }

    /**
     * Convert a julian day count to a java util Date @ midnight
     *
     * @param julian the given Julian day number.
     * @return java.util.Date
     */
    public static final Date julianDayToDate(long julian) {
        return new Date(julianDayToMillis(julian));
    }

    /**
     * Converts Julian day to time as milliseconds.
     *
     * @param julian the given Julian day number.
     * @return time as milliseconds.
     */
    public static final long julianDayToMillis(long julian) {
        return (julian - EPOCH_JULIAN_DAY + JAN_1_1_JULIAN_DAY) * ONE_DAY;
    }

    /**
    * Converts time as milliseconds to Julian day count
     * A Julian day is defined as the number of days since Jan 1, 1.
     *
    * @param millis the given milliseconds.
    * @return the Julian day number.
    */
    public static final long millisToJulianDay(long millis) {
        return EPOCH_JULIAN_DAY - JAN_1_1_JULIAN_DAY + (millis / ONE_DAY);
    }

    /**
     * Time Field Rolling function.
     * Rolls (up/down) a single unit of time on the given time field.
     *
     * @param isoString
     * @param field the time field.
     * @param up Indicates if rolling up or rolling down the field value.
     * @param expanded use formating char's
     * @exception ParseException if an unknown field value is given.
     */
    public static final String roll(String isoString, int field, boolean up, boolean expanded) throws ParseException {
        Calendar cal = GregorianCalendar.getInstance(getTimeZoneFromDateTime(isoString, expanded));
        cal.setTime(isoToDate(isoString, expanded));
        cal.roll(field, up);

        return dateToISO(cal.getTime(), expanded);
    }

    /**
     * Time Field Rolling function.
     * Rolls (up/down) a single unit of time on the given time field.
     *
     * @param isoString
     * @param field the time field.
     * @param up Indicates if rolling up or rolling down the field value.
     * @exception ParseException if an unknown field value is given.
     */
    public static final String roll(String isoString, int field, boolean up) throws ParseException {
        return roll(isoString, field, up, false);
    }

    /**
     *
     * @param expanded expanded use formating charaters
     * @param date
     * @return ISO Time String
     */
    public static final String timeToISO(Time date, boolean expanded) {
        SimpleDateFormat formatter;

        if (expanded) {
            formatter = new SimpleDateFormat(ISO_EXPANDED_TIME_FORMAT, dateFormatSymbles);
        } else {
            formatter = new SimpleDateFormat(ISO_TIME_FORMAT, dateFormatSymbles);
        }

        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        return formatter.format(date);
    }

    /**
     * non-expanded
     *
     * @param date
     * @return ISO Time String
     */
    public static final String timeToISO(Time date) {
        return timeToISO(date, false);
    }

    /**
     *
     * @param expanded expanded use formating charaters
     * @param date
     * @return ISO Date Time String
     */
    public static final String timestampToISO(Timestamp date, boolean expanded) {
        SimpleDateFormat formatter;

        if (expanded) {
            formatter = new SimpleDateFormat(ISO_EXPANDED_DATE_TIME_FORMAT, dateFormatSymbles);
        } else {
            formatter = new SimpleDateFormat(ISO_DATE_TIME_FORMAT, dateFormatSymbles);
        }

        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        return formatter.format(date);
    }

    /**
     * non-expanded
     *
     * @param date
     * @return ISO Date/Time String
     */
    public static final String timestampToISO(Timestamp date) {
        return timestampToISO(date, false);
    }

    /**
     * Returns the Date from a julian. The Julian date will be converted to noon GMT,
         * such that it matches the nearest half-integer (i.e., a julian date of 1.4 gets
     * changed to 1.5, and 0.9 gets changed to 0.5.)
     *
     * @param JD the Julian date
     * @return the Gregorian date
     * @deprecated
     */
    public static final Date toDate(float JD) {
        /* To convert a Julian Day Number to a Gregorian date, assume that it is for 0 hours, Greenwich time (so
         * that it ends in 0.5). Do the following calculations, again dropping the fractional part of all
         * multiplicatons and divisions. Note: This method will not give dates accurately on the
         * Gregorian Proleptic Calendar, i.e., the calendar you get by extending the Gregorian
         * calendar backwards to years earlier than 1582. using the Gregorian leap year
         * rules. In particular, the method fails if Y<400. */
        float Z = (normalizedJulian(JD)) + 0.5f;
        float W = (int) ((Z - 1867216.25f) / 36524.25f);
        float X = (int) (W / 4f);
        float A = (Z + 1 + W) - X;
        float B = A + 1524;
        float C = (int) ((B - 122.1) / 365.25);
        float D = (int) (365.25f * C);
        float E = (int) ((B - D) / 30.6001);
        float F = (int) (30.6001f * E);
        int day = (int) (B - D - F);
        int month = (int) (E - 1);

        if (month > 12) {
            month = month - 12;
        }

        int year = (int) (C - 4715); //(if Month is January or February) or C-4716 (otherwise)

        if (month > 2) {
            year--;
        }

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1); // damn 0 offsets
        c.set(Calendar.DATE, day);

        return c.getTime();
    }

    /**
     * Return a Julian date based on the input parameter. This is
     * based from calculations found at
     * <a href="http://quasar.as.utexas.edu/BillInfo/JulianDatesG.html">Julian Day Calculations
     * (Gregorian Calendar)</a>, provided by Bill Jeffrys.
     * @param c a calendar instance
     * @return the julian day number
     */
    public static final float toJulian(Calendar c) {
        int Y = c.get(Calendar.YEAR);
        int M = c.get(Calendar.MONTH);
        int D = c.get(Calendar.DATE);
        int A = Y / 100;
        int B = A / 4;
        int C = 2 - A + B;
        float E = (int) (365.25f * (Y + 4716));
        float F = (int) (30.6001f * (M + 1));
        float JD = (C + D + E + F) - 1524.5f;

        return JD;
    }

    /**
     * Return a Julian date based on the input parameter. This is
     * based from calculations found at
     * <a href="http://quasar.as.utexas.edu/BillInfo/JulianDatesG.html">Julian Day Calculations
     * (Gregorian Calendar)</a>, provided by Bill Jeffrys.
     * @param date
     * @return the julian day number
     */
    public static final float toJulian(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return toJulian(c);
    }

    protected static final float normalizedJulian(float JD) {
        float f = Math.round(JD + 0.5f) - 0.5f;

        return f;
    }

    /**
    * Divide two long integers, returning the floor of the quotient.
    * <p>
    * Unlike the built-in division, this is mathematically well-behaved.
    * E.g., <code>-1/4</code> => 0
    * but <code>floorDivide(-1,4)</code> => -1.
    * @param numerator the numerator
    * @param denominator a divisor which must be > 0
    * @return the floor of the quotient.
    */
    private static final long floorDivide(long numerator, long denominator) {
        // We do this computation in order to handle
        // a numerator of Long.MIN_VALUE correctly
        return (numerator >= 0) ? (numerator / denominator) : (((numerator + 1) / denominator) - 1);
    }
}
