package codegen.aws.models.formation.iam;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IAMStatementPrincipal {
    @JsonProperty("Service")
    private String service = "lambda.amazonaws.com";

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
