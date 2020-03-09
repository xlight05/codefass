package codegen.aws.models.formation.lambda;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class LambdaRole {
    @JsonProperty("Fn::GetAtt")
    private ArrayList<String> attributes;

    public LambdaRole(ArrayList<String> attributes) {
        this.attributes = attributes;
    }

    public ArrayList<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<String> attributes) {
        this.attributes = attributes;
    }
}
