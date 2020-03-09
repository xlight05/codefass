package codegen.aws.models.formation.iam;

import codegen.aws.models.formation.CloudFormationComponent;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IAMRole implements CloudFormationComponent {
    @JsonProperty("Type")
    private String type = "AWS::IAM::Role";

    @JsonProperty("Properties")
    private IAMProperty property;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public IAMProperty getProperty() {
        return property;
    }

    public void setProperty(IAMProperty property) {
        this.property = property;
    }
}
