package compiler;

import codegen.FunctionOrchestrator;
import codegen.FunctionStep;
import codegen.IfBranch;
import codegen.Condition;
import codegen.Function;
import codegen.IfExpr;
import codegen.Parallel;
import codegen.Sequence;
import fass.FassBaseVisitor;
import fass.FassLexer;
import fass.FassParser;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvalVisitor extends FassBaseVisitor<Value> {

    // used to compare floating point numbers
    public static final double SMALL_VALUE = 0.00000000001;

    // store variables (there's only one global scope!)
    private Map<String, Value> memory = new HashMap<String, Value>();
    private Map<String, FunctionOrchestrator> imports = new HashMap<String, FunctionOrchestrator>();

    public FunctionOrchestrator functionOrchestrator;

    public EvalVisitor() {
        super();
        functionOrchestrator = new FunctionOrchestrator("FaaS deployment", "Auto generated serverless " +
                "workflow " +
                "from " +
                "FaaS"
                , "1.0.0");
    }

    @Override
    public Value visitOrchestrate_def(FassParser.Orchestrate_defContext ctx) {
        List<TerminalNode> paramList = ctx.param_block().PARAM();
        for (TerminalNode node : paramList) {
            String id = node.getText();
            Value value = new Value(node.getText());
            memory.put(id, value);
        }
        List<FassParser.Orchestrate_statContext> stats = ctx.orchestrate_block().orc_block().orchestrate_stat();
        for (FassParser.Orchestrate_statContext stat : stats) {
            if (stat.if_stat() != null) {
                this.visitIf_stat1(stat.if_stat());
            } else if (stat.parallel_def() != null){
                Parallel parallel = (Parallel) this.visitParallel_def1(stat.parallel_def()).value;
                functionOrchestrator.getStepList().add(parallel);
            } else if (stat.sequence_def() != null) {
                Sequence sequence = (Sequence) this.visitSequence_def1(stat.sequence_def()).value;
                functionOrchestrator.getStepList().add(sequence);
            } else if (stat.start_import() != null) {
                ArrayList<FunctionStep> importedList =
                        imports.get(stat.start_import().ID().getText()).getStepList();
                functionOrchestrator.getStepList().addAll(importedList);
            }
        }

//        try {
//            //TODO module name
//            Files.createDirectories(Paths.get("build"));
//            FileOutputStream f = new FileOutputStream(new File("build/object.txt"));
//            ObjectOutputStream o = new ObjectOutputStream(f);
//            o.writeObject(functionOrchestrator);
//            o.close();
//            f.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return new Value (functionOrchestrator);
    }

    @Override
    public Value visitImport_def(FassParser.Import_defContext ctx) {
        try {
            String moduleName = ctx.ID().getText();

            String args = moduleName+"/"+moduleName+".fass";
            FassLexer lexer = new FassLexer(new ANTLRFileStream(args));
            FassParser parser = new FassParser(new CommonTokenStream(lexer));
            ParseTree tree = parser.parse();
            EvalVisitor visitor = new EvalVisitor();
            visitor.visit(tree);
            FunctionOrchestrator importOrc = visitor.functionOrchestrator;
            imports.put(moduleName,importOrc);



//            try {
//                Files.createDirectories(Paths.get(moduleName+"/build"));
//                FileOutputStream f = new FileOutputStream(new File(moduleName+"/build/object.txt"));
//                ObjectOutputStream o = new ObjectOutputStream(f);
//                o.writeObject(functionOrchestrator);
//                o.close();
//                f.close();
//
//                //read now
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.visitImport_def(ctx);
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
        if (value == null) {
            throw new RuntimeException("no such variable: " + id);
        }
        return value;
    }

    @Override
    public Value visitParamAtom(FassParser.ParamAtomContext ctx) {
        String id = ctx.getText();
        Value value = memory.get(id);
        if (value == null) {
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
        String evaluator = "!";
        Condition condition = new Condition(value.value, null, evaluator);
        return new Value(condition);
    }

    @Override
    public Value visitMultiplicationExpr(@NotNull FassParser.MultiplicationExprContext ctx) {
        //System.out.println("In multi");
        Value left = this.visit(ctx.expr(0));
        Value right = this.visit(ctx.expr(1));
        String evaluator = "";
        switch (ctx.op.getType()) {
            case FassParser.MULT:
                evaluator = "*";
                break;
            case FassParser.DIV:
                evaluator = "/";
                break;
            case FassParser.MOD:
                evaluator = "%";
                break;
            default:
                //throw new RuntimeException("unknown operator: " + FassParser.tokenNames[ctx.op.getType()]);
        }
        Condition condition = new Condition(left.value, right.value, evaluator);
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
        String evaluator = "";
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
        Condition condition = new Condition(left, right, evaluator);
        return new Value(condition);
    }

    @Override
    public Value visitEqualityExpr(@NotNull FassParser.EqualityExprContext ctx) {
        //System.out.println("In Equality");
        Value left = this.visit(ctx.expr(0));
        Value right = this.visit(ctx.expr(1));
        String evaluator = "";
        switch (ctx.op.getType()) {
            case FassParser.EQ:
                evaluator = "==";
                break;
            case FassParser.NEQ:
                evaluator = "!=";
                break;
        }
        Condition condition = new Condition(left.value, right.value, evaluator);
        return new Value((condition));
    }

    @Override
    public Value visitAndExpr(FassParser.AndExprContext ctx) {
        //System.out.println("In AND");
        Value left = this.visit(ctx.expr(0));
        Value right = this.visit(ctx.expr(1));
        Condition condition = new Condition(left.value, right.value, "&&");
        return new Value(condition);
    }

    @Override
    public Value visitOrExpr(FassParser.OrExprContext ctx) {
        //System.out.println("In OR");
        Value left = this.visit(ctx.expr(0));
        Value right = this.visit(ctx.expr(1));
        Condition condition = new Condition(left.value, right.value, "||");
        return new Value(condition);
    }

    // log override
    @Override
    public Value visitLog(FassParser.LogContext ctx) {
        Value value = this.visit(ctx.expr());
        System.out.println(value);
        return value;
    }

    //@Override
    public Value visitIf_stat1(FassParser.If_statContext ctx) {
        List<FassParser.Condition_blockContext> condition_block = ctx.condition_block();
        IfExpr ifExpr = new IfExpr("ChoiceBlock"); //TODO check
        List<IfBranch> ifBranches = new ArrayList<>();
//        FassParser.Condition_blockContext firstIfCondition = condition_block.get(0);
//        System.out.println(this.visit(firstIfCondition.expr()));
        for (FassParser.Condition_blockContext ifCondition : condition_block) { //If and else if
            Condition condition = (Condition) this.visit(ifCondition.expr()).value;

            List<FassParser.Orchestrate_statContext> stats = ifCondition.orchestrate_block().orc_block().orchestrate_stat();//Probably avoid
            // multiple sequences
            ArrayList <FunctionStep> step = new ArrayList<>();
            for (FassParser.Orchestrate_statContext stat : stats) {
                if (stat.sequence_def() != null){
                    Sequence sequence =  (Sequence) this.visitSequence_def1(stat.sequence_def()).value;
                    step.add(sequence);
                } else if (stat.parallel_def() != null){
                    Parallel parallel =  (Parallel) this.visitParallel_def1(stat.parallel_def()).value;
                    step.add(parallel);
                } else if (stat.start_import() != null){
                    FunctionOrchestrator importedObj = imports.get(stat.start_import().ID().getText());
                    ArrayList<FunctionStep> stepList = importedObj.getStepList();
                    step.addAll(stepList);
                }
            }

            IfBranch ifBranch = new IfBranch(condition);

            ifBranch.setSuccessBranch(step);
            ifBranches.add(ifBranch);
        }
        ifExpr.setIfBranches(ifBranches);
        //assuming has else

        FassParser.Orchestrate_statContext stat = ctx.orchestrate_block().orc_block().orchestrate_stat().get(0);
        ArrayList <FunctionStep> step = new ArrayList<>();
        if (stat.sequence_def() != null){
            Sequence sequence =  (Sequence) this.visitSequence_def1(stat.sequence_def()).value;
            step.add(sequence);
        } else if (stat.parallel_def() != null){
            Parallel parallel =  (Parallel) this.visitParallel_def1(stat.parallel_def()).value;
            step.add(parallel);
        } else if (stat.start_import() != null){
            FunctionOrchestrator importedObj = imports.get(stat.start_import().ID().getText());
            ArrayList<FunctionStep> stepList = importedObj.getStepList();
            step.addAll(stepList);
        }
        ifExpr.setElseBranchBody(step);
        functionOrchestrator.getStepList().add(ifExpr);
        return super.visitIf_stat(ctx);
    }

    // while override
    @Override
    public Value visitWhile_stat(FassParser.While_statContext ctx) {

        Value value = this.visit(ctx.expr());

        while (value.asBoolean()) {

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
        String language ="";
        //TODO check
        for (FassParser.Function_statContext function : functionContents) {
            if (function.function_name() != null) {
                name = function.function_name().STRING().getText().replace("\"", "");
            } else if (function.function_handler() != null) {
                handler = function.function_handler().STRING().getText().replace("\"", "");
            }
            else if (function.function_language() != null) {
                language = function.function_language().STRING().getText().replace("\"", "");
            }
        }
        Function func = new Function(name, handler, language, "index.js");
        memory.put(id, new Value(func));
        return super.visitFunction_def(ctx);
    }

    public Value visitSequence_def1(FassParser.Sequence_defContext ctx) {
        String id = ctx.ID().getText();
        List<FassParser.Sequence_statContext> sequenceElements =
                ctx.sequence_block().sequence_repeat().sequence_stat();
        Sequence sequence = new Sequence(id);
        List<Function> functionList = new ArrayList<>();
        for (FassParser.Sequence_statContext functionElement : sequenceElements) {
            String elementKey = functionElement.ID().getText();
            if (memory.containsKey(elementKey)) {
                Function function = (Function) memory.get(elementKey).value;
                functionList.add(function);
            } else {
                throw new RuntimeException("no such variable: " + elementKey);
            }
        }
        sequence.setFunctionList(functionList);
        memory.put(id, new Value(sequence));
        //return super.visitSequence_def(ctx);
        return new Value(sequence);
    }

    public Value visitParallel_def1(FassParser.Parallel_defContext ctx) {
        String id = ctx.ID().getText();
        List<FassParser.Parallel_statContext> parallelElements =
                ctx.parallel_block().parallel_repeat().parallel_stat();
        Parallel parallel = new Parallel(id);
        List<Function> functionList = new ArrayList<>();
        for (FassParser.Parallel_statContext functionElement : parallelElements) {
            String elementKey = functionElement.ID().getText();
            if (memory.containsKey(elementKey)) {
                Function function = (Function) memory.get(elementKey).value;
                functionList.add(function);
            } else {
                throw new RuntimeException("no such variable: " + elementKey);
            }
        }
        parallel.setFunctionList(functionList);
        memory.put(id, new Value(parallel));
        //return super.visitParallel_def(ctx);
        return new Value(parallel);
    }

//    private static File fileWithDirectoryAssurance(String directory, String filename) {
//        File dir = new File(directory);
//        if (!dir.exists()) dir.mkdirs();
//        return new File(directory + "/" + filename);
//    }
}
