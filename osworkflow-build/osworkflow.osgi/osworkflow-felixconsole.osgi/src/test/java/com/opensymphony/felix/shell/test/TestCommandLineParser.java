package com.opensymphony.felix.shell.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.opensymphony.felix.shell.parser.CommandLineParser;

public class TestCommandLineParser {

    @Test
    public void testCommandLineNoEscape() {
        CommandLineParser parser = new CommandLineParser("!cmd 45 90 \"An argument\" Another AndAnother \"Another one in quotes\"");
        Assert.assertEquals(parser.getParametersCount(), 7);
        Assert.assertEquals(parser.getParameter(3), "An argument");
    }

    @Test
    public void testCommandLineWithEscape() {
        CommandLineParser parser = new CommandLineParser("!cmd 45 90 \"An argument\" Another AndAnother \"Another \\\"one\\\" in quotes\"");
        Assert.assertEquals(parser.getParametersCount(), 7);
        Assert.assertEquals(parser.getParameter(6), "Another \\\"one\\\" in quotes");
    }

    @Test
    public void testCommandLineIterator() {
        CommandLineParser parser = new CommandLineParser("!cmd 45 90 \"An argument\" Another AndAnother \"Another \\\"one\\\" in quotes\"");
        List<String> testParams = Arrays.asList("!cmd", "45", "90", "An argument", "Another", "AndAnother", "Another \\\"one\\\" in quotes");
        List<String> params = new ArrayList<String>();
        for(String param : parser)
            params.add(param);
        Assert.assertEquals(params, testParams);
    }

}
