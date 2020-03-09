package codegen.aws.models.formation.lambda;

import codegen.aws.models.formation.CloudFormationComponent;

public class Lambda implements CloudFormationComponent {
    private String type = "AWS::Lambda::Function";
    private String Properties = "temp";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProperties() {
        return Properties;
    }

    public void setProperties(String properties) {
        Properties = properties;
    }
}
