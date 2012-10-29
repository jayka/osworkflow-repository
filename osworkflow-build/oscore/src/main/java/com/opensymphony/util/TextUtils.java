/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.util;

import java.awt.Color;

import java.io.*;

import java.net.*;

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
import java.util.*;


/**
 * Utilities for common String manipulations.
 *
 * This is a class contains static methods only and is not meant to be instantiated.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @author <a href="mailto:pkan@internet.com">Patrick Kan</a>
 * @author <a href="mailto:mcannon@internet.com">Mike Cannon-Brookes</a>
 * @author <a href="mailto:hani@fate.demon.co.uk">Hani Suleiman</a>
 * @author <a href="mailto:joeo@adjacency.org">Joseph B. Ottinger</a>
 * @author <a href="mailto:scott@atlassian.com">Scott Farquhar</a>
 *
 * @version $Revision: 153 $
 */
public class TextUtils {
    //~ Static fields/initializers /////////////////////////////////////////////

    /**
     * An array of HTML tags that, in HTML, don't require closing tags. Note that
     * XHTML doesn't work this way.
     */
    public final static String[] SINGLE_TAGS = {"br", "p", "hr"};

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Convert line breaks to html <code>&lt;br&gt;</code> tag.
     * @param s the String to convert
     * @return the converted string
     */
    public final static String br(String s) {
        s = noNull(s);

        StringBuffer str = new StringBuffer();

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '\n') {
                str.append("<br/>");
            }

