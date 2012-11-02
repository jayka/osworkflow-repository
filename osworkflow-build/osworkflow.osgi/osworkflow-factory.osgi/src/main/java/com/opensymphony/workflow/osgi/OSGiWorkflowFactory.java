package com.opensymphony.workflow.osgi;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.workflow.FactoryException;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.loader.WorkflowFactory;

@Component(immediate = true)
@Reference(name = "service", referenceInterface = WorkflowDescriptor.class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
@Service(WorkflowFactory.class)
public class OSGiWorkflowFactory implements WorkflowFactory {

    protected static final Logger LOG = LoggerFactory.getLogger(OSGiWorkflowFactory.class);

    protected final List<ServiceReference> servicesBuffer;

    protected final Map<String, WorkflowDescriptor> workflowDescriptors;

    protected ComponentContext context;

    public OSGiWorkflowFactory() {
        servicesBuffer = new LinkedList<ServiceReference>();
        workflowDescriptors = new LinkedHashMap<String, WorkflowDescriptor>();
    }

    protected void bindService(ServiceReference serviceReference) {
        if(context == null) {
            // store the reference in the buffer and return
            servicesBuffer.add(serviceReference);
            return;
        }
        Object service = context.getBundleContext().getService(serviceReference);
        if(LOG.isDebugEnabled())
            LOG.debug("Bind component of type {} from bundle {}", service.getClass().getCanonicalName(), serviceReference.getBundle().getSymbolicName());
        registerService(serviceReference, service);
    }

    protected void unbindService(ServiceReference serviceReference) {
        Object service = context.getBundleContext().getService(serviceReference);
        if(LOG.isDebugEnabled())
            LOG.debug("Unbind component of type {} from bundle {}", service.getClass().getCanonicalName(), serviceReference.getBundle().getSymbolicName());
        unregisterService(serviceReference, service);
    }

    protected void registerService(ServiceReference serviceReference, Object service) {
        String workflowName = getWorkflowName(serviceReference, service);
        synchronized (workflowDescriptors) {
            workflowDescriptors.put(workflowName, (WorkflowDescriptor)service);
        }
    }

    protected void unregisterService(ServiceReference serviceReference, Object service) {
        String workflowName = getWorkflowName(serviceReference, service);
        synchronized (workflowDescriptors) {
            workflowDescriptors.remove(workflowName);
        }
    }

    protected String getWorkflowName(ServiceReference serviceReference, Object service) {
        String workflowName = serviceReference.getProperty("workflow.name").toString();
        if(workflowName == null || workflowName.trim().length() == 0)
            workflowName = ((WorkflowDescriptor)service).getName();
        if(workflowName == null || workflowName.trim().length() == 0)
            workflowName = UUID.randomUUID().toString();
        return workflowName;
    }

    protected void activate(ComponentContext context) {
        this.context = context;
        for(Iterator<ServiceReference> it = servicesBuffer.iterator(); it.hasNext();) {
            ServiceReference serviceReference = it.next();
            bindService(serviceReference);
            it.remove();
        }
    }

    protected void deactivate(ComponentContext context) {

    }

    public void setLayout(String workflowName, Object layout) {
    }

    public Object getLayout(String workflowName) {
        return null;
    }

    public boolean isModifiable(String name) {
        return false;
    }

    public String getName() {
        return null;
    }

    public Properties getProperties() {
        return null;
    }

    public WorkflowDescriptor getWorkflow(final String inName) {
        synchronized (workflowDescriptors) {
            return workflowDescriptors.get(inName);
        }
    }

    public WorkflowDescriptor getWorkflow(String name, boolean validate) throws FactoryException {
        return getWorkflow(name);
    }

    public String[] getWorkflowNames() {
        synchronized (workflowDescriptors) {
            String names[] = new String[workflowDescriptors.size()];
            int i = 0;
            for(String name : workflowDescriptors.keySet())
                names[i] = name;
            return names;
        }
    }

    public void createWorkflow(String name) {
    }

    public void init(Properties p) {
    }

    public void initDone() throws FactoryException {
    }

    public boolean removeWorkflow(final String inName) throws FactoryException {
        throw new FactoryException("Unsupported operation.");
    }

    public void renameWorkflow(String oldName, String newName) {
    }

    public void save() {
    }

    public boolean saveWorkflow(final String inName, final WorkflowDescriptor inDescriptor, final boolean inReplace) throws FactoryException {
        throw new FactoryException("Unsupported operation.");
    }

}
