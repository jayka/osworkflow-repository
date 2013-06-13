package com.opensymphony.felix.shell.commands.impl;

import java.io.PrintStream;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.shell.Command;

import com.opensymphony.felix.shell.commands.GenericOSWorkflowCommand;
import com.opensymphony.felix.shell.commands.OSWorkflowCommand;
import com.opensymphony.felix.shell.parser.CommandLineParser;

@Component(immediate = true)
@Service(OSWorkflowCommand.class)
public class ExecuteActionOSWorkflowCommand extends GenericOSWorkflowCommand {

    public void execute(Command parentCommand, CommandLineParser commandLine, PrintStream out, PrintStream err) {
        try {
            String user = commandLine.hasNext() ? commandLine.next() : null;
            Long workflowID = commandLine.hasNext() ? commandLine.nextLong() : null;
            Integer actionID = commandLine.hasNext() ? commandLine.nextInt() : null;
            if(user == null || workflowID == null || actionID == null) {
                printHelp(parentCommand, out, err);
                return;
            }

            workflowService.executeAction(workflowID, actionID, user, null);
        } catch(Throwable e) {
            LOG.error("Can't execute action on workflow", e);
            e.printStackTrace(err);
        }
    }

    public String getName() {
        return "exec";
    }

    public String getShortDescription() {
        return "Execute an action on a workflow";
    }

    public void printHelp(Command parentCommand, PrintStream out, PrintStream err) {
        out.println("Usage: " + parentCommand.getName() + " " + getName() + " <user> <workflow id> <action id>");
        out.println("  <user>:        The username used to execute the action");
        out.println("  <workflow id>: The id of the workflow on which the action will be executed");
        out.println("  <action id>:   The id of the action to execute");
    }

}
