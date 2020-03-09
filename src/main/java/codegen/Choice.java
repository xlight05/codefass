package codegen;

import java.util.ArrayList;

public class Choice implements FunctionStep {
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

    private class Condition {
        private String leftSide;
        private String rightSide;
        private String evaluator;

        public Condition(String leftSide, String rightSide, String evaluator) {
            this.leftSide = leftSide;
            this.rightSide = rightSide;
            this.evaluator = evaluator;
        }

        public String getLeftSide() {
            return leftSide;
        }

        public void setLeftSide(String leftSide) {
            this.leftSide = leftSide;
        }

        public String getRightSide() {
            return rightSide;
        }

        public void setRightSide(String rightSide) {
            this.rightSide = rightSide;
        }

        public String getEvaluator() {
            return evaluator;
        }

        public void setEvaluator(String evaluator) {
            this.evaluator = evaluator;
        }

        @Override
        public String toString() {
            return "Condition{" +
                    "leftSide='" + leftSide + '\'' +
                    ", rightSide='" + rightSide + '\'' +
                    ", evaluator='" + evaluator + '\'' +
                    '}';
        }
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

