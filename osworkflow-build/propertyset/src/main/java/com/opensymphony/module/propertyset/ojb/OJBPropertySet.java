/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.ojb;

import com.opensymphony.module.propertyset.AbstractPropertySet;
import com.opensymphony.module.propertyset.PropertyException;
import com.opensymphony.module.propertyset.PropertySet;

import com.opensymphony.util.Data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.ojb.broker.PBFactoryException;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.*;

import java.util.*;


/**
 * @author picard
 * Created on 11 sept. 2003
 */
public class OJBPropertySet extends AbstractPropertySet {
    //~ Static fields/initializers /////////////////////////////////////////////

    private static final Log log = LogFactory.getLog(OJBPropertySet.class);

    //~ Instance fields ////////////////////////////////////////////////////////

    String globalKey = null;

    //~ Methods ////////////////////////////////////////////////////////////////

    public Collection getKeys(String prefix, int type) throws PropertyException {
        PersistenceBroker broker = null;
        ArrayList list = new ArrayList();

        try {
            broker = this.getBroker();

            if (prefix == null) {
                prefix = "";
            }

            Criteria criteria = new Criteria();
            criteria.addEqualTo("globalKey", globalKey);
            criteria.addLike("itemKey", prefix);

            if (type != 0) {
                criteria.addEqualTo("itemType", new Integer(type));
            }

            ReportQueryByCriteria q = QueryFactory.newReportQuery(OJBPropertySetItem.class, criteria);
            q.setColumns(new String[] {"itemKey"});

            Iterator iter = broker.getReportQueryIteratorByQuery(q);

            while (iter.hasNext()) {
                Object[] obj = (Object[]) iter.next();

                String itemKey = (String) obj[0];

                list.add(itemKey);
            }
        } catch (Exception e) {
            log.error("An exception occured", e);

            throw new PropertyException(e.getMessage());
        } finally {
            broker.close();
        }

        return list;
    }

    public int getType(String key) throws PropertyException {
        PersistenceBroker broker = null;
        int type = 0;

        try {
            broker = this.getBroker();

            Criteria criteria = new Criteria();
            criteria.addEqualTo("globalKey", globalKey);
            criteria.addLike("itemKey", key);

            ReportQueryByCriteria q = QueryFactory.newReportQuery(OJBPropertySetItem.class, criteria);
            q.setColumns(new String[] {"itemType"});

            Iterator iter = broker.getReportQueryIteratorByQuery(q);

            if (iter.hasNext()) {
                Object[] obj = (Object[]) iter.next();

                type = ((Integer) obj[0]).intValue();
            }
        } catch (Exception e) {
            log.error("An exception occured", e);

            throw new PropertyException(e.getMessage());
        } finally {
            broker.close();
        }

        return type;
    }

    public boolean exists(String key) throws PropertyException {
        return getType(key) != 0;
    }

    public void init(Map config, Map args) {
        // args
        globalKey = (String) args.get("globalKey");
    }

    public void remove() throws PropertyException {
        PersistenceBroker broker = null;

        try {
            broker = this.getBroker();

            Criteria criteria = new Criteria();
            criteria.addEqualTo("globalKey", globalKey);

            Query query = new QueryByCriteria(OJBPropertySetItem.class, criteria);

            broker.delete(query);
        } catch (Exception e) {
            log.error("An exception occured", e);

            throw new PropertyException(e.getMessage());
        } finally {
            broker.close();
        }
    }

    public void remove(String key) throws PropertyException {
        PersistenceBroker broker = null;

        try {
            broker = this.getBroker();

            Criteria criteria = new Criteria();
            criteria.addEqualTo("globalKey", globalKey);
            criteria.addEqualTo("itemKey", key);

            Query query = new QueryByCriteria(OJBPropertySetItem.class, criteria);

            broker.delete(query);
        } catch (Exception e) {
            log.error("An exception occured", e);

            throw new PropertyException(e.getMessage());
        } finally {
            broker.close();
        }
    }

    protected void setImpl(int type, String key, Object value) throws PropertyException {
        PersistenceBroker broker = null;

        if (value == null) {
            throw new PropertyException("OJBPropertySet does not allow for null values to be stored");
        }

        try {
            broker = this.getBroker();

            OJBPropertySetItem newProperty = new OJBPropertySetItem();
            newProperty.setItemType(type);
            newProperty.setGlobalKey(globalKey);
            newProperty.setItemKey(key);

            switch (type) {
            case PropertySet.BOOLEAN:

                Boolean boolVal = (Boolean) value;
                newProperty.setLongValue(boolVal.booleanValue() ? 1 : 0);

                break;

            case PropertySet.DATE:
                newProperty.setDateValue((Date) value);

                break;

            case PropertySet.DOUBLE:

                Double dblValue = (Double) value;
                newProperty.setDoubleValue(dblValue.doubleValue());

                break;

            case PropertySet.LONG:

                Long lngValue = (Long) value;
                newProperty.setLongValue(lngValue.longValue());

                break;

            case PropertySet.INT:

                Integer intValue = (Integer) value;
                newProperty.setLongValue(intValue.longValue());

                break;

            case PropertySet.STRING:
                newProperty.setStringValue((String) value);

                break;

            case PropertySet.DATA:

                Data data = (Data) value;
                newProperty.setByteValue(data.getBytes());

                break;

            default:
                throw new PropertyException("This type : " + type + ", isn't supported!");
            }

            broker.store(newProperty);
        } catch (Exception e) {
            log.error("An exception occured", e);

            throw new PropertyException(e.getMessage());
        } finally {
            broker.close();
        }
    }

    protected Object get(int type, String key) throws PropertyException {
        PersistenceBroker broker = null;
        Object value = null;

        try {
            broker = this.getBroker();

            Criteria criteria = new Criteria();
            criteria.addEqualTo("globalKey", globalKey);
            criteria.addLike("itemKey", key);

            Query query = new QueryByCriteria(OJBPropertySetItem.class, criteria);

            OJBPropertySetItem item = (OJBPropertySetItem) broker.getObjectByQuery(query);

            switch (type) {
            case PropertySet.BOOLEAN:

                if (item.getLongValue() == 1) {
                    value = new Boolean(true);
                } else {
                    value = new Boolean(false);
                }

                break;

            case PropertySet.DATE:
                value = item.getDateValue();

                break;

            case PropertySet.DOUBLE:
                value = new Double(item.getDoubleValue());

                break;

            case PropertySet.LONG:
                value = new Long(item.getLongValue());

                break;

            case PropertySet.INT:
                value = new Integer((int) item.longValue);

                break;

            case PropertySet.STRING:
                value = item.getStringValue();

                break;

            case PropertySet.DATA:
                value = item.getByteValue();

                break;

            default:
                throw new PropertyException("Type " + type + " is not supported");
            }
        } catch (Exception e) {
            log.error("Could not get value for key " + key + " of type " + type, e);
            throw new PropertyException(e.getMessage());
        } finally {
            broker.close();
        }

        return value;
    }

    // get an instance.
    private PersistenceBroker getBroker() throws PBFactoryException {
        PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();

        return broker;
    }
}
