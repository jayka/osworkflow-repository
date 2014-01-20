/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
/*
 * Created on 30-nov-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.opensymphony.workflow.loader;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StringType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class NotNullStringType extends StringType {
    //~ Constructors ///////////////////////////////////////////////////////////

    public NotNullStringType() {
        super();
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public Object get(ResultSet rs, String name, SessionImplementor session) throws SQLException {
        return "!void!".equals(super.get(rs, name, session)) ? "" : super.get(rs, name, session);
    }

    public void set(PreparedStatement st, String value, int index, SessionImplementor session) throws SQLException {
        if (!"".equals(value)) {
            super.set(st, (String)value, index, session);
        } else {
            super.set(st, "!void!", index, session);
        }
    }
}
