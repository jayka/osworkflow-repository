package com.opensymphony.felix.shell.commands;

import java.io.PrintStream;

import org.apache.felix.shell.Command;

import com.opensymphony.felix.shell.parser.CommandLineParser;

public interface OSWorkflowCommand {

    public void execute(Command parentCommand, CommandLineParser commandLine, PrintStream out, PrintStream err);

    public String getName();

    public String getShortDescription();

    public void printHelp(Command parentCommand, PrintStream out, PrintStream err);

}
