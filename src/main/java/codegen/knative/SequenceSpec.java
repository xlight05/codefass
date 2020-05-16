package codegen.knative;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class SequenceSpec {
    @JsonProperty("channelTemplate")
    private ChannelTemplate channelTemplate;

    @JsonProperty("steps")
    private ArrayList<SequenceBranch> sequenceStep = new ArrayList<>();

    public ArrayList<SequenceBranch> getSequenceStep() {
        return sequenceStep;
    }

    public void setSequenceStep(ArrayList<SequenceBranch> sequenceStep) {
        this.sequenceStep = sequenceStep;
    }

    public ChannelTemplate getChannelTemplate() {
        return channelTemplate;
    }

    public void setChannelTemplate(ChannelTemplate channelTemplate) {
        this.channelTemplate = channelTemplate;
    }
}
