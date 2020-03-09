package codegen.aws.models.formation.iam;

import codegen.aws.models.formation.CloudFormationComponent;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;

public class IAMStatement {
    @JsonProperty("Effect")
    private String Effect = "Allow";

    @JsonProperty("Principal")
    private Map<String , Object> principal = new LinkedHashMap<>();

    @JsonProperty("Action")
    private String action = "sts:AssumeRole";

    public IAMStatement() {
        this.principal.put("Service","lambda.amazonaws.com");
    }

    public String getEffect() {
        return Effect;
    }

    public void setEffect(String effect) {
        Effect = effect;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
