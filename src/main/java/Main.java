import java.lang.Exception;

import fass.FassLexer;
import fass.FassParser;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            args = new String[]{"src/main/fass/test.fass"};
        }

        System.out.println("parsing: " + args[0]);

        FassLexer lexer = new FassLexer(new ANTLRFileStream(args[0]));
        FassParser parser = new FassParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.parse();
        EvalVisitor visitor = new EvalVisitor();
        visitor.visit(tree);
    }
}