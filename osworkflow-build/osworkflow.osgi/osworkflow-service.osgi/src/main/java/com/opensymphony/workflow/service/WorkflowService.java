package com.opensymphony.workflow.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.workflow.service.bean.WorkflowDescriptorBean;
import com.opensymphony.workflow.service.exception.InvalidActionException;
import com.opensymphony.workflow.service.exception.InvalidEntryStateException;
import com.opensymphony.workflow.service.exception.InvalidInputException;
import com.opensymphony.workflow.service.exception.InvalidRoleException;
import com.opensymphony.workflow.service.exception.WorkflowException;

public interface WorkflowService {

    @Transactional(readOnly = true)
    public List<String> getWorkflowNames();

    @SuppressWarnings("rawtypes")
    @Transactional
    public Long initWorkflow(String workflowName, Integer initialAction, Map inputs) throws InvalidRoleException, InvalidInputException,
                                                                                            WorkflowException, InvalidEntryStateException,
                                                                                            InvalidActionException;

    @SuppressWarnings("rawtypes")
    @Transactional
    public Long initWorkflow(String workflowName, Integer initialAction, String caller, Map inputs) throws InvalidRoleException, InvalidInputException,
                                                                                                           WorkflowException, InvalidEntryStateException,
                                                                                                           InvalidActionException;

    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public WorkflowDescriptorBean describeWorkflow(long workflowId, Map inputs);

    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public WorkflowDescriptorBean describeWorkflow(long workflowId, String caller, Map inputs);

    @SuppressWarnings("rawtypes")
    @Transactional
    public void executeAction(long workflowId, int actionId, Map inputs) throws InvalidInputException, InvalidActionException,
                                                                                WorkflowException;

    @SuppressWarnings("rawtypes")
    @Transactional
    public void executeAction(long workflowId, int actionId, String caller, Map inputs) throws InvalidInputException,
                                                                                               InvalidActionException,
                                                                                               WorkflowException;

}
