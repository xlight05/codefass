package codegen.aws.models.formation.lambda;

import codegen.aws.models.formation.CloudFormationComponent;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Lambda implements CloudFormationComponent {
    @JsonProperty("Type")
    private String type = "AWS::Lambda::Function";
    @JsonProperty("Properties")
    private LambdaProperty properties;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LambdaProperty getProperties() {
        return properties;
    }

    public void setProperties(LambdaProperty properties) {
        this.properties = properties;
    }
}
