package com.mscg.osworkflow;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.Step;
import com.opensymphony.workflow.spi.WorkflowEntry;

public class TestWorkflow {

    public static void main(String args[]) {
        try {
            DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

            // Define a bean and register it
            beanFactory.registerSingleton("args", Arrays.asList(args));
            GenericApplicationContext cmdArgCxt = new GenericApplicationContext(beanFactory);
            // Must call refresh to initialize context
            cmdArgCxt.refresh();

            String configs[] = {"/META-INF/spring/app-config.xml"};
            AbstractApplicationContext context = new ClassPathXmlApplicationContext(configs, cmdArgCxt);
            context.registerShutdownHook();
            Workflow workflow = (Workflow) context.getBean("workflow");
            System.out.println("Workflow names:");
            int i = 1;
            for(String name: workflow.getWorkflowNames()) {
                System.out.format("%02d) %s%n", i++, name);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("User: ");
            String username = in.readLine();

            HashMap<String, String> inputs = new HashMap<String, String>();
            inputs.put("caller", username);

            String command;
            do {
                try {
                    System.out.print("Enter command: ");
                    command = in.readLine();

                    if(command == null || "q".equals(command) || "quit".equals(command))
                        break;

                    if("log".equals(command)) {
                        System.out.print("User: ");
                        username = in.readLine();
                        inputs.put("caller", username);
                    }
                    if("init".equals(command)) {
                        System.out.print("Workflow name: ");
                        String workflowName = in.readLine();

                        long id = workflow.initialize(workflowName, 1, inputs);
                        System.out.println("Workflow initialized with id: " + id);
                    }
                    else if("desc".equals(command)) {
                        System.out.print("Workflow ID: ");
                        long id = Long.parseLong(in.readLine());

                        String workflowName = workflow.getWorkflowName(id);
                        WorkflowDescriptor descriptor = workflow.getWorkflowDescriptor(workflowName);

                        System.out.print("Workflow state: ");
                        switch(workflow.getEntryState(id)) {
                        case WorkflowEntry.CREATED:
                            System.out.println("CREATED"); break;
                        case WorkflowEntry.ACTIVATED:
                            System.out.println("ACTIVATED"); break;
                        case WorkflowEntry.SUSPENDED:
                            System.out.println("SUSPENDED"); break;
                        case WorkflowEntry.KILLED:
                            System.out.println("KILLED"); break;
                        case WorkflowEntry.COMPLETED:
                            System.out.println("COMPLETED"); break;
                        default:
                            System.out.println("UNKNOWN");
                        }

                        List<Step> currentSteps = workflow.getCurrentSteps(id);
                        System.out.println("Current steps:");
                        int j = 0;
                        for(Step step : currentSteps) {
                            ++j;
                            StepDescriptor stepDesc = descriptor.getStep(step.getStepId());
                            System.out.println("Meta attributes:");
                            System.out.format("\t%s%n", stepDesc.getMetaAttributes().toString());
                            System.out.format("%02d) %s (%s - %s)%n",
                                                  step.getStepId(),
                                                  stepDesc.getName(),
                                                  step.getStatus(),
                                                  step.getOwner());
                        }

                        int[] availableActions = workflow.getAvailableActions(id, inputs);
                        System.out.println("Available actions:");
                        for(j = 0; j < availableActions.length; j++) {
                            ActionDescriptor action = descriptor.getAction(availableActions[j]);
                            System.out.println(String.format("%02d) %s", availableActions[j], action.getName()));
                        }
                    }
                    else if("exec".equals(command)) {
                        System.out.print("Workflow ID: ");
                        long id = Long.parseLong(in.readLine());
                        System.out.print("Action ID: ");
                        int actionId = Integer.parseInt(in.readLine());

                        workflow.doAction(id, actionId, inputs);
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }

                System.out.println("-------------------------------------");
            } while(true);

            System.out.println("Exiting");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
