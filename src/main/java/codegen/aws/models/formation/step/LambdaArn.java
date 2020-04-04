package codegen.aws.models.formation.step;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class LambdaArn {
    @JsonProperty("Fn::GetAtt")
    private ArrayList<String> attr = new ArrayList<>();

    public ArrayList<String> getAttr() {
        return attr;
    }

    public void setAttr(ArrayList<String> attr) {
        this.attr = attr;
    }
}
