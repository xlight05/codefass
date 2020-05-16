package codegen.aws.models.formation;

import codegen.IfBranch;
import codegen.CloudArtifactGenerator;
import codegen.Condition;
import codegen.Function;
import codegen.FunctionOrchestrator;
import codegen.FunctionStep;
import codegen.IfExpr;
import codegen.Parallel;
import codegen.Sequence;
import codegen.aws.models.formation.iam.IAMPolicyDocument;
import codegen.aws.models.formation.iam.IAMPolicy;
import codegen.aws.models.formation.iam.IAMProperty;
import codegen.aws.models.formation.iam.IAMRole;
import codegen.aws.models.formation.iam.IAMStatement;
import codegen.aws.models.formation.lambda.Lambda;
import codegen.aws.models.formation.lambda.LambdaCode;
import codegen.aws.models.formation.lambda.LambdaProperty;
import codegen.aws.models.formation.lambda.LambdaRole;
import codegen.aws.models.formation.step.BooleanComparision;
import codegen.aws.models.formation.step.ChoiceStep;
import codegen.aws.models.formation.step.Comparision;
import codegen.aws.models.formation.step.LambdaArn;
import codegen.aws.models.formation.step.NestedComparision;
import codegen.aws.models.formation.step.NumericComparision;
import codegen.aws.models.formation.step.ParNestedBranch;
import codegen.aws.models.formation.step.ParStep;
import codegen.aws.models.formation.step.SeqStep;
import codegen.aws.models.formation.step.SimpleComparision;
import codegen.aws.models.formation.step.StateDefString;
import codegen.aws.models.formation.step.StateMachine;
import codegen.aws.models.formation.step.StateProperties;
import codegen.aws.models.formation.step.Step;
import codegen.aws.models.formation.step.StringComparision;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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
import java.util.stream.Stream;

public class CloudFormationGenerator extends CloudArtifactGenerator {

    private ArrayList<Function> functionList;

