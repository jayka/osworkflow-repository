package com.opensymphony.workflow.util.osgi;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.TargetSource;
import org.springframework.aop.support.AopUtils;

import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.Register;
import com.opensymphony.workflow.Validator;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.util.SpringTypeResolver;

public class OSGiSpringTypeResolver extends SpringTypeResolver {

    private static final Logger LOG = LoggerFactory.getLogger(OSGiSpringTypeResolver.class);

    protected List<WorkflowDescriptor> workflowDescriptors;

    public OSGiSpringTypeResolver() {
        this(false);
    }

    public OSGiSpringTypeResolver(boolean initDescriptorList) {
        super();
        logClassLoadErrors = false;
        if(initDescriptorList)
            workflowDescriptors = new ArrayList<WorkflowDescriptor>();
    }

    public List<WorkflowDescriptor> getWorkflowDescriptors() {
        return workflowDescriptors;
    }

    public void setWorkflowDescriptors(List<WorkflowDescriptor> workflowDescriptors) {
        this.workflowDescriptors = workflowDescriptors;
    }

    @SuppressWarnings("rawtypes")
    public synchronized void bindDescriptor(WorkflowDescriptor descriptor, Map properties) {
        if (AopUtils.isCglibProxy(descriptor)) {
            try {
                descriptor = (WorkflowDescriptor) findSpringTargetSource(descriptor);
            } catch (Exception e) {
                LOG.debug("Can't extract target object from proxied one", e);
            }
        }
        workflowDescriptors.add(descriptor);
    }

    @SuppressWarnings("rawtypes")
    public synchronized void unbindDescriptor(WorkflowDescriptor descriptor, Map properties) {
        if (AopUtils.isCglibProxy(descriptor)) {
            try {
                descriptor = (WorkflowDescriptor) findSpringTargetSource(descriptor);
            } catch (Exception e) {
                LOG.debug("Can't extract target object from proxied one", e);
            }
        }
        workflowDescriptors.remove(descriptor);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public FunctionProvider getFunction(String type, Map args) throws WorkflowException {
        FunctionProvider function = super.getFunction(type, args);
        if(function == null) {
            String className = (String) args.get(Workflow.CLASS_NAME);
            function = (FunctionProvider)loadObjectWithBundleClassloader(className);
        }
        return function;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Condition getCondition(String type, Map args) throws WorkflowException {
        Condition condition = super.getCondition(type, args);
        if(condition == null) {
            String className = (String) conditions.get(type);

            if (className == null) {
                className = (String) args.get(Workflow.CLASS_NAME);
                condition = (Condition)loadObjectWithBundleClassloader(className);
            }
        }

        return condition;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Register getRegister(String type, Map args) throws WorkflowException {
        Register register = super.getRegister(type, args);
        if(register == null) {
            String className = (String) registers.get(type);

            if (className == null) {
                className = (String) args.get(Workflow.CLASS_NAME);
                register = (Register)loadObjectWithBundleClassloader(className);
            }
        }

        return register;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Validator getValidator(String type, Map args) throws WorkflowException {
        Validator validator = super.getValidator(type, args);
        if(validator == null) {
            String className = (String) validators.get(type);

            if (className == null) {
                className = (String) args.get(Workflow.CLASS_NAME);
                validator = (Validator)loadObjectWithBundleClassloader(className);
            }
        }
        return validator;
    }

    protected Object loadObjectWithBundleClassloader(String className) {
        Object ret = null;
        if(className != null) {
            synchronized (this) {
                for(WorkflowDescriptor descriptor : workflowDescriptors) {
                    if (AopUtils.isCglibProxy(descriptor)) {
                        try {
                            descriptor = (WorkflowDescriptor) findSpringTargetSource(descriptor);
                        } catch (Exception e) {
                            LOG.debug("Can't extract target object from proxied one", e);
                        }
                    }
                    ret = loadObject(className, descriptor.getClass().getClassLoader(), false);
                    if(ret != null)
                        break;
                }
            }
        }
        return ret;
    }

    protected Object findSpringTargetSource(Object proxied) {
        Method[] methods = proxied.getClass().getDeclaredMethods();
        Method targetSourceMethod = findTargetSourceMethod(methods);
        targetSourceMethod.setAccessible(true);
        try {
            Object ret = targetSourceMethod.invoke(proxied);
            if(ret instanceof TargetSource)
                ret = ((TargetSource)ret).getTarget();
            return ret;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Method findTargetSourceMethod(Method[] methods) {
        for (Method method : methods) {
            if (method.getName().endsWith("getTargetSource") || method.getName().endsWith("getWrappedObject")) {
                return method;
            }
        }
        throw new IllegalStateException("Could not find target source method on proxied object");
    }

}
