package codegen.knative;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class TemplateSpec {
    @JsonProperty("containers")
    private ArrayList<Container> containers= new ArrayList<>();

    public ArrayList<Container> getContainers() {
        return containers;
    }

    public void setContainers(ArrayList<Container> containers) {
        this.containers = containers;
    }
}
