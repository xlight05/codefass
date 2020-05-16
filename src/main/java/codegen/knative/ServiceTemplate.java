package codegen.knative;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceTemplate {
    @JsonProperty("spec")
    private TemplateSpec templateSpec;

    public TemplateSpec getTemplateSpec() {
        return templateSpec;
    }

    public void setTemplateSpec(TemplateSpec templateSpec) {
        this.templateSpec = templateSpec;
    }
}
