package codegen.knative;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Metadata {
    @JsonProperty("name")
    private String name = "cronjob-source";

    public Metadata(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
