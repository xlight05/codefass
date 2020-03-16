package codegen;

import java.util.ArrayList;
import java.util.List;

public class Sequence implements FunctionStep {
    private List<Function> functionList;
    private String name;

    public List<Function> getFunctionList() {
        return functionList;
    }

    public void setFunctionList(List<Function> functionList) {
        this.functionList = functionList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Sequence{" +
                "functionList=" + functionList +
                '}';
    }
}
