package codegen.knative;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceYaml {
    @JsonProperty("apiVersion")
    private String apiVersion = "serving.knative.dev/v1";

    @JsonProperty("kind")
    private String kind = "Service";

    @JsonProperty("metadata")
    private Metadata metadata;

    @JsonProperty("spec")
    private ServiceSpec spec;

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

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public ServiceSpec getSpec() {
        return spec;
    }

    public void setSpec(ServiceSpec spec) {
        this.spec = spec;
    }
}
