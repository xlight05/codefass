import codegen.Function;
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
        return this.visit(ctx.expr());
    }

    @Override
    public Value visitPowExpr(FassParser.PowExprContext ctx) {
        Value left = this.visit(ctx.expr(0));
        Value right = this.visit(ctx.expr(1));
        return new Value(Math.pow(left.asDouble(), right.asDouble()));
    }

    @Override
    public Value visitUnaryMinusExpr(FassParser.UnaryMinusExprContext ctx) {
        Value value = this.visit(ctx.expr());
        return new Value(-value.asDouble());
    }

    @Override
    public Value visitNotExpr(FassParser.NotExprContext ctx) {
        Value value = this.visit(ctx.expr());
        return new Value(!value.asBoolean());
    }

    @Override
    public Value visitMultiplicationExpr(@NotNull FassParser.MultiplicationExprContext ctx) {

        Value left = this.visit(ctx.expr(0));
        Value right = this.visit(ctx.expr(1));

        switch (ctx.op.getType()) {
            case FassParser.MULT:
                return new Value(left.asDouble() * right.asDouble());
            case FassParser.DIV:
                return new Value(left.asDouble() / right.asDouble());
            case FassParser.MOD:
                return new Value(left.asDouble() % right.asDouble());
            default:
                throw new RuntimeException("unknown operator: " + FassParser.tokenNames[ctx.op.getType()]);
        }
    }

    @Override
    public Value visitAdditiveExpr(@NotNull FassParser.AdditiveExprContext ctx) {

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

        Value left = this.visit(ctx.expr(0));
        Value right = this.visit(ctx.expr(1));

        switch (ctx.op.getType()) {
            case FassParser.LT:
                return new Value(left.asDouble() < right.asDouble());
            case FassParser.LTEQ:
                return new Value(left.asDouble() <= right.asDouble());
            case FassParser.GT:
                return new Value(left.asDouble() > right.asDouble());
            case FassParser.GTEQ:
                return new Value(left.asDouble() >= right.asDouble());
            default:
                throw new RuntimeException("unknown operator: " + FassParser.tokenNames[ctx.op.getType()]);
        }
    }

    @Override
    public Value visitEqualityExpr(@NotNull FassParser.EqualityExprContext ctx) {

        Value left = this.visit(ctx.expr(0));
        Value right = this.visit(ctx.expr(1));

        switch (ctx.op.getType()) {
            case FassParser.EQ:
                return left.isDouble() && right.isDouble() ?
                        new Value(Math.abs(left.asDouble() - right.asDouble()) < SMALL_VALUE) :
                        new Value(left.equals(right));
            case FassParser.NEQ:
                return left.isDouble() && right.isDouble() ?
                        new Value(Math.abs(left.asDouble() - right.asDouble()) >= SMALL_VALUE) :
                        new Value(!left.equals(right));
            default:
                throw new RuntimeException("unknown operator: " + FassParser.tokenNames[ctx.op.getType()]);
        }
    }

    @Override
    public Value visitAndExpr(FassParser.AndExprContext ctx) {
        Value left = this.visit(ctx.expr(0));
        Value right = this.visit(ctx.expr(1));
        return new Value(left.asBoolean() && right.asBoolean());
    }

    @Override
    public Value visitOrExpr(FassParser.OrExprContext ctx) {
        Value left = this.visit(ctx.expr(0));
        Value right = this.visit(ctx.expr(1));
        return new Value(left.asBoolean() || right.asBoolean());
    }

    // log override
    @Override
    public Value visitLog(FassParser.LogContext ctx) {
        Value value = this.visit(ctx.expr());
        System.out.println(value);
        return value;
    }

    // if override
    @Override
    public Value visitIf_stat(FassParser.If_statContext ctx) {
        List<FassParser.Condition_blockContext> conditions =  ctx.condition_block();

        boolean evaluatedBlock = false;

        for(FassParser.Condition_blockContext condition : conditions) {

            Value evaluated = this.visit(condition.expr());

            if(evaluated.asBoolean()) {
                evaluatedBlock = true;
                // evaluate this block whose expr==true
                this.visit(condition.stat_block());
                break;
            }
        }

        if(!evaluatedBlock && ctx.stat_block() != null) {
            // evaluate the else-stat_block (if present == not null)
            this.visit(ctx.stat_block());
        }

        return Value.VOID;
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
        Sequence sequence = new Sequence();
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
        System.out.println(sequence);
        return super.visitSequence_def(ctx);
    }

    @Override
    public Value visitParallel_def(FassParser.Parallel_defContext ctx) {
        String id = ctx.ID().getText();
        List<FassParser.Parallel_statContext> parallelElements =
                ctx.parallel_block().parallel_repeat().parallel_stat();
        Parallel parallel = new Parallel();
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
        System.out.println(parallel);
        return super.visitParallel_def(ctx);
    }
}
