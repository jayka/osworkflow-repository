/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset;

import com.opensymphony.util.XMLUtils;

import junit.framework.TestCase;

import org.w3c.dom.Document;

import java.text.DateFormat;

import java.util.Date;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;


/**
 * User: bbulger
 * Date: May 22, 2004
 */
public abstract class AbstractPropertySetTest extends TestCase {
    //~ Instance fields ////////////////////////////////////////////////////////

    protected PropertySet ps;
    protected final String TEXT_VALUE = "12345678901234567890123456789012345678901234567890" + "12345678901234567890123456789012345678901234567890" + "12345678901234567890123456789012345678901234567890" + "12345678901234567890123456789012345678901234567890" + "12345678901234567890123456789012345678901234567890" + "12345678901234567890123456789012345678901234567890";

    //~ Methods ////////////////////////////////////////////////////////////////

    public void testAbstractPropertySetTypeMethods() {
        if (ps instanceof AbstractPropertySet) {
            assertEquals("boolean", ((AbstractPropertySet) ps).type(PropertySet.BOOLEAN));
            assertEquals("data", ((AbstractPropertySet) ps).type(PropertySet.DATA));
            assertEquals("date", ((AbstractPropertySet) ps).type(PropertySet.DATE));
            assertEquals("double", ((AbstractPropertySet) ps).type(PropertySet.DOUBLE));
            assertEquals("int", ((AbstractPropertySet) ps).type(PropertySet.INT));
            assertEquals("long", ((AbstractPropertySet) ps).type(PropertySet.LONG));
            assertEquals("object", ((AbstractPropertySet) ps).type(PropertySet.OBJECT));
            assertEquals("properties", ((AbstractPropertySet) ps).type(PropertySet.PROPERTIES));
            assertEquals("string", ((AbstractPropertySet) ps).type(PropertySet.STRING));
            assertEquals("text", ((AbstractPropertySet) ps).type(PropertySet.TEXT));
            assertEquals("xml", ((AbstractPropertySet) ps).type(PropertySet.XML));
            assertEquals(null, ((AbstractPropertySet) ps).type(-1));

            assertEquals(PropertySet.BOOLEAN, ((AbstractPropertySet) ps).type("boolean"));
            assertEquals(PropertySet.DATA, ((AbstractPropertySet) ps).type("data"));
            assertEquals(PropertySet.DATE, ((AbstractPropertySet) ps).type("date"));
            assertEquals(PropertySet.DOUBLE, ((AbstractPropertySet) ps).type("double"));
            assertEquals(PropertySet.INT, ((AbstractPropertySet) ps).type("int"));
            assertEquals(PropertySet.LONG, ((AbstractPropertySet) ps).type("long"));
            assertEquals(PropertySet.OBJECT, ((AbstractPropertySet) ps).type("object"));
            assertEquals(PropertySet.PROPERTIES, ((AbstractPropertySet) ps).type("properties"));
            assertEquals(PropertySet.STRING, ((AbstractPropertySet) ps).type("string"));
            assertEquals(PropertySet.TEXT, ((AbstractPropertySet) ps).type("text"));
            assertEquals(PropertySet.XML, ((AbstractPropertySet) ps).type("xml"));
            assertEquals(0, ((AbstractPropertySet) ps).type("unknown"));
            assertEquals(0, ((AbstractPropertySet) ps).type(null));
        }
    }

    public void testExistsOnPropertyInPropertySet() {
        ps.setString("test1", "value1");
        assertTrue(ps.exists("test1"));
    }

    public void testExistsOnPropertyNotInPropertySet() {
        assertFalse(ps.exists("test425"));
    }

    public void testGetKeys() {
        ps.setString("test1", "value1");
        ps.setString("test2", "value2");
        ps.setString("test3", "value3");
        assertEquals(3, ps.getKeys().size());
    }

    public void testGetKeysOfType() {
        if (ps.supportsTypes()) {
            ps.setString("test1", "value1");
            ps.setString("test2", "value2");
            ps.setInt("testInt", 14);
            assertEquals(2, ps.getKeys(PropertySet.STRING).size());
            assertEquals(1, ps.getKeys(PropertySet.INT).size());
        }
    }

