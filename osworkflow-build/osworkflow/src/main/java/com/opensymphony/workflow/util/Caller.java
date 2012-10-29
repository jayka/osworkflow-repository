/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.workflow.util;

import java.util.Map;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.loader.ClassLoaderUtil;
import com.opensymphony.workflow.util.caller.CallerInputRetriever;
import com.opensymphony.workflow.util.caller.CallerRetriever;


/**
 * Sets the transient variable "caller" to the current user executing an action.
 *
 * @author <a href="mailto:plightbo@hotmail.com">Pat Lightbody</a>
 * @version $Revision: 1.4 $
 */
public class Caller implements FunctionProvider {
    //~ Methods ////////////////////////////////////////////////////////////////

    public void execute(Map transientVars, Map args, PropertySet ps) {

        CallerRetriever callerRetriever = null;
        try {
            if(args.containsKey("caller.retriever")) {
                String className = (String)args.get("caller.retriever");
                Class<CallerRetriever> clazz = ClassLoaderUtil.loadClass(className.trim(), getClass());
                callerRetriever = clazz.newInstance();
            }
        } catch(Exception e) { }
        if(callerRetriever == null)
            callerRetriever = new CallerInputRetriever();

        transientVars.put("caller", callerRetriever.findCaller(transientVars, args, ps));
    }
}
