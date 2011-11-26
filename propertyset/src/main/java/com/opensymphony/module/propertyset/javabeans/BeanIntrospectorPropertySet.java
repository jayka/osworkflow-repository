/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.javabeans;

import com.opensymphony.module.propertyset.*;

import java.beans.*;

import java.util.*;


/**
 * PropertySet wrapper for any javabean.
 * Dynamically looks up all bean properties (those exposed by get/setXXX) and invokes
 * them on the getXXX/setXXX propertyset methods.
 * <p>
 *
 * <b>Required Args</b>
 * <ul>
 *  <li><b>bean</b> - any Object that can be introspected</li>
 * </ul>
 * Date: Dec 16, 2001
 * Time: 6:20:00 PM
 * @author Hani Suleiman
 */
public class BeanIntrospectorPropertySet extends AbstractPropertySet {
    //~ Instance fields ////////////////////////////////////////////////////////

    private Map descriptors = new HashMap();
    private Object bean = null;

    //~ Methods ////////////////////////////////////////////////////////////////

    public void setBean(Object bean) throws PropertyImplementationException {
        this.bean = bean;

        try {
            BeanInfo info = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] beanDescriptors = info.getPropertyDescriptors();

            for (int i = 0; i < beanDescriptors.length; i++) {
                PropertyDescriptor beanDescriptor = beanDescriptors[i];
                descriptors.put(beanDescriptor.getName(), beanDescriptor);
            }
        } catch (IntrospectionException e) {
            throw new PropertyImplementationException("Object is not a bean", e);
        }
    }

    public Collection getKeys(String prefix, int type) throws PropertyException {
        Collection keys = new ArrayList();
        Iterator iter = descriptors.values().iterator();

        while (iter.hasNext()) {
            PropertyDescriptor descriptor = (PropertyDescriptor) iter.next();

            if (((prefix == null) || descriptor.getName().startsWith(prefix)) && ((type == 0) || (getType(descriptor.getName()) == type))) {
                keys.add(descriptor.getName());
            }
        }

        return keys;
    }

    public boolean isSettable(String property) {
        PropertyDescriptor descriptor = (PropertyDescriptor) descriptors.get(property);

        return (descriptor != null) && (descriptor.getWriteMethod() != null);
    }

    public int getType(String key) throws PropertyException {
        PropertyDescriptor descriptor = (PropertyDescriptor) descriptors.get(key);

        if (descriptor == null) {
            throw new PropertyException("No key " + key + " found");
        }

        Class c = descriptor.getPropertyType();

        if ((c == Integer.TYPE) || (c == Integer.class)) {
            return PropertySet.INT;
        }

        if ((c == Long.TYPE) || (c == Long.class)) {
            return PropertySet.LONG;
        }

        if ((c == Double.TYPE) || (c == Double.class)) {
            return PropertySet.DOUBLE;
        }

        //XXX Shouldn't this be TEXT?
        if (c == String.class) {
            return PropertySet.STRING;
        }

        if ((c == Boolean.TYPE) || (c == Boolean.class)) {
            return PropertySet.BOOLEAN;
        }

        if (c == byte[].class) {
            return PropertySet.DATA;
        }

        if (java.util.Date.class.isAssignableFrom(c)) {
            return PropertySet.DATE;
        }

        if (java.util.Properties.class.isAssignableFrom(c)) {
            return PropertySet.PROPERTIES;
        }

        return PropertySet.OBJECT;
    }

    public boolean exists(String key) throws PropertyException {
        return descriptors.get(key) != null;
    }

    public void init(Map config, Map args) {
        Object bean = args.get("bean");
        setBean(bean);
    }

    public void remove() throws PropertyException {
        //no-op, doesn't make sense to remove bean properties
    }

    public void remove(String key) throws PropertyException {
        throw new PropertyImplementationException("Remove not supported in BeanIntrospectorPropertySet, use setXXX(null) instead");
    }

    protected void setImpl(int type, String key, Object value) throws PropertyException {
        if (getType(key) != type) {
            throw new InvalidPropertyTypeException(key + " is not of type " + type);
        }

        PropertyDescriptor descriptor = (PropertyDescriptor) descriptors.get(key);

        try {
            Object result = descriptor.getWriteMethod().invoke(bean, new Object[] {
                    value
                });
        }
        //pretty lame way of doing this, but I'm lazy
         catch (NullPointerException ex) {
            throw new PropertyImplementationException("Property " + key + " is read-only");
        } catch (Exception ex) {
            throw new PropertyImplementationException("Cannot invoke write method for key " + key, ex);
        }
    }

    protected Object get(int type, String key) throws PropertyException {
        if (getType(key) != type) {
            throw new InvalidPropertyTypeException(key + " is not of type " + type);
        }

        PropertyDescriptor descriptor = (PropertyDescriptor) descriptors.get(key);

        try {
            Object result = descriptor.getReadMethod().invoke(bean, new Object[0]);

            return result;
        }
        //pretty lame way of doing this, but I'm lazy
         catch (NullPointerException ex) {
            throw new PropertyImplementationException("Property " + key + " is write-only");
        } catch (Exception ex) {
            throw new PropertyImplementationException("Cannot invoke read method for key " + key, ex);
        }
    }
}
