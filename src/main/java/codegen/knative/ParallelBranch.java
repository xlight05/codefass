package codegen.knative;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ParallelBranch {
    @JsonProperty("subscriber")
    private Subscriber subscriber;

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }
}
