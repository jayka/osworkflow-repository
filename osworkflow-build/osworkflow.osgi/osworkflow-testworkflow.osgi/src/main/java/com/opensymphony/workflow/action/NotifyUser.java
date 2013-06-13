package com.opensymphony.workflow.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.ClassLoaderUtil;
import com.opensymphony.workflow.spi.Step;
import com.opensymphony.workflow.util.caller.CallerInputRetriever;
import com.opensymphony.workflow.util.caller.CallerRetriever;

public class NotifyUser implements FunctionProvider {

    private static final Log LOG = LogFactory.getLog(NotifyUser.class);

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void execute(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
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

        List<Step> currentSteps = (List<Step>)transientVars.get("currentSteps");

        String caller = callerRetriever.findCaller(transientVars, args, ps);

        LOG.info(caller + " wants to notify the owner " + currentSteps.get(0).getOwner());
    }

}
