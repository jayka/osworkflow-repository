/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.provider.xpath;

import com.opensymphony.provider.*;

import org.apache.xpath.XPathAPI;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.TransformerException;


/**
 * XPathProvider implementation that uses the XPath capabilities of Xalan 1.x.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 5 $
 */
public class XalanXPathProvider implements XPathProvider {
    //~ Methods ////////////////////////////////////////////////////////////////

    public Node getNode(Node base, String xpath) throws ProviderInvocationException {
        try {
            return XPathAPI.selectSingleNode(base, xpath);
        } catch (TransformerException e) {
            throw new ProviderInvocationException(e);
        }
    }

    public NodeList getNodes(Node base, String xpath) throws ProviderInvocationException {
        try {
            return XPathAPI.selectNodeList(base, xpath);
        } catch (TransformerException e) {
            throw new ProviderInvocationException(e);
        }
    }

    public void destroy() {
    }

    public void init() throws ProviderConfigurationException {
    }
}
