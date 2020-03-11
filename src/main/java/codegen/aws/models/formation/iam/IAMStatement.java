package codegen.aws.models.formation.iam;

import codegen.aws.models.formation.CloudFormationComponent;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class IAMStatement {
    @JsonProperty("Effect")
    private String effect = "Allow";

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Principal")
    private Map<String , Object> principal;

    @JsonProperty("Action")
    private Object action;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Resource")
    private String resource;

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public Object getAction() {
        return action;
    }

    public void setAction(Object action) {
        this.action = action;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Map<String, Object> getPrincipal() {
        return principal;
    }

    public void setPrincipal(Map<String, Object> principal) {
        this.principal = principal;
    }
}
