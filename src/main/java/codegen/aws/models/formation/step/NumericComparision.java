package codegen.aws.models.formation.step;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NumericComparision extends SimpleComparision {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("NumericEquals")
    private Integer numericEquals = null;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("NumericGreaterThan")
    private Integer numericGreaterThan = null;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("NumericGreaterThanEquals")
    private Integer numericGreaterThanEquals = null;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("NumericLessThan")
    private Integer numericLessThan = null;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("NumericLessThanEquals")
    private Integer numericLessThanEquals = null;

    public Integer getNumericEquals() {
        return numericEquals;
    }

    public void setNumericEquals(Integer numericEquals) {
        this.numericEquals = numericEquals;
    }

    public Integer getNumericGreaterThan() {
        return numericGreaterThan;
    }

    public void setNumericGreaterThan(Integer numericGreaterThan) {
        this.numericGreaterThan = numericGreaterThan;
    }

    public Integer getNumericGreaterThanEquals() {
        return numericGreaterThanEquals;
    }

    public void setNumericGreaterThanEquals(Integer numericGreaterThanEquals) {
        this.numericGreaterThanEquals = numericGreaterThanEquals;
    }

    public Integer getNumericLessThan() {
        return numericLessThan;
    }

    public void setNumericLessThan(Integer numericLessThan) {
        this.numericLessThan = numericLessThan;
    }

    public Integer getNumericLessThanEquals() {
        return numericLessThanEquals;
    }

    public void setNumericLessThanEquals(Integer numericLessThanEquals) {
        this.numericLessThanEquals = numericLessThanEquals;
    }
}
