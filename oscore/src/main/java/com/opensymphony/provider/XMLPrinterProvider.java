/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.provider;

import org.w3c.dom.Document;

import java.io.IOException;
import java.io.Writer;


/**
 * Provider for pretty printing XML DOM documents to a stream.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 5 $
 *
 * @see com.opensymphony.util.XMLUtils
 */
public interface XMLPrinterProvider extends Provider {
    //~ Methods ////////////////////////////////////////////////////////////////

    void print(Document doc, Writer out) throws IOException;
}
