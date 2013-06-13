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
public class InitOSWorkflowCommand extends GenericOSWorkflowCommand {

    public void execute(Command parentCommand, CommandLineParser commandLine, PrintStream out, PrintStream err) {
        try {
            String user = commandLine.hasNext() ? commandLine.next() : null;
            String workflowName = commandLine.hasNext() ? commandLine.next() : null;
            if(user == null || workflowName == null) {
                printHelp(parentCommand, out, err);
                return;
            }

            out.println(workflowService.initWorkflow(workflowName, 1, user, null));
        } catch (Throwable e) {
            LOG.error("Can't init workflow", e);
            e.printStackTrace(err);
        }
    }

    public String getName() {
        return "init";
    }

    public String getShortDescription() {
        return "Inits a workflow by name";
    }

    public void printHelp(Command parentCommand, PrintStream out, PrintStream err) {
        out.println("Usage: " + parentCommand.getName() + " " + getName() + " <user> <workflow name>");
        out.println("  <user>:          The username used to initialize the workflow");
        out.println("  <workflow name>: The name of the workflow to init");
        out.println("This command prints out the id of the newly initialized workflow");
    }

}