    public void testGetKeysWithPrefix() {
        ps.setString("test1", "value1");
        ps.setString("test2", "value2");
        ps.setString("username", "user1");
        assertEquals(2, ps.getKeys("test").size());
        assertEquals(1, ps.getKeys("user").size());
    }

    public void testGetKeysWithPrefixOfType() {
        if (ps.supportsTypes()) {
            ps.setString("test1", "value1");
            ps.setString("test2", "value2");
            ps.setString("username", "user1");
            ps.setInt("testInt", 32);
            ps.setInt("usernum", 18);
            assertEquals(2, ps.getKeys("test", PropertySet.STRING).size());
            assertEquals(1, ps.getKeys("user", PropertySet.STRING).size());
            assertEquals(1, ps.getKeys("test", PropertySet.INT).size());
            assertEquals(1, ps.getKeys("user", PropertySet.INT).size());
        }
    }

    public void testGetStringNotInPropertySet() {
        assertNull(ps.getString("test555"));
    }

    public void testGetTypeForBoolean() {
        if (ps.supportsType(PropertySet.BOOLEAN)) {
            ps.setBoolean("testBoolean", true);
            assertEquals(PropertySet.BOOLEAN, ps.getType("testBoolean"));
        }
    }

    public void testGetTypeForData() {
        if (ps.supportsType(PropertySet.DATA)) {
            ps.setData("testData", "value2".getBytes());
            assertEquals(PropertySet.DATA, ps.getType("testData"));
        }
    }

    public void testGetTypeForDate() {
        if (ps.supportsType(PropertySet.DATE)) {
            ps.setDate("testDate", new Date());
            assertEquals(PropertySet.DATE, ps.getType("testDate"));
        }
    }

    public void testGetTypeForDouble() {
        if (ps.supportsType(PropertySet.DOUBLE)) {
            ps.setDouble("testDouble", 10.456D);
            assertEquals(PropertySet.DOUBLE, ps.getType("testDouble"));
        }
    }

    public void testGetTypeForInt() {
        if (ps.supportsType(PropertySet.INT)) {
            ps.setInt("testInt", 7);
            assertEquals(PropertySet.INT, ps.getType("testInt"));
        }
    }

    public void testGetTypeForLong() {
        if (ps.supportsType(PropertySet.LONG)) {
            ps.setLong("testLong", 7L);
            assertEquals(PropertySet.LONG, ps.getType("testLong"));
        }
    }

    public void testGetTypeForObject() {
        if (ps.supportsType(PropertySet.OBJECT)) {
            ps.setObject("testObject", new StringBuffer());
            assertEquals(PropertySet.OBJECT, ps.getType("testObject"));
        }
    }

    public void testGetTypeForProperties() {
        if (ps.supportsType(PropertySet.PROPERTIES)) {
            ps.setProperties("testProperties", new Properties());
            assertEquals(PropertySet.PROPERTIES, ps.getType("testProperties"));
        }
    }

    public void testGetTypeForString() {
        if (ps.supportsType(PropertySet.STRING)) {
            ps.setString("testString", "value7");
            assertEquals(PropertySet.STRING, ps.getType("testString"));
        }
    }

    public void testGetTypeForText() {
        if (ps.supportsType(PropertySet.TEXT)) {
            ps.setText("testText", TEXT_VALUE);
            assertEquals(PropertySet.TEXT, ps.getType("testText"));
        }
    }

    public void testGetTypeForXml() throws ParserConfigurationException {
        if (ps.supportsType(PropertySet.XML)) {
            Document doc = XMLUtils.newDocument();
            doc.appendChild(doc.createElement("root"));
            ps.setXML("testXml", doc);
            assertEquals(doc.getFirstChild().getNodeName(), ps.getXML("testXml").getFirstChild().getNodeName());
        }
    }

    public void testRemoveAllKeys() {
        ps.setString("test1", "value1");
        assertEquals(1, ps.getKeys().size());

        try {
            ps.remove();
            assertEquals(0, ps.getKeys().size());
        } catch (PropertyException e) {
            // this is ok too for read only PropertySets
        }
    }

