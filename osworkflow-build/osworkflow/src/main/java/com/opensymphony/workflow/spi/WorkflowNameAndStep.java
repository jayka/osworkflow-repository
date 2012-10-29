package com.opensymphony.workflow.spi;

public class WorkflowNameAndStep {

    protected String workflowName;
    protected int stepId;

    public WorkflowNameAndStep(int stepId, String workflowName) {
        this.stepId = stepId;
        this.workflowName = workflowName;
    }

    public int getStepId() {
        return stepId;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof WorkflowNameAndStep))
            return false;

        WorkflowNameAndStep that = (WorkflowNameAndStep) o;

        if (stepId != that.stepId)
            return false;
        if (workflowName != null ? !workflowName.equals(that.workflowName) : that.workflowName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = workflowName != null ? workflowName.hashCode() : 0;
        result = 31 * result + stepId;
        return result;
    }

    @Override
    public String toString() {
        return "WorkflowNameAndStep [workflowName=" + workflowName + ", stepId=" + stepId + "]";
    }

}
