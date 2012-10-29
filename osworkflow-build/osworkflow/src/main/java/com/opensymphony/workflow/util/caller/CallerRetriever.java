package com.opensymphony.workflow.util.caller;

import java.util.Map;

import com.opensymphony.module.propertyset.PropertySet;

public interface CallerRetriever {

    public String findCaller(Map transientVars, Map args, PropertySet ps);

}
