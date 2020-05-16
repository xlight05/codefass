package codegen.knative;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceSpec {
    @JsonProperty("template")
    private ServiceTemplate serviceTemplate;

    public ServiceTemplate getServiceTemplate() {
        return serviceTemplate;
    }

    public void setServiceTemplate(ServiceTemplate serviceTemplate) {
        this.serviceTemplate = serviceTemplate;
    }
}
