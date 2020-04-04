package codegen.aws.models.formation.step;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;

public class StateProperties {
    @JsonProperty("DefinitionString")
    private StateDefString stateDefString;
    @JsonProperty("RoleArn")
    private LambdaArn roleMap = new LambdaArn();

    public StateDefString getStateDefString() {
        return stateDefString;
    }

    public void setStateDefString(StateDefString stateDefString) {
        this.stateDefString = stateDefString;
    }

    public LambdaArn getRoleMap() {
        return roleMap;
    }

    public void setRoleMap(LambdaArn roleMap) {
        this.roleMap = roleMap;
    }
}
