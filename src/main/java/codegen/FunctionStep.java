package codegen;

import java.io.Serializable;

public abstract class FunctionStep implements Serializable {
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
