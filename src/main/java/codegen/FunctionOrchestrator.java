package codegen;

import java.io.Serializable;
import java.util.ArrayList;

public class FunctionOrchestrator implements Serializable {
    private String name;
    private String description;
    private String version;
    private ArrayList<FunctionStep> stepList;

    public FunctionOrchestrator(String name, String description, String version) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.stepList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ArrayList<FunctionStep> getStepList() {
        return stepList;
    }

    public void setStepList(ArrayList<FunctionStep> stepList) {
        this.stepList = stepList;
    }

    @Override
    public String toString() {
        return "FunctionOrchestrator{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", version='" + version + '\'' +
                ", stepList=" + stepList +
                '}';
    }
}
