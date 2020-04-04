package codegen.aws.models.formation.step;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class StateDefString {
    @JsonProperty("Fn::Sub")
    private ArrayList<Object> sub;

    public ArrayList<Object> getSub() {
        return sub;
    }

    public void setSub(ArrayList<Object> sub) {
        this.sub = sub;
    }
}
