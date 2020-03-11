package cli;

import codegen.Choice;
import codegen.CloudArtifactGenerator;
import codegen.Condition;
import codegen.Function;
import codegen.FunctionOrchestrator;
import codegen.FunctionStep;
import codegen.Parallel;
import codegen.Sequence;
import codegen.aws.models.formation.CloudFormationGenerator;
import picocli.CommandLine;

import java.util.ArrayList;

@CommandLine.Command(name = "aws")
public class FassBuildAws implements Runnable {

    public void run() {
        System.out.println("Building AWS shit");

        FunctionOrchestrator testWorkflow = testChoiceFlow();
        //System.out.println(testWorkflow);
        CloudArtifactGenerator awsGen = new CloudFormationGenerator(testWorkflow);
        awsGen.build();

    }

    public static FunctionOrchestrator testSequencialFlow () {
        FunctionOrchestrator testWorkflow = new FunctionOrchestrator("Test Sequential Workflow", "Test " +
                "workfolow to " +
                "test the Sequential flow", "1.0.0");
        ArrayList<FunctionStep> stepList = new ArrayList<>();
        Sequence sequence = new Sequence();

        Function f1 = new Function("Function 1","index.handler","nodejs12.x","exports.handler = (event, " +
                "context, callback) => {\n    callback(null, \"Hello From One!\");\n};\n");
        Function f2 = new Function("Function 2","index.handler","nodejs12.x","exports.handler = (event, " +
                "context, callback) => {\n    callback(null, \"Hello From One!\");\n};\n");
        Function f3 = new Function("Function 3","index.handler","nodejs12.x","exports.handler = (event, " +
                "context, callback) => {\n    callback(null, \"Hello From One!\");\n};\n");

        ArrayList<Function> functionList = new ArrayList<>();
        functionList.add(f1);
        functionList.add(f2);
        functionList.add(f3);

        sequence.setFunctionList(functionList);
        stepList.add(sequence);
        testWorkflow.setStepList(stepList);
        return  testWorkflow;
    }

    public static FunctionOrchestrator testParallelFlow () {
        FunctionOrchestrator testWorkflow = new FunctionOrchestrator("Test Parallel Workflow", "Test " +
                "workfolow to " +
                "test the Parallel flow", "1.0.0");
        ArrayList<FunctionStep> stepList = new ArrayList<>();
        Parallel parallel = new Parallel();

        Function f1 = new Function("Function 1","index.handler","nodejs12.x","exports.handler = (event, " +
                "context, callback) => {\n    callback(null, \"Hello From One!\");\n};\n");
        Function f2 = new Function("Function 2","index.handler","nodejs12.x","exports.handler = (event, " +
                "context, callback) => {\n    callback(null, \"Hello From One!\");\n};\n");
        Function f3 = new Function("Function 3","index.handler","nodejs12.x","exports.handler = (event, " +
                "context, callback) => {\n    callback(null, \"Hello From One!\");\n};\n");

        ArrayList<Function> functionList = new ArrayList<>();
        functionList.add(f1);
        functionList.add(f2);
        functionList.add(f3);

        parallel.setFunctionList(functionList);
        stepList.add(parallel);
        testWorkflow.setStepList(stepList);
        return  testWorkflow;
    }

    public static FunctionOrchestrator testChoiceFlow () {
        FunctionOrchestrator testWorkflow = new FunctionOrchestrator("Test Choice Workflow", "Test " +
                "workflow to test the Choice flow", "1.0.0");
        ArrayList<FunctionStep> stepList = new ArrayList<>();

        Condition condition = new Condition("25","55",">=");

        Choice choice = new Choice(condition);

        Parallel parallel = new Parallel();

        Function f1 = new Function("Success Parrarel Function 1","index.handler","nodejs12.x","exports.handler = (event, " +
                "context, callback) => {\n    callback(null, \"Hello From One!\");\n};\n");
        Function f2 = new Function("Success Parrarel Function 2","index.handler","nodejs12.x","exports.handler = (event, " +
                "context, callback) => {\n    callback(null, \"Hello From One!\");\n};\n");
        Function f3 = new Function("Success Parrarel  Function 3","index.handler","nodejs12.x","exports.handler = (event, " +
                "context, callback) => {\n    callback(null, \"Hello From One!\");\n};\n");

        ArrayList<Function> successFunctionList = new ArrayList<>();
        successFunctionList.add(f1);
        successFunctionList.add(f2);
        successFunctionList.add(f3);

        parallel.setFunctionList(successFunctionList);

        ArrayList<FunctionStep> successFlow = new ArrayList<>();
        successFlow.add(parallel);
        choice.setSuccessBranch(successFlow);

        Sequence sequence = new Sequence();

        Function fs1 = new Function("Success Seq Function 1","index.handler","nodejs12.x","exports.handler = (event, " +
                "context, callback) => {\n    callback(null, \"Hello From One!\");\n};\n");
        Function fs2 = new Function("Success Seq Function 2","index.handler","nodejs12.x","exports.handler = (event, " +
                "context, callback) => {\n    callback(null, \"Hello From One!\");\n};\n");
        Function fs3 = new Function("Success Seq  Function 3","index.handler","nodejs12.x","exports.handler = (event, " +
                "context, callback) => {\n    callback(null, \"Hello From One!\");\n};\n");

        ArrayList<Function> failureFunctionList = new ArrayList<>();
        failureFunctionList.add(fs1);
        failureFunctionList.add(fs2);
        failureFunctionList.add(fs3);

        sequence.setFunctionList(failureFunctionList);

        ArrayList<FunctionStep> failureFlow = new ArrayList<>();
        failureFlow.add(sequence);
        choice.setFailureBranch(failureFlow);

        stepList.add(choice);
        testWorkflow.setStepList(stepList);
        return  testWorkflow;
    }
}
