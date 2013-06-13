package com.opensymphony.felix.shell;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.shell.Command;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.felix.shell.commands.OSWorkflowCommand;
import com.opensymphony.felix.shell.parser.CommandLineParser;

@Component(immediate = true)
@Reference(name = "service", referenceInterface = OSWorkflowCommand.class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
@Service(Command.class)
public class OSWorkflowShellCommand implements Command {

    protected static final Logger LOG = LoggerFactory.getLogger(OSWorkflowShellCommand.class);

    protected final List<ServiceReference> servicesBuffer;
    protected final Map<String, OSWorkflowCommand> commands;
    protected ComponentContext context;

    public OSWorkflowShellCommand() {
        servicesBuffer = new ArrayList<ServiceReference>();
        commands = new TreeMap<String, OSWorkflowCommand>();
    }

    public void execute(String commandLineStr, PrintStream out, PrintStream err) {
        CommandLineParser commandLine = new CommandLineParser(commandLineStr);
        commandLine.next(); // discard this command name
        String commandName = commandLine.hasNext() ? commandLine.next() : "help";
        if("help".equals(commandName))
            printHelp(commandLine, out, err);
        else {
            OSWorkflowCommand command = commands.get(commandName);
            command.execute(this, commandLine, out, err);
        }
    }

    protected void printHelp(CommandLineParser commandLine, PrintStream out, PrintStream err) {
        String commandName = commandLine.hasNext() ? commandLine.next() : null;
        if(commandName == null || !commands.containsKey(commandName)) {
            // list all available commands
            out.println("Available OSWorkflow commands:");
            if(commands.isEmpty()) {
                out.println("  No OSWorkflow commands are available in the system");
            }
            else {
                for(Map.Entry<String, OSWorkflowCommand> entry : commands.entrySet())
                    out.println("  " + entry.getKey() + ": " + entry.getValue().getShortDescription());
                out.println("Type " + getName() + " help <command> to get a detailed description of the command");
            }
        }
        else {
            // print command specific help
            OSWorkflowCommand command = commands.get(commandName);
            out.println(command.getShortDescription());
            out.println();
            command.printHelp(this, out, err);
        }
    }

    public String getName() {
        return "osw";
    }

    public String getShortDescription() {
        return "Executes actions on OSWorkflow workflows";
    }

    public String getUsage() {
        return getName() + " help";
    }

    protected void bindService(ServiceReference serviceReference) {
        if(context == null) {
            // store the reference in the buffer and return
            servicesBuffer.add(serviceReference);
            return;
        }
        OSWorkflowCommand service = (OSWorkflowCommand)context.getBundleContext().getService(serviceReference);
        if(LOG.isDebugEnabled())
            LOG.debug("Bind component of type {} from bundle {}", service.getClass().getCanonicalName(), serviceReference.getBundle().getSymbolicName());
        registerService(serviceReference, service);
    }

    protected void unbindService(ServiceReference serviceReference) {
        OSWorkflowCommand service = (OSWorkflowCommand)context.getBundleContext().getService(serviceReference);
        if(LOG.isDebugEnabled())
            LOG.debug("Unbind component of type {} from bundle {}", service.getClass().getCanonicalName(), serviceReference.getBundle().getSymbolicName());
        unregisterService(serviceReference, service);
    }

    protected void registerService(ServiceReference serviceReference, OSWorkflowCommand service) {
        synchronized (commands) {
            commands.put(service.getName(), service);
        }
    }

    protected void unregisterService(ServiceReference serviceReference, OSWorkflowCommand service) {
        synchronized (commands) {
            commands.remove(service.getName());
        }
    }

    protected void activate(ComponentContext context) {
        this.context = context;
        for(Iterator<ServiceReference> it = servicesBuffer.iterator(); it.hasNext();) {
            ServiceReference serviceReference = it.next();
            bindService(serviceReference);
            it.remove();
        }
    }

    protected void deactivate(ComponentContext context) {

    }

}
