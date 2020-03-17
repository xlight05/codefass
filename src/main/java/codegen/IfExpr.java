package codegen;

import java.util.List;

public class IfExpr extends FunctionStep{
    private List<IfBranch> ifBranches;
    private FunctionStep elseBranchBody;

    public IfExpr(String name) {
        super(name);
    }

    public List<IfBranch> getIfBranches() {
        return ifBranches;
    }

    public void setIfBranches(List<IfBranch> ifBranches) {
        this.ifBranches = ifBranches;
    }

    public FunctionStep getElseBranchBody() {
        return elseBranchBody;
    }

    public void setElseBranchBody(FunctionStep elseBranchBody) {
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
