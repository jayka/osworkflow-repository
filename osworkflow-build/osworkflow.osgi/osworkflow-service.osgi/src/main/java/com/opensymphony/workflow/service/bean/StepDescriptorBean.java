package com.opensymphony.workflow.service.bean;

import java.util.Map;

public class StepDescriptorBean extends MetaInfoWorkflowBean {

    private int stepId;
    private String name;
    private String owner;
    private String status;

    public StepDescriptorBean(int stepId, String name, String owner, String status, Map<String, String> meta) {
        this.meta = meta;
        this.name = name;
        this.owner = owner;
        this.status = status;
        this.stepId = stepId;
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
}