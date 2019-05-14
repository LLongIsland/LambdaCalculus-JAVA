package cn.seecoder;


import java.util.ArrayList;

class Parser {
    private Lexer _lexer;

    Parser(Lexer lexer) {
        this._lexer = lexer;
    }

    //the method to begin
    AST _Set() {
        AST ret = this.term(new ArrayList<String>());
        this._lexer.match(Token.Type.EOF);
        return ret;
    }

    /**
     *   get BNF term by recursion
     *   Term::=LAMBDA LCID DOT Term
     *          | Application
     **/
    private AST term(ArrayList<String> ctx) {
        if (this._lexer.skip(Token.Type.LAMBDA)) {
            String id = this._lexer.token(Token.Type.LCID).toString();
            ArrayList<String> temp = new ArrayList<String>();
            this._lexer.match(Token.Type.DOT);
            temp.add(id);
            temp.addAll(ctx);
            AST term = this.term(temp);
            return new AST(new StringBuilder(id), term);//find the next term
        } else {
            return this.application(ctx);
        }
    }

    /**
     * remove left recursions
     *
     *    Application::=Application Atom | Atom
     * -> Application ::=Atom ... Atom
     * -> Application'::=Atom Application'
     *                  | 'Empty
     **/
    private AST application(ArrayList<String> ctx) {
        AST left = this.atom(ctx);
        while (true) {
            AST right = this.atom(ctx);
            if (right == null) return left;       //check whether the recursion overs and return left part
            else left = new AST(left, right);  //if not, build left&right subtrees
        }
    }

    /**
     *   Atom::=LPAREN Term RPAREN
     *         | LCID
     **/
    private AST atom(ArrayList<String> ctx) {
        if (this._lexer.skip(Token.Type.LPAREN)) {
            AST term = this.term(ctx);
            this._lexer.match(Token.Type.RPAREN);
            return term;
        }                                     //check LPAREN and return Term
        else if (this._lexer.check(Token.Type.LCID)) {
            StringBuilder id = this._lexer.token(Token.Type.LCID);
            if (ctx.indexOf(id.toString()) == -1)
                return new AST(-1, id.toString()); //free variable
            return new AST(ctx.indexOf(id.toString()));
        }                                    //check LCID
        else return null;
    }
}
