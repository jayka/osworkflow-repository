package com.opensymphony.workflow.definition;

import java.io.PrintWriter;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;

import com.opensymphony.workflow.InvalidWorkflowDescriptorException;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.ConditionsDescriptor;
import com.opensymphony.workflow.loader.FunctionDescriptor;
import com.opensymphony.workflow.loader.JoinDescriptor;
import com.opensymphony.workflow.loader.SplitDescriptor;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.loader.WorkflowLoader;

@Component(immediate = true)
@Service(WorkflowDescriptor.class)
@Properties({
    @Property(name = "workflow.name", value = "example")
})
@SuppressWarnings("deprecation")
public class TestWorkflowDefinition extends WorkflowDescriptor {

    private static final long serialVersionUID = -5274978158921988532L;

    private WorkflowDescriptor workflowDescriptor;

    @SuppressWarnings("unchecked")
    protected void activate(ComponentContext context) {
        try {
            Enumeration<URL> entries = context.getBundleContext().getBundle().findEntries("META-INF/workflow", "example.xml", false);
            URL workflowDefURL = entries.nextElement();
            workflowDescriptor = WorkflowLoader.load(workflowDefURL, false);
        } catch (Exception e) {
            throw new IllegalArgumentException("Can't load workflow workflowDescriptor", e);
        }
    }

    @Override
    public void setEntityId(int entityId) {
        workflowDescriptor.setEntityId(entityId);
    }

    @Override
    public int getEntityId() {
        return workflowDescriptor.getEntityId();
    }

    @Override
    public void setId(int id) {
        workflowDescriptor.setId(id);
    }

    @Override
    public int getId() {
        return workflowDescriptor.getId();
    }

    @Override
    public void setParent(AbstractDescriptor parent) {
        workflowDescriptor.setParent(parent);
    }

    @Override
    public AbstractDescriptor getParent() {
        return workflowDescriptor.getParent();
    }

    @Override
    public String asXML() {
        return workflowDescriptor.asXML();
    }

    @Override
    public boolean hasId() {
        return workflowDescriptor.hasId();
    }

    @Override
    public int hashCode() {
        return workflowDescriptor.hashCode();
    }

    @Override
    public ActionDescriptor getAction(int id) {
        return workflowDescriptor.getAction(id);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Map getCommonActions() {
        return workflowDescriptor.getCommonActions();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List getGlobalActions() {
        return workflowDescriptor.getGlobalActions();
    }

    @Override
    public boolean equals(Object obj) {
        return workflowDescriptor.equals(obj);
    }

    @Override
    public ConditionsDescriptor getGlobalConditions() {
        return workflowDescriptor.getGlobalConditions();
    }

    @Override
    public ActionDescriptor getInitialAction(int id) {
        return workflowDescriptor.getInitialAction(id);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List getInitialActions() {
        return workflowDescriptor.getInitialActions();
    }

    @Override
    public JoinDescriptor getJoin(int id) {
        return workflowDescriptor.getJoin(id);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List getJoins() {
        return workflowDescriptor.getJoins();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Map getMetaAttributes() {
        return workflowDescriptor.getMetaAttributes();
    }

    @Override
    public void setName(String name) {
        workflowDescriptor.setName(name);
    }

    @Override
    public String getName() {
        return workflowDescriptor.getName();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List getRegisters() {
        return workflowDescriptor.getRegisters();
    }

    @Override
    public SplitDescriptor getSplit(int id) {
        return workflowDescriptor.getSplit(id);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List getSplits() {
        return workflowDescriptor.getSplits();
    }

    @Override
    public StepDescriptor getStep(int id) {
        return workflowDescriptor.getStep(id);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List getSteps() {
        return workflowDescriptor.getSteps();
    }

    @Override
    public FunctionDescriptor setTriggerFunction(int id, FunctionDescriptor descriptor) {
        return workflowDescriptor.setTriggerFunction(id, descriptor);
    }

    @Override
    public FunctionDescriptor getTriggerFunction(int id) {
        return workflowDescriptor.getTriggerFunction(id);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Map getTriggerFunctions() {
        return workflowDescriptor.getTriggerFunctions();
    }

    @Override
    public void addCommonAction(ActionDescriptor descriptor) {
        workflowDescriptor.addCommonAction(descriptor);
    }

    @Override
    public void addGlobalAction(ActionDescriptor descriptor) {
        workflowDescriptor.addGlobalAction(descriptor);
    }

    @Override
    public void addInitialAction(ActionDescriptor descriptor) {
        workflowDescriptor.addInitialAction(descriptor);
    }

    @Override
    public void addJoin(JoinDescriptor descriptor) {
        workflowDescriptor.addJoin(descriptor);
    }

    @Override
    public void addSplit(SplitDescriptor descriptor) {
        workflowDescriptor.addSplit(descriptor);
    }

    @Override
    public void addStep(StepDescriptor descriptor) {
        workflowDescriptor.addStep(descriptor);
    }

    @Override
    public boolean removeAction(ActionDescriptor actionToRemove) {
        return workflowDescriptor.removeAction(actionToRemove);
    }

    @Override
    public String toString() {
        return workflowDescriptor.toString();
    }

    @Override
    public void validate() throws InvalidWorkflowDescriptorException {
        workflowDescriptor.validate();
    }

    @Override
    public void writeXML(PrintWriter out, int indent) {
        workflowDescriptor.writeXML(out, indent);
    }

}
