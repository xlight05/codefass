package codegen.aws.models.formation;

import codegen.aws.models.formation.iam.IAMAssumeRolePolicyDocument;
import codegen.aws.models.formation.iam.IAMProperty;
import codegen.aws.models.formation.iam.IAMRole;
import codegen.aws.models.formation.iam.IAMStatement;
import codegen.aws.models.formation.iam.IAMStatementPrincipal;
import codegen.aws.models.formation.lambda.Lambda;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CloudFormation {
    @JsonProperty("AWSTemplateFormatVersion")
    private String version = "2010-09-09";

    @JsonProperty("Description")
    private String description = "An example template with an IAM role for a Lambda state machine.";

    @JsonProperty("Resources")
    private Map<String ,Object> objectMap = new LinkedHashMap<>();

    public static void main (String [] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

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

        Lambda lambda = new Lambda();

        Map<String ,Object> objectMap = new LinkedHashMap<>();
        objectMap.put("LambdaExecutionRole",iamRole);
        objectMap.put("MyLambdaFunction",lambda);


        CloudFormation cloudFormation = new CloudFormation();
        cloudFormation.setObjectMap(objectMap);


        String cloudFormationJson = mapper.writeValueAsString(cloudFormation);
        System.out.println(cloudFormationJson);

    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getObjectMap() {
        return objectMap;
    }

    public void setObjectMap(Map<String, Object> objectMap) {
        this.objectMap = objectMap;
    }
}
