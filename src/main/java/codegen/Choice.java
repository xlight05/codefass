package codegen;

import java.util.ArrayList;

public class Choice implements FunctionStep {
    private String name;
    private Condition condition;
    private ArrayList<FunctionStep>  successBranch;
    private ArrayList<FunctionStep>  FailureBranch;

    public Choice(Condition condition) {
        this.condition = condition;
        this.successBranch = new ArrayList<>();
        this.FailureBranch = new ArrayList<>();
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public ArrayList<FunctionStep> getSuccessBranch() {
        return successBranch;
    }

    public void setSuccessBranch(ArrayList<FunctionStep> successBranch) {
        this.successBranch = successBranch;
    }

    public ArrayList<FunctionStep> getFailureBranch() {
        return FailureBranch;
    }

    public void setFailureBranch(ArrayList<FunctionStep> failureBranch) {
        FailureBranch = failureBranch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Choice{" +
                "condition=" + condition +
                ", successBranch=" + successBranch +
                ", FailureBranch=" + FailureBranch +
                '}';
    }
}

