package compiler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.Exception;
import java.nio.file.Files;
import java.nio.file.Paths;

import codegen.FunctionOrchestrator;
import fass.FassLexer;
import fass.FassParser;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Executor {

//    public static void main(String[] args) throws Exception {
//
//        if (args.length == 0) {
//            args = new String[]{"src/main/fass/test.fass"};
//        }
//
//        System.out.println("parsing: " + args[0]);
//
//        FassLexer lexer = new FassLexer(new ANTLRFileStream(args[0]));
//        FassParser parser = new FassParser(new CommonTokenStream(lexer));
//        ParseTree tree = parser.parse();
//        EvalVisitor visitor = new EvalVisitor();
//        visitor.visit(tree);
//        Double d = EvalVisitor.SMALL_VALUE;
//    }

    public FunctionOrchestrator compile(String args) throws IOException {

        FassLexer lexer = new FassLexer(new ANTLRFileStream(args));
        FassParser parser = new FassParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.parse();
        EvalVisitor visitor = new EvalVisitor();
        visitor.visit(tree);
        //        try {
//            Files.createDirectories(Paths.get("build"));
//            FileOutputStream f = new FileOutputStream(new File("build/object.txt"));
//            ObjectOutputStream o = new ObjectOutputStream(f);
//            o.writeObject(functionOrchestrator);
//            o.close();
//            f.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return visitor.functionOrchestrator;
    }


}