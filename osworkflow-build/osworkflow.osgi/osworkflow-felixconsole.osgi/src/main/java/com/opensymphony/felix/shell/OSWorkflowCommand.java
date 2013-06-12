package com.opensymphony.felix.shell;

import java.io.PrintStream;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.shell.Command;

@Component(immediate=true)
@Service(Command.class)
public class OSWorkflowCommand implements Command {

    public void execute(String commandLine, PrintStream out, PrintStream err) {
    }

    public String getName() {
        return "osw";
    }

    public String getShortDescription() {
        return "Executes actions on OSWorkflow workflows";
    }

    public String getUsage() {
        return "osw help";
    }

}
