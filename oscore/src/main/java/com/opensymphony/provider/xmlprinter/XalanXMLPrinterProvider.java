/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.provider.xmlprinter;

import com.opensymphony.provider.ProviderConfigurationException;
import com.opensymphony.provider.XMLPrinterProvider;

import org.apache.xalan.serialize.SerializerToXML;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.IOException;
import java.io.Writer;


/**
 * XMLPrinterProvider implementation that uses the XML serializers built into Xalan 1.x
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 5 $
 */
public class XalanXMLPrinterProvider implements XMLPrinterProvider {
    //~ Methods ////////////////////////////////////////////////////////////////

    public void destroy() {
    }

    public void init() throws ProviderConfigurationException {
    }

    public void print(Document doc, Writer out) throws IOException {
        SerializerToXML serializer = new SerializerToXML() {
            public void serialize(Node node) throws IOException {
                m_doIndent = true;
                m_indentAmount = 2;
                super.serialize(node);
            }
        };

        serializer.setWriter(out);
        serializer.serialize(doc);
        out.flush();
    }
}
