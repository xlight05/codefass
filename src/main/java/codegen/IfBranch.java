package codegen;

import java.io.Serializable;
import java.util.ArrayList;

public class IfBranch implements Serializable {
    private Condition condition;
    private FunctionStep successBranch;

    public IfBranch(Condition condition) {
        this.condition = condition;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public FunctionStep getSuccessBranch() {
        return successBranch;
    }

    public void setSuccessBranch(FunctionStep successBranch) {
        this.successBranch = successBranch;
    }


    @Override
    public String toString() {
        return "Choice{" +
                "condition=" + condition +
                ", successBranch=" + successBranch +
                '}';
    }
}

