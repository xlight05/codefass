package codegen.knative;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;

public class InputSpec {
    @JsonProperty("schedule")
    private String schedule = "*/1 * * * *";

    @JsonProperty("data")
    private String data;

    @JsonProperty("sink")
    private Map<String , Ref> sink = new LinkedHashMap<>();

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Map<String, Ref> getSink() {
        return sink;
    }

    public void setSink(Map<String, Ref> sink) {
        this.sink = sink;
    }
}
