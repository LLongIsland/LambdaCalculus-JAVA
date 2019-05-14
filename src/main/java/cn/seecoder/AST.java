package cn.seecoder;


import java.util.ArrayList;

public class AST implements Application, Identifier, Abstraction {
    //
    //data part
    private String id;
    StringBuilder param;
    AST body, left, right;
    Node type;
    int value;

    public enum Node {
        Application, Identifier, Abstraction
    }

    //
    //the interface method - three kinds of output
    //check kinds of left and right avoiding null error
    public String ApptoString(ArrayList<String> ctx) {
        if(this.left.value==-1&&this.right.value==-1)
            return this.left.IdetoString(ctx)+" "+this.right.IdetoString(ctx);
        String leftstr = "", rightstr = "";
        switch (this.left.type) {
            case Application:
                leftstr = this.left.ApptoString(ctx);
                break;
            case Identifier:
                leftstr = this.left.IdetoString(ctx);
                break;
            case Abstraction:
                leftstr = this.left.AbstoString(ctx);
                break;
        }
        switch (this.right.type) {
            case Application:
                rightstr = this.right.ApptoString(ctx);
                break;
            case Identifier:
                rightstr = this.right.IdetoString(ctx);
                break;
            case Abstraction:
                rightstr = this.right.AbstoString(ctx);
                break;
        }
        return leftstr + " " + rightstr;
    }

    //return the correct index of param or free variable
    public String IdetoString(ArrayList<String> ctx) {
        if (this.value == -1) return id;
        return ctx.get(this.value);
    }

    //return lambda expression by recursion
    public String AbstoString(ArrayList<String> ctx) {
        if(this.type==Node.Abstraction&&this.param==null)return "";
        StringBuilder temp = new StringBuilder("(\\" + this.param.toString() + ". ");
        ArrayList<String> tmp = new ArrayList<String>();
        switch (this.body.type) {
            case Application:
                tmp.add(this.param.toString());
                tmp.addAll(ctx);
                temp.append(this.body.ApptoString(tmp));
                break;
            case Identifier:
                tmp.add(this.param.toString());
                tmp.addAll(ctx);
                temp.append(this.body.IdetoString(tmp));
                break;
            case Abstraction:
                tmp.add(this.param.toString());
                tmp.addAll(ctx);
                temp.append(this.body.AbstoString(tmp));
                break;
        }
        return temp.append(")").toString();
    }
    String finaltoString(ArrayList<String> ctx){
        if(this.type==Node.Application)return this.ApptoString(ctx);
        else if(this.type==Node.Abstraction) return this.AbstoString(ctx);
        else return this.IdetoString(ctx);
    }
    //
    //
    //methods to build 3 kinds of nodes
    public AST(StringBuilder id, AST next) {
        this.param = id;
        this.body = next;
        this.type = Node.Abstraction;
    }

    public AST(AST left, AST right) {
        this.left = left;
        this.right = right;
        this.type = Node.Application;
    }

    public AST(int value) {
        this.value = value;
        this.type = Node.Identifier;
    }

    //free variable
    public AST(int value, String id) {
        this.value = value;
        this.id = id;
        this.type = Node.Identifier;
    }
}
