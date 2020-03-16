package codegen.aws.models.formation.step;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class NestedComparision implements Comparision {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Not")
    private SimpleComparision not;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("And")
    private List<SimpleComparision> and;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Or")
    private List<SimpleComparision> or;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Next")
    private String next = null;

    public SimpleComparision getNot() {
        return not;
    }

    public void setNot(SimpleComparision not) {
        this.not = not;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public List<SimpleComparision> getAnd() {
        return and;
    }

    public void setAnd(List<SimpleComparision> and) {
        this.and = and;
    }

    public List<SimpleComparision> getOr() {
        return or;
    }

    public void setOr(List<SimpleComparision> or) {
        this.or = or;
    }
}
