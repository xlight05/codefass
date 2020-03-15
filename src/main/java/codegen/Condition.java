package codegen;

public class Condition {
    private Object leftSide;
    private Object rightSide;
    private String evaluator;

    public Condition(Object leftSide, Object rightSide, String evaluator) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.evaluator = evaluator;
    }

    public Object getLeftSide() {
        return leftSide;
    }

    public void setLeftSide(Object leftSide) {
        this.leftSide = leftSide;
    }

    public Object getRightSide() {
        return rightSide;
    }

    public void setRightSide(Object rightSide) {
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
                "leftSide=" + leftSide +
                ", rightSide=" + rightSide +
                ", evaluator='" + evaluator + '\'' +
                '}';
    }
}