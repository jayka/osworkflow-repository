package com.opensymphony.workflow.util;

import java.util.Map;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.util.ClassLoaderUtil;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowException;

public class RevertCondition implements Condition {

    @Override
    public boolean passesCondition(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
        String className = (String)args.get("orig.condition");
        if(className == null)
            throw new WorkflowException(new NullPointerException("Empty class name"));
        else {
            try {
                Class clazz = ClassLoaderUtil.loadClass(className.trim(), getClass());
                if(!Condition.class.isAssignableFrom(clazz))
                    throw new ClassCastException(className + " is not a Condition class");
                Condition condition = (Condition)clazz.newInstance();
                return !condition.passesCondition(transientVars, args, ps);
            } catch (Exception e) {
                throw new WorkflowException("Cannot load original condition class", e);
            }
        }
    }

}
