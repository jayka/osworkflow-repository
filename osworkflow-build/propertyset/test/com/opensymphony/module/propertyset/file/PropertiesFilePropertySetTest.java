/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.file;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.module.propertyset.TestObject;

import com.opensymphony.util.XMLUtils;

import junit.framework.TestCase;

import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;

import java.text.DateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;


/**
 * User: bbulger
 * Date: May 24, 2004
 */
public class PropertiesFilePropertySetTest extends TestCase {
    //~ Instance fields ////////////////////////////////////////////////////////

    protected final String PROPERTIES_FILE = "build/filePropertySet.properties";
    protected PropertySet ps;
    protected final String TEXT_VALUE = "12345678901234567890123456789012345678901234567890" + "12345678901234567890123456789012345678901234567890" + "12345678901234567890123456789012345678901234567890" + "12345678901234567890123456789012345678901234567890" + "12345678901234567890123456789012345678901234567890" + "12345678901234567890123456789012345678901234567890";

    //~ Methods ////////////////////////////////////////////////////////////////

    public void testFlushAndLoad() throws IOException, ParserConfigurationException {
        // populate the propertyset
        Date now = new Date();
        DateFormat df = DateFormat.getInstance();
        Document doc = XMLUtils.newDocument("test-propertiesfile");
        Properties embeddedProps = new Properties();
        embeddedProps.put("prop1", "value1");

        ps.setBoolean("testBoolean", true);
        ps.setData("testData", "value1".getBytes());
        ps.setDate("testDate", now);
        ps.setDouble("testDouble", 10.245);
        ps.setInt("testInt", 7);
        ps.setLong("testLong", 100000);
        ps.setObject("testObject", new TestObject(1));
        ps.setProperties("testProperties", embeddedProps);
        ps.setString("testString", "value1");
        ps.setText("testText", TEXT_VALUE);
        ps.setXML("testXml", doc);

        ((PropertiesFilePropertySet) ps).flush();

        // reload and test
        HashMap args = new HashMap();
        args.put("file", PROPERTIES_FILE);
        ps = new PropertiesFilePropertySet();
        ps.init(null, args);

        assertEquals(true, ps.getBoolean("testBoolean"));
        assertEquals(new String("value1".getBytes()), new String(ps.getData("testData")));
        assertEquals(df.format(now), df.format(ps.getDate("testDate")));
        assertEquals(10.245, ps.getDouble("testDouble"), 0);
        assertEquals(7, ps.getInt("testInt"));
        assertEquals(100000, ps.getLong("testLong"));
        assertEquals(new TestObject(1), ps.getObject("testObject"));
        assertEquals(embeddedProps, ps.getProperties("testProperties"));
        assertEquals("value1", ps.getString("testString"));
        assertEquals(TEXT_VALUE, ps.getText("testText"));
        assertNotNull(ps.getXML("testXml"));
    }

    protected void setUp() throws Exception {
        HashMap args = new HashMap();
        args.put("file", PROPERTIES_FILE);
        ps = new PropertiesFilePropertySet();
        ps.init(null, args);
    }

    protected void tearDown() throws Exception {
        File file = new File(PROPERTIES_FILE);

        if (file.exists()) {
            file.delete();
        }
    }
}
