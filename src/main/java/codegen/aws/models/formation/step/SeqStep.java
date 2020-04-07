package codegen.aws.models.formation.step;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SeqStep {
    @JsonProperty("Type")
    private String type = "Task";

    @JsonProperty("Resource")
    private String resource = "lambdaArn";

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Next")
    private String next = null;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("End")
    private Boolean end = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
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
}
