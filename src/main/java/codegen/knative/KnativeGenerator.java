package codegen.knative;

import codegen.CloudArtifactGenerator;
import codegen.Condition;
import codegen.Function;
import codegen.FunctionOrchestrator;
import codegen.FunctionStep;
import codegen.IfBranch;
import codegen.IfExpr;
import codegen.Parallel;
import codegen.Sequence;
import codegen.aws.models.formation.step.ChoiceStep;
import codegen.aws.models.formation.step.Comparision;
import codegen.aws.models.formation.step.NestedComparision;
import codegen.aws.models.formation.step.SimpleComparision;
import codegen.aws.models.formation.step.Step;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class KnativeGenerator extends CloudArtifactGenerator {
    private ArrayList<Function> functionList;

    public KnativeGenerator(FunctionOrchestrator functionOrchestrator) {
        super(functionOrchestrator);
        functionList = getFunctionsFromStepList(functionOrchestrator.getStepList());
    }

    public ArrayList<Function> getFunctionsFromStepList(List<FunctionStep> stepList) {
        ArrayList<Function> functions = new ArrayList<>();
        for (FunctionStep step : stepList) {
            functions.addAll(getFunctionsFromStep(step));
        }
        return functions;
    }

    public ArrayList<Function> getFunctionsFromStep(FunctionStep step) {
        ArrayList<Function> functions = new ArrayList<>();
        if (step instanceof Sequence) {
            Sequence sequence = (Sequence) step;
            functions.addAll(sequence.getFunctionList());
        } else if (step instanceof Parallel) {
            Parallel parallel = (Parallel) step;
            functions.addAll(parallel.getFunctionList());
        } else if (step instanceof IfExpr) {
            IfExpr ifExpr = (IfExpr) step;
            List<IfBranch> ifBranches = ifExpr.getIfBranches();
            for (IfBranch ifBranch : ifBranches) {
                functions.addAll(getFunctionsFromStepList(ifBranch.getSuccessBranch()));
            }
            functions.addAll(getFunctionsFromStepList(ifExpr.getElseBranchBody())); //test logic
        }
        return functions;
    }

    @Override
    public void build() {
        FunctionOrchestrator functionOrchestrator = super.getFunctionOrchestrator();
        ArrayList<FunctionStep> functionStepList = functionOrchestrator.getStepList();

        buildFlows(functionStepList);
        buildServices(functionStepList);
        buildInput(functionOrchestrator);
        File file = new File("output/knative/");
        System.out.println("Knative artifacts generated :"+file.getAbsolutePath());
    }

    private void buildFlows(ArrayList<FunctionStep> functionStepList) {
        for (FunctionStep functionStep : functionStepList) {
            if (functionStep instanceof IfExpr) {
                IfExpr ifExpr = (IfExpr) functionStep;
                List<IfBranch> ifBranches = ifExpr.getIfBranches();

                FlowsYaml flowsYaml = new FlowsYaml();
                flowsYaml.setApiVersion("flows.knative.dev/v1alpha1");
                flowsYaml.setKind("Parallel");

                Metadata metadata = new Metadata(ifExpr.getName());
                flowsYaml.setMetadata(metadata);

                FlowSpec flowSpec = new FlowSpec();

                ChannelTemplate channelTemplate = new ChannelTemplate();
                channelTemplate.setApiVersion("messaging.knative.dev/v1alpha1");
                channelTemplate.setKind("InMemoryChannel");
                flowSpec.setChannelTemplate(channelTemplate);

                ArrayList<FlowBranch> branches = new ArrayList<>();
                //if
                FlowBranch ifBranch = new FlowBranch();

                Filter filter = new Filter();

                Ref refF = new Ref("Service","if-filter");
                refF.setApiVersion("serving.knative.dev/v1");
                filter.setRef(refF);

                ifBranch.setFilter(filter);
                FunctionStep successBranch = ifBranches.get(0).getSuccessBranch().get(0);
                Subscriber subscriber = new Subscriber();
                String successKind;
                if (successBranch instanceof Parallel){
                    successKind = "Parallel";
                } else {
                    successKind = "Sequence";
                }
                Ref refS = new Ref(successKind,successBranch.getName());
                refS.setApiVersion("flows.knative.dev/v1alpha1");
                subscriber.setRef(refS);

                ifBranch.setSubscriber(subscriber);

                branches.add(ifBranch);
                //
                ArrayList<FunctionStep> elseBranchBody = ifExpr.getElseBranchBody();
                //else
                FlowBranch elseBranch = new FlowBranch();

                Filter elseFilter = new Filter();
                Ref elseRefF = new Ref("Service","else-filter");
                elseRefF.setApiVersion("serving.knative.dev/v1");
                elseFilter.setRef(elseRefF);

                elseBranch.setFilter(elseFilter);
                FunctionStep elseBranchStep = elseBranchBody.get(0);
                Subscriber elseSub = new Subscriber();
                String elseKind;
                if (elseBranchStep instanceof Parallel){
                    elseKind = "Parallel";
                } else {
                    elseKind = "Sequence";
                }
                Ref elseRefS = new Ref(elseKind,elseBranchStep.getName());
                elseRefS.setApiVersion("flows.knative.dev/v1alpha1");
                elseSub.setRef(elseRefS);

                elseBranch.setSubscriber(elseSub);

                branches.add(elseBranch);
                //

                flowSpec.setFlowBranches(branches);
                flowsYaml.setSpec(flowSpec);

                outputKnativeArifacts(flowsYaml,"flows");
            }
            if (functionStep instanceof Parallel){
                Parallel parallel = (Parallel) functionStep;
                ParallelYaml parallelYaml= new ParallelYaml();
                parallelYaml.setApiVersion("flows.knative.dev/v1alpha1");
                parallelYaml.setKind("Parallel");
                Metadata metadata = new Metadata(parallel.getName());
                parallelYaml.setMetadata(metadata);
                ParallelSpec parallelSpec =new ParallelSpec();
                ChannelTemplate channelTemplate = new ChannelTemplate();
                parallelSpec.setChannelTemplate(channelTemplate);
                ArrayList<ParallelBranch> parallelBranches = new ArrayList<>();
                for (Function function:parallel.getFunctionList()) {
                    ParallelBranch parallelBranch = new ParallelBranch();
                    Subscriber subscriber = new Subscriber();
                    Ref ref = new Ref("Service",function.getName());
                    ref.setApiVersion("serving.knative.dev/v1");
                    subscriber.setRef(ref);
                    parallelBranch.setSubscriber(subscriber);
                    parallelBranches.add(parallelBranch);
                }

                parallelSpec.setParallelBranches(parallelBranches);
                parallelYaml.setSpec(parallelSpec);
                outputKnativeArifacts(parallelYaml,parallel.getName());

            } else if (functionStep instanceof Sequence){
                Sequence sequence = (Sequence) functionStep;
                SequenceYaml sequenceYaml = new SequenceYaml();
                sequenceYaml.setApiVersion("flows.knative.dev/v1alpha1");
                sequenceYaml.setKind("Sequence");
                Metadata metadata = new Metadata(sequence.getName());
                sequenceYaml.setMetadata(metadata);

                SequenceSpec sequenceSpec = new SequenceSpec();
                ChannelTemplate channelTemplate = new ChannelTemplate();
                sequenceSpec.setChannelTemplate(channelTemplate);
                ArrayList<SequenceBranch> sequenceBranches = new ArrayList<>();

                for (Function function:sequence.getFunctionList()){
                    SequenceBranch sequenceBranch = new SequenceBranch();
                    Ref ref = new Ref("Service",function.getName());
                    ref.setApiVersion("serving.knative.dev/v1");
                    sequenceBranch.setRef(ref);
                    sequenceBranches.add(sequenceBranch);
                }

                sequenceSpec.setSequenceStep(sequenceBranches);
                sequenceYaml.setSpec(sequenceSpec);
                outputKnativeArifacts(sequenceYaml,sequence.getName());

            }
        }
    }

    public void buildServices (ArrayList<FunctionStep> functionStepList){
        for (FunctionStep functionStep : functionStepList) {
            if (functionStep instanceof IfExpr) {
                IfExpr ifExpr = (IfExpr) functionStep;
                List<IfBranch> ifBranches = ifExpr.getIfBranches();
                //if-filter
                IfBranch firstBranch = ifBranches.get(0);
                Condition condition = firstBranch.getCondition();
                String choiceConditionVar = getKnativeConditionString(condition);
                buildKsvc("if-filter","villardl/filter-nodejs:0.1",choiceConditionVar);

                //else-fliter
                String choiceElseCondition = "!("+choiceConditionVar+")";
                buildKsvc("else-filter","villardl/filter-nodejs:0.1",choiceElseCondition);


                FunctionStep successBranch = ifBranches.get(0).getSuccessBranch().get(0);
                if (successBranch instanceof Sequence){
                    for (Function function:((Sequence) successBranch).getFunctionList()){
                        buildFunction(function);
                    }
                } else {
                    for (Function function:((Parallel) successBranch).getFunctionList()){
                        buildFunction(function);
                    }
                }
                FunctionStep elseBranch = ifExpr.getElseBranchBody().get(0);

                if (elseBranch instanceof Sequence){
                    for (Function function:((Sequence) elseBranch).getFunctionList()){
                        buildFunction(function);
                    }
                } else {
                    for (Function function:((Parallel) elseBranch).getFunctionList()){
                        buildFunction(function);
                    }
                }
            } else if (functionStep instanceof Parallel){
                for (Function function:((Parallel) functionStep).getFunctionList()){
                    buildFunction(function);
                }
            } else {
                for (Function function:((Sequence) functionStep).getFunctionList()){
                    buildFunction(function);
                }
            }
        }

    }

    public void buildFunction (Function function){
        String handler = function.getHandler();
        String dir = handler.split("/")[0];
        //Build docker

        buildKsvc(function.getName(),"xlight05/faas-"+function.getName(),null);

    }

    public void buildKsvc (String name,String image, String condition) {
        ServiceYaml serviceYaml = new ServiceYaml();
        serviceYaml.setApiVersion("serving.knative.dev/v1");
        serviceYaml.setKind("Service");
        Metadata metadata = new Metadata(name);
        serviceYaml.setMetadata(metadata);

        ServiceSpec serviceSpec = new ServiceSpec();
        ServiceTemplate serviceTemplate = new ServiceTemplate();
        TemplateSpec templateSpec = new TemplateSpec();
        ArrayList<Container> containers = new ArrayList<>();
        Container container = new Container();
        container.setImage(image);
        if (condition != null){
            ArrayList<Env> envs= new ArrayList<>();
            Env env = new Env();
            env.setName("FILTER");
            env.setValue(condition);
            envs.add(env);
            container.setEnv(envs);
        }
        containers.add(container);

        templateSpec.setContainers(containers);
        serviceTemplate.setTemplateSpec(templateSpec);
        serviceSpec.setServiceTemplate(serviceTemplate);
        serviceYaml.setSpec(serviceSpec);

        outputKnativeArifacts(serviceYaml,name);
    }

    public void buildInput(FunctionOrchestrator functionOrchestrator) {
        InputYaml inputYaml = new InputYaml();
        inputYaml.setKind("PingSource");
        inputYaml.setApiVersion("sources.knative.dev/v1alpha2");
        Metadata metadata = new Metadata(functionOrchestrator.getName());
        inputYaml.setMetadata(metadata);
        InputSpec inputSpec = new InputSpec();
        inputSpec.setSchedule("*/1 * * * *");
        inputSpec.setData("{\"message\": \"Hello world!\"}");

        FunctionStep initialStep = functionOrchestrator.getStepList().get(0);
        String initialKind;
        if (initialStep instanceof Parallel){
            initialKind = "Parallel";
        } else {
            initialKind = "Sequence";
        }
        Ref ref = new Ref(initialKind,initialStep.getName());
        ref.setApiVersion("flows.knative.dev/v1beta1");
        inputSpec.getSink().put("ref", ref);
        inputYaml.setSpec(inputSpec);

        outputKnativeArifacts(inputYaml,"input");
    }

    public String getKnativeConditionString (Condition condition){
        Object left = condition.getLeftSide();
        Object right = condition.getRightSide();
        String evaluator = condition.getEvaluator();
        if (!(left instanceof Condition || right instanceof Condition)) {
            String leftStr = (String) left;
            String rightStr = (String) right;
            String variable;
            String value;
            if (leftStr.startsWith("$")){
                variable = leftStr.substring(1);
                value = rightStr;
            }else {
                variable = rightStr.substring(1);
                value = leftStr;
            }
            return "event."+variable+evaluator+value;

        } else if (left instanceof Condition && right instanceof Condition) {
            //both complex condition
//            SimpleComparision leftCompare = (SimpleComparision) comparisionBuilder((Condition) left);
//            SimpleComparision rightCompare = (SimpleComparision) comparisionBuilder((Condition) right);
//            List<SimpleComparision> simpleComparisionList = new ArrayList<>();
//            simpleComparisionList.add(leftCompare);
//            simpleComparisionList.add(rightCompare);
//            NestedComparision comparision = new NestedComparision();
//            switch (condition.getEvaluator()) {
//                case "!":
//                    System.out.println("Inside the Case");
//                    System.out.println(leftCompare);
//                    System.out.println(rightCompare);
//                    break;
//                case "&&":
//                    comparision.setAnd(simpleComparisionList);
//                    break;
//                case "||":
//                    comparision.setOr(simpleComparisionList);
//                    break;
//            }
//            return comparision;
        } else {
//            if (condition.getEvaluator().equals("!")) {
//                if (left instanceof Condition) {
//                    Comparision comparision = comparisionBuilder((Condition) left);
//                    if (comparision instanceof SimpleComparision) {
//                        NestedComparision nestedComparision = new NestedComparision();
//                        nestedComparision.setNot((SimpleComparision) comparision);
//                        return nestedComparision;
//                    } else {
//                        System.out.println("Not a simple Branch");
//                        return null;
//                    }
//                } else {
//                    System.out.println("Not a Condition");
//                    return null;
//                }
//            } else {
//                System.out.println("Invalid Code");
//                return null;
//            }
        }
        return null;
    }

    private void outputKnativeArifacts (Object object,String name){
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            String cloudFormationJson = mapper.writeValueAsString(object);

            List<String> lines = Arrays.asList(cloudFormationJson);

            File directory = new File("output");
            if (! directory.exists()){
                directory.mkdir();
                // If you require it to make the entire directory path including parents,
                // use directory.mkdirs(); here instead.
            }

            File directory1 = new File("output/knative");
            if (! directory1.exists()){
                directory1.mkdir();
                // If you require it to make the entire directory path including parents,
                // use directory.mkdirs(); here instead.
            }


            Path file = Paths.get("output/knative/"+name+".json");
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
