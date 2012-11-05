package com.opensymphony.workflow.service.bean;

public class ActionDescriptorBean extends MetaInfoWorkflowBean {

    private int actionId;
    private String name;

    public ActionDescriptorBean(int actionId, String name) {
        this.actionId = actionId;
        this.name = name;
    }

    public int getActionId() {
        return actionId;
    }

    public String getName() {
        return name;
    }
}