    public void testRemoveSingleKey() {
        ps.setString("test1", "value1");
        assertEquals(1, ps.getKeys().size());

        try {
            ps.remove("test1");
            assertEquals(0, ps.getKeys().size());
        } catch (PropertyException e) {
            // this is ok too for read only PropertySets
        }
    }

    public void testSetAsActualTypeGetAsActualTypeForBoolean() {
        if (ps.supportsType(PropertySet.BOOLEAN)) {
            ps.setAsActualType("testBoolean", new Boolean(true));
            assertEquals(new Boolean(true), ps.getAsActualType("testBoolean"));
        }
    }

    public void testSetAsActualTypeGetAsActualTypeForData() {
        if (ps.supportsType(PropertySet.DATA)) {
            ps.setAsActualType("testData", "value1".getBytes());
            assertEquals(new String("value1".getBytes()), new String((byte[]) ps.getAsActualType("testData")));
        }
    }

    public void testSetAsActualTypeGetAsActualTypeForDate() {
        if (ps.supportsType(PropertySet.DATE)) {
            DateFormat df = DateFormat.getInstance();
            Date now = new Date();
            ps.setAsActualType("testDate", now);
            assertEquals(df.format(now), df.format(ps.getAsActualType("testDate")));
        }
    }

    public void testSetAsActualTypeGetAsActualTypeForDouble() {
        if (ps.supportsType(PropertySet.DOUBLE)) {
            ps.setAsActualType("testDouble", new Double(10.234));
            assertEquals(new Double(10.234), (Double) ps.getAsActualType("testDouble"));
        }
    }

    public void testSetAsActualTypeGetAsActualTypeForInt() {
        if (ps.supportsType(PropertySet.INT)) {
            ps.setAsActualType("testInt", new Integer(7));
            assertEquals(new Integer(7), ps.getAsActualType("testInt"));
        }
    }

    public void testSetAsActualTypeGetAsActualTypeForLong() {
        if (ps.supportsType(PropertySet.LONG)) {
            ps.setAsActualType("testLong", new Long(70000));
            assertEquals(new Long(70000), ps.getAsActualType("testLong"));
        }
    }

    public void testSetAsActualTypeGetAsActualTypeForObject() {
        if (ps.supportsType(PropertySet.OBJECT)) {
            TestObject testObject = new TestObject(2);
            ps.setAsActualType("testObject", testObject);
            assertEquals(testObject, (TestObject) ps.getAsActualType("testObject"));
        }
    }

    public void testSetAsActualTypeGetAsActualTypeForProperties() {
        if (ps.supportsType(PropertySet.PROPERTIES)) {
            Properties props = new Properties();
            props.setProperty("prop1", "value1");
            ps.setAsActualType("testProperties", props);
            assertEquals(props, ps.getAsActualType("testProperties"));
        }
    }

    public void testSetAsActualTypeGetAsActualTypeForString() {
        if (ps.supportsType(PropertySet.STRING)) {
            ps.setAsActualType("testString", "value1");
            assertEquals("value1", ps.getAsActualType("testString"));
        }
    }

    public void testSetAsActualTypeGetAsActualTypeForText() {
        if (ps.supportsType(PropertySet.TEXT)) {
            ps.setAsActualType("testText", TEXT_VALUE);
            assertEquals(TEXT_VALUE, ps.getAsActualType("testText"));
        }
    }

    public void testSetAsActualTypeGetAsActualTypeForXml() throws ParserConfigurationException {
        if (ps.supportsType(PropertySet.XML)) {
            Document doc = XMLUtils.newDocument();
            doc.appendChild(doc.createElement("root"));
            ps.setAsActualType("testXml", doc);
            assertEquals(doc.getFirstChild().getNodeName(), ps.getXML("testXml").getFirstChild().getNodeName());
        }
    }

