/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.util;

import junit.framework.*;

import java.util.Locale;


/**
 * JUnit test suite for BeanUtils... accessing properties.
 *
 * Simple AWT javabeans are used for testing purposes.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 100 $
 */
public class BeanUtilsTest extends TestCase {
    //~ Instance fields ////////////////////////////////////////////////////////

    SampleBean sample = new SampleBean();

    //~ Constructors ///////////////////////////////////////////////////////////

    public BeanUtilsTest(String name) {
        super(name);
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * test getFoo().getBar()
     */

    //    public void testCompositeGetters() throws Exception {
    //       assertEquals("arial", BeanUtils.getValue(sample, "font.name"));
    //        assertEquals(new Integer(Font.BOLD), BeanUtils.getValue(sample, "font.style"));
    //    }

    /**
     * test composite setters (eg. x.getBlah().setFoo())
     */
    public void testCompositeSetters() throws Exception {
        A a = new A();
        B b = new B();
        a.setB(b);
        b.setName("test1");
        assertEquals("test1", a.getB().getName());
        assertEquals("test1", BeanUtils.getValue(a, "b.name"));
        assertTrue(BeanUtils.setValue(a, "b.name", "newname"));
        assertEquals("newname", BeanUtils.getValue(a, "b.name"));
        assertEquals("newname", a.getB().getName());
        assertTrue(BeanUtils.setValue(a, "b.name", "test1"));
        assertEquals("test1", BeanUtils.getValue(a, "b.name"));
    }

    /**
     * test getters that return null
     */
    public void testEmptyGetters() throws Exception {
        assertNull(BeanUtils.getValue(sample, "empty"));
    }

    /**
     * test getters that don't exist, or contain invalid chars
     */
    public void testInvalidGetters() throws Exception {
        assertNull(BeanUtils.getValue(sample, "xcsdfs"));
        assertNull(BeanUtils.getValue(sample, "_()"));
        assertNull(BeanUtils.getValue(sample, ""));
        assertNull(BeanUtils.getValue(sample, null));
    }

    /**
     * test setters that don't exist.
     */
    public void testInvalidSetters() throws Exception {
        assertTrue(BeanUtils.setValue(sample, "label", "My label"));
        assertTrue(!BeanUtils.setValue(sample, "xcsdfs", "dfsd"));
        assertTrue(!BeanUtils.setValue(sample, "_()", "sdfd"));
        assertTrue(!BeanUtils.setValue(sample, "", ""));
        assertTrue(!BeanUtils.setValue(sample, null, null));
        sample.setLabel("my sample");
    }

    /**
     * test isBlah() instead of getBlah()
     */
    public void testIsGetters() throws Exception {
        assertEquals(Boolean.FALSE, BeanUtils.getValue(sample, "visible"));
    }

    /**
     * test setting null values
     */
    public void testNullSetters() throws Exception {
        assertTrue(BeanUtils.setValue(sample, "label", null));
        assertNull(sample.getLabel());
    }

    /**
     * test available property names for a class.
     */
    public void testPropertyNames() throws Exception {
        String[] buttonPs = BeanUtils.getPropertyNames(sample);
        assertTrue(arrayContains(buttonPs, "label"));
        assertTrue(arrayContains(buttonPs, "visible"));
        assertTrue(!arrayContains(buttonPs, "dfds"));
        assertTrue(!arrayContains(buttonPs, "NAME"));
        assertTrue(!arrayContains(buttonPs, "Name"));

        String[] aPs = BeanUtils.getPropertyNames(new A());
        String[] bPs = BeanUtils.getPropertyNames(new B());
        assertTrue(arrayContains(aPs, "b"));
        assertTrue(arrayContains(bPs, "name"));
    }

    /**
     * test simple getBlah() methods
     */
    public void testSimpleGetters() throws Exception {
        sample.setLabel("my sample");
        sample.setLocale(Locale.CANADA);
        assertEquals("my sample", BeanUtils.getValue(sample, "label"));
        assertEquals(Locale.CANADA, BeanUtils.getValue(sample, "locale"));
    }

    /**
     * test simple setBlah() methods.
     */
    public void testSimpleSetters() throws Exception {
        assertEquals("my sample", sample.getLabel());
        assertTrue(BeanUtils.setValue(sample, "label", "new sample"));
        assertEquals("new sample", sample.getLabel());
        sample.setLabel("my sample");

        assertEquals(Locale.CANADA, sample.getLocale());
        assertTrue(BeanUtils.setValue(sample, "locale", Locale.ENGLISH));
        assertEquals(Locale.ENGLISH, sample.getLocale());
        sample.setLocale(Locale.CANADA);

        sample.setVisible(true);
        assertTrue(sample.isVisible());
        assertTrue(BeanUtils.setValue(sample, "visible", Boolean.FALSE));
        assertTrue(!sample.isVisible());
        sample.setVisible(true);
    }

    protected void setUp() throws Exception {
        sample = new SampleBean();
        sample.setLabel("my sample");
        sample.setLocale(Locale.CANADA);
    }

    /**
     * Check if array contains particular value.
     */
    private boolean arrayContains(String[] array, String value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) {
                return true;
            }
        }

        return false;
    }

    //~ Inner Classes //////////////////////////////////////////////////////////

    public class A {
        private B b;

        public void setB(B b) {
            this.b = b;
        }

        public B getB() {
            return b;
        }
    }

    public class B {
        private String name;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
