package codegen;

import java.util.ArrayList;
import java.util.List;

public class Parallel implements FunctionStep {
    private List<Function> functionList;

    public List<Function> getFunctionList() {
        return functionList;
    }

    public void setFunctionList(List<Function> functionList) {
        this.functionList = functionList;
    }

    @Override
    public String toString() {
        return "Parallel{" +
                "functionList=" + functionList +
                '}';
    }
}
