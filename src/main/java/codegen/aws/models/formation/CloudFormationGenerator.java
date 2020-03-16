package codegen.aws.models.formation;

import codegen.Choice;
import codegen.CloudArtifactGenerator;
import codegen.Condition;
import codegen.Function;
import codegen.FunctionOrchestrator;
import codegen.FunctionStep;
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
import codegen.aws.models.formation.step.NestedComparision;
import codegen.aws.models.formation.step.NumericComparision;
import codegen.aws.models.formation.step.SimpleComparision;
import codegen.aws.models.formation.step.Step;
import codegen.aws.models.formation.step.StringComparision;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.javaws.exceptions.InvalidArgumentException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CloudFormationGenerator extends CloudArtifactGenerator {
    private ArrayList<Function> functionList;
    public CloudFormationGenerator(FunctionOrchestrator functionOrchestrator) {
        super(functionOrchestrator);
        functionList = getFunctionsFromStepList(functionOrchestrator.getStepList());
    }

    public ArrayList<Function> getFunctionsFromStepList (List<FunctionStep> stepList) {
        ArrayList<Function> functions = new ArrayList<>();
        for (FunctionStep step: stepList){
            functions.addAll(getFunctionsFromStep(step));
        }
        return functions;
    }

    public ArrayList<Function> getFunctionsFromStep (FunctionStep step) {
        ArrayList<Function> functions = new ArrayList<>();
        if (step instanceof Sequence) {
            Sequence sequence = (Sequence)step;
            functions.addAll(sequence.getFunctionList());
        } else if (step instanceof Parallel) {
            Parallel parallel = (Parallel)step;
            functions.addAll(parallel.getFunctionList());
        } else if (step instanceof Choice) {
            Choice choice = (Choice)step;
            functions.addAll(getFunctionsFromStepList(choice.getSuccessBranch()));
            functions.addAll(getFunctionsFromStepList(choice.getFailureBranch()));
        }
        return functions;
    }

//    public void build() {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.enable(SerializationFeature.INDENT_OUTPUT);
//
//        IAMRole iamRole = generateLambdaIAM();
//        IAMRole stateIAM = generateStateMachineIAM();
//
//        Map<String,Lambda> lambdas = generateLambdas();
//
//        Map<String ,CloudFormationComponent> objectMap = new LinkedHashMap<>();
//        objectMap.put("LambdaExecutionRole",iamRole);
//
//        for (String name : lambdas.keySet()){
//            objectMap.put(name,lambdas.get(name));
//        }
//        objectMap.put("StatesExecutionRole",stateIAM);
//
//
//
//
//        CloudFormation cloudFormation = new CloudFormation();
//        cloudFormation.setObjectMap(objectMap);
//
//        try {
//            String cloudFormationJson = mapper.writeValueAsString(cloudFormation);
//            System.out.println(cloudFormationJson);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//    }

    public void build() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

//        Step step = new Step();
//
//        ChoiceStep choiceStep = new ChoiceStep();
//
//        List<Comparision> choices = new ArrayList<>();
//
//        SimpleComparision simpleComparision = new SimpleComparision();
//        simpleComparision.setVariable("$.value");
//        simpleComparision.setNumericEquals(0);
//        simpleComparision.setNext("ValueIsZero");
//
//        SimpleComparision simpleComparision1 = new SimpleComparision();
//        simpleComparision1.setVariable("$.type");
//        simpleComparision1.setStringEquals("Private");
//        simpleComparision1.setNext("Public");
//
//        NestedComparision nestedComparision1 = new NestedComparision();
//        SimpleComparision simpleComparision2 = new SimpleComparision();
//        simpleComparision2.setVariable("$.type");
//        simpleComparision2.setStringEquals("Private");
//
//        nestedComparision1.setNot(simpleComparision2);
//        nestedComparision1.setNext("Public");
//
//        choices.add(simpleComparision);
//        choices.add(simpleComparision1);
//        choices.add(nestedComparision1);
//
//        choiceStep.setChoices(choices);
//        Map<String,Object> stepList = new LinkedHashMap<>();
//        stepList.put("ChoiceStateX",choiceStep);
//        step.setStates(stepList);

        Step step = generateStep();

        try {
            String cloudFormationJson = mapper.writeValueAsString(step);
            System.out.println(cloudFormationJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public Step generateStep () {
        FunctionOrchestrator functionOrchestrator = super.getFunctionOrchestrator();

        Step step = new Step();
        step.setComment(functionOrchestrator.getDescription());

        List <FunctionStep> functionStepList = functionOrchestrator.getStepList();
        List<Comparision> comparisionList = new ArrayList<>();
        //Because first one is Choice
        for (FunctionStep functionStep : functionStepList){
            if (functionStep instanceof Choice){
                Choice choice = (Choice)functionStep;
                step.setStartsAt(choice.getName());//Fix
                Condition rootCondition = choice.getCondition();

                SimpleComparision simpleComparision= (SimpleComparision) comparisionBuilder(rootCondition);

                comparisionList.add(simpleComparision);
            }
        }
        ChoiceStep choiceStep = new ChoiceStep();
        choiceStep.setChoices(comparisionList);
        Map<String,Object> stepList = new LinkedHashMap<>();
        stepList.put("ChoiceStateX",choiceStep);
        step.setStates(stepList);
        return step;
    }


    public Comparision comparisionBuilder (Condition condition){
        Object left = condition.getLeftSide();
        Object right = condition.getRightSide();
        if (!(left instanceof Condition || right instanceof Condition)){
            //assuming compiler validates only one side is the var
            String variable = "";
            SimpleComparision comparision =assignEvaulator(left,condition);
            if (comparision==null){
                variable = (String)left;
                comparision = assignEvaulator(right,condition);
            } else {
                variable = (String)right;
            }
            return comparision;
        } else {
            //fill
            return null;
        }
    }
    public SimpleComparision assignEvaulator(Object left,Condition condition){
        if (left instanceof String){
            String leftStr = (String)left;
            if (((String) left).startsWith("$.")){
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
                }
                return stringComparision;
            }
        } else if (left instanceof Integer){
            Integer leftInt = (Integer) left;
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
            }
            return numericComparision;
        } else if (left instanceof Boolean){
            //boolean
            Boolean leftBool = (Boolean) left;
            BooleanComparision booleanComparision = new BooleanComparision();
            if (condition.getEvaluator().equals("==")){
                booleanComparision.setBooleanEquals(leftBool);
            }
            return booleanComparision;
        } else {
            throw new IllegalArgumentException("Object is invalid");
        }
    }

    public IAMRole generateLambdaIAM () {

        IAMStatement iamStatement = new IAMStatement();
        Map<String,Object> princi = new LinkedHashMap<>();
        princi.put("Service","lambda.amazonaws.com");
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

    public IAMRole generateStateMachineIAM () {

        IAMStatement iamStatement = new IAMStatement();
        ArrayList<Object> servicesList = new ArrayList<>();
        Map <String,String> service = new LinkedHashMap<>();
        service.put("Fn::Sub","states.${AWS::Region}.amazonaws.com");
        servicesList.add(service);

        Map<String,Object> princi = new LinkedHashMap<>();
        princi.put("Service",servicesList);

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

    public Map<String,Lambda> generateLambdas () {
        Map<String,Lambda> lamdas = new LinkedHashMap<>();
        for (Function function:functionList){
            ArrayList<String> attributes= new ArrayList<>();
            attributes.add("LambdaExecutionRole"); //Permission
            attributes.add("Arn");
            LambdaRole lambdaRole = new LambdaRole(attributes);

            LambdaCode lambdaCode = new LambdaCode("exports.handler = (event, context, callback) => {\n    " +
                    "callback(null, \"Hello From "+function.getName()+"!\");\n};\n");
            LambdaProperty lambdaProperty = new LambdaProperty(function.getHandler(),lambdaRole,lambdaCode,
                    function.getLanguage(),"25");

            Lambda lambda = new Lambda();
            lambda.setProperties(lambdaProperty);
            lamdas.put(function.getName(),lambda);
        }
        return lamdas;
    }
}
