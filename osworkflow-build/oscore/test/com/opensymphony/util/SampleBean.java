/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.util;

import java.util.Locale;


/**
 * Mock object for BeanUtilsTest
 *
 * @version $Revision: 100 $
 */
public class SampleBean {
    //~ Instance fields ////////////////////////////////////////////////////////

    Locale locale = null;
    String empty = null;
    String label = null;
    String name = null;
    boolean visible = false;

    //~ Methods ////////////////////////////////////////////////////////////////

    public String getEmpty() {
        return empty;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }
}
