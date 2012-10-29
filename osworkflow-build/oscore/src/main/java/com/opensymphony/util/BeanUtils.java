/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.util;

import com.opensymphony.provider.BeanProvider;
import com.opensymphony.provider.ProviderFactory;

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
import java.beans.*;

import java.util.*;

import javax.servlet.ServletRequest;


/**
 * Convenience methods for manipulating the getter/setter properties of JavaBeans
 * (well any kind of Object).
 *
 * <p>These methods are provided purely for convenience to frequent operations.
 * No exceptions are thrown from the methods.</p>
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 47 $
 */
public class BeanUtils {
    //~ Static fields/initializers /////////////////////////////////////////////

    private static final BeanProvider beanProvider;

    static {
        ProviderFactory factory = ProviderFactory.getInstance();
        providerModify();
        beanProvider = (BeanProvider) factory.getProvider("bean.provider", com.opensymphony.provider.bean.DefaultBeanProvider.class.getName());
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Get list of property names of bean.
     *
     * @param obj Object to query for property names.
     * @return Array of property names, or null if an error occurred.
     */
    public final static String[] getPropertyNames(Object obj) {
        try {
            BeanInfo info = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] properties = info.getPropertyDescriptors();
            String[] result = new String[properties.length];

            for (int i = 0; i < properties.length; i++) {
                result[i] = properties[i].getName();
            }

            return result;
        } catch (IntrospectionException e) {
            return null;
        }
    }

    /**
     * Set a single property of the bean.
     *
     * @param obj The object to be manipulated.
     * @param property Name of property to set.
     * @param value Value to set property to.
     * @return Boolean indicating success.
     */
    public final static boolean setValue(Object obj, String property, Object value) {
        return beanProvider.setProperty(obj, property, value);
    }

    /**
     * Get a single property of the bean.
     *
     * @param obj The object to be accessed.
     * @param property Name of property to get.
     * @return Value of property. If property was not found, null is returned.
     */
    public final static Object getValue(Object obj, String property) {
        return beanProvider.getProperty(obj, property);
    }

    /**
     * Set multiple properties of a bean at once using a Map. Any unknown properties
     * shall be ignored.
     *
     * @param obj The object to be manipulated.
     * @param valueMap Map containing property-name (String) / property-value (Object)
     *        pairs to set in the object.
     * @param allowedProperties If array is NOT null, only the properties matching names
     *        passed here shall be set.
     */
    public final static void setValues(Object obj, Map valueMap, String[] allowedProperties) {
        Iterator keys = valueMap.keySet().iterator();

        while (keys.hasNext()) {
            String property = keys.next().toString();
            Object value = valueMap.get(property);

            if (allowed(property, allowedProperties)) {
                setValue(obj, property, value);
            }
        }
    }

    /**
     * Set multiple properties of a bean at once using properties of another bean.
     * The beans may be of different types and any properties not common to both types
     * shall be ignored.
     *
     * @param obj The object to be manipulated.
     * @param src The object containing the properties to be copied.
     * @param allowedProperties If array is NOT null, only the properties matching names
     *        passed here shall be set.
     */
    public final static void setValues(Object obj, Object src, String[] allowedProperties) {
        setValues(obj, getValues(src, allowedProperties), allowedProperties);
    }

    /**
     * Set multiple properties of a bean at once using the params passed across
     * from the ServletRequest (useful for mapping HTML forms to beans). Any properties
     * not known shall be ignored.
     *
     * @param obj The object to be manipulated.
     * @param request ServletRequest to get params from.
     * @param allowedProperties If array is NOT null, only the properties matching names
     *        passed here shall be set.
     */
    public final static void setValues(Object obj, ServletRequest request, String[] allowedProperties) {
        Map params = new HashMap();
        Enumeration paramNames = request.getParameterNames();

        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            params.put(paramName, paramValue);
        }

        setValues(obj, params, allowedProperties);
    }

    /**
     * Get Map of property values from a bean.
     *
     * @param obj Object to query for properties.
     * @param allowedProperties If array is NOT null, only the properties matching names
     *        passed here shall be retrieved.
     * @return Map containing property-name (String) / property-value (Object) pairs.
     */
    public final static Map getValues(Object obj, String[] allowedProperties) {
        Map result = new HashMap();
        String[] propertyNames = getPropertyNames(obj);

        for (int i = 0; i < propertyNames.length; i++) {
            String propertyName = propertyNames[i];
            Object propertyValue = getValue(obj, propertyName);

            if ((propertyName == null) || (propertyValue == null)) {
                continue;
            }

            if (allowed(propertyName, allowedProperties)) {
                result.put(propertyName, propertyValue);
            }
        }

        return result;
    }

    private final static boolean allowed(String string, String[] array) {
        if (array == null) {
            return true;
        }

        if (string == null) {
            return false;
        }

        synchronized (array) {
            for (int i = 0; i < array.length; i++) {
                if (string.equals(array[i])) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Perform some custom modifications to the bean.provider for some special cases.
     */
    private final static void providerModify() {
        // some aliases for common providers so full class name need not be used
        {
            Map providerAliases = new HashMap();
            providerAliases.put("default", "com.opensymphony.provider.bean.DefaultBeanProvider");
            providerAliases.put("ognl", "com.opensymphony.provider.bean.OGNLBeanProvider");

            if ((System.getProperty("bean.provider") != null) && providerAliases.containsKey(System.getProperty("bean.provider"))) {
                System.setProperty("bean.provider", (String) providerAliases.get(System.getProperty("bean.provider")));
            }
        }
    }
}
