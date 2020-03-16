package codegen.aws.models.formation.step;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StringComparision extends SimpleComparision{
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("StringEquals")
    private String stringEquals = null;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("StringGreaterThan")
    private String stringGreaterThan = null;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("StringGreaterThanEquals")
    private String stringGreaterThanEquals = null;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("StringLessThan")
    private String stringLessThan = null;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("StringLessThanEquals")
    private String stringLessThanEquals = null;

    public String getStringEquals() {
        return stringEquals;
    }

    public void setStringEquals(String stringEquals) {
        this.stringEquals = stringEquals;
    }

    public String getStringGreaterThan() {
        return stringGreaterThan;
    }

    public void setStringGreaterThan(String stringGreaterThan) {
        this.stringGreaterThan = stringGreaterThan;
    }

    public String getStringGreaterThanEquals() {
        return stringGreaterThanEquals;
    }

    public void setStringGreaterThanEquals(String stringGreaterThanEquals) {
        this.stringGreaterThanEquals = stringGreaterThanEquals;
    }

    public String getStringLessThan() {
        return stringLessThan;
    }

    public void setStringLessThan(String stringLessThan) {
        this.stringLessThan = stringLessThan;
    }

    public String getStringLessThanEquals() {
        return stringLessThanEquals;
    }

    public void setStringLessThanEquals(String stringLessThanEquals) {
        this.stringLessThanEquals = stringLessThanEquals;
    }
}
