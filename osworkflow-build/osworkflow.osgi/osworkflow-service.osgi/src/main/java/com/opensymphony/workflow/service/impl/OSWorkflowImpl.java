package com.opensymphony.workflow.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.bean.ActionDescriptorBean;
import com.opensymphony.workflow.service.bean.StepDescriptorBean;
import com.opensymphony.workflow.service.bean.WorkflowDescriptorBean;
import com.opensymphony.workflow.service.exception.InvalidActionException;
import com.opensymphony.workflow.service.exception.InvalidEntryStateException;
import com.opensymphony.workflow.service.exception.InvalidInputException;
import com.opensymphony.workflow.service.exception.InvalidRoleException;
import com.opensymphony.workflow.service.exception.WorkflowException;
import com.opensymphony.workflow.service.util.Util;
import com.opensymphony.workflow.spi.Step;

public class OSWorkflowImpl implements WorkflowService {

    protected Workflow workflow;

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected Map insertCallerInMap(String caller, Map inputs) {
        if (inputs == null)
            inputs = new HashMap();
        inputs.put("caller", caller);
        return inputs;
    }

    public List<String> getWorkflowNames() {
        return null;
    }

    @SuppressWarnings("rawtypes")
    @Transactional
    public Long initWorkflow(String workflowName, Integer initialAction, Map inputs) throws InvalidRoleException,
                                                                                    InvalidInputException, WorkflowException,
                                                                                    InvalidEntryStateException,
                                                                                    InvalidActionException {
        return initWorkflow(workflowName, initialAction, Util.getActualLoggedUser().getName(), inputs);
    }

    @SuppressWarnings("rawtypes")
    @Transactional
    public Long initWorkflow(String workflowName, Integer initialAction, String caller, Map inputs) throws InvalidRoleException,
                                                                                                   InvalidInputException,
                                                                                                   WorkflowException,
                                                                                                   InvalidEntryStateException,
                                                                                                   InvalidActionException {
        try {
            return workflow.initialize(workflowName, initialAction, insertCallerInMap(caller, inputs));
        } catch (com.opensymphony.workflow.InvalidRoleException e) {
            throw new InvalidRoleException(e);
        } catch (com.opensymphony.workflow.InvalidEntryStateException e) {
            throw new InvalidEntryStateException(e);
        } catch (com.opensymphony.workflow.InvalidInputException e) {
            throw new InvalidInputException(e);
        } catch (com.opensymphony.workflow.InvalidActionException e) {
            throw new InvalidActionException(e);
        } catch (com.opensymphony.workflow.WorkflowException e) {
            throw new WorkflowException(e);
        }
    }

    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public WorkflowDescriptorBean describeWorkflow(long workflowId, Map inputs) {
        return describeWorkflow(workflowId, Util.getActualLoggedUser().getName(), inputs);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional(readOnly = true)
    public WorkflowDescriptorBean describeWorkflow(long workflowId, String caller, Map inputs) {
        String workflowName = workflow.getWorkflowName(workflowId);
        WorkflowDescriptorBean wfDescriptor = new WorkflowDescriptorBean(workflowId, workflowName);

        WorkflowDescriptor descriptor = workflow.getWorkflowDescriptor(workflowName);

        Set<Map.Entry> metaEntrySet = descriptor.getMetaAttributes().entrySet();
        for (Map.Entry entry : metaEntrySet) {
            wfDescriptor.addMeta((String) entry.getKey(), (String) entry.getValue());
        }

        List<Step> steps = workflow.getCurrentSteps(workflowId);
        for (Step step : steps) {
            StepDescriptor stepDescriptor = descriptor.getStep(step.getStepId());
            StepDescriptorBean stDescriptor = new StepDescriptorBean(step.getStepId(), stepDescriptor.getName(), step.getOwner(),
                                                                     step.getStatus(), stepDescriptor.getMetaAttributes());

            metaEntrySet = stepDescriptor.getMetaAttributes().entrySet();
            for (Map.Entry entry : metaEntrySet) {
                stDescriptor.addMeta((String) entry.getKey(), (String) entry.getValue());
            }

            wfDescriptor.addCurrentStep(stDescriptor);
        }

        int[] actions = workflow.getAvailableActions(workflowId, insertCallerInMap(caller, inputs));
        for (int action : actions) {
            ActionDescriptor actionDescriptor = descriptor.getAction(action);
            ActionDescriptorBean actDescriptor = new ActionDescriptorBean(action, actionDescriptor.getName());

            metaEntrySet = actionDescriptor.getMetaAttributes().entrySet();
            for (Map.Entry entry : metaEntrySet) {
                actDescriptor.addMeta((String) entry.getKey(), (String) entry.getValue());
            }

            wfDescriptor.addAvailableAction(actDescriptor);
        }

        return wfDescriptor;
    }

    @SuppressWarnings("rawtypes")
    @Transactional
    public void executeAction(long workflowId, int actionId, Map inputs) throws InvalidInputException, InvalidActionException,
                                                                        WorkflowException {
        executeAction(workflowId, actionId, Util.getActualLoggedUser().getName(), inputs);
    }

    @SuppressWarnings("rawtypes")
    @Transactional
    public void executeAction(long workflowId, int actionId, String caller, Map inputs) throws InvalidInputException,
                                                                                       InvalidActionException, WorkflowException {
        try {
            workflow.doAction(workflowId, actionId, insertCallerInMap(caller, inputs));
        } catch (com.opensymphony.workflow.InvalidInputException e) {
            throw new InvalidInputException(e);
        } catch (com.opensymphony.workflow.InvalidActionException e) {
            throw new InvalidActionException(e);
        } catch (com.opensymphony.workflow.WorkflowException e) {
            throw new WorkflowException(e);
        }

    }

}