package codegen;

public abstract class FunctionStep {
    private String name;

    public FunctionStep(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
