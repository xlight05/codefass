package codegen.aws.models.formation.iam;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IAMStatement {
    @JsonProperty("Effect")
    private String Effect = "Allow";

    @JsonProperty("Principal")
    private IAMStatementPrincipal principal;

    @JsonProperty("Action")
    private String action = "sts:AssumeRole";

    public String getEffect() {
        return Effect;
    }

    public void setEffect(String effect) {
        Effect = effect;
    }

    public IAMStatementPrincipal getPrincipal() {
        return principal;
    }

    public void setPrincipal(IAMStatementPrincipal principal) {
        this.principal = principal;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
