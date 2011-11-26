/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.provider.bean;

import com.opensymphony.provider.BeanProvider;
import com.opensymphony.provider.ProviderConfigurationException;

import ognl.Ognl;
import ognl.OgnlException;


/**
 * BeanProvider implementation that uses OGNL for expression evaluation.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 5 $
 */
public class OGNLBeanProvider implements BeanProvider {
    //~ Methods ////////////////////////////////////////////////////////////////

    public boolean setProperty(Object object, String property, Object value) {
        if ((object == null) || (property == null)) {
            return false;
        }

        try {
            Ognl.setValue(property, object, value);

            return true;
        } catch (OgnlException e) {
            return false;
        }
    }

    public Object getProperty(Object object, String property) {
        if ((object == null) || (property == null)) {
            return null;
        }

        try {
            return Ognl.getValue(property, object);
        } catch (OgnlException e) {
            return null;
        }
    }

    public void destroy() {
    }

    public void init() throws ProviderConfigurationException {
    }
}
