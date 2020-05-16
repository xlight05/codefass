package codegen.knative;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChannelTemplate {
    @JsonProperty("apiVersion")
    private String apiVersion = "messaging.knative.dev/v1alpha1";

    @JsonProperty("kind")
    private String kind = "InMemoryChannel";

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
}
