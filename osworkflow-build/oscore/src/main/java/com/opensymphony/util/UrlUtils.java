/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.util;


/**
 * A class for validating URLs according to RFC 2396.  Used mainly for
 * finding and linking URLs within text blogs.
 *
 * @author $author$
 * @version $Revision: 153 $
 */
public class UrlUtils {
    //~ Static fields/initializers /////////////////////////////////////////////

    public static final String SCHEME_URL = "://";

    //~ Constructors ///////////////////////////////////////////////////////////

    private UrlUtils() {
        //no instances created.
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Acceptable characters in a URI, but are reserved according to RFC 2396 Section 2.2
     */
    public final static boolean isAcceptableReservedChar(char c) {
        return (c == ';') || (c == '/') || (c == '?') || (c == ':') || (c == '@') || (c == '&') || (c == '=') || (c == '+') || (c == '$') || (c == ',');
    }

    public final static boolean isAlpha(char c) {
        return ((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z'));
    }

    public final static boolean isDigit(char c) {
        return ((c >= '0') && (c <= '9'));
    }

    /**
     * Other characters which are 'delims' according to RFC 2396 Section 2.4.3, but we include them anyhow
     */
    public final static boolean isOtherChar(char c) {
        return (c == '#') || (c == '%');
    }

    /**
     *  Unreserved characters in a URI, according to RFC 2396 Section 2.3
     */
    public final static boolean isUnreservedChar(char c) {
        return (c == '-') || (c == '_') || (c == '.') || (c == '!') || (c == '~') || (c == '*') || (c == '\'') || (c == '(') || (c == ')');
    }

    public final static boolean isValidEmailChar(char c) {
        //RFC 1035 Section 2.3.1 specifies letter, digit, '-' or '.' for domain names.  '_' is assumed as allowed for email addresses
        return (isAlpha(c) || isDigit(c) || (c == '_') || (c == '-') || (c == '.'));
    }

    /**
     * According to RFC 2396 Section 3.1, a valid scheme is:
     * <pre>
     * scheme        = alpha *( alpha | digit | "+" | "-" | "." )
     * </pre>
     * @param scheme    The scheme name (without '://')
     * @return  true    if the string is a valid scheme name
     */
    public final static boolean isValidScheme(String scheme) {
        if ((scheme == null) || (scheme.length() == 0)) {
            return false;
        }

        char[] schemeChars = scheme.toCharArray();

        // /test for first alpha character
        if (!isAlpha(schemeChars[0])) {
            return false;
        }

        //test the rest of the scheme
        for (int i = 1; i < schemeChars.length; i++) {
            char schemeChar = schemeChars[i];

            if (!(isValidSchemeChar(schemeChar))) {
                return false;
            }
        }

        //all tests passed
        return true;
    }

    public final static boolean isValidSchemeChar(char c) {
        return isAlpha(c) || isDigit(c) || (c == '+') || (c == '-') || (c == '.');
    }

    public final static boolean isValidURLChar(char c) {
        return isAlpha(c) || isDigit(c) || isAcceptableReservedChar(c) || isUnreservedChar(c) || isOtherChar(c);
    }

    /**
     * Validate a URL according to RFC 2396
     */
    public static boolean verifyHierachicalURI(String uri) {
        return verifyHierachicalURI(uri, null);
    }

    /**
     * Validate an URI according to RFC 2396.  It will use schemesConsideredInvalid to indicate that
     * URI schemes are considered invalid.  For example javascript: might be considered harmful
     * in some cases.
     *
     * @param uri                      the URI string to check
     * @param schemesConsideredInvalid an array of Strings of URI schemes considered invalid.  Accepts null, matches case insensitive.
     * @return true if the URI is valid according to RFC 2396 and does not have one of the invalid  schemes
     */
    public static boolean verifyHierachicalURI(String uri, String[] schemesConsideredInvalid) {
        if ((uri == null) || (uri.length() < SCHEME_URL.length())) {
            return false;
        }

        int schemeUrlIndex = uri.indexOf(SCHEME_URL);

        if (schemeUrlIndex == -1) {
            return false;
        }

        final String scheme = uri.substring(0, schemeUrlIndex);

        if (!isValidScheme(scheme)) {
            return false;
        }

        // ok check if the caller doesnt want this scheme
        if (schemesConsideredInvalid != null) {
            for (int i = 0; i < schemesConsideredInvalid.length; i++) {
                String invalidScheme = schemesConsideredInvalid[i];

                if (scheme.equalsIgnoreCase(invalidScheme)) {
                    return false;
                }
            }
        }

        //ensure that the URL has at least one character after '://'
        if (uri.length() < (schemeUrlIndex + SCHEME_URL.length() + 1)) {
            return false;
        }

        for (int i = schemeUrlIndex + SCHEME_URL.length(); i < uri.length();
                i++) {
            char c = uri.charAt(i);

            if (!isValidURLChar(c)) {
                return false;
            }
        }

        return true;
    }
}
