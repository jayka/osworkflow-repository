/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.util;

import junit.framework.*;

import java.util.Calendar;

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
import java.util.Date;


/**
 * JUnit test case for TextUtils
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 153 $
 */
public class TextUtilsTest extends TestCase {
    //~ Constructors ///////////////////////////////////////////////////////////

    public TextUtilsTest(String name) {
        super(name);
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public void testBr() {
        assertEquals("abc<br/>\ndef", TextUtils.br("abc\ndef"));
        assertEquals("abc<br/>\n<br/>\ndef", TextUtils.br("abc\n\ndef"));
    }

    public void testCloseTags() {
        //		assertEquals( "", TextUtils.closeTags(""));
        //		assertEquals( "test", TextUtils.closeTags("test"));
        //		assertEquals( "<b>test</b>", TextUtils.closeTags("<b>test"));
        //		assertEquals( "<b>test</b>", TextUtils.closeTags("<b>test</b>"));
        //		assertEquals( "<b><b><b><b>test</b></b></b></b>", TextUtils.closeTags("<b><b><b><b>test"));
        //		assertEquals( "<b><b><font><b>test</b>test</font></b></b>", TextUtils.closeTags("<b><b><font><b>test</b>test"));
        //		assertEquals( "<a href=\"\">test</a>", TextUtils.closeTags("<a href=\"\">test"));
        //		assertEquals( "<a href=\"\" foobar >test</a>", TextUtils.closeTags("<a href=\"\" foobar >test"));
        //		assertEquals( "<a href=\"\"><b><b>test</b><p>fred<b></b></b></a>", TextUtils.closeTags("<a href=\"\"><b><b>test</b><p>fred<b>"));
        //		String html = "<A href=\"http://203.111.125.138/aicbeta/r/article/jsp/sid/253905\"><B><FONT size=3>ACA Impressed With Growth of Wireless Industry Over Past Year</FONT></B></A><BR>Dramatic change in Australia?s mobile network infrastructure this year has paved the way for a far more competitive and healthy market in 2001 and beyond, the Australian Communications Authority has concluded in its latest telecommunications industry checkup.";
        //		assertEquals( html, TextUtils.closeTags(html));
    }

    public void testExtractNumber() {
        assertEquals("12345", TextUtils.extractNumber(" 12345"));
        assertEquals("123", TextUtils.extractNumber("hello123bye"));
        assertEquals("2468", TextUtils.extractNumber("a2b4c6 8 "));
        assertEquals("-22", TextUtils.extractNumber(" -22"));
        assertEquals("5.512", TextUtils.extractNumber("5.512"));
        assertEquals("1.234", TextUtils.extractNumber("1.2.3.4"));
        assertEquals("0.2", TextUtils.extractNumber(".2"));
        assertEquals("-555.7", TextUtils.extractNumber("-555.7"));
        assertEquals("-0.6", TextUtils.extractNumber("-..6"));
        assertEquals("-0.979", TextUtils.extractNumber("abc- dx.97 9"));
        assertEquals("1000000.00", TextUtils.extractNumber("$1,000,000.00 per year"));
        assertEquals("0", TextUtils.extractNumber(""));
        assertEquals("0", TextUtils.extractNumber("asdf"));
        assertEquals("123", TextUtils.extractNumber("123."));
        assertEquals("0", TextUtils.extractNumber(null));
    }

    public void testHtmlEncode() {
        assertEquals("test&amp;", TextUtils.htmlEncode("test&"));
        assertEquals("&lt;test&lt;", TextUtils.htmlEncode("<test<"));
        assertEquals("&gt;test&gt;", TextUtils.htmlEncode(">test>"));
        assertEquals("&quot;test&quot;", TextUtils.htmlEncode("\"test\""));
        assertEquals("\u0445test\u0445", TextUtils.htmlEncode("\u0445test\u0445"));
        assertEquals("\u0445\u0642test\u0445", TextUtils.htmlEncode("\u0445\u0642test\u0445"));

        String input = "http://www.xxx.com/getStuff?stuff_id=12&readonly=true";
        String expectedResult = "http://www.xxx.com/getStuff?stuff_id=12&amp;readonly=true";
        assertEquals(expectedResult, TextUtils.htmlEncode(input));
    }

    public void testHyperLink() {
        //email within hyperlink - CORE-47
        String s = "http://www.someserver.com/index.jsp?assignee=test@test.com&version=2";
        assertEquals("Urls with email addresses don't work", "<a href=\"" + s + "\">" + "http://www.someserver.com/index.jsp?assignee=test@test.com&amp;version=2" + "</a>", TextUtils.hyperlink(s));
        assertEquals("http://<hostname>", TextUtils.hyperlink("http://<hostname>"));
        assertEquals("http://<hostname>/<foo>", TextUtils.hyperlink("http://<hostname>/<foo>"));
    }

    public void testInnerTrim() {
        assertEquals("", TextUtils.innerTrim("  "));
        assertEquals("hithere", TextUtils.innerTrim(" hithere"));
        assertEquals("hithere", TextUtils.innerTrim("hithere "));
        assertEquals("hi there", TextUtils.innerTrim("hi  there"));
        assertEquals("hi there", TextUtils.innerTrim(" hi  there"));
        assertEquals("hi there", TextUtils.innerTrim("hi  there "));
        assertEquals("hi there", TextUtils.innerTrim("hi  there    "));
    }

    public void testLeadingSpaces() {
        assertEquals("&nbsp;&nbsp;foo\n&nbsp;bar\n", TextUtils.leadingSpaces("  foo\n bar\n"));
        assertEquals("foo\n&nbsp;bar\n\n", TextUtils.leadingSpaces("foo\n bar\n\n"));
        assertEquals("foo bar", TextUtils.leadingSpaces("foo bar"));
        assertEquals("\n&nbsp;f bar", TextUtils.leadingSpaces("\n f bar"));
    }

    public void testLinkEmail() {
        //Email addresses at the start of the text - CORE-52
        assertEquals("<a href='mailto:admin@opensymphony.com'>admin@opensymphony.com</a>", TextUtils.linkEmail("admin@opensymphony.com"));

        //emails surrounded by characters
        assertEquals("(<a href='mailto:admin@opensymphony.com'>admin@opensymphony.com</a>)", TextUtils.linkEmail("(admin@opensymphony.com)"));
        assertEquals("[<a href='mailto:admin@opensymphony.com'>admin@opensymphony.com</a>]", TextUtils.linkEmail("[admin@opensymphony.com]"));

        assertEquals("text <a href='mailto:admin@opensymphony.com'>admin@opensymphony.com</a>", TextUtils.linkEmail("text admin@opensymphony.com"));

        // Don't escape emails that form part of URLs.  Eg. the URLs generated by mod_mbox
        assertEquals("http://example.com/2005.mbox/foo@example.com%3e", TextUtils.linkEmail("http://example.com/2005.mbox/foo@example.com%3e"));
        assertEquals("http://mail-archives.apache.org/mod_mbox/incubator-general/200507.mbox/%3c67912d02ca6b0465f38ad27796d0b0c7@gbiv.com%3e", TextUtils.linkEmail("http://mail-archives.apache.org/mod_mbox/incubator-general/200507.mbox/%3c67912d02ca6b0465f38ad27796d0b0c7@gbiv.com%3e"));

        //don't link emails inside URLs
        assertEquals("text http://admin@opensymphony:abc.com", TextUtils.linkEmail("text http://admin@opensymphony:abc.com"));

        //check that funky email addresses still work
        //assertEquals("<a href='mailto:mail/Aforce%AFORCE@aforce.com'>", TextUtils.linkEmail("mail/Aforce%AFORCE@aforce.com")); - doesn't currently work.  Would require a rewrite of how we do emails.
    }

    public void testLinkURL() {
        //email in url test CORE-47
        assertEquals("<a href=\"http://www.someserver.com/index.jsp?assignee=test@test.com&version=2\">http://www.someserver.com/index.jsp?assignee=test@test.com&amp;version=2</a>", TextUtils.linkURL("http://www.someserver.com/index.jsp?assignee=test@test.com&version=2"));

        _testLinkUrl("https://www.opensymphony.com");
        _testLinkUrl("http://issues.locavista.com/jira/secure/SavePortlet!default.jspa?portletIdStr=SEARCHREQUEST"); //test exclamation marks
        _testLinkUrl("http://jakarta.apache.org/velocity/api/org/apache/velocity/app/VelocityEngine.html#mergeTemplate(java.lang.String,%20java.lang.String,%20org.apache.velocity.context.Context,%20java.io.Writer)");
        assertEquals("<a href=\"http://www.altavista.com\">www.altavista.com</a>", TextUtils.linkURL("www.altavista.com"));
        assertEquals(" <a href=\"http://cvsbook.red-bean.com/cvsbook.html#Commit_Emails\">http://cvsbook.red-bean.com/cvsbook.html#Commit_Emails</a> ", TextUtils.linkURL(" http://cvsbook.red-bean.com/cvsbook.html#Commit_Emails "));
        assertEquals("this is an <a href=\"https://www.yahoo.com\">https://www.yahoo.com</a> test", TextUtils.linkURL("this is an https://www.yahoo.com test"));
        assertEquals("this is a <a href=\"http://www.server.com/snipsnap/space/Misc+Server+Details+and+URLs\">http://www.server.com/snipsnap/space/Misc+Server+Details+and+URLs</a> test", TextUtils.linkURL("this is a http://www.server.com/snipsnap/space/Misc+Server+Details+and+URLs test"));
        assertEquals("<a href=\"http://www.opensymphony.com&a=b\">http://www.opensymphony.com&amp;a=b</a>", TextUtils.linkURL("http://www.opensymphony.com&amp;a=b")); //test url containing &amp;
        assertEquals(" \"<a href=\"http://www.opensymphony.com\">http://www.opensymphony.com</a>\" ", TextUtils.linkURL(" \"http://www.opensymphony.com\" "));

        // '$' is a valid URL char (CORE-45)
        assertEquals(" \"<a href=\"http://frontier.userland.com/discuss/msgReader$10806?mode=topic\">http://frontier.userland.com/discuss/msgReader$10806?mode=topic</a>\" ", TextUtils.linkURL(" \"http://frontier.userland.com/discuss/msgReader$10806?mode=topic\" "));

        //surround URLs
        assertEquals("(<a href=\"http://www.opensymphony.com\">http://www.opensymphony.com</a>)", TextUtils.linkURL("(http://www.opensymphony.com)")); //brackets test
        assertEquals("\"<a href=\"http://www.opensymphony.com\">http://www.opensymphony.com</a>\"", TextUtils.linkURL("\"http://www.opensymphony.com\"")); //quotes test
        assertEquals("'<a href=\"http://www.opensymphony.com\">http://www.opensymphony.com</a>'", TextUtils.linkURL("'http://www.opensymphony.com'")); //single quotes test
        assertEquals("&quot;<a href=\"http://www.opensymphony.com\">http://www.opensymphony.com</a>&quot;", TextUtils.linkURL("&quot;http://www.opensymphony.com&quot;")); //quotes test (CORE-45)
        assertEquals("&lt;<a href=\"http://www.opensymphony.com\">http://www.opensymphony.com</a>&gt;", TextUtils.linkURL("&lt;http://www.opensymphony.com&gt;")); //angle brackets test

        //Don't link already valid URLs - CORE-44
        String validUrl = "<a href=\"http://www.opensymphony.com\">http://www.opensymphony.com</a>";
        assertEquals(validUrl, TextUtils.linkURL(validUrl));

        String validUrl2 = "Click here for a URL: (<a href=\"http://www.opensymphony.com\">http://www.opensymphony.com?abcer</a>) ";
        assertEquals(validUrl2, TextUtils.linkURL(validUrl2));

        assertEquals("<a href=\"http://a\">http://a</a> abc <a href=\"ftp://b\">ftp://b</a> def <a href=\"http://www.xyz.com\">www.xyz.com</a>", TextUtils.linkURL("http://a abc ftp://b def www.xyz.com")); //multiple URLs test
        assertEquals("<a href=\"http://www.xyz.com\">www.xyz.com</a> <a href=\"http://a\">http://a</a> abc <a href=\"ftp://b\">ftp://b</a> def ", TextUtils.linkURL("www.xyz.com http://a abc ftp://b def ")); //multiple URLs test
        assertEquals("<a href=\"http://www.xyz.com\">www.xyz.com</a> 56a://a abc <a href=\"ftp://b\">ftp://b</a> def ", TextUtils.linkURL("www.xyz.com 56a://a abc ftp://b def ")); //multiple URLs test with invalid URL in middle
        assertEquals("abcdef", TextUtils.linkURL("abcdef")); //No URLs

        //Other schemes - CORE-50
        _testLinkUrl("notes://www.opensymphony.com");
        _testLinkUrl("file:///tmp/opensymphony/files");
        assertEquals("11le:///tmp/opensymphony/files", TextUtils.linkURL("11le:///tmp/opensymphony/files")); //invalid protocol

        //nullpointer exception with ampersands
        assertEquals("<a href=\"http://www.abcdef&12=4\">http://www.abcdef&amp;12=4</a>", TextUtils.linkURL("http://www.abcdef&12=4"));
        assertEquals("<a href=\"http://www.abcdef&\">http://www.abcdef&amp;</a>", TextUtils.linkURL("http://www.abcdef&amp;"));

        //problem with the whole string not being linked if the last url is 'http://' - CORE-65
        assertEquals("<a href=\"http://testUrl\">http://testUrl</a> http:// ", TextUtils.linkURL("http://testUrl http:// "));

        // colon is legal in the path component
        _testLinkUrl("http://foo.com/bar:baz");

        // dot on the end of a url is interpreted as a full stop for the sentence.
        assertEquals("Check out <a href=\"http://www.somewhere.com/foobar\">http://www.somewhere.com/foobar</a>.", TextUtils.linkURL("Check out http://www.somewhere.com/foobar."));

        // this is a way of putting the dot on the end of a url
        assertEquals("Check out '<a href=\"http://www.somewhere.com/url-ending-in-dot.\">http://www.somewhere.com/url-ending-in-dot.</a>'", TextUtils.linkURL("Check out 'http://www.somewhere.com/url-ending-in-dot.'"));

        // full stop should be removed
        assertEquals("Check out '<a href=\"http://www.somewhere.com\">http://www.somewhere.com</a>'.", TextUtils.linkURL("Check out 'http://www.somewhere.com'."));

        // CORE-76 trailing legal URL characters ':' '-' '~' should be included unlike "." which is still guessed to be a full stop
        _testLinkUrl("http://foo.com/bar:");
        _testLinkUrl("http://something.com/ending-in-minus-");
        _testLinkUrl("http://something.com/ending-in-tilda~");
        assertEquals("<a href=\"http://something.com/ending-in-minus-\">http://something.com/ending-in-minus-</a>", TextUtils.linkURL("http://something.com/ending-in-minus-"));
        assertEquals("<a href=\"http://something.com/ending-in-tilda~\">http://something.com/ending-in-tilda~</a>", TextUtils.linkURL("http://something.com/ending-in-tilda~"));

        // TODO: what about making these work?
        //        assertEquals("(yep <a href=\"http://www.opensymphony.com/\">http://www.opensymphony.com/</a>)", TextUtils.linkURL("(yep http://www.opensymphony.com/)")); //brackets test
        //        assertEquals("(yep <a href=\"http://www.opensymphony.com\">http://www.opensymphony.com</a>)", TextUtils.linkURL("(yep http://www.opensymphony.com)")); //brackets test
        //        assertEquals("(see <a href=\"http://something.com/ending-in-minus-\">http://something.com/ending-in-minus-</a>)", TextUtils.linkURL("(see http://something.com/ending-in-minus-)"));
    }

    public void testNulls() {
        assertEquals("", TextUtils.noNull(""));
        assertEquals("blah", TextUtils.noNull("blah"));
        assertEquals("la", TextUtils.noNull("la"));
        assertEquals("null", TextUtils.noNull("null"));
        assertEquals("", TextUtils.noNull(null));

        assertEquals("blah", TextUtils.noNull("", "blah"));
        assertEquals("blah", TextUtils.noNull("blah", "blah"));
        assertEquals("la", TextUtils.noNull("la", "blah"));
        assertEquals("null", TextUtils.noNull("null", "blah"));
        assertEquals("blah", TextUtils.noNull(null, "blah"));

        assertTrue("", !TextUtils.stringSet(""));
        assertTrue("blah", TextUtils.stringSet("blah"));
        assertTrue("la", TextUtils.stringSet("la"));
        assertTrue("null", TextUtils.stringSet("null"));
        assertTrue("(null)", !TextUtils.stringSet(null));
    }

    public void testParseDate() {
        _testParseDate(1978, 8, 25, "1978", "8", "25");
        _testParseDate(1978, 8, 25, "1978", "8", "25th");
        _testParseDate(1978, 8, 25, "1978", "aug", "25");
        _testParseDate(1978, 8, 25, "1978", "August", "25");
        _testParseDate(2000, 2, 1, "2000", "feb", "");
        _testParseDate(1930, 3, 1, "1930", "mar", null);
        _testParseDate(2050, 1, 1, "2050", null, null);
    }

    public void testPlainTextToHtml() {
        //Don't link already valid URLs - CORE-44
        //        String validUrl = "<a href=\"http://www.opensymphony.com\">http://www.opensymphony.com</a>";
        //        assertEquals(validUrl, TextUtils.plainTextToHtml(validUrl));
        //CORE-53 - escaping problems - quotes
        String input = "<foo rdf:datatype=\"http://abc.com\">12;</foo>";
        String expectedResult = "&lt;foo rdf:datatype=&quot;<a href=\"http://abc.com\">http://abc.com</a>&quot;&gt;12;&lt;/foo&gt;";
        assertEquals(expectedResult, TextUtils.plainTextToHtml(input));

        input = "http://www.xxx.com/getStuff?stuff_id=12&readonly=true";
        expectedResult = "<a href=\"http://www.xxx.com/getStuff?stuff_id=12&readonly=true\">http://www.xxx.com/getStuff?stuff_id=12&amp;readonly=true</a>";
        assertEquals(expectedResult, TextUtils.plainTextToHtml(input));
    }

    public void testPlainTextToHtmlNoJavaScriptByDefault() throws Exception {
        String input = "Here is some text that should not encode the javascript://link in anchor tags";
        String expected = input;
        assertEquals(expected, TextUtils.plainTextToHtml(input));

        input = "Here is some text that should not encode the javascript://link in anchor tags but it ok for this http://ok to be anchored";
        expected = "Here is some text that should not encode the javascript://link in anchor tags but it ok for this <a href=\"http://ok\">http://ok</a> to be anchored";
        assertEquals(expected, TextUtils.plainTextToHtml(input));
    }

    public void testVerifyEmail() {
        _testVerifyEmail("joe@truemesh.com", true);
        _testVerifyEmail("asf@aol.com", true);
        _testVerifyEmail("blah blah", false);
        _testVerifyEmail("la-de-da@blah-sdf.co.uk", true);
        _testVerifyEmail("df-se.xzsa@fdg.it", true);
        _testVerifyEmail("x xzsa@fdg.it", false);
        _testVerifyEmail("df-se.xzsa@fdg.it@blah.com", false);
        _testVerifyEmail("abcd-efddf.dfgr.erre-dfge@fdg.dfgdf-df.fdg4fd.com", true);
        _testVerifyEmail("a@b.cd", true);
        _testVerifyEmail("fred", false);
        _testVerifyEmail("", false);
        _testVerifyEmail(null, false);
    }

    public void testVerifyUrl() {
        _testVerifyUrl("https://www.opensymphony.com", true);
        _testVerifyUrl("http://www.opensymphony.com", true);
        _testVerifyUrl("ftp://www.opensymphony.com", true);
        _testVerifyUrl("abcdef://www.opensymphony.com", false);
        _testVerifyUrl("http://", true);
        _testVerifyUrl("http", false);
        _testVerifyUrl("http://abc.com:80", true);
        _testVerifyUrl(null, false);
    }

    private boolean _compareDates(Date a, Date b) {
        Calendar ca = Calendar.getInstance();
        Calendar cb = Calendar.getInstance();
        ca.setTime(a);
        cb.setTime(b);

        return (ca.get(Calendar.DAY_OF_MONTH) == cb.get(Calendar.DAY_OF_MONTH)) && (ca.get(Calendar.MONTH) == cb.get(Calendar.MONTH)) && (ca.get(Calendar.YEAR) == cb.get(Calendar.YEAR));
    }

    private void _testLinkUrl(String s) {
        assertEquals("<a href=\"" + s + "\">" + s + "</a>", TextUtils.linkURL(s));
    }

    private void _testParseDate(int yy, int mm, int dd, String y, String m, String d) {
        Calendar cal = Calendar.getInstance();
        cal.set(yy, mm - 1, dd);

        Date expected = cal.getTime();
        Date result = TextUtils.parseDate(y, m, d);
        String msg = "Expected <" + expected + "> but was <" + result + ">";
        assertTrue(msg, _compareDates(expected, result));
    }

    private void _testVerifyEmail(String email, boolean result) {
        assertTrue(email, TextUtils.verifyEmail(email) == result);
    }

    private void _testVerifyUrl(String url, boolean result) {
        assertTrue(url, TextUtils.verifyUrl(url) == result);
    }
}
