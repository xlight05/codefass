package cli;

import codegen.CloudArtifactGenerator;
import codegen.Function;
import codegen.FunctionOrchestrator;
import codegen.FunctionStep;
import codegen.Sequence;
import codegen.aws.models.formation.CloudFormationGenerator;
import picocli.CommandLine;

import java.util.ArrayList;

@CommandLine.Command(name = "aws")
public class FassBuildAws implements Runnable {

    public void run() {
        System.out.println("Building AWS shit");

        //From Code
        FunctionOrchestrator testWorkflow = new FunctionOrchestrator("Test Sequential Workflow", "Test " +
                "workfolow to " +
                "test the Sequential flow", "1.0.0");
        ArrayList<FunctionStep> stepList = new ArrayList<>();
        Sequence sequence = new Sequence();

        Function f1 = new Function("Function 1");
        Function f2 = new Function("Function 2");
        Function f3 = new Function("Function 3");

        ArrayList<Function> functionList = new ArrayList<>();
        functionList.add(f1);
        functionList.add(f2);
        functionList.add(f3);

        sequence.setFunctionList(functionList);
        stepList.add(sequence);
        testWorkflow.setStepList(stepList);
        System.out.println(testWorkflow.toString());
        //END

        CloudArtifactGenerator awsGen = new CloudFormationGenerator(testWorkflow);
        awsGen.build();

    }
}
