package codegen;

public class Condition {
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