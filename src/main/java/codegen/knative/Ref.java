package codegen.knative;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ref {
    @JsonProperty("apiVersion")
    private String apiVersion = "flows.knative.dev/v1alpha1";

    @JsonProperty("kind")
    private String kind = "Parallel";

    @JsonProperty("name")
    private String name = "odd-even-parallel";

    public Ref(String kind, String name) {
        this.kind = kind;
        this.name = name;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
