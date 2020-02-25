package codegen.aws.models.formation.iam;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class IAMAssumeRolePolicyDocument {
    @JsonProperty("Version")
    private String version = "2012-10-17";
    @JsonProperty("Statement")
    private ArrayList<IAMStatement> statement;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ArrayList<IAMStatement> getStatement() {
        return statement;
    }

    public void setStatement(ArrayList<IAMStatement> statement) {
        this.statement = statement;
    }
}
