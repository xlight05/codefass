package codegen.aws.models.formation.iam;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class IAMProperty {
    @JsonProperty("AssumeRolePolicyDocument")
    private IAMPolicyDocument assumeRolePolicyDocument;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Path")
    private String path;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Policies")
    private ArrayList<IAMPolicy> policies;

    public IAMPolicyDocument getAssumeRolePolicyDocument() {
        return assumeRolePolicyDocument;
    }

    public void setAssumeRolePolicyDocument(IAMPolicyDocument assumeRolePolicyDocument) {
        this.assumeRolePolicyDocument = assumeRolePolicyDocument;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArrayList<IAMPolicy> getPolicies() {
        return policies;
    }

    public void setPolicies(ArrayList<IAMPolicy> policies) {
        this.policies = policies;
    }
}
