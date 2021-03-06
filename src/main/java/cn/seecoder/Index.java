package cn.seecoder;

import java.util.ArrayList;

public class Index {
    public static void main(String[] args) {
        String source = "(\\n.\\f.\\x.f(n f x))(\\f.\\x.f x)";
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        AST ast = parser._Set();
        Interpreter interpreter = new Interpreter();
        AST newast = interpreter.cal(ast);
        System.out.println(newast.finaltoString(new ArrayList<String>()));
    }
}
