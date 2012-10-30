/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import com.opensymphony.module.propertyset.memory.MemoryPropertySet;
import com.opensymphony.util.Data;
import com.opensymphony.util.XMLUtils;


/**
 * Date: Mar 21, 2004
 * Time: 4:50:09 PM
 *
 * @author hani
 */
public class PropertiesFilePropertySet extends MemoryPropertySet {
    //~ Static fields/initializers /////////////////////////////////////////////

    protected static Log log = LogFactory.getLog(PropertiesFilePropertySet.class);

    //~ Instance fields ////////////////////////////////////////////////////////

    private File file;

    //~ Methods ////////////////////////////////////////////////////////////////

    public void flush() throws IOException {
        Iterator iter = getMap().entrySet().iterator();
        Properties p = new Properties();

        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String name = (String) entry.getKey();
            ValueEntry valueEntry = (ValueEntry) entry.getValue();
            String value;

            switch (valueEntry.getType()) {
            case XML:
                value = XMLUtils.print((Document) valueEntry.getValue());

                break;

            case PROPERTIES:
            case OBJECT:

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(bos);
                os.writeObject(valueEntry.getValue());

                byte[] data = bos.toByteArray();
                value = new String(Base64.encodeBase64(data));

                break;

            case DATE:
                value = DateFormat.getDateTimeInstance().format((Date) valueEntry.getValue());

                break;

            case DATA:

                if (valueEntry.getValue() instanceof byte[]) {
                    value = new String(Base64.encodeBase64((byte[]) valueEntry.getValue()));
                } else {
                    value = new String(Base64.encodeBase64(((Data) valueEntry.getValue()).getBytes()));
                }

                break;

            default:
                value = valueEntry.getValue().toString();
            }

            p.put(name + "." + valueEntry.getType(), value);
        }

        p.store(new FileOutputStream(file), null);
    }

    @Override
    public void init(Map config, Map args) {
        super.init(config, args);
        file = new File((String) args.get("file"));

        if (file.exists()) {
            try {
                load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void load() throws IOException, ClassNotFoundException {
        Properties p = new Properties();
        p.load(new FileInputStream(file));

        Iterator iter = p.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            int dot = key.lastIndexOf('.');
            int type = Integer.parseInt(key.substring(dot + 1));
            String name = key.substring(0, dot);

            if (log.isDebugEnabled()) {
                log.debug("key=" + key + " type=" + type + " name=" + name);
            }

            String data = (String) entry.getValue();
            Object value = null;

            switch (type) {
            case STRING:
            case TEXT:
                value = data;

                break;

            case INT:
                value = new Integer(data);

                break;

            case LONG:
                value = new Long(data);

                break;

            case DOUBLE:
                value = new Double(data);

                break;

            case BOOLEAN:
                value = new Boolean(data);

                break;

            case DATA:
                value = Base64.decodeBase64(data.getBytes());

                break;

            case DATE:

                try {
                    value = DateFormat.getDateTimeInstance().parse(data);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;

            case PROPERTIES:
            case OBJECT:

                byte[] bytes = Base64.decodeBase64(data.getBytes());
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInputStream is = new ObjectInputStream(bis);
                value = is.readObject();

                break;

            case XML:

                try {
                    value = XMLUtils.parse(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            default:
                throw new IOException("Unsupported type " + type);
            }

            getMap().put(name, new ValueEntry(type, value));
        }
    }
}
