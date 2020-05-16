package cli;

import codegen.IfBranch;
import codegen.CloudArtifactGenerator;
import codegen.Condition;
import codegen.Function;
import codegen.FunctionOrchestrator;
import codegen.FunctionStep;
import codegen.IfExpr;
import codegen.Parallel;
import codegen.Sequence;
import codegen.aws.models.formation.CloudFormationGenerator;
import compiler.Executor;
import org.apache.commons.lang3.time.StopWatch;
import org.codehaus.plexus.util.FileUtils;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@CommandLine.Command(name = "aws")
public class FassBuildAws implements Runnable {

    public void run() {
        final File tmp = new File("");
        final File folder = new File(tmp.getAbsolutePath());
        String defFilePath = tmp.getAbsolutePath()+File.separator+listFilesForFolder(folder);
        System.out.println("Compiling: "+ defFilePath);

        Executor compiler = new Executor();
        try {
            FunctionOrchestrator liveFlow = compiler.compile(defFilePath);
            //System.out.println(liveFlow);
            CloudArtifactGenerator awsGen = new CloudFormationGenerator(liveFlow);
            awsGen.build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String listFilesForFolder(File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                if (fileEntry.getName().endsWith(".fass")){
                    return fileEntry.getName();
                }
            }
        }
        return null;
    }