    public void testSetBooleanGetBoolean() {
        if (ps.supportsType(PropertySet.BOOLEAN)) {
            ps.setBoolean("testBoolean", true);
            assertTrue(ps.getBoolean("testBoolean"));
            ps.setBoolean("testBoolean", false);
            assertFalse(ps.getBoolean("testBoolean"));
        }
    }

    public void testSetDataGetData() {
        if (ps.supportsType(PropertySet.DATA)) {
            ps.setData("testData", "value1".getBytes());
            assertEquals(new String("value1".getBytes()), new String(ps.getData("testData")));
            ps.setData("testData", "value2".getBytes());
            assertEquals(new String("value2".getBytes()), new String(ps.getData("testData")));
        }
    }

    public void testSetDateGetDate() {
        if (ps.supportsType(PropertySet.DATE)) {
            DateFormat df = DateFormat.getInstance();
            Date now = new Date();
            ps.setDate("testDate", now);
            assertEquals(df.format(now), df.format(ps.getDate("testDate")));
            ps.setDate("testDate", new Date());
            assertEquals(df.format(now), df.format(ps.getDate("testDate")));
        }
    }

    public void testSetDoubleGetDouble() {
        if (ps.supportsType(PropertySet.DOUBLE)) {
            ps.setDouble("testDouble", 1D);
            assertEquals(1D, ps.getDouble("testDouble"), 0);
            ps.setDouble("testDouble", 100000D);
            assertEquals(100000D, ps.getDouble("testDouble"), 0);
        }
    }

    public void testSetIntGetInt() {
        if (ps.supportsType(PropertySet.INT)) {
            ps.setInt("testInt", 7);
            assertEquals(7, ps.getInt("testInt"));
            ps.setInt("testInt", 11);
            assertEquals(11, ps.getInt("testInt"));
        }
    }

    public void testSetLongGetLong() {
        if (ps.supportsType(PropertySet.LONG)) {
            ps.setLong("testLong", 1L);
            assertEquals(1L, ps.getLong("testLong"));
            ps.setLong("testLong", 100000);
            assertEquals(100000, ps.getLong("testLong"));
        }
    }

    public void testSetObjectGetObject() {
        if (ps.supportsType(PropertySet.OBJECT)) {
            TestObject testObject = new TestObject(1);
            ps.setObject("testObject", testObject);
            assertEquals(testObject, ps.getObject("testObject"));
        }
    }

    public void testSetPropertiesGetProperties() {
        if (ps.supportsType(PropertySet.PROPERTIES)) {
            Properties props = new Properties();
            props.setProperty("prop1", "propValue1");
            ps.setProperties("testProperties", props);
            assertEquals(props, ps.getProperties("testProperties"));
            props.setProperty("prop2", "propValue2");
            ps.setProperties("testProperties", props);
            assertEquals(props, ps.getProperties("testProperties"));
        }
    }

    public void testSetStringGetStringLengthGreaterThan255() {
        try {
            ps.setString("testString", TEXT_VALUE);
            fail("Should not be able to setString() with a String longer than 255 chars.");
        } catch (IllegalPropertyException e) {
            // expected
        }
    }

    public void testSetStringGetStringLengthLessThan255() {
        ps.setString("testString", "value1");
        assertTrue("value1".equals(ps.getString("testString")));
        ps.setString("testString", "value2");
        assertTrue("value2".equals(ps.getString("testString")));
    }

    public void testSetTextGetText() {
        if (ps.supportsType(PropertySet.TEXT)) {
            ps.setText("testText", TEXT_VALUE);
            assertEquals(TEXT_VALUE, ps.getText("testText"));
            ps.setText("testText", TEXT_VALUE + "A");
            assertEquals(TEXT_VALUE + "A", ps.getText("testText"));
        }
    }

    public void testSetXmlGetXml() throws ParserConfigurationException {
        if (ps.supportsType(PropertySet.XML)) {
            Document doc = XMLUtils.newDocument();
            doc.appendChild(doc.createElement("root"));
            ps.setXML("testXml", doc);
            assertEquals(doc.getFirstChild().getNodeName(), ps.getXML("testXml").getFirstChild().getNodeName());
        }
    }
}
