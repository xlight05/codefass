package codegen;

import java.util.ArrayList;
import java.util.List;

public class Sequence extends FunctionStep {
    private List<Function> functionList;

    public Sequence(String name) {
        super(name);
    }

    public List<Function> getFunctionList() {
        return functionList;
    }

    public void setFunctionList(List<Function> functionList) {
        this.functionList = functionList;
    }

    @Override
    public String toString() {
        return "Sequence{" +
                "functionList=" + functionList +
                '}';
    }
}
