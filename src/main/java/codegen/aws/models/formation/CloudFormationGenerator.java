package codegen.aws.models.formation;

import codegen.CloudArtifactGenerator;
import codegen.FunctionOrchestrator;
import codegen.aws.models.formation.iam.IAMAssumeRolePolicyDocument;
import codegen.aws.models.formation.iam.IAMProperty;
import codegen.aws.models.formation.iam.IAMRole;
import codegen.aws.models.formation.iam.IAMStatement;
import codegen.aws.models.formation.iam.IAMStatementPrincipal;
import codegen.aws.models.formation.lambda.Lambda;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class CloudFormationGenerator extends CloudArtifactGenerator {

    public CloudFormationGenerator(FunctionOrchestrator functionOrchestrator) {
        super(functionOrchestrator);
    }

    public void build() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        IAMRole iamRole = generateLambdaIAM();

        Lambda lambda = new Lambda();

        Map<String ,CloudFormationComponent> objectMap = new LinkedHashMap<>();
        objectMap.put("LambdaExecutionRole",iamRole);
        objectMap.put("MyLambdaFunction",lambda);


        CloudFormation cloudFormation = new CloudFormation();
        cloudFormation.setObjectMap(objectMap);

        try {
            String cloudFormationJson = mapper.writeValueAsString(cloudFormation);
            System.out.println(cloudFormationJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public IAMRole generateLambdaIAM () {
        IAMStatementPrincipal iamStatementPrincipal= new IAMStatementPrincipal();

        IAMStatement iamStatement = new IAMStatement();
        iamStatement.setPrincipal(iamStatementPrincipal);
        ArrayList<IAMStatement> iamStatements = new ArrayList<>();
        iamStatements.add(iamStatement);

        IAMAssumeRolePolicyDocument iamAssumeRolePolicyDocument = new IAMAssumeRolePolicyDocument();
        iamAssumeRolePolicyDocument.setStatement(iamStatements);

        IAMProperty iamProperty = new IAMProperty();
        iamProperty.setAssumeRolePolicyDocument(iamAssumeRolePolicyDocument);

        IAMRole iamRole = new IAMRole();
        iamRole.setProperty(iamProperty);
        return iamRole;
    }
}
