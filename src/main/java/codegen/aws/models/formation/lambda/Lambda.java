package codegen.aws.models.formation.lambda;

public class Lambda {
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
