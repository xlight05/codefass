package codegen;

import java.io.Serializable;

public abstract class CloudArtifactGenerator implements Serializable {
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
