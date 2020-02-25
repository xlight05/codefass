package codegen.aws.models.formation.iam;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IAMProperty {
    @JsonProperty("AssumeRolePolicyDocument")
    private IAMAssumeRolePolicyDocument assumeRolePolicyDocument;

    public IAMAssumeRolePolicyDocument getAssumeRolePolicyDocument() {
        return assumeRolePolicyDocument;
    }

    public void setAssumeRolePolicyDocument(IAMAssumeRolePolicyDocument assumeRolePolicyDocument) {
        this.assumeRolePolicyDocument = assumeRolePolicyDocument;
    }
}
