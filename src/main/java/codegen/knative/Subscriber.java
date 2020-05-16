package codegen.knative;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Subscriber {

    @JsonProperty("ref")
    private Ref ref;

    public Ref getRef() {
        return ref;
    }

    public void setRef(Ref ref) {
        this.ref = ref;
    }
}
