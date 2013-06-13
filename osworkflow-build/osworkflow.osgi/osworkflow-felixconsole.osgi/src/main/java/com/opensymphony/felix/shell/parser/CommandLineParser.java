package com.opensymphony.felix.shell.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandLineParser implements Iterable<String>, Iterator<String> {

    private static final Pattern commandLinePattern = Pattern.compile("\"((\\\\\"|[^\"])*)\"|(\\S+)");

    private List<String> parameters;
    private int currentParam;
    private String commandLine;
    private int parametersCount;

    public CommandLineParser(String commandLine) {
        this.commandLine = commandLine;
        Matcher commandLineMatcher = commandLinePattern.matcher(commandLine);
        parameters = new ArrayList<String>();
        currentParam = 0;
        while(commandLineMatcher.find()) {
            String group = commandLineMatcher.group(1);
            parameters.add(group == null ? commandLineMatcher.group(3) : group);
        }
        ((ArrayList<String>)parameters).trimToSize();
        parametersCount = parameters.size();
    }

    public String getCommandLine() {
        return commandLine;
    }

    public int getParametersCount() {
        return parametersCount;
    }

    public String getParameter(int index) throws IndexOutOfBoundsException {
        return parameters.get(index);
    }

    public boolean hasNext() {
        return currentParam < parametersCount;
    }

    public String next() throws IndexOutOfBoundsException {
        return getParameter(currentParam++);
    }

    public Boolean nextBoolean() throws IndexOutOfBoundsException {
        return Boolean.parseBoolean(next());
    }

    public Integer nextInt() throws IndexOutOfBoundsException {
        String value = next();
        Integer ret = null;
        try {
            ret = Integer.parseInt(value);
        } catch(Exception e){}
        return ret;
    }

    public Long nextLong() throws IndexOutOfBoundsException {
        String value = next();
        Long ret = null;
        try {
            ret = Long.parseLong(value);
        } catch(Exception e){}
        return ret;
    }

    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported by command line iterator");
    }

    public Iterator<String> iterator() {
        return this;
    }

}
