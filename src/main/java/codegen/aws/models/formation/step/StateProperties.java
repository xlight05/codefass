package codegen.aws.models.formation.step;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StateProperties {
    @JsonProperty("DefinitionString")
    private StateDefString stateDefString;
    @JsonProperty("RoleArn")
    private StateRoleArn stateRoleArn;
}
