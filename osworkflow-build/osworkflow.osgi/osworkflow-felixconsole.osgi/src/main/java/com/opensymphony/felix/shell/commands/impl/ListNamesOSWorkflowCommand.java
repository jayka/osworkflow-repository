package com.opensymphony.felix.shell.commands.impl;

import java.io.PrintStream;
import java.util.List;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.shell.Command;

import com.opensymphony.felix.shell.commands.GenericOSWorkflowCommand;
import com.opensymphony.felix.shell.commands.OSWorkflowCommand;
import com.opensymphony.felix.shell.parser.CommandLineParser;

@Component(immediate = true)
@Service(OSWorkflowCommand.class)
public class ListNamesOSWorkflowCommand extends GenericOSWorkflowCommand {

    public void execute(Command parentCommand, CommandLineParser commandLine, PrintStream out, PrintStream err) {
        try {
            List<String> workflowNames = workflowService.getWorkflowNames();
            if(workflowNames == null || workflowNames.isEmpty())
                out.println("No workflows configured");
            else {
                for(String name : workflowNames)
                    out.println(name);
            }
        } catch(Throwable e) {
            LOG.error("Can't list workflow names", e);
            e.printStackTrace(err);
        }
    }

    public String getName() {
        return "list";
    }

    public String getShortDescription() {
        return "Lists names of all configured workflows";
    }

    public void printHelp(Command parentCommand, PrintStream out, PrintStream err) {
        out.println("Usage: " + parentCommand.getName() + " " + getName());
    }

}
