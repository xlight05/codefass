package codegen.knative;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FlowBranch {
    @JsonProperty("filter")
    private Filter filter;

    @JsonProperty("subscriber")
    private Subscriber subscriber;

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }
}
