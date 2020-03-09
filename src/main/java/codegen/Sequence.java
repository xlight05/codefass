package codegen;

import java.util.ArrayList;

public class Sequence implements FunctionStep {
    private ArrayList<Function> functionList;

    public ArrayList<Function> getFunctionList() {
        return functionList;
    }

    public void setFunctionList(ArrayList<Function> functionList) {
        this.functionList = functionList;
    }

    @Override
    public String toString() {
        return "Sequence{" +
                "functionList=" + functionList +
                '}';
    }
}
