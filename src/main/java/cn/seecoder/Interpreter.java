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
                if(ast.left.type==AST.Node.Application) {
                    ast.left = cal(ast.left);
                    if (ast.left.type == AST.Node.Application)
                        return ast;
                }
                else if(ast.left.type==AST.Node.Abstraction){
                    if(ast.right.type==AST.Node.Application)
                        ast.right=cal(ast.right);
                    ast=substitute(ast.right,ast.left.body);
                }
                else{
                    if(ast.right.type==AST.Node.Application){
                        ast.right=cal(ast.right);
                        return ast;
                    }
                    else if(ast.right.type==AST.Node.Abstraction){
                        ast.right=cal(ast.right);
                        return ast;
                    }
                    else return ast;
                }
            }
            else if (isValue(ast)) {
                ast.body=cal(ast.body);
                return ast;
            }
            else if(ast.value==-1)return ast;
            else return ast;
        }
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
                return shift(depth, value, 0);
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