            str.append(s.charAt(i));
        }

        return str.toString();
    }

    /**
     * Search through a String for any tags that have been opened and append closing tags
     * for those that have not been closed.
     * @param str A string possibly containing unclosed HTML tags
     * @return the converted string
     * #see #SINGLE_TAGS
     */
    public final static String closeTags(String str) {
        Map openTags = new HashMap();
        str = noNull(str);

        boolean inTag = false;
        boolean inTagName = false;
        boolean inOpenTag = true;
        String tagName = "";
        List singleTags = Arrays.asList(SINGLE_TAGS);

        char[] strA = str.toCharArray();

        for (int i = 0; i < strA.length; i++) {
            char c = strA[i];

            if (!inTag) // not in a tag
             {
                if (c == '<') // start of a tag
                 {
                    // reset all state variables at start of each new tag
                    inTag = true;
                    inTagName = true;
                    inOpenTag = true;
                    tagName = "";
                }
            } else // in a tag
             {
                if ((tagName.length() == 0) && (c == '/')) // start of a close tag
                 {
                    inOpenTag = false;
                } else if (inTagName && ((c == ' ') || (c == '>') || (c == '/'))) // end of the tagname or tag
                 {
                    inTagName = false;

                    if (inOpenTag && !singleTags.contains(tagName.toLowerCase())) {
                        // count this tag in the list of open tags
                        if (openTags.get(tagName) == null) {
                            openTags.put(tagName, new Integer(1));
                        } else {
                            int tagCount = ((Integer) openTags.get(tagName)).intValue();
                            openTags.put(tagName, new Integer(tagCount + 1));
                        }
                    } else // in close tag
                     {
                        // remove it from closetags
                        if (openTags.get(tagName) != null) {
                            int tagCount = ((Integer) openTags.get(tagName)).intValue();

                            if (tagCount > 1) {
                                openTags.put(tagName, new Integer(tagCount - 1));
                            } else {
                                openTags.remove(tagName);
                            }
                        }
                    }

                    if (c == '>') // end of tag
                     {
                        inTag = false;
                    }
                } else if (inTagName) // still in tag name
                 {
                    tagName += c;
                } else if (c == '>') // end of tag and there were attributes
                 {
                    inTag = false;
                }
            }
        }

        // cycle through remaining open tags and close them
        Iterator openTagNames = openTags.keySet().iterator();
        StringBuffer closedString = new StringBuffer(str);

        while (openTagNames.hasNext()) {
            String openTagName = (String) openTagNames.next();

            //System.out.println("removing " + openTagName);
            for (int i = 0;
                    i < ((Integer) openTags.get(openTagName)).intValue();
                    i++) {
                //System.out.println("appended </ " + openTagName + ">");
                closedString.append("</").append(openTagName).append('>');
            }
        }

        // return closed string
        return closedString.toString();
    }

    /**
     * Convert Color to html hex string. (#012345)
     * @param c the Color to convert
     * @return A string with a hexadecimal RGB encoding
     */
    public final static String colorToHex(java.awt.Color c) {
        String r = Integer.toHexString(c.getRed());
        String g = Integer.toHexString(c.getGreen());
        String b = Integer.toHexString(c.getBlue());

        if (r.length() < 2) {
            r = '0' + r;
        }

        if (g.length() < 2) {
            g = '0' + g;
        }

        if (b.length() < 2) {
            b = '0' + b;
        }

        return '#' + r + g + b;
    }

    /**
     * Decode binary data from String using base64.
     * @deprecated use {@link MailUtils#decodeBytes(String)} instead.
     * @see #encodeBytes(byte[])
     */
    public final static byte[] decodeBytes(String str) throws IOException {
        return MailUtils.decodeBytes(str);
    }

    /**
     * Decode Object from a String by decoding with base64 then deserializing.
     * @see #encodeObject(java.lang.Object)
     */
    public final static Object decodeObject(String str) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bytes = new ByteArrayInputStream(MailUtils.decodeBytes(str));
        ObjectInputStream stream = new ObjectInputStream(bytes);
        Object result = stream.readObject();
        stream.close();

        return result;
    }

    /**
     * Encode binary data into String using base64.
     * @deprecated use {@link MailUtils#encodeBytes(byte[])} instead.
     * @see #decodeBytes(java.lang.String)
     */
    public final static String encodeBytes(byte[] data) throws IOException {
        return MailUtils.encodeBytes(data);
    }

    /**
     * Encode an Object to String by serializing it and encoding using base64.
     *
     * @see #decodeObject(java.lang.String)
     */
    public final static String encodeObject(Object o) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(bytes);
        stream.writeObject(o);
        stream.close();
        bytes.flush();

        return MailUtils.encodeBytes(bytes.toByteArray());
    }

    /**
     * Extract a number from a String.
     *
     * <h5>Example</h5>
     *
     * <pre>
     *   " 12345"                 -&gt;     "12345"
     *   "hello123bye"            -&gt;     "123"
     *   "a2b4c6 8 "              -&gt;     "2468"
     *   " -22"                   -&gt;     "-22"
     *   "5.512"                  -&gt;     "5.512"
     *   "1.2.3.4"                -&gt;     "1.234"
     *   ".2"                     -&gt;     "0.2"
     *   "-555.7"                 -&gt;     "-555.7"
     *   "-..6"                   -&gt;     "-0.6"
     *   "abc- dx.97 9"           -&gt;     "-0.979"
     *   "\ufffd1,000,000.00 per year" -&gt;     "1000000.00"
     *   ""                       -&gt;     "0"
     *   "asdsf"                  -&gt;     "0"
     *   "123."                   -&gt;     "123"
     *   null                     -&gt;     "0"
     * </pre>
     *
     * @param in    Original String containing number to be extracted.
     * @return      String stripped of all non-numeric chars.
     *
     * @see #parseInt(String)
     * @see #parseLong(String)
     */
    public final static String extractNumber(String in) {
        if (in == null) {
            return "0";
        }

        StringBuffer result = new StringBuffer();
        boolean seenDot = false;
        boolean seenMinus = false;
        boolean seenNumber = false;

        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);

            if (c == '.') {
                // insert dot if not yet encountered
                if (!seenDot) {
                    seenDot = true;

                    if (!seenNumber) {
                        result.append('0'); // padding zero if no number yet
                    }

                    result.append('.');
                }
            } else if (c == '-') {
                // insert minus sign if not yet encountered
                if (!seenMinus) {
                    seenMinus = true;
                    result.append('-');
                }
            } else if ((c == '0') || ((c >= '1') && (c <= '9'))) {
                // add number
                seenNumber = true;
                result.append(c);
            }
        }

        // remove trailing .
        int length = result.length();

        if ((length > 0) && (result.charAt(length - 1) == '.')) {
            result.deleteCharAt(length - 1);
        }

        return (result.length() == 0) ? "0" : result.toString(); // if nothing left, return 0
    }

    /**
     * Convert html hex string to Color. If the hexadecimal string is not
     * a valid character, <code>Color.black</code> is returned.
     * Only the first six hexadecimal characters are considered; any
     * extraneous values are discarded. Also, a leading "#", if any, is allowed
     * (and ignored).
     * @param color the String (in RGB hexadecimal format) to convert
     * @return the java.awt.Color
     */
    public final static Color hexToColor(String color) {
        try {
            if (color.charAt(0) == '#') {
                color = color.substring(1, 7);
            }

            int[] col = new int[3];

            for (int i = 0; i < 3; i++) {
                col[i] = Integer.parseInt(color.substring(i * 2, (i * 2) + 2), 16);
            }

            return new Color(col[0], col[1], col[2]);
        } catch (Exception e) {
            return Color.black;
        }
    }

    /**
     * Escape html entity characters and high characters (eg "curvy" Word quotes).
     *
     * Note this method can also be used to encode XML now
     *
     * @deprecated use htmlEncode(String) instead.
     */
    public final static String html(String s) {
        return htmlEncode(s);
    }

    public final static String htmlEncode(String s) {
        return htmlEncode(s, true);
    }

    /**
     * Escape html entity characters and high characters (eg "curvy" Word quotes).
     * Note this method can also be used to encode XML.
     * @param s the String to escape.
     * @param encodeSpecialChars if true high characters will be encode other wise not.
     * @return the escaped string
     */
    public final static String htmlEncode(String s, boolean encodeSpecialChars) {
        s = noNull(s);

        StringBuffer str = new StringBuffer();

        for (int j = 0; j < s.length(); j++) {
            char c = s.charAt(j);

            // encode standard ASCII characters into HTML entities where needed
            if (c < '\200') {
                switch (c) {
                case '"':
                    str.append("&quot;");

                    break;

                case '&':
                    str.append("&amp;");

                    break;

                case '<':
                    str.append("&lt;");

                    break;

                case '>':
                    str.append("&gt;");

                    break;

                default:
                    str.append(c);
                }
            }
            // encode 'ugly' characters (ie Word "curvy" quotes etc)
            else if (encodeSpecialChars && (c < '\377')) {
                String hexChars = "0123456789ABCDEF";
                int a = c % 16;
                int b = (c - a) / 16;
                String hex = "" + hexChars.charAt(b) + hexChars.charAt(a);
                str.append("&#x" + hex + ";");
            }
            //add other characters back in - to handle charactersets
            //other than ascii
            else {
                str.append(c);
            }
        }

        return str.toString();
    }

    /**
     * Convert all URLs and E-mail addresses in a string into hyperlinks.
     *
     * @param text The block of text to hyperlink.
     * @return the text with known uri formats hyperlinked
     *
     * @see #hyperlink(String, String)
     */
    public final static String hyperlink(String text) {
        return hyperlink(text, null);
    }

    /**
     * Convert all URLs and E-mail addresses in a string into hyperlinks.
     *
     * @param text The block of text to hyperlink.
     * @param target The target attribute to use for href (optional).
     * @return the text with known uri formats hyperlinked
     *
     * @see #linkEmail(String)
     * @see #linkURL(String)
     */
    public final static String hyperlink(String text, String target) {
        text = noNull(text);

        StringBuffer sb = new StringBuffer((int) (text.length() * 1.1));
        sb.append(text);
        linkEmail(sb);
        linkURL(sb, target);

        return sb.toString();
    }

    /**
     * Indent a String with line-breaks.
     *
     * @param string String to indent.
     * @param indentSize Number of spaces to indent by. 0 will indent using a tab.
     * @param initialLine Whether to indent initial line.
     * @return Indented string.
     */
    public final static String indent(String string, int indentSize, boolean initialLine) {
        // Create indent String
        String indent;

        if (indentSize == 0) {
            indent = "\t";
        } else {
            StringBuffer s = new StringBuffer();

            for (int i = 0; i < indentSize; i++) {
                s.append(' ');
            }

            indent = s.toString();
        }

        // Apply indent to input
        StringBuffer result = new StringBuffer();

        if (initialLine) {
            result.append(indent);
        }

        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            result.append(c);

            if (c == '\n') {
                result.append(indent);
            }
        }

        return result.toString();
    }

    /**
     * Returns a string that has whitespace removed from
     * both ends of the String, as well as duplicate whitespace
     * removed from within the String.
     */
    public final static String innerTrim(String s) {
        StringBuffer b = new StringBuffer(s);
        int index = 0;

        while ((b.length() != 0) && (b.charAt(0) == ' ')) {
            b.deleteCharAt(0);
        }

        while (index < b.length()) {
            if (Character.isWhitespace(b.charAt(index))) {
                if (((index + 1) < b.length()) && (Character.isWhitespace(b.charAt(index + 1)))) {
                    b.deleteCharAt(index + 1);
                    index--; // let's restart at this character!
                }
            }

            index++;
        }

        if (b.length() > 0) {
            int l = b.length() - 1;

            if (b.charAt(l) == ' ') {
                b.setLength(l);
            }
        }

        String result = b.toString();

        return result;
    }

    /**
     * Join an Iteration of Strings together.
     *
     * <h5>Example</h5>
     *
     * <pre>
     *   // get Iterator of Strings ("abc","def","123");
     *   Iterator i = getIterator();
     *   out.print( TextUtils.join(", ",i) );
     *   // prints: "abc, def, 123"
     * </pre>
     *
     * @param glue Token to place between Strings.
     * @param pieces Iteration of Strings to join.
     * @return String presentation of joined Strings.
     */
    public final static String join(String glue, Iterator pieces) {
        StringBuffer s = new StringBuffer();

        while (pieces.hasNext()) {
            s.append(pieces.next().toString());

            if (pieces.hasNext()) {
                s.append(glue);
            }
        }

        return s.toString();
    }

    /**
     * Join an array of Strings together.
     *
     * @param glue Token to place between Strings.
     * @param pieces Array of Strings to join.
     * @return String presentation of joined Strings.
     *
     * @see #join(String, java.util.Iterator)
     */
    public final static String join(String glue, String[] pieces) {
        return join(glue, Arrays.asList(pieces).iterator());
    }

    /**
     * Join a Collection of Strings together.
     *
     * @param glue Token to place between Strings.
     * @param pieces Collection of Strings to join.
     * @return String presentation of joined Strings.
     *
     * @see #join(String, java.util.Iterator)
     */
    public final static String join(String glue, Collection pieces) {
        return join(glue, pieces.iterator());
    }

    /**
     * Finds all leading spaces on each line and replaces it with
     * an HTML space (&amp;nbsp;)
     *
     * @param s string containing text to replaced with &amp;nbsp;
     * @return the new string
     */
    public final static String leadingSpaces(String s) {
        s = noNull(s);

        StringBuffer str = new StringBuffer();
        boolean justAfterLineBreak = true;

        for (int i = 0; i < s.length(); i++) {
            if (justAfterLineBreak) {
                if (s.charAt(i) == ' ') {
                    str.append("&nbsp;");
                } else if (s.charAt(i) == '\n') {
                    str.append(s.charAt(i));
                } else {
                    str.append(s.charAt(i));
                    justAfterLineBreak = false;
                }
            } else {
                if (s.charAt(i) == '\n') {
                    justAfterLineBreak = true;
                }

                str.append(s.charAt(i));
            }
        }

        return str.toString();
    }

    /**
     * Returns the leftmost n chars of the string.  If n is larger than the length of the string,
     * return the whole string unchanged.
     *
     * @param s - the string to operate on.
     * @param n - the number of chars to return.
     */
    public final static String left(String s, int n) {
        if (n >= s.length()) {
            return s;
        }

        return s.substring(0, n);
    }

    /**
     * Wrap all email addresses in specified string with href tags.
     * @param string The block of text to check.
     * @return String The block of text with all email addresses placed in href tags.
     */
    public final static String linkEmail(String string) {
        StringBuffer str = new StringBuffer((int) (string.length() * 1.05));
        str.append(string);
        linkEmail(str);

        return str.toString();
    }

    /**
     * Wrap all urls ('http://', 'www.', and 'ftp://') in specified string with href tags.
     * @param str The block of text to check.
     * @return String The block of text with all url's placed in href tags.
     */
    public final static String linkURL(String str) {
        return linkURL(str, null);
    }

    /**
     * Wrap all urls ('abc://' and 'www.abc') in specified string with href tags.
     *
     * @param str The block of text to check.
     * @param target The target to use for the href (optional).
     * @return String The block of text with all url's placed in href tags.
     */
    public final static String linkURL(String str, String target) {
        StringBuffer sb = new StringBuffer((int) (str.length() * 1.05));
        sb.append(str);
        linkURL(sb, target);

        return sb.toString();
    }

    /**
     * Create <li> elements in a piece of plain text;
     * Will convert lines starting with - or *. It might have been
     * useful for the people writing earlier versions of this module's Javadocs.
     * <tt>;)</tt>
     * @param str A string, possibly containing a plaintext "list"
     * @return a converted string
     */
    public final static String list(String str) {
        str = noNull(str);

        String strToRet = "";

        boolean inList = false;

        if (str.startsWith("-") || str.startsWith("*")) {
            // if first char is '-' or '*' then put a linebreak before str so that the following
            // code will take it as a list...
            str = '\n' + str;
        }

        for (int i = 0; i < str.length(); i++) {
            // look at whether the character at i is '\n'...
            if (str.charAt(i) == '\n') {
                // if so, look at the next char and see whether it's '-' or '*'...
                if (i != (str.length() - 1)) {
                    if ((str.charAt(i + 1) == '-') || (str.charAt(i + 1) == '*')) {
                        // if so, and if we are not currently in a list, we start
                        // a list...
                        if (!inList) {
                            strToRet += "<ul>";
                            inList = true;
                        } else {
                            // if we are already in a list, then the previous point is unclosed...
                            strToRet += "</li>";
                        }

                        // we add the <li> tag since a new point is started...
                        strToRet += "<li>";

                        i++; // since we've taken care of the '-' or '*' char already...
                    } else {
                        // if we are currently in a list, and we have a linebreak char but
                        // no '-' or '*' after it, then that means the list is closed...
                        if (inList) {
                            strToRet += "</li></ul>";
                            inList = false;
                        } else {
                            // if we are not currently in a list, simply add the linebreak
                            // char to the string...
                            strToRet += str.charAt(i);
                        }
                    }
                } else {
                    // there is no next char since this char is the last char in the string...
                    // if we are in a list, then we close the list (and the current point in the list)
                    // by adding </li> and </ul> tags...
                    if (inList) {
                        strToRet += "</li></ul>";
                    }
                }
            } else {
                // not a linebreak char - simply add the char to the string...
                strToRet += str.charAt(i);
            }
        }

        // if we're still in a list, then we close it...
        if (inList) {
            strToRet += "</li></ul>";
        }

        return strToRet;
    }

    /**
     * Return <code>string</code>, or <code>defaultString</code> if
     * <code>string</code> is <code>null</code> or <code>""</code>.
     * Never returns <code>null</code>.
     *
     * <p>Examples:</p>
     * <pre>
     * // prints "hello"
     * String s=null;
     * System.out.println(TextUtils.noNull(s,"hello");
     *
     * // prints "hello"
     * s="";
     * System.out.println(TextUtils.noNull(s,"hello");
     *
     * // prints "world"
     * s="world";
     * System.out.println(TextUtils.noNull(s, "hello");
     * </pre>
     *
     * @param string the String to check.
     * @param defaultString The default string to return if <code>string</code> is <code>null</code> or <code>""</code>
     * @return <code>string</code> if <code>string</code> is non-empty, and <code>defaultString</code> otherwise
     * @see #stringSet(java.lang.String)
     */
    public final static String noNull(String string, String defaultString) {
        return (stringSet(string)) ? string : defaultString;
    }

    /**
     * Return <code>string</code>, or <code>""</code> if <code>string</code>
     * is <code>null</code>. Never returns <code>null</code>.
     * <p>Examples:</p>
     * <pre>
     * // prints 0
     * String s=null;
     * System.out.println(TextUtils.noNull(s).length());
     *
     * // prints 1
     * s="a";
     * System.out.println(TextUtils.noNull(s).length());
     * </pre>
     * @param string the String to check
     * @return a valid (non-null) string reference
     */
    public final static String noNull(String string) {
        return noNull(string, "");
    }

    /**
     * Convert a String to an boolean.
     * Accepts: 1/0, yes/no, true/false - case insensitive. If the value does
     * not map to "true,", <code>false</code> is returned.
     *
     * @param in String to be parsed.
     * @return boolean representation of String.
     */
    public final static boolean parseBoolean(String in) {
        in = noNull(in);

        if (in.length() == 0) {
            return false;
        }

        switch (in.charAt(0)) {
        case '1':
        case 'y':
        case 'Y':
        case 't':
        case 'T':
            return true;
        }

        return false;
    }

    /**
     * Given 3 Strings representing the the year, month and day, return a Date object.
     * This is only valid for Gregorian calendars.
     *
     * <p>If the day cannot be determined, 1st will be used.
     * If the month cannot be determined, Jan will be used.</p>
     *
     * @param year Year : 4 digit
     * @param month Month : 1 or 2 digit (1=jan, 2=feb, ...) or name (jan, JAN, January, etc). If null, default is Jan.
     * @param day Day of month : 1 or 2 digit, prefix will be stripped (1, 30, 05, 3rd). If null, default is 1st.
     */
    public final static Date parseDate(String year, String month, String day) {
        year = noNull(year);
        month = noNull(month);
        day = noNull(day);

        int y = parseInt(year);
        int m = parseInt(month) - 1;
        int d = parseInt(extractNumber(day));

        if (m == -1) { // month was not a number, parse text

            // @todo i18n support
            if (month.length() < 3) {
                month = month + "   ";
            }

            String str = month.toLowerCase().substring(0, 3);

            if (str.equals("jan")) {
                m = 0;
            } else if (str.equals("feb")) {
                m = 1;
            } else if (str.equals("mar")) {
                m = 2;
            } else if (str.equals("apr")) {
                m = 3;
            } else if (str.equals("may")) {
                m = 4;
            } else if (str.equals("jun")) {
                m = 5;
            } else if (str.equals("jul")) {
                m = 6;
            } else if (str.equals("aug")) {
                m = 7;
            } else if (str.equals("sep")) {
                m = 8;
            } else if (str.equals("oct")) {
                m = 9;
            } else if (str.equals("nov")) {
                m = 10;
            } else if (str.equals("dec")) {
                m = 11;
            } else {
                m = 0; // jan
            }
        }

        if (d == 0) {
            d = 1; // if no day, set to 1st
        }

        Calendar cal = Calendar.getInstance();
        cal.set(y, m, d);

        return cal.getTime();
    }

    /**
     * Convert a String to a double.
     *
     * @param in String containing number to be parsed.
     * @return Double value of number or 0 if error.
     *
     * @see #extractNumber(String)
     */
    public final static double parseDouble(String in) {
        double d = 0;

        try {
            d = Double.parseDouble(in);
        } catch (Exception e) {
        }

        ;

        return d;
    }

    /**
     * Convert a String to a float.
     *
     * @param in String containing number to be parsed.
     * @return Float value of number or 0 if error.
     *
     * @see #extractNumber(String)
     */
    public final static float parseFloat(String in) {
        float f = 0;

        try {
            f = Float.parseFloat(in);
        } catch (Exception e) {
        }

        ;

        return f;
    }

    /**
     * Convert a String to an int. Truncates numbers if it's a float string;
     * for example, 4.5 yields a value of 4.
     *
     * @param in String containing number to be parsed.
     * @return Integer value of number or 0 if error.
     *
     * @see #extractNumber(String)
     */
    public final static int parseInt(String in) {
        int i;

        try {
            i = Integer.parseInt(in);
        } catch (Exception e) {
            i = (int) parseFloat(in);
        }

        ;

        return i;
    }

    /**
     * Convert a String to a long. Truncates numbers if it's a float or double string;
     * for example, 4.5 yields a value of 4.
     *
     * @param in String containing number to be parsed.
     * @return Long value of number or 0 if error.
     *
     * @see #extractNumber(String)
     */
    public final static long parseLong(String in) {
        long l;

        try {
            l = Long.parseLong(in);
        } catch (Exception e) {
            l = (long) parseDouble(in);
        }

        ;

        return l;
    }

    /**
     * Converts plain text to html code.
     *
     * <ul>
     * <li>escapes appropriate characters
     * <li>puts in line breaks
     * <li>hyperlinks link and email addresses
     * </ul>
     *
     * @param str - String containing the plain text.
     * @return the escaped string
     */
    public final static String plainTextToHtml(String str) {
        return plainTextToHtml(str, null);
    }

    public final static String plainTextToHtml(String str, boolean encodeSpecialChars) {
        return plainTextToHtml(str, null, encodeSpecialChars);
    }

    public final static String plainTextToHtml(String str, String target) {
        return plainTextToHtml(str, target, true);
    }

    /**
     * Converts plain text to html code.
     *
     * <ul>
     * <li>escapes appropriate characters
     * <li>puts in line breaks
     * <li>hyperlinks link and email addresses
     * </ul>
     *
     * @param str - String containing the plain text.
     * @param target - Target for href tags (optional).
     * @param encodeSpecialChars - if true high characters will be encode other wise not.
     * @return the escaped string
     */
    public final static String plainTextToHtml(String str, String target, boolean encodeSpecialChars) {
        str = noNull(str);

        //First, convert all the special chars...
        str = htmlEncode(str, encodeSpecialChars);

        //Convert all leading whitespaces
        str = leadingSpaces(str);

        //Then convert all line breaks...
        str = br(str);

        //Then create hyperlinks...
        str = hyperlink(str, target);

        return str;
    }

    /**
     * Removes part of the original string starting at removeAndInsertStart and ending at removeEnd.
     * Then insert another string at the same position where the part was removed (ie. at removeAndInsertStart).
     *
     * @param str - the original string.
     * @param removeAndInsertStart - the index within the original string at which removal is to start.
     * @param removeEnd - the index within the original string at which removal is to end (exclusive - ie. does not
     * remove the character at that index).
     * @param insertStr - the string to be inserted.
     */
    public final static String removeAndInsert(String str, int removeAndInsertStart, int removeEnd, String insertStr) {
        //Take the part of the str before removeAndInsertStart, the part including and after removeEnd, and add
        //the insertStr between those two parts...
        String partBefore = str.substring(0, removeAndInsertStart);
        String partAfter = str.substring(removeEnd);

        str = partBefore + insertStr + partAfter;

        return str;
    }

    /**
     * Escape chars that need slashes in front of them.
     * @param s the String to add escape characters to
     * @return the converted String
     */
    public final static String slashes(String s) {
        s = noNull(s);

        StringBuffer str = new StringBuffer();
        char[] chars = s.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if ((chars[i] == '\\') || (chars[i] == '\"') || (chars[i] == '\'')) {
                str.append('\\');
            }

            str.append(chars[i]);
        }

        return str.toString();
    }

    /**
     * Check whether <code>string</code> has been set to
     * something other than <code>""</code> or <code>null</code>.
     * @param string the <code>String</code> to check
     * @return a boolean indicating whether the string was non-empty (and non-null)
     */
    public final static boolean stringSet(String string) {
        return (string != null) && !"".equals(string);
    }

    /**
     * Trim a String to the specified length. However, if the cutoff
     * point is in the middle of a sentence (remember that a String can contain many
     * sentences), trim the string such that it ends with an ending char, which is
     * defined in the isEndingChar() method.
     *
     * @param str - String to trim.
     * @param len - length to which string is to be trimmed.
     * @return the trimmed string
     */
    public final static String trimToEndingChar(String str, int len) {
        boolean inTag = false;
        boolean anyTags = false;
        String result = "";
        int goodChars = 0;
        int lastEndingCharPos = -1;

        if (str.length() < len) {
            return str;
        }

        char[] strA = str.toCharArray();

        for (int i = 0; i < strA.length; i++) {
            if ((strA[i] == '<') && !inTag) {
                anyTags = true;
                inTag = true;
            }

            if ((strA[i] == '>') && inTag) {
                inTag = false;
            }

            if (!inTag) {
                // loop through ending chars
                // if this char == ending char, record last seen
                if (isEndingChar(strA[i])) {
                    lastEndingCharPos = i;
                }

                goodChars++;
            }

            result += strA[i];

            if (goodChars == len) {
                break;
            }
        }

        // ok, now we have a string consisting of a bunch of tags and tagless characters.
        // we now see whether the last char is an ending char (by comparing lastEndingCharPos+1 with
        // the length of the string). If it is not, then it means that the end of the string is the middle
        // of some sentence in the original string. In this case, we would have to trim the string further so
        // that the end of the string corresponds to the end of some sentence, but keeping the length of the string
        // closest to the specified len. We do this by utilising lastEndingCharPos...
        if ((lastEndingCharPos + 1) != result.length()) {
            if (lastEndingCharPos != -1) {
                result = result.substring(0, lastEndingCharPos + 1);
            } else {
                // there aren't any ending chars...
                // best thing we could do is to trim the result to the nearest word...
                // if there aren't any spaces in the result, then we can do nothing at all.
                int spacePos = result.lastIndexOf(' ');

                if (spacePos != -1) {
                    result = result.substring(0, spacePos);
                }
            }
        }

        if (anyTags) {
            return closeTags(result); //Put closing tags and return the result...
        }

        return result;
    }

    /**
     * Verify that the given string is a valid email address.
     * "Validity" in this context only means that the address conforms
     * to the correct syntax (not if the address actually exists).
     *
     * @param email The email address to verify.
     * @return a boolean indicating whether the email address is correctly formatted.
     */
    public final static boolean verifyEmail(String email) {
        return MailUtils.verifyEmail(email);
    }

    /**
     * Verify That the given String is in valid URL format.
     * @param url The url string to verify.
     * @return a boolean indicating whether the URL seems to be incorrect.
     */
    public final static boolean verifyUrl(String url) {
        if (url == null) {
            return false;
        }

        if (url.startsWith("https://")) {
            // URL doesn't understand the https protocol, hack it
            url = "http://" + url.substring(8);
        }

        try {
            new URL(url);

            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    /**
     * Wrap paragraphs in html <code>&lt;p&gt;</code> tags.
     * Paragraphs are seperated by blank lines.
     * @param s the String to reformat
     * @return the reformatted string
     */
    public final static String wrapParagraph(String s) {
        s = noNull(s);

        StringBuffer result = new StringBuffer();
        result.append("<p>");

        char lastC = 0;
        char thisC;

        for (int i = 0; i < s.length(); i++) {
            thisC = s.charAt(i);

            if ((thisC == '\n') && (lastC == '\n')) {
                result.append("</p>\n\n<p>");
            } else {
                result.append(thisC);
            }

            lastC = thisC;
        }

        result.append("</p>");

        return result.toString();
    }

    /**
     * Determine if <code>c</code> is a valid end-of-sentence character.
     * Currently, only English characters are included.
     *
     * @param c a character to consider
     * @return whether the character is a valid end-of-sentence mark in English
     */
    private final static boolean isEndingChar(char c) {
        return ((c == '.') || (c == '!') || (c == ',') || (c == '?'));
    }

    /**
     * Get the starting index of a URL (either 'abc://' or 'www.')
     */
    private static final int getStartUrl(StringBuffer str, int startIndex) {
        int schemeIndex = getSchemeIndex(str, startIndex);
        final int wwwIndex = str.indexOf("www.", startIndex + 1);

        if ((schemeIndex == -1) && (wwwIndex == -1)) {
            return -1;
        } else if (schemeIndex == -1) {
            return wwwIndex;
        } else if (wwwIndex == -1) {
            return schemeIndex;
        }

        return Math.min(schemeIndex, wwwIndex);
    }

    private final static void linkEmail(StringBuffer str) {
        int lastEndIndex = -1; //Store the index position of the end char of last email address found...

main: 
        while (true) {
            // get index of '@'...
            int atIndex = str.indexOf("@", lastEndIndex + 1);

            if (atIndex == -1) {
                break;
            } else {
                //Get the whole email address...
                //Get the part before '@' by moving backwards and taking each character
                //until we encounter an invalid URL char...
                String partBeforeAt = "";
                int linkStartIndex = atIndex;
                boolean reachedStart = false;

                while (!reachedStart) {
                    linkStartIndex--;

                    if (linkStartIndex < 0) {
                        reachedStart = true;
                    } else {
                        char c = str.charAt(linkStartIndex);

                        //if we find these chars in an email, then it's part of a url, so lets leave it alone
                        //Are there any other chars we should abort email checking for??
                        if ((c == '?') || (c == '&') || (c == '=') || (c == '/') || (c == '%')) {
                            lastEndIndex = atIndex + 1;

                            continue main;
                        }

                        if (UrlUtils.isValidEmailChar(c)) {
                            partBeforeAt = c + partBeforeAt;
                        } else {
                            reachedStart = true;
                        }
                    }
                }

                //Increment linkStartIndex back by 1 to reflect the real starting index of the
                //email address...
                linkStartIndex++;

                //Get the part after '@' by doing pretty much the same except moving
                //forward instead of backwards.
                String partAfterAt = "";
                int linkEndIndex = atIndex;
                boolean reachedEnd = false;

                while (!reachedEnd) {
                    linkEndIndex++;

                    if (linkEndIndex == str.length()) {
                        reachedEnd = true;
                    } else {
                        char c = str.charAt(linkEndIndex);

                        if (UrlUtils.isValidEmailChar(c)) {
                            partAfterAt += c;
                        } else {
                            reachedEnd = true;
                        }
                    }
                }

                //Decrement linkEndIndex back by 1 to reflect the real ending index of the
                //email address...
                linkEndIndex--;

                //Reassemble the whole email address...
                String emailStr = partBeforeAt + '@' + partAfterAt;

                //If the last chars of emailStr is a '.', ':', '-', '/' or '~' then we exclude those chars.
                //The '.' at the end could be just a fullstop to a sentence and we don't want
                //that to be part of an email address (which would then be invalid).
                //Pretty much the same for the other symbols - we don't want them at the end of any email
                //address cos' this would stuff the address up.
                while (true) {
                    char lastChar = emailStr.charAt(emailStr.length() - 1);

                    if ((lastChar == '.') || (lastChar == ':') || (lastChar == '-') || (lastChar == '/') || (lastChar == '~')) {
                        emailStr = emailStr.substring(0, emailStr.length() - 1);
                        linkEndIndex--;
                    } else {
                        break;
                    }
                }

                //Verify if email is valid...
                if (verifyEmail(emailStr)) {
                    //Construct the hyperlink for the email address...
                    String emailLink = "<a href='mailto:" + emailStr + "'>" + emailStr + "</a>";

                    //Take the part of the str before the email address, the part after, and add
                    //the emailLink between those two parts, so that in effect the original email
                    //address is replaced by a hyperlink to it...
                    str.replace(linkStartIndex, linkEndIndex + 1, emailLink);

                    //Set lastEndIndex to reflect the position of the end of emailLink
                    //within the whole string...
                    lastEndIndex = (linkStartIndex - 1) + emailLink.length();
                } else {
                    //lastEndIndex is different from the one above cos' there's no
                    //<a href...> tags added...
                    lastEndIndex = (linkStartIndex - 1) + emailStr.length();
                }
            }
        }
    }

    private final static void linkURL(StringBuffer str, String target) {
        String urlToDisplay;

        int lastEndIndex = -1; //Stores the index position, within the whole string, of the ending char of the last URL found.

        String targetString = ((target == null) || (target.trim().length() == 0)) ? "" : (" target=\"" + target.trim() + '\"');

        while (true) {
            int linkStartIndex = getStartUrl(str, lastEndIndex);

            //if no more links found - then end the loop
            if (linkStartIndex == -1) {
                break;
            } else {
                //Get the whole URL...
                //We move forward and add each character to the URL string until we encounter
                //an invalid URL character (we assume that the URL ends there).
                int linkEndIndex = linkStartIndex;
                String urlStr = "";

                while (true) {
                    // if char at linkEndIndex is '&' then we look at the next 4 chars
                    // to see if they make up "&amp;" altogether. This is the html coded
                    // '&' and will pretty much stuff up an otherwise valid link becos of the ';'.
                    // We therefore have to remove it before proceeding...
                    if (str.charAt(linkEndIndex) == '&') {
                        if (((linkEndIndex + 6) <= str.length()) && "&quot;".equals(str.substring(linkEndIndex, linkEndIndex + 6))) {
                            break;
                        } else if (((linkEndIndex + 5) <= str.length()) && "&amp;".equals(str.substring(linkEndIndex, linkEndIndex + 5))) {
                            str.replace(linkEndIndex, linkEndIndex + 5, "&");
                        }
                    }

                    if (UrlUtils.isValidURLChar(str.charAt(linkEndIndex))) {
                        urlStr += str.charAt(linkEndIndex);
                        linkEndIndex++;

                        if (linkEndIndex == str.length()) { //Reached end of str...

                            break;
                        }
                    } else {
                        break;
                    }
                }

                //if the characters before the linkStart equal 'href="' then don't link the url - CORE-44
                if (linkStartIndex >= 6) { //6 = "href\"".length()

                    String prefix = str.substring(linkStartIndex - 6, linkStartIndex);

                    if ("href=\"".equals(prefix)) {
                        lastEndIndex = linkEndIndex;

                        continue;
                    }
                }

                //if the characters after the linkEnd are '</a>' then this url is probably already linked - CORE-44
                if (str.length() >= (linkEndIndex + 4)) { //4 = "</a>".length()

                    String suffix = str.substring(linkEndIndex, linkEndIndex + 4);

                    if ("</a>".equals(suffix)) {
                        lastEndIndex = linkEndIndex + 4;

                        continue;
                    }
                }

                //Decrement linkEndIndex back by 1 to reflect the real ending index position of the URL...
                linkEndIndex--;

                // If the last char of urlStr is a '.' we exclude it. It is most likely a full stop and
                // we don't want that to be part of an url.
                while (true) {
                    char lastChar = urlStr.charAt(urlStr.length() - 1);

                    if (lastChar == '.') {
                        urlStr = urlStr.substring(0, urlStr.length() - 1);
                        linkEndIndex--;
                    } else {
                        break;
                    }
                }

                //if the URL had a '(' before it, and has a ')' at the end, trim the last ')' from the url
                //ie '(www.opensymphony.com)' => '(<a href="http://www.openymphony.com/">www.opensymphony.com</a>)'
                char lastChar = urlStr.charAt(urlStr.length() - 1);

                if (lastChar == ')') {
                    if ((linkStartIndex > 0) && ('(' == (str.charAt(linkStartIndex - 1)))) {
                        urlStr = urlStr.substring(0, urlStr.length() - 1);
                        linkEndIndex--;
                    }
                } else if (lastChar == '\'') {
                    if ((linkStartIndex > 0) && ('\'' == (str.charAt(linkStartIndex - 1)))) {
                        urlStr = urlStr.substring(0, urlStr.length() - 1);
                        linkEndIndex--;
                    }
                }
                //perhaps we ended with '&gt;', '&lt;' or '&quot;'
                //We need to strip these
                //ie '&quot;www.opensymphony.com&quot;' => '&quot;<a href="http://www.openymphony.com/">www.opensymphony.com</a>&quot;'
                //ie '&lt;www.opensymphony.com&gt;' => '&lt;<a href="http://www.openymphony.com/">www.opensymphony.com</a>&gt;'
                else if (lastChar == ';') {
                    // 6 = "&quot;".length()
                    if ((urlStr.length() > 6) && "&quot;".equalsIgnoreCase(urlStr.substring(urlStr.length() - 6))) {
                        urlStr = urlStr.substring(0, urlStr.length() - 6);
                        linkEndIndex -= 6;
                    }
                    // 4 = "&lt;".length()  || "&gt;".length()
                    else if (urlStr.length() > 4) {
                        final String endingStr = urlStr.substring(urlStr.length() - 4);

                        if ("&lt;".equalsIgnoreCase(endingStr) || "&gt;".equalsIgnoreCase(endingStr)) {
                            urlStr = urlStr.substring(0, urlStr.length() - 4);
                            linkEndIndex -= 4;
                        }
                    }
                }

                // we got the URL string, now we validate it and convert it into a hyperlink...
                urlToDisplay = htmlEncode(urlStr);

                if (urlStr.toLowerCase().startsWith("www.")) {
                    urlStr = "http://" + urlStr;
                }

                if (UrlUtils.verifyHierachicalURI(urlStr, new String[] {
                                "javascript"
                            })) {
                    //Construct the hyperlink for the url...
                    String urlLink = "<a href=\"" + urlStr + '\"' + targetString + '>' + urlToDisplay + "</a>";

                    //Remove the original urlStr from str and put urlLink there instead...
                    str.replace(linkStartIndex, linkEndIndex + 1, urlLink);

                    //Set lastEndIndex to reflect the position of the end of urlLink
                    //within the whole string...
                    lastEndIndex = (linkStartIndex - 1) + urlLink.length();
                } else {
                    //lastEndIndex is different from the one above cos' there's no
                    //<a href...> tags added...
                    lastEndIndex = (linkStartIndex - 1) + urlStr.length();
                }
            }
        }
    }

    /**
     * Given a string, and the index to start looking at, find the index of the start of the scheme. Eg.
     * <pre>
     * getSchemeIndex("notes://abc", 0) -> 0
     * getSchemeIndex("abc notes://abc", 0) -> 4
     * </pre>
     * @param str    The string to search for
     * @param startIndex   Where to start looking at
     * @return The location the string was found, ot -1 if the string was not found.
     */
    private static int getSchemeIndex(StringBuffer str, int startIndex) {
        int schemeIndex = str.indexOf(UrlUtils.SCHEME_URL, startIndex + 1);

        //if it was not found, or found at the start of the string, then return 'not found'
        if (schemeIndex <= 0) {
            return -1;
        }

        //walk backwards through the scheme until we find the first non valid character
        int schemeStart;

        for (schemeStart = schemeIndex - 1; schemeStart >= 0; schemeStart--) {
            char currentChar = str.charAt(schemeStart);

            if (!UrlUtils.isValidSchemeChar(currentChar)) {
                break;
            }
        }

        //reset the scheme to the starting character
        schemeStart++;

        // we don't want to do this, otherwise an invalid scheme would ruin the linking for later schemes
        //        if (UrlUtils.isValidScheme(str.substring(schemeStart, schemeIndex)))
        //            return schemeStart;
        //        else
        //            return -1;
        return schemeStart;
    }
}
