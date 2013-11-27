package com.opensymphony.workflow.service.bean;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StepDescriptorBean extends MetaInfoWorkflowBean {

    private int stepId;
    private String name;
    private String owner;
    private String status;
    private List<ActionDescriptorBean> actions;

    public StepDescriptorBean(int stepId, String name, String owner, String status, List<ActionDescriptorBean> actions, Map<String, String> meta) {
        this.meta = meta;
        this.name = name;
        this.owner = owner;
        this.status = status;
        this.stepId = stepId;
        this.actions = (actions == null || actions.isEmpty()) ? Collections.<ActionDescriptorBean>emptyList() : Collections.unmodifiableList(actions);
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public String getStatus() {
        return status;
    }

    public int getStepId() {
        return stepId;
    }

    public List<ActionDescriptorBean> getActions() {
        return actions;
    }

    public void setActions(List<ActionDescriptorBean> actions) {
        this.actions = actions;
    }
}