package compiler;

import codegen.FunctionOrchestrator;
import codegen.IfBranch;
import codegen.Condition;
import codegen.Function;
import codegen.IfExpr;
import codegen.Parallel;
import codegen.Sequence;
import fass.FassBaseVisitor;
import fass.FassParser;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvalVisitor extends FassBaseVisitor<Value> {

    // used to compare floating point numbers
    public static final double SMALL_VALUE = 0.00000000001;

    // store variables (there's only one global scope!)
    private Map<String, Value> memory = new HashMap<String, Value>();

    public static FunctionOrchestrator functionOrchestrator;

    public EvalVisitor() {
        super();
        functionOrchestrator = new FunctionOrchestrator("Test Name","Test Desc","1.0.0");
    }

    // assignment/id overrides
    @Override
    public Value visitAssignment(FassParser.AssignmentContext ctx) {
        String id = ctx.ID().getText();
        Value value = this.visit(ctx.expr());
        return memory.put(id, value);
    }

    @Override
    public Value visitIdAtom(FassParser.IdAtomContext ctx) {
        String id = ctx.getText();
        Value value = memory.get(id);
        if(value == null) {
            throw new RuntimeException("no such variable: " + id);
        }
        return value;
    }

    // atom overrides
    @Override
    public Value visitStringAtom(FassParser.StringAtomContext ctx) {
        String str = ctx.getText();
        // strip quotes
        str = str.substring(1, str.length() - 1).replace("\"\"", "\"");
        return new Value(str);
    }

    @Override
    public Value visitNumberAtom(FassParser.NumberAtomContext ctx) {
        return new Value(Double.valueOf(ctx.getText()));
    }

    @Override
    public Value visitBooleanAtom(FassParser.BooleanAtomContext ctx) {
        return new Value(Boolean.valueOf(ctx.getText()));
    }

    @Override
    public Value visitNilAtom(FassParser.NilAtomContext ctx) {
        return new Value(null);
    }

    // expr overrides
    @Override
    public Value visitParExpr(FassParser.ParExprContext ctx) {

        Value value = this.visit(ctx.expr());
        //System.out.println(value);
        return value;
    }

    @Override
    public Value visitNotExpr(FassParser.NotExprContext ctx) {
        Value value = this.visit(ctx.expr());
        //System.out.println("In Not");
        String evaluator="!";
        Condition condition = new Condition(value.value,null,evaluator);
        return new Value(condition);
    }

    @Override
    public Value visitMultiplicationExpr(@NotNull FassParser.MultiplicationExprContext ctx) {
        //System.out.println("In multi");
        Value left = this.visit(ctx.expr(0));
        Value right = this.visit(ctx.expr(1));
        String evaluator="";
        switch (ctx.op.getType()) {
            case FassParser.MULT:
                evaluator= "*";
                break;
            case FassParser.DIV:
                evaluator="/";
                break;
            case FassParser.MOD:
                evaluator="%";
                break;
            default:
                //throw new RuntimeException("unknown operator: " + FassParser.tokenNames[ctx.op.getType()]);
        }
        Condition condition = new Condition(left.value,right.value,evaluator);
        return new Value(condition);
    }

    @Override
    public Value visitAdditiveExpr(@NotNull FassParser.AdditiveExprContext ctx) {
        //System.out.println("in Additive");
        Value left = this.visit(ctx.expr(0));
        Value right = this.visit(ctx.expr(1));

        switch (ctx.op.getType()) {
            case FassParser.PLUS:
                return left.isDouble() && right.isDouble() ?
                        new Value(left.asDouble() + right.asDouble()) :
                        new Value(left.asString() + right.asString());
            case FassParser.MINUS:
                return new Value(left.asDouble() - right.asDouble());
            default:
                throw new RuntimeException("unknown operator: " + FassParser.tokenNames[ctx.op.getType()]);
        }
    }

    @Override
    public Value visitRelationalExpr(@NotNull FassParser.RelationalExprContext ctx) {
        //System.out.println("In Relational");
        Value left = this.visit(ctx.expr(0));
        Value right = this.visit(ctx.expr(1));
        String evaluator ="";
        switch (ctx.op.getType()) {
            case FassParser.LT:
                evaluator = "<";
                break;
            case FassParser.LTEQ:
                evaluator = "<=";
                break;
            case FassParser.GT:
                evaluator = ">";
                break;
            case FassParser.GTEQ:
                evaluator = ">=";
        }
        Condition condition = new Condition(left,right,evaluator);
        return new Value(condition);
    }

    @Override
    public Value visitEqualityExpr(@NotNull FassParser.EqualityExprContext ctx) {
        //System.out.println("In Equality");
        Value left = this.visit(ctx.expr(0));
        Value right = this.visit(ctx.expr(1));
        String evaluator ="";
        switch (ctx.op.getType()) {
            case FassParser.EQ:
                evaluator="==";
                break;
            case FassParser.NEQ:
                evaluator="!=";
                break;
        }
        Condition condition = new Condition(left.value,right.value,evaluator);
        return new Value((condition));
    }

    @Override
    public Value visitAndExpr(FassParser.AndExprContext ctx) {
        //System.out.println("In AND");
        Value left = this.visit(ctx.expr(0));
        Value right = this.visit(ctx.expr(1));
        Condition condition = new Condition(left.value,right.value,"&&");
        return new Value(condition);
    }

    @Override
    public Value visitOrExpr(FassParser.OrExprContext ctx) {
        //System.out.println("In OR");
        Value left = this.visit(ctx.expr(0));
        Value right = this.visit(ctx.expr(1));
        Condition condition = new Condition(left.value,right.value,"||");
        return new Value(condition);
    }

    // log override
    @Override
    public Value visitLog(FassParser.LogContext ctx) {
        Value value = this.visit(ctx.expr());
        System.out.println(value);
        return value;
    }

    // if override
//    @Override
//    public Compiler.Value visitIf_stat(FassParser.If_statContext ctx) {
//        List<FassParser.Condition_blockContext> conditions =  ctx.condition_block();
//
//        boolean evaluatedBlock = false;
//
//        for(FassParser.Condition_blockContext condition : conditions) {
//
//            Compiler.Value evaluated = this.visit(condition.expr());
//
//            if(evaluated.asBoolean()) {
//                evaluatedBlock = true;
//                // evaluate this block whose expr==true
//                this.visit(condition.stat_block());
//                break;
//            }
//        }
//
//        if(!evaluatedBlock && ctx.stat_block() != null) {
//            // evaluate the else-stat_block (if present == not null)
//            this.visit(ctx.stat_block());
//        }
//
//        return Compiler.Value.VOID;
//    }

    @Override
    public Value visitIf_stat(FassParser.If_statContext ctx) {
        List<FassParser.Condition_blockContext> condition_block = ctx.condition_block();
        IfExpr ifExpr = new IfExpr("ChoiceBlock"); //TODO check
        List<IfBranch> ifBranches = new ArrayList<>();
//        FassParser.Condition_blockContext firstIfCondition = condition_block.get(0);
//        System.out.println(this.visit(firstIfCondition.expr()));
        for (FassParser.Condition_blockContext ifCondition : condition_block){ //If and else if
            Condition condition = (Condition)this.visit(ifCondition.expr()).value;

            List<FassParser.StatContext> stats = ifCondition.stat_block().block().stat();//Probably avoid multiple sequences
            Sequence sequence = null;
            for (FassParser.StatContext stat:stats){
                if (stat.ID() != null){
                    String sequenceId = stat.ID().getText();
                    sequence = (Sequence) memory.get(sequenceId).value;//Add support for not just seq
                } else {
                    //Fill
                    System.out.println("Not a ID");
                }
            }

            IfBranch ifBranch = new IfBranch(condition);
            ifBranch.setSuccessBranch(sequence);
            ifBranches.add(ifBranch);
        }
        ifExpr.setIfBranches(ifBranches);
        //assuming has else
        String sequenceId =ctx.stat_block().block().stat().get(0).ID().getText();
        Sequence sequence = (Sequence) memory.get(sequenceId).value;
        ifExpr.setElseBranchBody(sequence);
        functionOrchestrator.getStepList().add(ifExpr);
        return super.visitIf_stat(ctx);
    }

    // while override
    @Override
    public Value visitWhile_stat(FassParser.While_statContext ctx) {

        Value value = this.visit(ctx.expr());

        while(value.asBoolean()) {

            // evaluate the code block
            this.visit(ctx.stat_block());

            // evaluate the expression
            value = this.visit(ctx.expr());
        }

        return Value.VOID;
    }

    @Override
    public Value visitFunction_def(FassParser.Function_defContext ctx) {
        String id = ctx.ID().getText();
        List<FassParser.Function_statContext> functionContents =
                ctx.function_block().function_repeat().function_stat();


        String name = "";
        String handler = "";
        for (FassParser.Function_statContext function:functionContents){
            if (function.function_name() != null){
                name = function.function_name().STRING().getText();
            } else if (function.function_handler() != null){
                handler = function.function_handler().STRING().getText();
            }
        }
        Function func = new Function(name,handler,"Java","index.java");
        memory.put(id,new Value(func));
        return super.visitFunction_def(ctx);
    }

    @Override
    public Value visitSequence_def(FassParser.Sequence_defContext ctx) {
        String id = ctx.ID().getText();
        List<FassParser.Sequence_statContext> sequenceElements =
                ctx.sequence_block().sequence_repeat().sequence_stat();
        Sequence sequence = new Sequence(id);
        List<Function> functionList = new ArrayList <>();
        for (FassParser.Sequence_statContext functionElement:sequenceElements){
            String elementKey = functionElement.ID().getText();
            if (memory.containsKey(elementKey)){
                Function function = (Function) memory.get(elementKey).value;
                functionList.add(function);
            } else {
                throw new RuntimeException("no such variable: " + elementKey);
            }
        }
        sequence.setFunctionList(functionList);
        memory.put(id,new Value(sequence));
        return super.visitSequence_def(ctx);
    }

    @Override
    public Value visitParallel_def(FassParser.Parallel_defContext ctx) {
        String id = ctx.ID().getText();
        List<FassParser.Parallel_statContext> parallelElements =
                ctx.parallel_block().parallel_repeat().parallel_stat();
        Parallel parallel = new Parallel(id);
        List<Function> functionList = new ArrayList <>();
        for (FassParser.Parallel_statContext functionElement:parallelElements){
            String elementKey = functionElement.ID().getText();
            if (memory.containsKey(elementKey)){
                Function function = (Function) memory.get(elementKey).value;
                functionList.add(function);
            } else {
                throw new RuntimeException("no such variable: " + elementKey);
            }
        }
        parallel.setFunctionList(functionList);
        memory.put(id,new Value(parallel));
        return super.visitParallel_def(ctx);
    }
}
