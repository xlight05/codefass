package codegen;

public abstract class CloudArtifactGenerator {
    private FunctionOrchestrator functionOrchestrator;

    public CloudArtifactGenerator(FunctionOrchestrator functionOrchestrator) {
        this.functionOrchestrator = functionOrchestrator;
    }

    public abstract void build();

    public FunctionOrchestrator getFunctionOrchestrator() {
        return functionOrchestrator;
    }

    public void setFunctionOrchestrator(FunctionOrchestrator functionOrchestrator) {
        this.functionOrchestrator = functionOrchestrator;
    }
}
