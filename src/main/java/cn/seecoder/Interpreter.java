package cn.seecoder;

import java.util.ArrayList;

public class Interpreter {
    private boolean isValue(AST node) {
        return node.type == AST.Node.Abstraction;
    }

    //
    //calculus by recursion
    public AST cal(AST ast) {
        while (true) {
            if (ast.type == AST.Node.Application) {
                if(ast.left.value==-1&&ast.right.value==-1)break;
                if ((isValue(ast.left) && isValue(ast.right))||
                        (isValue(ast.left)&&ast.right.value==-1)) {
                    ast = substitute(ast.right, ast.left.body);
                }                               //if both are value,substitute & check subtree(unlock)
                else if (isValue(ast.left))
                    ast.right = cal(ast.right);//calculus right tree by recursion
                else
                    ast.left = cal(ast.left);  //the left tree
            }
            else if (isValue(ast))
                return ast;
            else if(ast.value==-1)return ast;
        }
        return ast;
    }

    //
    //specific shift action
    private AST shift(int by, AST node, int from) {
        if (node.type == AST.Node.Identifier) {
            if(node.value==-1)
                return node;
            return new AST(
                    node.value + (node.value >= from ? by : 0)
            );
        } else if (node.type == AST.Node.Application) {
             return new AST(
                    shift(by, node.left, from),
                    shift(by, node.right, from)
            );
            /*Lexer lexer= new Lexer(ast.finaltoString(new ArrayList<String>()));
            Parser parser=new Parser(lexer);
            Interpreter interpreter=new Interpreter();
            ast=interpreter.cal(ast);
            return ast;*/
        }
        else if (isValue(node)) {
            return new AST(
                    node.param,
                    shift(by, node.body, from + 1)
            );
        }
        return null;
    }

    /**
     * process of substitute
     *        app
     *       /  \
     *      abs  abs(value)  ::the former:node
     *     /  \
     * param  body
     * app->substitute(value,body)->the outermost param was dropped out
     **/
    private AST operate(AST value, AST node, int depth) {
        if (node.type == AST.Node.Identifier) {
            if (depth == node.value)
                return shift(depth, value, depth);
            else return node;
        } else if (node.type == AST.Node.Application)
            return new AST(
                    operate(value, node.left, depth),
                    operate(value, node.right, depth)
            );
        else if (isValue(node)) {
            return new AST(
                    node.param,
                    operate(value, node.body, depth + 1)
            );
        }
        return null;
    }

    //
    //combination to substitute
    private AST substitute(AST value, AST node) {
        return shift(-1, operate(shift(1, value, 0), node, 0), 0);
    }
}
