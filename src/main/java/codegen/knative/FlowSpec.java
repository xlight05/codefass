package codegen.knative;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class FlowSpec {
    @JsonProperty("channelTemplate")
    private ChannelTemplate channelTemplate;

    @JsonProperty("branches")
    private ArrayList<FlowBranch> flowBranches = new ArrayList<>();

    public ChannelTemplate getChannelTemplate() {
        return channelTemplate;
    }

    public void setChannelTemplate(ChannelTemplate channelTemplate) {
        this.channelTemplate = channelTemplate;
    }

    public ArrayList<FlowBranch> getFlowBranches() {
        return flowBranches;
    }

    public void setFlowBranches(ArrayList<FlowBranch> flowBranches) {
        this.flowBranches = flowBranches;
    }
}
