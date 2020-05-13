package codegen.aws.models.formation.step;

import codegen.aws.models.formation.CloudFormationComponent;
import codegen.aws.models.formation.lambda.LambdaProperty;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;

public class Step {
    @JsonProperty("Comment")
    private String comment = "Auto generated serverless workflow from FaaS";
    @JsonProperty("StartAt")
    private String startsAt = "ChoiceStateX";
    @JsonProperty("States")
    private Map<String , Object> states = new LinkedHashMap<>();

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(String startsAt) {
        this.startsAt = startsAt;
    }

    public Map<String, Object> getStates() {
        return states;
    }

    public void setStates(Map<String, Object> states) {
        this.states = states;
    }
}
