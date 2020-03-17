package codegen.aws.models.formation.step;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NumericComparision extends SimpleComparision {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("NumericEquals")
    private Double numericEquals = null;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("NumericGreaterThan")
    private Double numericGreaterThan = null;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("NumericGreaterThanEquals")
    private Double numericGreaterThanEquals = null;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("NumericLessThan")
    private Double numericLessThan = null;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("NumericLessThanEquals")
    private Double numericLessThanEquals = null;

    public Double getNumericEquals() {
        return numericEquals;
    }

    public void setNumericEquals(Double numericEquals) {
        this.numericEquals = numericEquals;
    }

    public Double getNumericGreaterThan() {
        return numericGreaterThan;
    }

    public void setNumericGreaterThan(Double numericGreaterThan) {
        this.numericGreaterThan = numericGreaterThan;
    }

    public Double getNumericGreaterThanEquals() {
        return numericGreaterThanEquals;
    }

    public void setNumericGreaterThanEquals(Double numericGreaterThanEquals) {
        this.numericGreaterThanEquals = numericGreaterThanEquals;
    }

    public Double getNumericLessThan() {
        return numericLessThan;
    }

    public void setNumericLessThan(Double numericLessThan) {
        this.numericLessThan = numericLessThan;
    }

    public Double getNumericLessThanEquals() {
        return numericLessThanEquals;
    }

    public void setNumericLessThanEquals(Double numericLessThanEquals) {
        this.numericLessThanEquals = numericLessThanEquals;
    }
}
