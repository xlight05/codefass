package codegen.aws.models.formation.step;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ChoiceStep {
    @JsonProperty("Type")
    private String type = "Choice";

    @JsonProperty("Choices")
    private List<Comparision> choices;

    @JsonProperty("Default")
    private String def = "DefaultState";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Comparision> getChoices() {
        return choices;
    }

    public void setChoices(List<Comparision> choices) {
        this.choices = choices;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }
}
