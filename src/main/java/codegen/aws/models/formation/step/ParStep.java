package codegen.aws.models.formation.step;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class ParStep {
    @JsonProperty("Type")
    private String type = "Parallel";

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Next")
    private String next = null;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("End")
    private Boolean end = null;

    @JsonProperty("Branch")
    private ArrayList<ParNestedBranch> branch = new ArrayList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public Boolean getEnd() {
        return end;
    }

    public void setEnd(Boolean end) {
        this.end = end;
    }

    public ArrayList<ParNestedBranch> getBranch() {
        return branch;
    }

    public void setBranch(ArrayList<ParNestedBranch> branch) {
        this.branch = branch;
    }
}
