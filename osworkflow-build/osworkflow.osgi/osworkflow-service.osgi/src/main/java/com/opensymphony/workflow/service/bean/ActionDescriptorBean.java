package com.opensymphony.workflow.service.bean;

public class ActionDescriptorBean extends MetaInfoWorkflowBean {

    private int actionId;
    private String name;
    private Integer unconditionalTargetStep;

    public ActionDescriptorBean(int actionId, String name, Integer unconditionalTargetStep) {
        this.actionId = actionId;
        this.name = name;
        this.unconditionalTargetStep = unconditionalTargetStep;
    }

    public int getActionId() {
        return actionId;
    }

    public String getName() {
        return name;
    }

    public Integer getUnconditionalTargetStep() {
        return unconditionalTargetStep;
    }

}