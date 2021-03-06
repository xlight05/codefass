package codegen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IfExpr extends FunctionStep implements Serializable {
    private List<IfBranch> ifBranches;
    private ArrayList<FunctionStep> elseBranchBody;

    public IfExpr(String name) {
        super(name);
    }

    public List<IfBranch> getIfBranches() {
        return ifBranches;
    }

    public void setIfBranches(List<IfBranch> ifBranches) {
        this.ifBranches = ifBranches;
    }

    public ArrayList<FunctionStep> getElseBranchBody() {
        return elseBranchBody;
    }

    public void setElseBranchBody(ArrayList<FunctionStep> elseBranchBody) {
        this.elseBranchBody = elseBranchBody;
    }

    @Override
    public String toString() {
        return "IfExpr{" +
                "ifBranches=" + ifBranches +
                ", elseBranchBody=" + elseBranchBody +
                '}';
    }
}
