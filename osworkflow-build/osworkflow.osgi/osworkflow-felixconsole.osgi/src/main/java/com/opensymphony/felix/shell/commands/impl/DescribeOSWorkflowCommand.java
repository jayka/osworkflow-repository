package com.opensymphony.felix.shell.commands.impl;

import java.io.PrintStream;
import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.shell.Command;

import com.opensymphony.felix.shell.commands.GenericOSWorkflowCommand;
import com.opensymphony.felix.shell.commands.OSWorkflowCommand;
import com.opensymphony.felix.shell.parser.CommandLineParser;
import com.opensymphony.workflow.service.bean.ActionDescriptorBean;
import com.opensymphony.workflow.service.bean.StepDescriptorBean;
import com.opensymphony.workflow.service.bean.WorkflowDescriptorBean;

@Component(immediate = true)
@Service(OSWorkflowCommand.class)
public class DescribeOSWorkflowCommand extends GenericOSWorkflowCommand {

    public void execute(Command parentCommand, CommandLineParser commandLine, PrintStream out, PrintStream err) {
        try {
            String user = commandLine.hasNext() ? commandLine.next() : null;
            Long workflowID = commandLine.hasNext() ? commandLine.nextLong() : null;
            if(user == null || workflowID == null) {
                printHelp(parentCommand, out, err);
                return;
            }

            boolean listAllSteps = commandLine.hasNext() ? commandLine.nextBoolean() : false;

            WorkflowDescriptorBean descriptor = workflowService.describeWorkflow(workflowID, user, null);
            out.format("Workflow %d: %s%n", descriptor.getWorkflowId(), descriptor.getName());
            printMetadata(out, descriptor.getMeta(), "");

            out.println("CURRENT STEPS:");
            for(StepDescriptorBean step : descriptor.getCurrentSteps()) {
                out.format("  %- 2d: %s (%s) owned by %s%n", step.getStepId(), step.getName(), step.getStatus(),
                           step.getOwner().trim().isEmpty() ? "<no owner>" : step.getOwner());
                printMetadata(out, step.getMeta(), "  ");
            }
            out.println();
            out.println("ACTIONS:");
            for(ActionDescriptorBean action : descriptor.getAvailableActions()) {
                out.format("  %- 2d: %s%n", action.getActionId(), action.getName());
                printMetadata(out, action.getMeta(), "  ");
            }

            if(listAllSteps) {
                out.println("------------------------");
                out.println("ALL STEPS:");
                for(StepDescriptorBean step : descriptor.getAllSteps()) {
                    out.format("  %- 2d: %s %n", step.getStepId(), step.getName());
                    printMetadata(out, step.getMeta(), "  ");
                    out.println();
                    out.println("  ACTIONS:");
                    for(ActionDescriptorBean action : step.getActions()) {
                        out.format("    %- 2d: %s -> %2d%n", action.getActionId(), action.getName(), action.getUnconditionalTargetStep());
                        printMetadata(out, action.getMeta(), "    ");
                    }
                }
            }

            out.flush();
        } catch(Throwable e) {
            LOG.error("Can't describe workflow", e);
            e.printStackTrace(err);
        }
    }

    protected void printMetadata(PrintStream out, Map<String, String> metadata, String prefix) {
        if(!metadata.isEmpty()) {
            out.println(prefix + "METADATA:");
            for(Map.Entry<String, String> entry : metadata.entrySet()) {
                out.format(prefix + "  %s = %s%n", entry.getKey(), entry.getValue());
            }
        }
    }

    public String getName() {
        return "desc";
    }

    public String getShortDescription() {
        return "Describes current workflow states";
    }

    public void printHelp(Command parentCommand, PrintStream out, PrintStream err) {
        out.println("Usage: " + parentCommand.getName() + " " + getName() + " <user> <workflow id>");
        out.println("  <user>:        The username used to query the workflow");
        out.println("  <workflow id>: The id of the workflow to describe");
        out.println("This command prints out a description for all active states and available actions on the specified workflow");
    }

}
