package codegen;

import java.io.Serializable;
import java.util.ArrayList;

public class IfBranch implements Serializable {
    private Condition condition;
    private ArrayList<FunctionStep> successBranch;

    public IfBranch(Condition condition) {
        this.condition = condition;
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

    @Override
    public String toString() {
        return "IfBranch{" +
                "condition=" + condition +
                ", successBranch=" + successBranch +
                '}';
    }
}

