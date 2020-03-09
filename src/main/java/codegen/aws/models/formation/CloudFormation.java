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
    private Map<String ,CloudFormationComponent> objectMap = new LinkedHashMap<>();

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

    public Map<String, CloudFormationComponent> getObjectMap() {
        return objectMap;
    }

    public void setObjectMap(Map<String, CloudFormationComponent> objectMap) {
        this.objectMap = objectMap;
    }
}
