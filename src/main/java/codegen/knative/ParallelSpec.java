package codegen.knative;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class ParallelSpec {
    @JsonProperty("channelTemplate")
    private ChannelTemplate channelTemplate;

    @JsonProperty("branches")
    private ArrayList<ParallelBranch> parallelBranches = new ArrayList<>();

    public ArrayList<ParallelBranch> getParallelBranches() {
        return parallelBranches;
    }

    public void setParallelBranches(ArrayList<ParallelBranch> parallelBranches) {
        this.parallelBranches = parallelBranches;
    }

    public ChannelTemplate getChannelTemplate() {
        return channelTemplate;
    }

    public void setChannelTemplate(ChannelTemplate channelTemplate) {
        this.channelTemplate = channelTemplate;
    }
}
