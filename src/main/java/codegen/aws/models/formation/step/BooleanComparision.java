package codegen.aws.models.formation.step;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BooleanComparision extends SimpleComparision {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("BooleanEquals")
    private Boolean booleanEquals = null;

    public Boolean getBooleanEquals() {
        return booleanEquals;
    }

    public void setBooleanEquals(Boolean booleanEquals) {
        this.booleanEquals = booleanEquals;
    }
}
