/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.xml;

import com.opensymphony.module.propertyset.TestObject;
import com.opensymphony.module.propertyset.memory.SerializablePropertySetTest;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import java.util.Date;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;


/**
 * User: bbulger
 * Date: May 23, 2004
 */
public class XMLPropertySetTest extends SerializablePropertySetTest {
    //~ Instance fields ////////////////////////////////////////////////////////

    protected final String XML_STRING = "<property-set>" + "<property key=\"testBoolean\" type=\"boolean\">true</property>" + "<property key=\"testData\" type=\"data\">dmFsdWUx</property>" + "<property key=\"testDate\" type=\"date\">2004-05-23 10:06:00</property>" + "<property key=\"testDouble\" type=\"double\">10.245</property>" + "<property key=\"testInt\" type=\"int\">7</property>" + "<property key=\"testLong\" type=\"long\">100000</property>" + "<property key=\"testObject\" type=\"object\">rO0ABXNyAC5jb20ub3BlbnN5bXBob255Lm1vZHVsZS5wcm9wZXJ0eXNldC5UZXN0T2JqZWN0A6KYOgP1WoYCAAFKAAJpZHhwAAAAAAAAAAE=</property>" + "<property key=\"testString\" type=\"string\">value1</property>" + "<property key=\"testProperties\" type=\"properties\">" + "<properties><property key=\"prop1\" type=\"string\">value1</property></properties>" + "</property>" + "<property key=\"testText\" type=\"text\"><![CDATA[" + TEXT_VALUE + "]]></property>" + "<property key=\"testUnknown\" type=\"unknown\">unknown</property>" + "</property-set>";

    //~ Methods ////////////////////////////////////////////////////////////////

    public void testLoadWithReader() throws IOException, ParserConfigurationException, SAXException {
        StringReader reader = new StringReader(XML_STRING);
        ((XMLPropertySet) ps).load(reader);
        assertEquals(true, ps.getBoolean("testBoolean"));
        assertEquals(new String("value1".getBytes()), new String(ps.getData("testData")));
        assertEquals(10.245D, ps.getDouble("testDouble"), 0);
        assertEquals(7, ps.getInt("testInt"));
        assertEquals(100000, ps.getLong("testLong"));
        assertEquals(new TestObject(1), ps.getObject("testObject"));
        assertEquals("value1", ps.getString("testString"));

        Properties props = new Properties();
        props.setProperty("prop1", "value1");
        assertEquals(props, ps.getProperties("testProperties"));
        assertEquals(TEXT_VALUE, ps.getText("testText"));
    }

    public void testSaveWithWriter() throws IOException, ParserConfigurationException {
        ps.setBoolean("testBoolean", true);
        ps.setData("testData", "value1".getBytes());
        ps.setDate("testDate", new Date());
        ps.setDouble("testDouble", 10.245D);
        ps.setInt("testInt", 7);
        ps.setLong("testLong", 100000);
        ps.setObject("testObject", new TestObject(1));

        Properties props = new Properties();
        props.setProperty("prop1", "value1");
        ps.setProperties("testProperties", props);
        ps.setString("testString", "value1");
        ps.setText("testText", TEXT_VALUE);

        StringWriter writer = new StringWriter();
        ((XMLPropertySet) ps).save(writer);
        assertNotNull(writer.toString());
    }

    protected void setUp() throws Exception {
        ps = new XMLPropertySet();
        ps.init(null, null);
    }
}
