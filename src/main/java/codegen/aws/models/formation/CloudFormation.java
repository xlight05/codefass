package codegen.aws.models.formation;

import com.fasterxml.jackson.annotation.JsonProperty;

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
