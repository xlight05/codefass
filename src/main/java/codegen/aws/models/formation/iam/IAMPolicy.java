package codegen.aws.models.formation.iam;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IAMPolicy {
    @JsonProperty("PolicyName")
    private String policyName = "StatesExecutionPolicy";

    @JsonProperty("PolicyDocument")
    private IAMPolicyDocument policyDocument;

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public IAMPolicyDocument getPolicyDocument() {
        return policyDocument;
    }

    public void setPolicyDocument(IAMPolicyDocument policyDocument) {
        this.policyDocument = policyDocument;
    }
}
