package com.opensymphony.workflow.service.bean;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class WorkflowDescriptorBean extends MetaInfoWorkflowBean {

    private Long workflowId;
    private String name;
    private List<StepDescriptorBean> allSteps;
    private List<StepDescriptorBean> currentSteps;
    private List<ActionDescriptorBean> availableActions;
    private List<ActionDescriptorBean> availableActionsForMenu;

    public WorkflowDescriptorBean(Long workflowId, String name) {
        this.name = name;
        this.workflowId = workflowId;

        availableActions = new LinkedList<ActionDescriptorBean>();
        allSteps = new LinkedList<StepDescriptorBean>();
        currentSteps = new LinkedList<StepDescriptorBean>();

    }

    public List<ActionDescriptorBean> getAvailableActions() {
        return Collections.unmodifiableList(availableActions);
    }

    public void addAvailableAction(ActionDescriptorBean action) {
        availableActions.add(action);
    }

    public synchronized List<ActionDescriptorBean> getAvailableActionsForMenu() {
        if (availableActionsForMenu == null) {
            availableActionsForMenu = new LinkedList<ActionDescriptorBean>();
            for (ActionDescriptorBean action : availableActions) {
                Boolean hideInMenu = Boolean.valueOf(action.getMeta().get("hide-in-menu"));
                if (!hideInMenu)
                    availableActionsForMenu.add(action);
            }
        }
        return availableActionsForMenu;
    }

    public List<StepDescriptorBean> getAllSteps() {
        return Collections.unmodifiableList(allSteps);
    }

    public void addStep(StepDescriptorBean step) {
        allSteps.add(step);
    }

    public List<StepDescriptorBean> getCurrentSteps() {
        return Collections.unmodifiableList(currentSteps);
    }

    public void addCurrentStep(StepDescriptorBean step) {
        currentSteps.add(step);
    }

    public String getName() {
        return name;
    }

    public Long getWorkflowId() {
        return workflowId;
    }

}