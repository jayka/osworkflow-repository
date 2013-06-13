package com.opensymphony.felix.shell.commands;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.workflow.service.WorkflowService;

@Reference(name = "workflowService", referenceInterface = WorkflowService.class, cardinality = ReferenceCardinality.MANDATORY_UNARY)
public abstract class GenericOSWorkflowCommand implements OSWorkflowCommand {

    protected final Logger LOG;

    protected WorkflowService workflowService;

    public GenericOSWorkflowCommand() {
        LOG = LoggerFactory.getLogger(this.getClass());
    }

    protected void bindWorkflowService(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    protected void unbindWorkflowService(WorkflowService workflowService) {
        this.workflowService = null;
    }
}
