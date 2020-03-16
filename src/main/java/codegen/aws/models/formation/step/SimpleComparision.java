package codegen.aws.models.formation.step;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleComparision implements Comparision{
    @JsonProperty("Variable")
    private String variable = "$.type";

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Next")
    private String next = null;

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
