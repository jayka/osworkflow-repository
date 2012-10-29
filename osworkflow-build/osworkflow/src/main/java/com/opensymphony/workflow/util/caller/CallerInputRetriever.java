package com.opensymphony.workflow.util.caller;

import java.util.Map;

import com.opensymphony.module.propertyset.PropertySet;

public class CallerInputRetriever extends ContextCallerRetriever {

    @Override
    public String findCaller(Map transientVars, Map args, PropertySet ps) {
        if(transientVars.containsKey("caller")) {
            try {
                return (String)transientVars.get("caller");
            } catch(Exception e){}
        }
        return super.findCaller(transientVars, args, ps);
    }

}
