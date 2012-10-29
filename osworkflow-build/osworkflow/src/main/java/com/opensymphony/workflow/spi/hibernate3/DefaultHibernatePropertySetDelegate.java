package com.opensymphony.workflow.spi.hibernate3;

import com.opensymphony.module.propertyset.*;
import com.opensymphony.module.propertyset.hibernate3.DefaultHibernateConfigurationProvider;
import com.opensymphony.workflow.util.PropertySetDelegate;
import org.hibernate.SessionFactory;

import java.util.HashMap;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class DefaultHibernatePropertySetDelegate implements PropertySetDelegate {
    //~ Instance fields ////////////////////////////////////////////////////////

    private SessionFactory sessionFactory;

    //~ Constructors ///////////////////////////////////////////////////////////

    public DefaultHibernatePropertySetDelegate() {
        super();
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public PropertySet getPropertySet(long entryId) {
        HashMap args = new HashMap();
        args.put("entityName", "OSWorkflowEntry");
        args.put("entityId", new Long(entryId));

        DefaultHibernateConfigurationProvider configurationProvider = new DefaultHibernateConfigurationProvider();
        configurationProvider.setSessionFactory(getSessionFactory());

        args.put("configurationProvider", configurationProvider);

        return PropertySetManager.getInstance("hibernate3", args);
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}