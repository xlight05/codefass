package codegen.knative;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SequenceYaml {
    @JsonProperty("apiVersion")
    private String apiVersion = "flows.knative.dev/v1alpha1";

    @JsonProperty("kind")
    private String kind = "CronJobSource";

    @JsonProperty("metadata")
    private Metadata metadata;

    @JsonProperty("spec")
    private SequenceSpec spec;

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

    public SequenceSpec getSpec() {
        return spec;
    }

    public void setSpec(SequenceSpec spec) {
        this.spec = spec;
    }
}
