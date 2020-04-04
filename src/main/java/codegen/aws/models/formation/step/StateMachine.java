package codegen.aws.models.formation.step;

import codegen.aws.models.formation.CloudFormationComponent;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StateMachine implements CloudFormationComponent{
    @JsonProperty("Type")
    private String type = "AWS::StepFunctions::StateMachine";

    @JsonProperty("Properties")
    private StateProperties properties;

}
