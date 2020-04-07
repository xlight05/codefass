package codegen.aws.models.formation.step;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;

public class ParNestedBranch {
    @JsonProperty("StartAt")
    private String startsAt = "ChoiceStateX";
    @JsonProperty("States")
    private Map<String , SeqStep> states = new LinkedHashMap<>();

    public String getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(String startsAt) {
        this.startsAt = startsAt;
    }

    public Map<String, SeqStep> getStates() {
        return states;
    }

    public void setStates(Map<String, SeqStep> states) {
        this.states = states;
    }
}