    public static FunctionOrchestrator testSequencialFlow () {
        FunctionOrchestrator testWorkflow = new FunctionOrchestrator("Test Sequential Workflow", "Test " +
                "workfolow to " +
                "test the Sequential flow", "1.0.0");
        ArrayList<FunctionStep> stepList = new ArrayList<>();
        Sequence sequence = new Sequence("s1");

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
        Parallel parallel = new Parallel("p1");

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

//    public static FunctionOrchestrator testChoiceFlow () {
//        FunctionOrchestrator testWorkflow = new FunctionOrchestrator("Test Choice Workflow", "Test " +
//                "workflow to test the Choice flow", "1.0.0");
//        ArrayList<FunctionStep> stepList = new ArrayList<>();
//
//        Condition condition = new Condition("25","55",">=");
//        IfExpr ifExpr = new IfExpr("TestChoice");
//        ifExpr.setName("ChoiceStep1");
//
//        IfBranch ifBranch = new IfBranch(condition);
//        Parallel parallel = new Parallel("p1");
//
//        Function f1 = new Function("Success Parrarel Function 1","index.handler","nodejs12.x","exports.handler = (event, " +
//                "context, callback) => {\n    callback(null, \"Hello From One!\");\n};\n");
//        Function f2 = new Function("Success Parrarel Function 2","index.handler","nodejs12.x","exports.handler = (event, " +
//                "context, callback) => {\n    callback(null, \"Hello From One!\");\n};\n");
//        Function f3 = new Function("Success Parrarel  Function 3","index.handler","nodejs12.x","exports.handler = (event, " +
//                "context, callback) => {\n    callback(null, \"Hello From One!\");\n};\n");
//
//        ArrayList<Function> successFunctionList = new ArrayList<>();
//        successFunctionList.add(f1);
//        successFunctionList.add(f2);
//        successFunctionList.add(f3);
//
//        parallel.setFunctionList(successFunctionList);
//
//        ifBranch.setSuccessBranch(parallel);
//
//        Sequence sequence = new Sequence("s1");
//
//        Function fs1 = new Function("Success Seq Function 1","index.handler","nodejs12.x","exports.handler = (event, " +
//                "context, callback) => {\n    callback(null, \"Hello From One!\");\n};\n");
//        Function fs2 = new Function("Success Seq Function 2","index.handler","nodejs12.x","exports.handler = (event, " +
//                "context, callback) => {\n    callback(null, \"Hello From One!\");\n};\n");
//        Function fs3 = new Function("Success Seq  Function 3","index.handler","nodejs12.x","exports.handler = (event, " +
//                "context, callback) => {\n    callback(null, \"Hello From One!\");\n};\n");
//
//        ArrayList<Function> failureFunctionList = new ArrayList<>();
//        failureFunctionList.add(fs1);
//        failureFunctionList.add(fs2);
//        failureFunctionList.add(fs3);
//
//        sequence.setFunctionList(failureFunctionList);
//        ifExpr.setElseBranchBody(sequence);
//
//        Condition condition1 = new Condition("25","55","==");
//
//        IfBranch ifBranch1 = new IfBranch(condition1);
//        ifBranch1.setSuccessBranch(sequence);
//
//        List<IfBranch> ifBranches = new ArrayList<>();
//        ifBranches.add(ifBranch);
//        ifBranches.add(ifBranch1);
//
//        ifExpr.setIfBranches(ifBranches);
//
//        stepList.add(ifExpr);
//
//        testWorkflow.setStepList(stepList);
//        return  testWorkflow;
//    }

//    public static FunctionOrchestrator testComplexChoiceFlow () {
//        FunctionOrchestrator testWorkflow = new FunctionOrchestrator("Test Choice Workflow", "Test " +
//                "workflow to test the Choice flow", "1.0.0");
//        ArrayList<FunctionStep> stepList = new ArrayList<>();
//        //Choice{condition=Condition{leftSide=Condition{leftSide=Condition{leftSide=9.0, rightSide=2.0,
//        // evaluator='%'}, rightSide=0.0, evaluator='=='}, rightSide=null, evaluator='!'},
//        // successBranch=[], FailureBranch=[]}
//
//        Condition smallCondition = new Condition(9.0,2.0,"%");
//
//        Condition condition = new Condition(smallCondition,0.0,"==");
//
//        Condition rootCondition = new Condition(condition,null,"!");
//
//        IfBranch ifBranch = new IfBranch(rootCondition);
//
//        Parallel parallel = new Parallel();
//
//        Function f1 = new Function("Success Parrarel Function 1","index.handler","nodejs12.x","exports.handler = (event, " +
//                "context, callback) => {\n    callback(null, \"Hello From One!\");\n};\n");
//        Function f2 = new Function("Success Parrarel Function 2","index.handler","nodejs12.x","exports.handler = (event, " +
//                "context, callback) => {\n    callback(null, \"Hello From One!\");\n};\n");
//        Function f3 = new Function("Success Parrarel  Function 3","index.handler","nodejs12.x","exports.handler = (event, " +
//                "context, callback) => {\n    callback(null, \"Hello From One!\");\n};\n");
//
//        ArrayList<Function> successFunctionList = new ArrayList<>();
//        successFunctionList.add(f1);
//        successFunctionList.add(f2);
//        successFunctionList.add(f3);
//
//        parallel.setFunctionList(successFunctionList);
//
//        ArrayList<FunctionStep> successFlow = new ArrayList<>();
//        successFlow.add(parallel);
//        ifBranch.setSuccessBranch(successFlow);
//
//        Sequence sequence = new Sequence();
//
//        Function fs1 = new Function("Success Seq Function 1","index.handler","nodejs12.x","exports.handler = (event, " +
//                "context, callback) => {\n    callback(null, \"Hello From One!\");\n};\n");
//        Function fs2 = new Function("Success Seq Function 2","index.handler","nodejs12.x","exports.handler = (event, " +
//                "context, callback) => {\n    callback(null, \"Hello From One!\");\n};\n");
//        Function fs3 = new Function("Success Seq  Function 3","index.handler","nodejs12.x","exports.handler = (event, " +
//                "context, callback) => {\n    callback(null, \"Hello From One!\");\n};\n");
//
//        ArrayList<Function> failureFunctionList = new ArrayList<>();
//        failureFunctionList.add(fs1);
//        failureFunctionList.add(fs2);
//        failureFunctionList.add(fs3);
//
//        sequence.setFunctionList(failureFunctionList);
//
//        ArrayList<FunctionStep> failureFlow = new ArrayList<>();
//        failureFlow.add(sequence);
//        ifBranch.setFailureBranch(failureFlow);
//
//        stepList.add(ifBranch);
//        testWorkflow.setStepList(stepList);
//        return  testWorkflow;
//    }
}
