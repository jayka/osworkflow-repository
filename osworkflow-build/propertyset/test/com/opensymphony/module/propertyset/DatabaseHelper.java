/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset;

import com.mckoi.database.jdbc.MSQLException;

import junit.framework.Assert;

import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.tool.hbm2ddl.SchemaExport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

import java.net.URL;

import java.sql.Connection;
import java.sql.Statement;

import javax.naming.InitialContext;

import javax.sql.DataSource;


/**
 * @author Eric Pugh
 *
 * This helper class populates a test mckoi database.
 */
public class DatabaseHelper {
    //~ Static fields/initializers /////////////////////////////////////////////

    private static Log log = LogFactory.getLog(DatabaseHelper.class);

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Create the database by loading a URL pointing at a SQL script.
     */
    public static void createDatabase(URL url) {
        Assert.assertNotNull("Database url is null", url);

        try {
            String sql = getDatabaseCreationScript(url);
            createDatabase(sql);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Create a new database and initialize it with the specified sql script.
     * @param sql the sql to execute
     */
    public static void createDatabase(String sql) {
        Connection connection;
        Statement statement = null;
        String sqlLine = null;

        try {
            InitialContext context = new InitialContext();
            DataSource ds = (DataSource) context.lookup("jdbc/CreateDS");
            connection = ds.getConnection();
            statement = connection.createStatement();

            String[] sqls = sql.split(";");

            for (int i = 0; i < sqls.length; i++) {
                sqlLine = sqls[i].trim();
                sqlLine.replace("\r", "");
                sqlLine.replace("\n", "");

                //String s = sqls[i];
                if ((sqlLine.length() > 0) && (sqlLine.charAt(0) != '#')) {
                    try {
                        statement.executeQuery(sqlLine);
                    } catch (MSQLException e) {
                        if (sqlLine.toLowerCase().indexOf("drop") == -1) {
                            log.error("Error executing " + sqlLine, e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Database creation error.  sqlLine:" + sqlLine, e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (Exception ex) {
                    //not catch
                }
            }
        }
    }

    /**
     * Use the default Hibernate *.hbm.xml files.  These build the primary keys
     * based on an identity or sequence, whatever is native to the database.
     * @throws Exception
     */
    public static Configuration createHibernateConfiguration() throws Exception {
        Configuration configuration = new Configuration();

        //cfg.addClass(HibernateHistoryStep.class);
        URL propertySet = DatabaseHelper.class.getResource("/com/opensymphony/module/propertyset/hibernate/PropertySetItemImpl.hbm.xml");
        Assert.assertTrue(propertySet != null);
        configuration.addURL(propertySet);
        configuration.setProperty("hibernate.dialect", "net.sf.hibernate.dialect.MckoiDialect");
        new SchemaExport(configuration).create(false, true);

        return configuration;
    }

    private static String getDatabaseCreationScript(URL url) throws IOException {
        InputStreamReader reader = new InputStreamReader(url.openStream());
        StringBuffer sb = new StringBuffer(100);
        int c = 0;

        while (c > -1) {
            c = reader.read();

            if (c > -1) {
                sb.append((char) c);
            }
        }

        return sb.toString();
    }
}