    public CloudFormationGenerator(FunctionOrchestrator functionOrchestrator) {
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

    public void build() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        IAMRole iamRole = generateLambdaIAM();
        IAMRole stateIAM = generateStateMachineIAM();

        Map<String,Lambda> lambdas = generateLambdas();

        Map<String ,CloudFormationComponent> objectMap = new LinkedHashMap<>();
        objectMap.put("LambdaExecutionRole",iamRole);

        for (String name : lambdas.keySet()){
            objectMap.put(name,lambdas.get(name));
        }
        objectMap.put("StatesExecutionRole",stateIAM);


        StateMachine stateMachine = generateStateMachine();
        objectMap.put("MyStateMachine",stateMachine);

        CloudFormation cloudFormation = new CloudFormation();
        cloudFormation.setObjectMap(objectMap);

        try {
            String cloudFormationJson = mapper.writeValueAsString(cloudFormation);
            //System.out.println(cloudFormationJson);
            List<String> lines = Arrays.asList(cloudFormationJson);

            File directory = new File("output");
            if (! directory.exists()){
                directory.mkdir();
                // If you require it to make the entire directory path including parents,
                // use directory.mkdirs(); here instead.
            }

            File directory1 = new File("output/aws");
            if (! directory1.exists()){
                directory1.mkdir();
                // If you require it to make the entire directory path including parents,
                // use directory.mkdirs(); here instead.
            }


            Path file = Paths.get("output/aws/cloudformation.json");
            Files.write(file, lines, StandardCharsets.UTF_8);
            System.out.println("Knative artifacts generated :"+directory1.getAbsolutePath()+File.separator+
                    "cloudformation.json");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Step generateStep() {
        FunctionOrchestrator functionOrchestrator = super.getFunctionOrchestrator();

        Step step = new Step();
        step.setComment(functionOrchestrator.getDescription());
        Map<String, Object> stepList = new LinkedHashMap<>();

        List<FunctionStep> functionStepList = functionOrchestrator.getStepList();
        List<Comparision> comparisionList = new ArrayList<>();
        //Because first one is Choice
        for (FunctionStep functionStep : functionStepList) {
            step.setStartsAt(functionStepList.get(0).getName());
            if (functionStep instanceof IfExpr) {
                List<IfBranch> ifBranches = ((IfExpr) functionStep).getIfBranches();
                for (IfBranch ifBranch : ifBranches) {
                    Condition rootCondition = ifBranch.getCondition();
                    Comparision comparision = comparisionBuilder(rootCondition);
                    if (comparision instanceof NestedComparision) {
                        NestedComparision nestedComparision = (NestedComparision) comparision;
                        if (ifBranch.getSuccessBranch().get(0) instanceof Sequence){
                            nestedComparision.setNext(((Sequence) ifBranch.getSuccessBranch().get(0)).getFunctionList().get(0).getName());
                        } else {
                            nestedComparision.setNext(ifBranch.getSuccessBranch().get(0).getName());
                        }
                        comparisionList.add(nestedComparision);
                    } else {
                        SimpleComparision simpleComparision = (SimpleComparision) comparision;
                        if (ifBranch.getSuccessBranch().get(0) instanceof Sequence){
                            simpleComparision.setNext(((Sequence) ifBranch.getSuccessBranch().get(0)).getFunctionList().get(0).getName());
                        } else {
                            simpleComparision.setNext(ifBranch.getSuccessBranch().get(0).getName());
                        }
                        //simpleComparision.setNext(ifBranch.getSuccessBranch().get(0).getName());
                        comparisionList.add(simpleComparision);
                    }
                    ArrayList<FunctionStep> successSteps = ifBranch.getSuccessBranch();
                    for (FunctionStep successStep:successSteps){
                        if (successStep instanceof Parallel){
                            createParallelFunctions(stepList, (Parallel) successStep);
                        } else if (successStep instanceof Sequence){ //TODO fix two same sequence //TODO fix first
                            // name bug
                            createSequenceFunctions(stepList, (Sequence) successStep);
                        }
                    }
                }
                ChoiceStep choiceStep = new ChoiceStep();
                choiceStep.setChoices(comparisionList);
                if (((IfExpr) functionStep).getElseBranchBody().get(0) instanceof Sequence){
                    choiceStep.setDef(((Sequence) ((IfExpr) functionStep).getElseBranchBody().get(0)).getFunctionList().get(0).getName());
                } else {
                    choiceStep.setDef(((IfExpr) functionStep).getElseBranchBody().get(0).getName());
                }

                ArrayList<FunctionStep> successSteps =  ((IfExpr) functionStep).getElseBranchBody();
                for (FunctionStep successStep:successSteps){
                    if (successStep instanceof Parallel){
                        createParallelFunctions(stepList, (Parallel) successStep);
                    } else if (successStep instanceof Sequence){ //TODO fix two same sequence //TODO fix first
                        // name bug
                        createSequenceFunctions(stepList, (Sequence) successStep);
                    }
                }
                stepList.put(functionStep.getName(), choiceStep);
            }
            if (functionStep instanceof Parallel){
                createParallelFunctions(stepList, (Parallel) functionStep);
            } else if (functionStep instanceof Sequence){ //TODO fix two same sequence //TODO fix first
                // name bug
                createSequenceFunctions(stepList, (Sequence) functionStep);
            }
        }

        step.setStates(stepList);
//        System.out.println("----------------------------------------------");
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
//        String cloudFormationJson = null;
//            cloudFormationJson = mapper.writeValueAsString(step);
//            System.out.println(cloudFormationJson);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        System.out.println("----------------------------------------------");
        return step;
    }

    private void createParallelFunctions(Map<String, Object> stepList, Parallel functionStep) {
        Parallel parallel = functionStep;
        ParStep parStep = new ParStep();
        parStep.setType("Parallel");
        //parStep.setNext();
        parStep.setEnd(true);//check
        ArrayList<ParNestedBranch> branchList = parStep.getBranch();
        for(Function function : parallel.getFunctionList()){
            ParNestedBranch nestedBranch = new ParNestedBranch();
            nestedBranch.setStartsAt(function.getName());

            SeqStep nestedStep = new SeqStep();
            nestedStep.setType("Task");
            nestedStep.setEnd(true);
            nestedStep.setResource("${"+function.getName()+"Arn}");
            nestedBranch.getStates().put(function.getName(),nestedStep);
            branchList.add(nestedBranch);
        }
        parStep.setBranch(branchList);
        stepList.put(parallel.getName(),parStep);
    }

    private void createSequenceFunctions(Map<String, Object> stepList, Sequence functionStep) {
        List<Function> sequenceFunctionList = functionStep.getFunctionList();
        for (int i=0;i<sequenceFunctionList.size();i++){
            Function function = sequenceFunctionList.get(i);
            SeqStep sequenceStep = new SeqStep();
            sequenceStep.setType("Task");
            sequenceStep.setResource("${"+function.getName()+"Arn}");
            if (i+1 != sequenceFunctionList.size()){
                sequenceStep.setNext(sequenceFunctionList.get(i+1).getName());
            } else {
                sequenceStep.setEnd(true);
            }
            stepList.put(function.getName(),sequenceStep);
        }
    }

    public Comparision comparisionBuilder(Condition condition) {
        Object left = condition.getLeftSide();
        Object right = condition.getRightSide();
        if (!(left instanceof Condition || right instanceof Condition)) {
            //assuming compiler validates only one side is the var
            String variable = "";
            SimpleComparision comparision = assignEvaulator(left, condition);
            if (comparision == null) {
                variable = ((String) left).replace("$","$.");
                comparision = assignEvaulator(right, condition);
            } else {
                variable = ((String) right).replace("$","$.");
            }
            comparision.setVariable(variable);

            if (condition.getEvaluator().equals("!=")) {
                NestedComparision nestedComparision = new NestedComparision();
                nestedComparision.setNot(comparision);
                return nestedComparision;
            } else {
                return comparision;
            }

        } else if (left instanceof Condition && right instanceof Condition) {
            SimpleComparision leftCompare = (SimpleComparision) comparisionBuilder((Condition) left);
            SimpleComparision rightCompare = (SimpleComparision) comparisionBuilder((Condition) right);
            List<SimpleComparision> simpleComparisionList = new ArrayList<>();
            simpleComparisionList.add(leftCompare);
            simpleComparisionList.add(rightCompare);
            NestedComparision comparision = new NestedComparision();
            switch (condition.getEvaluator()) {
                case "!":
                    System.out.println("Inside the Case");
                    System.out.println(leftCompare);
                    System.out.println(rightCompare);
                    break;
                case "&&":
                    comparision.setAnd(simpleComparisionList);
                    break;
                case "||":
                    comparision.setOr(simpleComparisionList);
                    break;
            }
            return comparision;
        } else {
            if (condition.getEvaluator().equals("!")) {
                if (left instanceof Condition) {
                    Comparision comparision = comparisionBuilder((Condition) left);
                    if (comparision instanceof SimpleComparision) {
                        NestedComparision nestedComparision = new NestedComparision();
                        nestedComparision.setNot((SimpleComparision) comparision);
                        return nestedComparision;
                    } else {
                        System.out.println("Not a simple Branch");
                        return null;
                    }
                } else {
                    System.out.println("Not a Condition");
                    return null;
                }
            } else {
                System.out.println("Invalid Code");
                return null;
            }
        }
    }

    public SimpleComparision assignEvaulator(Object left, Condition condition) {
        if (left instanceof String) {
            String leftStr = (String) left;
            if (((String) left).startsWith("$")) {
                return null;
            } else {
                StringComparision stringComparision = new StringComparision();
                switch (condition.getEvaluator()) {
                    case ">=":
                        stringComparision.setStringGreaterThanEquals(leftStr);
                        break;
                    case "<=":
                        stringComparision.setStringLessThanEquals(leftStr);
                        break;
                    case ">":
                        stringComparision.setStringGreaterThan(leftStr);
                        break;
                    case "<":
                        stringComparision.setStringLessThan(leftStr);
                        break;
                    case "==":
                        stringComparision.setStringEquals(leftStr);
                        break;
                    case "!=":
                        stringComparision.setStringEquals(leftStr);
                        break;
                }
                return stringComparision;
            }
        } else if (left instanceof Double) {
            Double leftInt = (Double) left;
            NumericComparision numericComparision = new NumericComparision();
            switch (condition.getEvaluator()) {
                case ">=":
                    numericComparision.setNumericGreaterThanEquals(leftInt);
                    break;
                case "<=":
                    numericComparision.setNumericLessThanEquals(leftInt);
                    break;
                case ">":
                    numericComparision.setNumericGreaterThan(leftInt);
                    break;
                case "<":
                    numericComparision.setNumericLessThan(leftInt);
                    break;
                case "==":
                    numericComparision.setNumericEquals(leftInt);
                    break;
                case "!=":
                    numericComparision.setNumericEquals(leftInt);
                    break;
            }
            return numericComparision;
        } else if (left instanceof Boolean) {
            //boolean
            Boolean leftBool = (Boolean) left;
            BooleanComparision booleanComparision = new BooleanComparision();
            if (condition.getEvaluator().equals("==")) {
                booleanComparision.setBooleanEquals(leftBool);
            } else if (condition.getEvaluator().equals("!=")) {
                booleanComparision.setBooleanEquals(leftBool);
            }
            return booleanComparision;
        } else {
            System.out.println();
            throw new IllegalArgumentException("Object is invalid");
        }
    }

    public IAMRole generateLambdaIAM() {

        IAMStatement iamStatement = new IAMStatement();
        Map<String, Object> princi = new LinkedHashMap<>();
        princi.put("Service", "lambda.amazonaws.com");
        iamStatement.setPrincipal(princi);
        iamStatement.setAction("sts:AssumeRole");

        ArrayList<IAMStatement> iamStatements = new ArrayList<>();
        iamStatements.add(iamStatement);

        IAMPolicyDocument iamAssumeRolePolicyDocument = new IAMPolicyDocument();
        iamAssumeRolePolicyDocument.setStatement(iamStatements);

        IAMProperty iamProperty = new IAMProperty();
        iamProperty.setAssumeRolePolicyDocument(iamAssumeRolePolicyDocument);

        IAMRole iamRole = new IAMRole();
        iamRole.setProperty(iamProperty);
        return iamRole;
    }

    public IAMRole generateStateMachineIAM() {

        IAMStatement iamStatement = new IAMStatement();
        ArrayList<Object> servicesList = new ArrayList<>();
        Map<String, String> service = new LinkedHashMap<>();
        service.put("Fn::Sub", "states.${AWS::Region}.amazonaws.com");
        servicesList.add(service);

        Map<String, Object> princi = new LinkedHashMap<>();
        princi.put("Service", servicesList);

        iamStatement.setPrincipal(princi);

        ArrayList<IAMStatement> iamStatements = new ArrayList<>();
        iamStatements.add(iamStatement);

        iamStatement.setAction("sts:AssumeRole");

        IAMPolicyDocument iamAssumeRolePolicyDocument = new IAMPolicyDocument();
        iamAssumeRolePolicyDocument.setStatement(iamStatements);

        IAMProperty iamProperty = new IAMProperty();
        iamProperty.setAssumeRolePolicyDocument(iamAssumeRolePolicyDocument);

        iamProperty.setPath("/");

        IAMPolicy iamPolicy = new IAMPolicy();
        IAMPolicyDocument iamPolicyDoc = new IAMPolicyDocument();

        IAMStatement statement = new IAMStatement();
        statement.setAction(new String[]{"lambda:InvokeFunction"});
        statement.setResource("*");

        ArrayList<IAMStatement> statementList = new ArrayList<>();
        statementList.add(statement);
        iamPolicyDoc.setStatement(statementList);

        iamPolicy.setPolicyDocument(iamPolicyDoc);

        ArrayList<IAMPolicy> iamPolicies = new ArrayList<>();
        iamPolicies.add(iamPolicy);
        iamProperty.setPolicies(iamPolicies);

        IAMRole iamRole = new IAMRole();
        iamRole.setProperty(iamProperty);
        return iamRole;
    }

    public Map<String, Lambda> generateLambdas() {
        Map<String, Lambda> lamdas = new LinkedHashMap<>();
        for (Function function : functionList) {
            ArrayList<String> attributes = new ArrayList<>();
            attributes.add("LambdaExecutionRole"); //Permission
            attributes.add("Arn");
            LambdaRole lambdaRole = new LambdaRole(attributes);

            LambdaCode lambdaCode = new LambdaCode(readFileContents(function.getHandler()));
            LambdaProperty lambdaProperty = new LambdaProperty("index.handler", lambdaRole, lambdaCode,
                    getLanguageRuntime(function.getLanguage()), "25");

            Lambda lambda = new Lambda();
            lambda.setProperties(lambdaProperty);
            lamdas.put(function.getName(), lambda);
        }
        return lamdas;
    }

    private static String readFileContents(String filePath)
    {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    private String getLanguageRuntime (String language) {
        switch (language){
            case "nodejs":
                return "nodejs12.x";
            case "java":
                return "java8";
            case "python":
                return "Python 3.8";
            default:
                throw new UnsupportedOperationException();
        }
    }


    public StateMachine generateStateMachine() {
        StateMachine stateMachine = new StateMachine();
        StateDefString defString = new StateDefString();
        ArrayList<Object> sub = new ArrayList<>();

        Step step = generateStep();

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            String stepString = mapper.writeValueAsString(step);

            sub.add(stepString);
            Map<String, LambdaArn> lambdaMap = new LinkedHashMap<>();

            for (Function function:functionList){
                LambdaArn lambdaArn = new LambdaArn();
                lambdaArn.getAttr().add(function.getName());
                lambdaArn.getAttr().add("Arn");
                lambdaMap.put(function.getName()+"Arn",lambdaArn);
            }

            sub.add(lambdaMap);

            defString.setSub(sub);

            StateProperties stateProperties = new StateProperties();
            stateProperties.setStateDefString(defString);

            //Map<String, LambdaArn> roleMap = new LinkedHashMap<>();
            LambdaArn roleArn = new LambdaArn();
            roleArn.getAttr().add("StatesExecutionRole");
            roleArn.getAttr().add("Arn");
            //roleMap.put("RoleArn",roleArn);

            stateProperties.setRoleMap(roleArn);
            stateMachine.setProperties(stateProperties);
            return stateMachine;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
