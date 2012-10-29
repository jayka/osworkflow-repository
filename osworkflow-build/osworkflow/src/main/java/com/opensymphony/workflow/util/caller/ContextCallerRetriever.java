package com.opensymphony.workflow.util.caller;

import java.util.Map;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowContext;

public class ContextCallerRetriever implements CallerRetriever {

    public String findCaller(Map transientVars, Map args, PropertySet ps) {
        WorkflowContext context = (WorkflowContext) transientVars.get("context");
        return context.getCaller();
    }

}
